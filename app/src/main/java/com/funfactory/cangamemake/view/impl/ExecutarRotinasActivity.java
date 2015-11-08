package com.funfactory.cangamemake.view.impl;

import android.os.Bundle;

import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.model.entity.Rotina;
import com.funfactory.cangamemake.presenter.IExecutarRotinaPresenter;
import com.funfactory.cangamemake.presenter.IGenericActivity;
import com.funfactory.cangamemake.presenter.impl.ExecutarRotinaPresenter;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.view.IExecutarRotinaActivity;

/**
 * Activity de Execução de Rotinas.
 * 
 * @author Silvino Neto.
 * 
 */
public class ExecutarRotinasActivity extends GenericActivity implements IExecutarRotinaActivity {

    private IExecutarRotinaPresenter mExecutarRotinaPresenter = new ExecutarRotinaPresenter();

    private Rotina                   rotina;
    private Paciente                 paciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setParentDependencies(this, mExecutarRotinaPresenter);
        mExecutarRotinaPresenter.setView(this);

        rotina = (Rotina) getIntent().getExtras().get(Constants.ROTINA);
        mExecutarRotinaPresenter.setRotina(rotina);
        paciente = (Paciente) getIntent().getExtras().get(Constants.PACIENTE) ;
        mExecutarRotinaPresenter.setPaciente(paciente);

        configureActions();

        super.onCreate(savedInstanceState);
    }

    private void configureActions() {
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
    }

    @Override
    public void reproduzir() {
        mExecutarRotinaPresenter.reproduzir();
    }

    @Override
    public IGenericActivity getGenericMethods() {
        return this;
    }

}
