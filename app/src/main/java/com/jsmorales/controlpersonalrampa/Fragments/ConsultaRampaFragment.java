package com.jsmorales.controlpersonalrampa.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jsmorales.controlpersonalrampa.Events.FlightSelectedEvent;
import com.jsmorales.controlpersonalrampa.Events.PositionSelectedEvent;
import com.jsmorales.controlpersonalrampa.Models.Agent;
import com.jsmorales.controlpersonalrampa.Models.Employee;
import com.jsmorales.controlpersonalrampa.Models.Flight;
import com.jsmorales.controlpersonalrampa.Models.Itinerary;
import com.jsmorales.controlpersonalrampa.Models.Position;
import com.jsmorales.controlpersonalrampa.Models.Resultado;
import com.jsmorales.controlpersonalrampa.Models.UtilsMainApp;
import com.jsmorales.controlpersonalrampa.R;
import com.jsmorales.controlpersonalrampa.utils.FormatDateUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultaRampaFragment extends Fragment {

    private static final String TAG = ConsultaRampaFragment.class.getSimpleName();
    private static final int MY_SOCKET_TIMEOUT_MS = 20000;
    public static final String POSITION_BUTTON_LABEL = "Seleccionar Posición";
    public static final String FLIGHT_BUTTON_LABEL = "Seleccionar Vuelo";
    public static final String LIST_AGENTS_BUTTON_LABEL = "Ver Lista Agentes";
    public static final String LOAD_LABEL = "Cargando...";

    public View consulta_progress;
    public Switch switchInputValue;
    public Button consultaButton;

    public TextInputLayout textInputSocialLayout;
    public EditText socialNumberTextInput;

    public TextView valueFlightsTextView;
    public Button buttonSelectFlight;

    public TextView valuePositionTextView;
    public Button buttonSelectPosition;
    public Button buttonGetPositionList;

    public Itinerary selectedFligth;
    public Position selectedPosition;
    public boolean switchValue = true;

    public Resultado resultado;
    public Employee empleado;
    public UtilsMainApp urls;
    public Context context;

    public ArrayList<Itinerary> itineraryArrayList;
    public ArrayList<Position> positionArrayList;
    public ArrayList<Agent> agentsArrayList;

    public FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;

    public assignFlightFragment assignFlightFragment;
    public AssignPositionFragment assignPositionFragment;
    public AgentsListFragment agentsListFragment;

    public RadioGroup rdgGroupItinerary;
    public String SelectedTypeService = "";

    private static final String ASSIGN_FLIGHT_TAG = "assignFlightFragment";
    private static final String ASSIGN_POSITION_TAG = "assignPositionFragment";
    public static final String AGENTS_LIST_TAG = "agentsListFragment";

    public ConsultaRampaFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consulta_rampa, container, false);

        consulta_progress = view.findViewById(R.id.consulta_progress_rampa);
        switchInputValue = view.findViewById(R.id.switchInputValue);
        consultaButton = view.findViewById(R.id.consultaButton);
        textInputSocialLayout = view.findViewById(R.id.textInputSocialLayout);
        socialNumberTextInput = view.findViewById(R.id.socialNumberTextInput);

        valueFlightsTextView = view.findViewById(R.id.valueFlightsTextView);
        buttonSelectFlight = view.findViewById(R.id.buttonSelectFlight);

        rdgGroupItinerary = view.findViewById(R.id.rdgGroupItinerary);

        buttonSelectFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItinerary(urls.getHostFireBase() + "diaryItinerary.json");
            }
        });

        valuePositionTextView = view.findViewById(R.id.valuePositionTextView);
        buttonSelectPosition = view.findViewById(R.id.buttonSelectPosition);

        buttonSelectPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPositions(urls.getHostFireBase() + "airportPositions.json");
            }
        });

        switchInputValue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchInputValue.setHint(isChecked ? "Entrada" : "Salida");
                consultaButton.setText(isChecked ? "Ingresar" : "Salir");
                switchValue = isChecked;
            }
        });

        consultaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSocialNumberAndExec();
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

        buttonGetPositionList = view.findViewById(R.id.buttonGetPositionList);

        buttonGetPositionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: method for get list
                //urls.getHostFireBase() + "/staffUbicationsRealTime/BOG/" + selectedPosition.getPosition()
                getAgentsOnPosition(urls.getHostFireBase() + "staffUbicationsRealTime/BOG/" + selectedPosition.getPosition() + ".json");
            }
        });

        rdgGroupItinerary.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Log.d(TAG, "Seleccionado check: " + checkedId);

                if(checkedId == R.id.rdbComplete){
                    SelectedTypeService = "Redondo";
                } else if (checkedId == R.id.rdbArriving) {
                    SelectedTypeService = "Llegando";
                } else if (checkedId == R.id.rdbDeparture) {
                    SelectedTypeService = "Saliendo";
                }

                Log.d(TAG, "Seleccionado check: " + checkedId);
                Log.d(TAG, "Seleccionado check: " + SelectedTypeService);

                Toast.makeText(getContext(), "Tipo vuelo: " + SelectedTypeService, Toast.LENGTH_SHORT).show();
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
        Itinerary vuelo = selectedFligth;
        Position position =  selectedPosition;
        String selectedServiceType = SelectedTypeService;

        if (TextUtils.isEmpty(socialNumber)) {

            socialNumberTextInput.setError(getString(R.string.error_field_required));
            socialNumberTextInput.requestFocus();

            return false;

        } else if (vuelo == null) {

            socialNumberTextInput.setError("Debe seleccionar un vuelo");
            socialNumberTextInput.setText("");

            return false;

        } else if (position == null) {

            socialNumberTextInput.setError("Debe seleccionar una posición");
            socialNumberTextInput.setText("");

            return false;

        } else if (TextUtils.isEmpty(selectedServiceType)){

            socialNumberTextInput.setError("Debe seleccionar un tipo de servicio");
            socialNumberTextInput.setText("");

            return false;

        } else {

            return true;

        }
    }

    public void consultar(String socialNumber){

        consulta_progress.setVisibility(View.VISIBLE);
        socialNumberTextInput.setEnabled(false);
        socialNumberTextInput.setFocusable(false);
        socialNumberTextInput.clearFocus();


        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        String urlApi = urls.getHost().concat("api/staff-control/checkAttendance");

        JSONObject req = new JSONObject();

        try {

            req.put("socialNumber", socialNumber);
            req.put("positionId", selectedPosition.getId());
            req.put("itineraryId", selectedFligth.getId());
            req.put("input", switchValue);
            req.put("typeService", SelectedTypeService);
            req.put("token",resultado.getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, urlApi, req, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {


                        consulta_progress.setVisibility(View.GONE);


                        socialNumberTextInput.setText("");
                        socialNumberTextInput.setEnabled(true);
                        socialNumberTextInput.setFocusable(true);
                        socialNumberTextInput.setFocusableInTouchMode(true);
                        socialNumberTextInput.requestFocus();

                        JSONObject res = response;

                        Log.d(TAG, "Status de la respuesta: " + res.toString());

                        try {
                            if(res.getBoolean("success")){

                                //simpleAlertDialogMessage(res.getString("message"));
                                createCustomAlert(res.getString("message"), true).show();

                            }else{
                                //simpleAlertDialogMessage(res.getString("message"));
                                createCustomAlert(res.getString("message"), false).show();
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

                        socialNumberTextInput.setText("");
                        socialNumberTextInput.requestFocus();
                        socialNumberTextInput.setFocusable(true);
                        socialNumberTextInput.setFocusableInTouchMode(true);
                        socialNumberTextInput.setEnabled(true);

                        //despliegue del error en un toast
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                        simpleAlertDialogMessage("Hubo un error en la peticiòn - ["+error.toString()+"]");

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }/**/

    public AlertDialog createCustomAlert(String message, boolean validation){

        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.alert_dialog_response_layout, null);
        //builder.setView(inflater.inflate(R.layout.dialog_signin, null))

        Button btnGeneric = v.findViewById(R.id.buttonOkGeneric);
        Button buttonIcSignal = v.findViewById(R.id.buttonIcSignal);
        TextView messageTextView = v.findViewById(R.id.messageTextView);

        buttonIcSignal.setBackgroundResource(validation ? R.drawable.ic_check : R.drawable.ic_close);
        messageTextView.setText(message);

        builder.setView(v);

        alertDialog = builder.create();

        btnGeneric.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                }
        );

        return alertDialog;

    }

    public void simpleAlertDialogMessage(String message){

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("Control Personal - SAI");

        alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("AlertDialog", "Pulsado el boton neutral!");
            }
        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
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
    public void onMessageEvent(FlightSelectedEvent flightSelectedEvent) {
        selectedFligth = flightSelectedEvent.getItinerary();
        Toast.makeText(getContext(), "Vuelo [" + selectedFligth.getArrivingFlightNumber() + " X " + selectedFligth.getDepartureFlightNumber() + "] seleccionado.", Toast.LENGTH_SHORT).show();
        valueFlightsTextView.setText(String.format("ARR[%s - ORG(%s)]  DEP[%s - DEST(%s)]",
                selectedFligth.getArrivingFlightNumber(), selectedFligth.getArrivingFlightOrigin(),
                selectedFligth.getDepartureFlightNumber(), selectedFligth.getDepartureFlightDestiny()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PositionSelectedEvent positionSelectedEvent) {
        selectedPosition = positionSelectedEvent.getPosition();
        Toast.makeText(getContext(), "Posición " + selectedPosition.getPosition() + " seleccionado.", Toast.LENGTH_SHORT).show();
        valuePositionTextView.setText(String.format("%s - Tipo:%s", selectedPosition.getPosition(), selectedPosition.getType()));

        buttonGetPositionList.setVisibility(View.VISIBLE);
    }

    public String formatDateValidation(String date) {

        String[] dateSplitted = date.split("-");
        String validMont = "";
        String finalDate = "";

        Log.d(TAG, "Tan dateSplitted: " + dateSplitted[1].length());

        if(dateSplitted[1].length() > 3){

            validMont = dateSplitted[1].substring(0,3);

            Log.d(TAG, "validMont: " + validMont);

            dateSplitted[1] = validMont;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                finalDate = String.join("-", dateSplitted);

            } else {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    StringJoiner joiner = new StringJoiner("-");
                    finalDate = joiner.add(dateSplitted[0]).add(dateSplitted[1]).add(dateSplitted[2]).toString();
                }

            }

        } else {

            finalDate = date;

        }

        return finalDate;
    }

    public void getItinerary(String urlApi){

        consulta_progress.setVisibility(View.VISIBLE);
        buttonSelectFlight.setText(LOAD_LABEL);

        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        JSONObject req = new JSONObject();

        try {
            //req.put("token",resultado.getToken());
            req.put("Json", true);
            String dateActualFormated = FormatDateUtil.getActualDateFormatted("d-MMM-YY");
            dateActualFormated = dateActualFormated.replace(".", "");

            //req.put("orderBy", "\"date\"");
            //req.put("equalTo", "\""+dateActualFormated+"\"");

            urlApi = urlApi + "?orderBy=\"date\"&equalTo=\"" + formatDateValidation(dateActualFormated.toLowerCase()) + "\"";

            Log.d(TAG, "Format Date Request: " + dateActualFormated);
            Log.d(TAG, "Format Date with validation: " + formatDateValidation(dateActualFormated));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG + " -> pet itinerary.", req.toString() + urlApi);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlApi, req, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {

                        consulta_progress.setVisibility(View.GONE);

                        JSONObject res = response;
                        Itinerary itinerary;

                        itineraryArrayList = new ArrayList<>();

                        Log.d(TAG, "Response itinerary: " + res.toString());
                        Log.d(TAG, "Length result: " + res.length());

                        try {

                            if (res.length() > 0) {

                                Iterator<String> keys = res.keys();

                                itineraryArrayList.clear();

                                while(keys.hasNext()) {

                                    String key = keys.next();

                                    if (res.get(key) instanceof JSONObject) {
                                        // do something with jsonObject here
                                        JSONObject val = (JSONObject) res.get(key);

                                        Log.d(TAG, "En " + key + ": " + val.toString());

                                        itinerary = new Itinerary();

                                        itinerary.setAircraftId(val.getString("aircraftId"));
                                        itinerary.setAircraftType(val.getString("aircraftType"));
                                        itinerary.setDate(val.getString("date"));
                                        itinerary.setAirlineCode(val.getString("airlineCode"));
                                        itinerary.setId(key);
                                        itinerary.setArrivingFlightDestiny(val.getString("arrivingFlightDestiny"));
                                        itinerary.setArrivingFlightNumber(val.getString("arrivingFlightNumber"));
                                        itinerary.setArrivingFlightOrigin(val.getString("arrivingFlightOrigin"));
                                        itinerary.setArrivingTime(val.getString("arrivingTime"));
                                        itinerary.setDepartureFlightDestiny(val.getString("departureFlightDestiny"));
                                        itinerary.setDepartureFlightNumber(val.getString("departureFlightNumber"));
                                        itinerary.setDepartureFlightOrigin(val.getString("departureFlightOrigin"));
                                        itinerary.setDepartureTime(val.getString("departureTime"));

                                        Log.d(TAG, itinerary.getAircraftId());

                                        itineraryArrayList.add(itinerary);
                                    }

                                }

                                initFlightFragment();
                                buttonSelectFlight.setText(FLIGHT_BUTTON_LABEL);
                                //---------------------------------------------------------

                            } else {
                                buttonSelectFlight.setText(FLIGHT_BUTTON_LABEL);
                                Toast.makeText(getContext(), "No hay ningún resultado", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            buttonSelectFlight.setText(FLIGHT_BUTTON_LABEL);
                            e.printStackTrace();
                        }/**/


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        Log.d(TAG, error.toString());

                        // consultaProgressMain.setVisibility(View.GONE);
                        // EventBus.getDefault().post(new progressServicesFragment(false));

                        //despliegue del error en un toast
                        //Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
                        buttonSelectFlight.setText(FLIGHT_BUTTON_LABEL);
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

    public void initFlightFragment() {

        Bundle bundle = new Bundle();

        bundle.putParcelable("resultado",resultado);
        bundle.putParcelable("urls", urls);
        // bundle.putParcelable("service", service);
        bundle.putParcelableArrayList("itineraryArrayList", itineraryArrayList);

        fragmentManager = getActivity().getSupportFragmentManager(); //((AppCompatActivity) context).getSupportFragmentManager();
        // ChangeStatusServiceFragment savedServicesFragment = (ServicesFragment) fragmentManager.findFragmentByTag(SERVICES_FRAGMENT_TAG);
        fragmentTransaction = fragmentManager.beginTransaction();

        assignFlightFragment = new assignFlightFragment();
        assignFlightFragment.setArguments(bundle);

        fragmentTransaction.add(R.id.contentRampaMain, assignFlightFragment, ASSIGN_FLIGHT_TAG);

        //para poder regresar
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void getPositions(String urlApi){

        consulta_progress.setVisibility(View.VISIBLE);
        buttonSelectPosition.setText(LOAD_LABEL);

        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        JSONObject req = new JSONObject();

        try {
            //req.put("token",resultado.getToken());
            req.put("Json", true);

            urlApi = urlApi + "?orderBy=\"region\"&equalTo=\"BOG\"";

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG + " -> pet positions.", req.toString() + urlApi);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlApi, req, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {

                        consulta_progress.setVisibility(View.GONE);

                        JSONObject res = response;
                        Position position;

                        positionArrayList = new ArrayList<>();

                        Log.d(TAG, "Response positions: " + res.toString());
                        Log.d(TAG, "Length result: " + res.length());

                        try {

                            if (res.length() > 0) {

                                Iterator<String> keys = res.keys();

                                positionArrayList.clear();

                                while(keys.hasNext()) {

                                    String key = keys.next();

                                    if (res.get(key) instanceof JSONObject) {
                                        // do something with jsonObject here
                                        JSONObject val = (JSONObject) res.get(key);

                                        Log.d(TAG, "En " + key + ": " + val.toString());

                                        position = new Position();

                                        position.setLatitude(val.getString("latitude"));
                                        position.setLongitude(val.getString("longitude"));
                                        position.setPosition(val.getString("position"));
                                        position.setRegion(val.getString("region"));
                                        position.setId(key);
                                        position.setType(val.getString("type"));

                                        Log.d(TAG, position.getPosition());

                                        positionArrayList.add(position);
                                    }

                                }
                                //---------------------------------------------------------
                                initPositionFragment();
                                buttonSelectPosition.setText(POSITION_BUTTON_LABEL);

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

                        // consultaProgressMain.setVisibility(View.GONE);
                        // EventBus.getDefault().post(new progressServicesFragment(false));

                        //despliegue del error en un toast
                        //Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

    public void initPositionFragment() {

        Bundle bundle = new Bundle();

        bundle.putParcelable("resultado",resultado);
        bundle.putParcelable("urls", urls);
        // bundle.putParcelable("service", service);
        bundle.putParcelableArrayList("positionArrayList", positionArrayList);

        fragmentManager = getActivity().getSupportFragmentManager(); //((AppCompatActivity) context).getSupportFragmentManager();
        // ChangeStatusServiceFragment savedServicesFragment = (ServicesFragment) fragmentManager.findFragmentByTag(SERVICES_FRAGMENT_TAG);
        fragmentTransaction = fragmentManager.beginTransaction();

        assignPositionFragment = new AssignPositionFragment();
        assignPositionFragment.setArguments(bundle);

        fragmentTransaction.add(R.id.contentRampaMain, assignPositionFragment, ASSIGN_POSITION_TAG);

        //para poder regresar
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void getAgentsOnPosition(String urlApi){

        consulta_progress.setVisibility(View.VISIBLE);
        buttonGetPositionList.setText(LOAD_LABEL);

        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        JSONObject req = new JSONObject();

        try {
            //req.put("token",resultado.getToken());
            req.put("Json", true);
            //String dateActualFormated = FormatDateUtil.getActualDateFormatted("d-MMM-YY");
            //dateActualFormated = dateActualFormated.replace(".", "");

            //req.put("orderBy", "\"date\"");
            //req.put("equalTo", "\""+dateActualFormated+"\"");

            //urlApi = urlApi + "?orderBy=\"date\"&equalTo=\"" + dateActualFormated.toLowerCase() + "\"";

            //Log.d(TAG, "Format Date Request: " + dateActualFormated);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG + "agents per position.", req.toString() + urlApi);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlApi, req, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {

                        consulta_progress.setVisibility(View.GONE);

                        Log.d(TAG, "Response agents pura: " + response.toString());

                        try {

                            JSONObject res = response;
                            Agent agent;

                            agentsArrayList = new ArrayList<>();

                            Log.d(TAG, "Response agents per position: " + res.toString());
                            Log.d(TAG, "Length result: " + res.length());

                            if (res.length() > 0) {

                                Iterator<String> keys = res.keys();

                                agentsArrayList.clear();

                                while(keys.hasNext()) {

                                    String key = keys.next();

                                    Log.d(TAG, "onResponseNamekey: " + key);

                                    if (key.equals("agentsJobs")) {

                                        if (res.get(key) instanceof JSONObject) {
                                            // do something with jsonObject here
                                            JSONObject val = (JSONObject) res.get(key);

                                            Log.d(TAG, "En " + key + ": " + val.toString());

                                            iterateAgentsJobs(val);
                                        }

                                    }

                                }

                                initAgentsListFragment();
                                buttonGetPositionList.setText(LIST_AGENTS_BUTTON_LABEL);
                                //---------------------------------------------------------

                            } else {
                                buttonGetPositionList.setText(LIST_AGENTS_BUTTON_LABEL);
                                Toast.makeText(getContext(), "No hay ningún resultado", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            buttonGetPositionList.setText(LIST_AGENTS_BUTTON_LABEL);
                            e.printStackTrace();
                        }/**/


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        Log.d(TAG, error.toString());

                        // consultaProgressMain.setVisibility(View.GONE);
                        // EventBus.getDefault().post(new progressServicesFragment(false));

                        //despliegue del error en un toast
                        //Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
                        consulta_progress.setVisibility(View.GONE);
                        buttonGetPositionList.setText(LIST_AGENTS_BUTTON_LABEL);
                        //Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "No hay agentes en esta posición.", Toast.LENGTH_SHORT).show();

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

    public void iterateAgentsJobs(JSONObject resObject) {

        Iterator<String> keys = resObject.keys();

        Agent agent;

        while(keys.hasNext()) {

            String key = keys.next();

            //Log.d(TAG, "iterateAgentsJobs key -> " +  key);

            try {

                //Log.d(TAG, resObject.get(key).toString());

                JSONArray arrayAgents = (JSONArray) resObject.get(key);

                for(int i = 0; i < arrayAgents.length(); i++) {

                    if (arrayAgents.get(i) instanceof JSONObject) {
                        // do something with jsonObject here
                        JSONObject val = (JSONObject) arrayAgents.get(i);

                        Log.d(TAG, "En " + key + ": " + val.toString());

                        agent = new Agent();

                        agent.setFirstName(val.getString("firstName"));
                        agent.setLastName(val.getString("lastName"));
                        agent.setPosition(key);
                        agent.setSocialNumber(val.getString("socialNumber"));

                        Log.d(TAG, agent.getFirstName());

                        agentsArrayList.add(agent);/**/
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        //---------------------------------------------------------
    }

    public void initAgentsListFragment() {

        Bundle bundle = new Bundle();

        bundle.putParcelable("resultado",resultado);
        bundle.putParcelable("urls", urls);
        // bundle.putParcelable("service", service);
        bundle.putParcelableArrayList("agentsArrayList", agentsArrayList);

        fragmentManager = getActivity().getSupportFragmentManager(); //((AppCompatActivity) context).getSupportFragmentManager();
        // ChangeStatusServiceFragment savedServicesFragment = (ServicesFragment) fragmentManager.findFragmentByTag(SERVICES_FRAGMENT_TAG);
        fragmentTransaction = fragmentManager.beginTransaction();

        agentsListFragment = new AgentsListFragment();
        agentsListFragment.setArguments(bundle);

        fragmentTransaction.add(R.id.contentRampaMain, agentsListFragment, AGENTS_LIST_TAG);

        //para poder regresar
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

}
