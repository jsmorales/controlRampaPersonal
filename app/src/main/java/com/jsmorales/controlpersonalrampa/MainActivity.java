package com.jsmorales.controlpersonalrampa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.jsmorales.controlpersonalrampa.Models.Employee;
import com.jsmorales.controlpersonalrampa.Models.Respuesta;
import com.jsmorales.controlpersonalrampa.Models.Resultado;
import com.jsmorales.controlpersonalrampa.Models.UtilsMainApp;
import com.jsmorales.controlpersonalrampa.utils.ConnectivityReceiver;
import com.jsmorales.controlpersonalrampa.utils.MyApplication;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CONSULTA_FRAGMENT_TAG = "consultaFragmentTAG";
    private static final String INCOME_RECENTS_FRAGMENT_TAG = "income_recents_fragment_tag";
    public static final String MIME_TEXT_PLAIN = "text/plain";
    private static final int MY_SOCKET_TIMEOUT_MS = 20000;

    public TextView userNameTextViewToolBar;
    public TextView positionTextViewToolBar;

    public Respuesta respuesta;
    public Resultado resultado;
    public Employee empleado;
    public UtilsMainApp urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SAI - Control Personal");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //-----------------------------------------------------------------------------------------
        //bind navigation view
        View hView =  navigationView.getHeaderView(0);
        userNameTextViewToolBar = hView.findViewById(R.id.userNameTextViewToolBar);
        positionTextViewToolBar = hView.findViewById(R.id.positionTextViewToolBar);

        respuesta = (Respuesta) Objects.requireNonNull(getIntent().getExtras()).get("respuesta");
        resultado = (Resultado) getIntent().getExtras().get("resultado");
        empleado = (Employee) getIntent().getExtras().get("empleado");
        urls = (UtilsMainApp) getIntent().getExtras().get("urls");

        if(respuesta != null){

            Log.d(TAG,  "respuesta sigue: "+respuesta.getMessage());
            Log.d(TAG,  "resultado sigue: "+resultado.getToken());
            Log.d(TAG,  "empleado sigue: "+empleado.getFirstName());
            Log.d(TAG,  "empleado filterUbication: "+empleado.getFilterUbication());

            Log.d(TAG,  "url que debe ejecutar: "+urls.getHost());

            CharSequence name = empleado.getFirstName()+" "+empleado.getLastName();

            userNameTextViewToolBar.setText(name);
            positionTextViewToolBar.setText(empleado.getPosition());

            //initialize main fragment
            //initMainFragment();

            //-----------------------------------------------------------------------------------------
            //define a tab layout
            TabLayout tabLayout = findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText("Control Ingreso")); //add a tab
            tabLayout.addTab(tabLayout.newTab().setText("Control Rampa"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

            final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), resultado, empleado, urls);

            viewPager.setAdapter(adapter);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            //-----------------------------------------------------------------------------------------
        }
        //-----------------------------------------------------------------------------------------

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "OnResume()");
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_close_session) {

            closeSession();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void closeSession(){

        respuesta = null;

        Intent intentCloseSession = new Intent(this, LoginActivity.class);

        //se deberia tener una instancia de LoginActivity para poder dejar null la respuesta

        startActivity(intentCloseSession);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.d(TAG, "La conexion cambió");
        checkConnection();
    }

    public void checkConnection(){
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    public void showSnack(boolean isConnected){

        String message;
        int color;

        if(isConnected){
            message = "Conectado a internet!";
            color = Color.GREEN;
        }else{
            message = "Conexion a internet perdida!";
            color = Color.RED;
        }

        Snackbar snack = Snackbar.make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);
        View sbView = snack.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snack.show();
    }
}
