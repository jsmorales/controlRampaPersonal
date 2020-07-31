package com.jsmorales.controlpersonalrampa.Fragments;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jsmorales.controlpersonalrampa.Events.UbicationSelectedEvent;
import com.jsmorales.controlpersonalrampa.Models.Employee;
import com.jsmorales.controlpersonalrampa.Models.Position;
import com.jsmorales.controlpersonalrampa.Models.Resultado;
import com.jsmorales.controlpersonalrampa.Models.Ubication;
import com.jsmorales.controlpersonalrampa.Models.UtilsMainApp;
import com.jsmorales.controlpersonalrampa.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultaIngresoFragment extends Fragment {

    private static final String TAG = ConsultaIngresoFragment.class.getSimpleName();
    private static final int MY_SOCKET_TIMEOUT_MS = 20000;
    private static final String NOTIFICACIONES_TITLE = "Notificaciones";
    public static final String ASSIGN_UBICATIONS_TAG = "AssignUbicationsFragment";
    public static final String UBICATION_BUTTON_LABEL = "Seleccionar Ubicación";
    public static final String LOAD_LABEL = "Cargando...";

    public EditText socialNumberTextInput;
    public TextInputLayout textInputSocialLayout;
    public View consulta_progress;
    public View consultaForm;
    public View resultadoLayout;

    public Button buttonSelectUbication;

    public TextView nameVerificationTextView;
    public TextView validationTextView;
    public TextView detailTextView;
    public TextView turnTextViewpublic;
    public TextView notificationsTextView;

    public Resultado resultado;
    public Employee empleado;
    public UtilsMainApp urls;

    public ArrayList<Ubication> arrayUbications;

    public FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;

    public AssignUbicationFragment assignUbicationFragment;
    public Ubication selectedUbication;
    public TextView valueUbicationTextView;

    public ConsultaIngresoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //para obtener los arguments
        Bundle bundle = getArguments();

        resultado = bundle.getParcelable("resultado");
        empleado = bundle.getParcelable("empleado");
        urls = bundle.getParcelable("urls");

        Log.d(TAG,resultado.getToken());
        Log.d(TAG, "Se debe ejecutar: "+urls.getHost());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_consulta_ingreso, container, false);

        //------------------------------------------------------
        //bind main fragment components UI
        socialNumberTextInput = view.findViewById(R.id.socialNumberTextInput);
        textInputSocialLayout = view.findViewById(R.id.textInputSocialLayout);
        consulta_progress = view.findViewById(R.id.consulta_progress);
        consultaForm = view.findViewById(R.id.consultaFormIngreso);
        resultadoLayout = view.findViewById(R.id.resultadoLayout);

        buttonSelectUbication = view.findViewById(R.id.buttonSelectUbication);
        valueUbicationTextView = view.findViewById(R.id.valueUbicationTextView);
        //------------------------------------------------------
        //bind result layout
        nameVerificationTextView = view.findViewById(R.id.nameVerificationTextView);
        validationTextView = view.findViewById(R.id.validationTextView);
        detailTextView = view.findViewById(R.id.detailTextView);
        turnTextViewpublic = view.findViewById(R.id.turnTextView);
        notificationsTextView = view.findViewById(R.id.notificationsTextView);


        Button consultaButton = view.findViewById(R.id.consultaButton);

        consultaButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String socialNumber = socialNumberTextInput.getText().toString();

                Log.d(TAG, "Social number: "+socialNumber);

                // Check for a valid password, if the user entered one.
                if (TextUtils.isEmpty(socialNumber)) {

                    socialNumberTextInput.setError(getString(R.string.error_field_required));
                    socialNumberTextInput.requestFocus();

                } else {

                    consultar(socialNumber);
                }

            }
        });



        buttonSelectUbication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUbications();
            }
        });

        /*lectura honeywell*/
        socialNumberTextInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                Log.d(TAG, "Event: "+event.getAction());
                Log.d(TAG, "keycode: "+keyCode);

                if(event.getAction() == 1 && keyCode == 0){

                    getSocialNumberAndExec();
                }

                return false;
            }
        });

        return view;
    }

    public void getSocialNumberAndExec(){

        String socialNumber = socialNumberTextInput.getText().toString();

        Log.d(TAG, "Social number: "+socialNumber);

        // Check for a valid password, if the user entered one.
        if (validateFormRequest()) {

            consultar(socialNumber); //TODO: change for the new method to post the new data.
            socialNumberTextInput.requestFocus();

        }
    }

    public boolean validateFormRequest(){

        // Reset errors.
        socialNumberTextInput.setError(null);

        String socialNumber = socialNumberTextInput.getText().toString();
        Ubication ubication = selectedUbication;

        if (TextUtils.isEmpty(socialNumber)) {

            socialNumberTextInput.setError(getString(R.string.error_field_required));
            socialNumberTextInput.requestFocus();

            return false;

        } else if (ubication == null) {

            socialNumberTextInput.setError("Debe seleccionar una ubicación");
            socialNumberTextInput.setText("");

            return false;

        } else {

            return true;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UbicationSelectedEvent ubicationSelectedEvent) {
        selectedUbication = ubicationSelectedEvent.getUbication();
        Toast.makeText(getContext(), "Ubicación " + selectedUbication.getPosition() + " seleccionado.", Toast.LENGTH_SHORT).show();
        valueUbicationTextView.setText(String.format("%s", selectedUbication.getPosition()));
    }

    public void consultar(String socialNumber){

        consulta_progress.setVisibility(View.VISIBLE);
        consultaForm.setVisibility(View.GONE);
        resultadoLayout.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(getContext());

        //.concat(String.format("api/checkArriving?token=%s&socialNumber=%s&filterUbication=%s",resultado.getToken(), socialNumber, selectedUbication.getPosition()))

        String urlApi = urls.getHost().concat(String.format("api/checkArriving?token=%s", resultado.getToken()));

        JSONObject req = new JSONObject();

        try {
            req.put("socialNumber",socialNumber);
            req.put("filterUbication",selectedUbication.getPosition());
            //req.put("token",resultado.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, urlApi, req, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {

                        //executeResponseJsonApp(response);

                        //hide the keyboard------------------------------------------------
                        //KeyboardUtils.hideKeyboard(getActivity());
                        //-----------------------------------------------------------------


                        consulta_progress.setVisibility(View.GONE);
                        consultaForm.setVisibility(View.VISIBLE);
                        resultadoLayout.setVisibility(View.VISIBLE);

                        socialNumberTextInput.setText("");
                        socialNumberTextInput.clearFocus();

                        JSONObject res = response;

                        Log.d(TAG, "Status de la respuesta: " + res.toString());

                        try {

                            if(res.getBoolean("success")) {
                                JSONObject result = response.getJSONObject("result");
                                Log.d(TAG, "onResponse: " + res.getBoolean("success"));
                                setContentValidation(result, "", true);
                            }else{
                                JSONObject result = new JSONObject();
                                Log.d(TAG, "onResponse: " + res.getString("message"));
                                setContentValidation(result, res.getString("message"), false);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        Log.d(TAG, error.toString());

                        consulta_progress.setVisibility(View.GONE);
                        consultaForm.setVisibility(View.VISIBLE);
                        resultadoLayout.setVisibility(View.VISIBLE);

                        nameVerificationTextView.setText("-");
                        validationTextView.setText("-");
                        detailTextView.setText("-");
                        turnTextViewpublic.setText("-");

                        socialNumberTextInput.setText("");
                        socialNumberTextInput.clearFocus();

                        //despliegue del error en un toast
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }/**/

    public void setContentValidation(JSONObject result, String message, boolean type){

        if(type) {
            try {
                nameVerificationTextView.setText(result.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                validationTextView.setText(result.getBoolean("validation") ? "Puede Ingresar" : "No puede Ingresar");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if(result.getBoolean("validation")){
                    resultadoLayout.setBackgroundResource(R.color.recentLogSuccess);
                }else{
                    resultadoLayout.setBackgroundResource(R.color.recentLogFail);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                detailTextView.setText(result.getString("detail"));
            } catch (JSONException e) {
                e.printStackTrace();
                detailTextView.setText("-");
            }
            try {
                turnTextViewpublic.setText(result.getString("turn"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                setStylesUI(result.getBoolean("validation"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //notificactions
            JSONArray not = null;
            try {
                not = result.getJSONArray("notifications");
                notificationsTextView.setText("");
                if(not.length() > 0){
                    notificationsTextView.append("Notificaciones:\n");
                    for(int i = 0; i < not.length(); i++){
                        JSONObject notification = (JSONObject) not.get(i);
                        notificationsTextView.append((i+1)+". "+notification.getString("message")+"\n");
                    }
                } else {
                    notificationsTextView.setText("-");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            resultadoLayout.setBackgroundResource(R.color.recentLogFail);
            nameVerificationTextView.setText("-");
            validationTextView.setText("-");
            detailTextView.setText("-");
            turnTextViewpublic.setText("-");
            notificationsTextView.setText("-");
            setStylesUI(false);
        }

    }

    public void showAlertDialogResult(JSONObject result){

        try {

            String notifications;

            //notificactions
            JSONArray not = result.getJSONArray("notifications");

            Log.d("not", not.toString());
            Log.d("cant:",String.valueOf(not.length()));

            if(not.length() > 0) {

                notifications = getNotificationsMessage(not);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                alertDialogBuilder.setMessage(notifications);
                alertDialogBuilder.setTitle(NOTIFICACIONES_TITLE);

                alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("AlertDialog", "Pulsado el boton neutral!");
                    }
                });

                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getNotificationsMessage(JSONArray notificationsArray){

        StringBuilder stringBuilder = new StringBuilder();
        String notifications = "";

        try {

            if(notificationsArray.length() > 0){

                for(int i = 0; i < notificationsArray.length(); i++){

                    JSONObject notification;

                    notification = (JSONObject) notificationsArray.get(i);

                    stringBuilder.append(i + 1).append(". ").append(notification.getString("message")).append("\n");
                }

                notifications = stringBuilder.toString();

                Log.d("notifications: ",notifications);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    public void setStylesUI(Boolean ableToEnter){

        int colorSuccess = ContextCompat.getColor(getContext(), R.color.recentLogFontSuccess);
        int colorFail = ContextCompat.getColor(getContext(), R.color.recentLogFontFail);

        nameVerificationTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        validationTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        detailTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        turnTextViewpublic.setTextColor(ableToEnter ? colorSuccess : colorFail);
        notificationsTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
    }


    public void getUbications(){

        consulta_progress.setVisibility(View.VISIBLE);
        buttonSelectUbication.setText(LOAD_LABEL);

        new Header("token", resultado.getToken());

        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        // urls.getHost() + "/api/filterUbications?control=rampa"
        String urlApi = urls.getHost().concat("api/filterUbications?type=rampa"); //?type=rampa &token=  + resultado.getToken()

        //JSONObject req = new JSONObject();

        /*try {
            req.put("token",resultado.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlApi, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {

                        consulta_progress.setVisibility(View.GONE);

                        JSONObject res = response;

                        arrayUbications = new ArrayList<>();

                        Log.d(TAG, "Status de la respuesta: " + res.toString());

                        try {

                            if(res.getBoolean("success")) {

                                JSONArray resArray = res.getJSONArray("result");

                                arrayUbications.clear();

                                for(Integer i = 0; i < resArray.length(); i++){

                                    JSONObject logJson;

                                    Ubication ubication = new Ubication();

                                    logJson = (JSONObject) resArray.get(i);

                                    ubication.setId(logJson.getString("id"));
                                    ubication.setPosition(logJson.getString("position"));
                                    ubication.setAirport(logJson.getString("airport"));

                                    Log.d(TAG, ubication.getPosition());

                                    arrayUbications.add(ubication);
                                }
                                buttonSelectUbication.setText(UBICATION_BUTTON_LABEL);
                                initUbicationsFragment();

                            }else{
                                Toast.makeText(getContext(), res.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }/**/

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        Log.d(TAG, error.toString());
                        consulta_progress.setVisibility(View.GONE);
                        buttonSelectUbication.setText(UBICATION_BUTTON_LABEL);
                        //despliegue del error en un toast
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();

                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        //params.put("content-type", "application/json");
                        params.put("token", resultado.getToken());
                        return params;
                }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void initUbicationsFragment() {

        Bundle bundle = new Bundle();

        bundle.putParcelable("resultado",resultado);
        bundle.putParcelable("urls", urls);
        // bundle.putParcelable("service", service);
        bundle.putParcelableArrayList("arrayUbications", arrayUbications);

        fragmentManager = getActivity().getSupportFragmentManager(); //((AppCompatActivity) context).getSupportFragmentManager();
        // ChangeStatusServiceFragment savedServicesFragment = (ServicesFragment) fragmentManager.findFragmentByTag(SERVICES_FRAGMENT_TAG);
        fragmentTransaction = fragmentManager.beginTransaction();

        assignUbicationFragment = new AssignUbicationFragment();
        assignUbicationFragment.setArguments(bundle);

        fragmentTransaction.add(R.id.contentIngresoMain, assignUbicationFragment, ASSIGN_UBICATIONS_TAG);

        //para poder regresar
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

}
