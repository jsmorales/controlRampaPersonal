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

import com.jsmorales.controlpersonalrampa.Adapters.AgentsListAdapter;
import com.jsmorales.controlpersonalrampa.Events.CloseAgentsListFragment;
import com.jsmorales.controlpersonalrampa.Models.Agent;
import com.jsmorales.controlpersonalrampa.Models.Resultado;
import com.jsmorales.controlpersonalrampa.Models.UtilsMainApp;
import com.jsmorales.controlpersonalrampa.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgentsListFragment extends Fragment {

    private static final String TAG = AgentsListFragment.class.getSimpleName();
    private static final int MY_SOCKET_TIMEOUT_MS = 20000;

    ArrayList<Agent> agentsArrayList;
    UtilsMainApp urls;
    Resultado resultado;
    RecyclerView recyclerView;

    Button buttonCancelAssign;


    public AgentsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //para obtener los arguments
        Bundle bundle = getArguments();

        if (bundle != null) {
            agentsArrayList = bundle.getParcelableArrayList("agentsArrayList");
            urls = bundle.getParcelable("urls");
            resultado = bundle.getParcelable("resultado");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "Create view Assign Position");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agents_list, container, false);

        recyclerView = view.findViewById(R.id.agentsListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AgentsListAdapter agentsListAdapter = new AgentsListAdapter(agentsArrayList,getContext(),urls,resultado);
        recyclerView.setAdapter(agentsListAdapter);/**/

        buttonCancelAssign = view.findViewById(R.id.buttonCancelAssign);
        buttonCancelAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click cancel!");
                closeChangeStatusFragment();
            }
        });

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
    public void onMessageEvent(CloseAgentsListFragment closeAgentsListFragment) {
        closeChangeStatusFragment();
    }

}
