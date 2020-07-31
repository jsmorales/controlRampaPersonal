package com.jsmorales.controlpersonalrampa.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jsmorales.controlpersonalrampa.Events.CloseAssignFligthFragment;
import com.jsmorales.controlpersonalrampa.Events.FlightSelectedEvent;
import com.jsmorales.controlpersonalrampa.Models.Itinerary;
import com.jsmorales.controlpersonalrampa.Models.Resultado;
import com.jsmorales.controlpersonalrampa.Models.UtilsMainApp;
import com.jsmorales.controlpersonalrampa.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

//5. se extiende la clase de --> extends RecyclerView.Adapter<Nombredelaclase.MyViewHolder>
public class assignnFlightAdapter extends RecyclerView.Adapter<assignnFlightAdapter.MyViewHolder> {

    private static final String TAG = assignnFlightAdapter.class.getSimpleName();
    private static final int MY_SOCKET_TIMEOUT_MS = 20000;

    //7.se crea un ArrayList de tipo de objeto que se le va a pasar a la lista y una variable tipo Context
    ArrayList<Itinerary> itineraryArrayList;
    UtilsMainApp urls;
    Resultado resultado;
    Context context;

    //9. se crea el constructor del adapter
    public assignnFlightAdapter(ArrayList<Itinerary> itineraryArrayList, Context context, UtilsMainApp urls, Resultado resultado){
        this.itineraryArrayList = itineraryArrayList;
        this.urls = urls;
        this.resultado = resultado;
        this.context = context;
    }

    //6. se implementan los metodos con alt + enter
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //11. se instancia el inflater con el layout que se va a ocupar por cada item mas el layout que ocupa el contexto
        //y se retorna esta vista instanciando la vista view holder

        Log.d(TAG, itineraryArrayList.toString());

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_assign_flight_item, viewGroup, false);

        MyViewHolder assignViewHolder = new MyViewHolder(view);

        return assignViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        //10. se hace el bind de datos con la layout elemento por elemento
        final Itinerary itineraryObjt = itineraryArrayList.get(i); //se obtiene el objeto del ArrayList en esa posicion
        myViewHolder.flightRouteTextView.setText(String.format("ARR[%s-ORG(%s)]",
                itineraryObjt.getArrivingFlightNumber(), itineraryObjt.getArrivingFlightOrigin()));

        myViewHolder.flightRouteDepTextView.setText((String.format("DEP[%s-DEST(%s)]", itineraryObjt.getDepartureFlightNumber(),  itineraryObjt.getDepartureFlightDestiny())));

        myViewHolder.equipTextView.setText(String.format("Equipo: %s -- Cliente: %s", itineraryObjt.getAircraftType(), itineraryObjt.getAirlineCode()));
        myViewHolder.assignFlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Click seleccionar desde adapter" + itineraryObjt.getId());
                EventBus.getDefault().post(new FlightSelectedEvent(itineraryObjt));
                EventBus.getDefault().post(new CloseAssignFligthFragment());
            }
        });
    }

    @Override
    public int getItemCount() {
        //8. se regresa el size del ArrayList
        return itineraryArrayList.size();
    }

    //1. primero se crea la clase viewHolder que extiende de RecyclerView.ViewHolder
    static public class MyViewHolder extends RecyclerView.ViewHolder{

        //3. se añaden los elementos del layout item
        public TextView flightRouteTextView;
        public TextView flightRouteDepTextView;
        public TextView equipTextView;
        public Button assignFlightButton;

        //2. se autocompleta el constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //4. se hace el bind de los elementos del layout item desde la view itemView
            flightRouteTextView = itemView.findViewById(R.id.flightRouteTextView);
            flightRouteDepTextView = itemView.findViewById(R.id.flightRouteDepTextView);
            equipTextView = itemView.findViewById(R.id.equipTextView);
            assignFlightButton = itemView.findViewById(R.id.assignFlightButton);

        }
    }
}
