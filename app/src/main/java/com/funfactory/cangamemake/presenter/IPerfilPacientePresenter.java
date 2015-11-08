package com.funfactory.cangamemake.presenter;

import com.funfactory.cangamemake.view.IPerfilPacienteActivity;

public interface IPerfilPacientePresenter {

    void onOptionSelected(int viewIdOption);

    void listarPecs();

    void listarRotinas();
    
    void setView(IPerfilPacienteActivity view);
}
