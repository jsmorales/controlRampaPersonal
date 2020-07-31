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
import com.jsmorales.controlpersonalrampa.Events.CloseAssignPositionFragment;
import com.jsmorales.controlpersonalrampa.Events.FlightSelectedEvent;
import com.jsmorales.controlpersonalrampa.Events.PositionSelectedEvent;
import com.jsmorales.controlpersonalrampa.Models.Itinerary;
import com.jsmorales.controlpersonalrampa.Models.Position;
import com.jsmorales.controlpersonalrampa.Models.Resultado;
import com.jsmorales.controlpersonalrampa.Models.UtilsMainApp;
import com.jsmorales.controlpersonalrampa.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

//5. se extiende la clase de --> extends RecyclerView.Adapter<Nombredelaclase.MyViewHolder>
public class AssignPositionAdapter extends RecyclerView.Adapter<AssignPositionAdapter.MyViewHolder> {

    private static final String TAG = AssignPositionAdapter.class.getSimpleName();
    private static final int MY_SOCKET_TIMEOUT_MS = 20000;

    //7.se crea un ArrayList de tipo de objeto que se le va a pasar a la lista y una variable tipo Context
    ArrayList<Position> positionArrayList;
    UtilsMainApp urls;
    Resultado resultado;
    Context context;

    //9. se crea el constructor del adapter
    public AssignPositionAdapter(ArrayList<Position> positionArrayList, Context context, UtilsMainApp urls, Resultado resultado){
        this.positionArrayList = positionArrayList;
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

        Log.d(TAG, positionArrayList.toString());

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_assign_position_item, viewGroup, false);

        MyViewHolder assignViewHolder = new MyViewHolder(view);

        return assignViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        //10. se hace el bind de datos con la layout elemento por elemento
        final Position positionObjt = positionArrayList.get(i); //se obtiene el objeto del ArrayList en esa posicion

        myViewHolder.positionTextView.setText(String.format("%s - Tipo:%s", positionObjt.getPosition(), positionObjt.getType()));
        myViewHolder.positionFlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Click seleccionar desde adapter" + positionObjt.getId());
                EventBus.getDefault().post(new PositionSelectedEvent(positionObjt));
                EventBus.getDefault().post(new CloseAssignPositionFragment());
            }
        });
    }

    @Override
    public int getItemCount() {
        //8. se regresa el size del ArrayList
        return positionArrayList.size();
    }

    //1. primero se crea la clase viewHolder que extiende de RecyclerView.ViewHolder
    static public class MyViewHolder extends RecyclerView.ViewHolder{

        //3. se añaden los elementos del layout item
        public TextView positionTextView;
        public Button positionFlightButton;

        //2. se autocompleta el constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //4. se hace el bind de los elementos del layout item desde la view itemView
            positionTextView = itemView.findViewById(R.id.positionTextView);
            positionFlightButton = itemView.findViewById(R.id.positionPositionButton);

        }
    }
}
