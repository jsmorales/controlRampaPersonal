package com.jsmorales.controlpersonalrampa;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jsmorales.controlpersonalrampa.Fragments.ConsultaIngresoFragment;
import com.jsmorales.controlpersonalrampa.Fragments.ConsultaRampaFragment;
import com.jsmorales.controlpersonalrampa.Models.Employee;
import com.jsmorales.controlpersonalrampa.Models.Resultado;
import com.jsmorales.controlpersonalrampa.Models.UtilsMainApp;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    Resultado resultado;
    Employee empleado;
    UtilsMainApp url;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, Resultado resultado, Employee empleado, UtilsMainApp url) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.resultado = resultado;
        this.empleado = empleado;
        this.url = url;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();

        bundle.putParcelable("resultado",resultado);
        bundle.putParcelable("empleado",empleado);
        bundle.putParcelable("urls", url);

        switch (position) {
            case 0:
                ConsultaIngresoFragment consultaIngresoFragment = new ConsultaIngresoFragment();
                consultaIngresoFragment.setArguments(bundle);
                return consultaIngresoFragment;
            case 1:
                ConsultaRampaFragment consultaRampaFragment = new ConsultaRampaFragment();
                consultaRampaFragment.setArguments(bundle);
                return consultaRampaFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
