package com.funfactory.cangamemake.presenter;

import java.util.Date;

import com.funfactory.cangamemake.view.IEdicaoActivity;

public interface IPacientePresenter {
    
    void setView(IEdicaoActivity view);

    void salvarPaciente(String nomePaciente, String nomePai, String nomeMae, String contatoPai, String contatoMae,
                        Date dataNascimento, Boolean  isSexoMasculino);
    
    boolean isDadosValidos(String nomePaciente, String nomePai, String nomeMae, String contatoPai, String contatoMae,
                           Date dataNascimento, Boolean isSexoMasculino);
    
    void onOptionSeleted(int viewOptionId);

    void salvarTela(String nomePaciente, String nomePai, String nomeMae, String contatoPai, String contatoMae,
            Date dataNascimento, Boolean isSexoMasculino);

}
