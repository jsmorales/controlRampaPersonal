package com.jsmorales.controlpersonalrampa.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.jsmorales.controlpersonalrampa.Adapters.assignnFlightAdapter;
import com.jsmorales.controlpersonalrampa.Events.CloseAssignFligthFragment;
import com.jsmorales.controlpersonalrampa.Events.FlightSelectedEvent;
import com.jsmorales.controlpersonalrampa.Models.Itinerary;
import com.jsmorales.controlpersonalrampa.Models.Resultado;
import com.jsmorales.controlpersonalrampa.Models.UtilsMainApp;
import com.jsmorales.controlpersonalrampa.R;
import com.jsmorales.controlpersonalrampa.utils.ContainsString;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class assignFlightFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String TAG = assignFlightFragment.class.getSimpleName();
    //public static final String BUTTON_LABEL = "Asignar";
    private static final int MY_SOCKET_TIMEOUT_MS = 20000;

    ArrayList<Itinerary> itineraryArrayList;
    UtilsMainApp urls;
    Resultado resultado;
    RecyclerView recyclerView; //itineraryListRecyclerView

    Button buttonCancelAssign;
    SearchView itinerarySearchView;

    public assignFlightFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //para obtener los arguments
        Bundle bundle = getArguments();

        if (bundle != null) {
            itineraryArrayList = bundle.getParcelableArrayList("itineraryArrayList");
            urls = bundle.getParcelable("urls");
            resultado = bundle.getParcelable("resultado");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "Create view Assign Flight");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assign_flight, container, false);

        recyclerView = view.findViewById(R.id.itineraryListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        assignnFlightAdapter assignnFlightAdapter = new assignnFlightAdapter(itineraryArrayList,getContext(),urls,resultado);
        recyclerView.setAdapter(assignnFlightAdapter);/**/

        buttonCancelAssign = view.findViewById(R.id.buttonCancelAssign);
        buttonCancelAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click cancel!");
                closeChangeStatusFragment();
            }
        });

        itinerarySearchView = view.findViewById(R.id.itinerarySearchView);
        itinerarySearchView.setQueryHint("Buscar Vuelo");
        itinerarySearchView.setOnQueryTextListener(this);

        return view;
    }

    public void closeChangeStatusFragment() {
        FragmentManager fm = ((AppCompatActivity) Objects.requireNonNull(getContext())).getSupportFragmentManager();

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            Log.i(TAG, "popping backstack");
        } else {
            Log.i(TAG, "nothing on backstack, calling super");
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
    public void onMessageEvent(CloseAssignFligthFragment closeAssignFligthFragment) {
        closeChangeStatusFragment();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        ArrayList<Itinerary> itineraryFiltered = new ArrayList<>();

        if( (itineraryArrayList != null) && (!newText.equals("")) ) {

            Log.d(TAG, "Buscando: " + newText);

            for (Itinerary itinerary : itineraryArrayList) {

                //Log.d(TAG, "Nombre: " + service.getPaxName());
                //Log.d(TAG, "Contiene: " + service.getPaxName().contains(text));

                if (ContainsString.containsIgnoreCase(itinerary.getArrivingFlightNumber(),newText) || ContainsString.containsIgnoreCase(itinerary.getDepartureFlightNumber(),newText) || ContainsString.containsIgnoreCase(itinerary.getAircraftType(),newText)) {
                    itineraryFiltered.add(itinerary);
                } else {
                    continue;
                }
            }

            putItineraryArrayList(itineraryFiltered);

        } else {
            putItineraryArrayList(itineraryArrayList);
        }

        return false;
    }

    private void putItineraryArrayList(ArrayList<Itinerary> list) {

        assignnFlightAdapter assignnFlightAdapter = new assignnFlightAdapter(list,getContext(),urls,resultado);
        recyclerView.setAdapter(assignnFlightAdapter);

    }

}
