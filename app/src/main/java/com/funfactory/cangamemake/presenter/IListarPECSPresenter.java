package com.funfactory.cangamemake.presenter;

import android.view.View;

import com.funfactory.cangamemake.view.IListarPECSActivity;

public interface IListarPECSPresenter {

    /**
     * Instru��es para presenter
     */
    public static final String INSTRUCAO_LISTAR_PECS_SISTEMA  = "normal";
    public static final String INSTRUCAO_LISTAR_PECS_PACIENTE = "pecs_paciente";
    public static final String INSTRUCAO_LISTAR_PECS_ROTINA   = "incluir_paciente";
    public static final String INSTRUCAO_INCLUIR_EM_PACIENTE  = "incluir_paciente";
    public static final String INSTRUCAO_INCLUIR_EM_ROTINA    = "incluir_rotina";

    /**
     * Set view
     * 
     * @param view
     */
    void setView(IListarPECSActivity view);

    public void onClick(View view);

}
