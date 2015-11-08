package com.funfactory.cangamemake.presenter;

import android.view.View;

import com.funfactory.cangamemake.view.IListarActivity;

public interface IListarPacientePresenter {

    /**
     * Instruções para este presenter
     */
    public static final String    INTRUCAO_MUDAR_PACIENTE = "selecionar_paciente";
    public static final String    INTRUCAO_AUTO_SELECIONAR_PACIENTE = "auto_selecionar_paciente";

    /**
     * Set view
     */
    void setView(IListarActivity view);

    void onClick(View view);

}
