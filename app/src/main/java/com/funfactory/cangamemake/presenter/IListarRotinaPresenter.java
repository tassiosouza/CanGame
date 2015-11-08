package com.funfactory.cangamemake.presenter;

import android.view.View;

import com.funfactory.cangamemake.view.IListarRotinaActivity;

public interface IListarRotinaPresenter {
    
    public static final String INTRUCAO_LISTAR_ROTINA_PACIENTE = "listar_rotinas_paciente";
    public static final String INTRUCAO_LISTAR_ROTINA_SISTEMA = "listar_rotinas_sistema";
    public static final String INTRUCAO_INCLUIR_EM_PACIENTE = "incluir_em_paciente";

    /**
     * Set view
     */
    void setView(IListarRotinaActivity view);

    void onClick(View view);

}
