package com.funfactory.cangamemake.presenter.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.IConfigModel;
import com.funfactory.cangamemake.model.IPacienteModel;
import com.funfactory.cangamemake.model.impl.ConfigModel;
import com.funfactory.cangamemake.model.impl.PacienteModel;
import com.funfactory.cangamemake.presenter.IStartActivityPresenter;
import com.funfactory.cangamemake.view.impl.ListarPacienteActivity;

public class StartActivityPresenter extends GenericPresenter implements IStartActivityPresenter {

    private static final int ACAO_SELECIONAR_PACIENTE       = 101;

    IBaseActivityInterface   mView;
    IConfigModel             mConfigModel                   = new ConfigModel();
    IPacienteModel           mPacienteModel                 = new PacienteModel();

    private Activity         mContext;

    @Override
    public void setView(IBaseActivityInterface view) {
        mView = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = mView.getGenericMethods().getContext();

        selecionarPaciente();
        mView.getGenericMethods().finalizarActivity();

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void selecionarPaciente() {
        Intent intent = new Intent(mContext, ListarPacienteActivity.class);
        mView.getGenericMethods().toastMessage(R.string.choose_a_pacient);
        mView.getGenericMethods().getContext().startActivityForResult(intent, ACAO_SELECIONAR_PACIENTE);
    }

}
