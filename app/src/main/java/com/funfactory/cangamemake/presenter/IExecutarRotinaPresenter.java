package com.funfactory.cangamemake.presenter;

import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.model.entity.Rotina;
import com.funfactory.cangamemake.view.IExecutarRotinaActivity;

/**
 * Interface Presenter para Execução de Rotina.
 * 
 * @author Silvino Neto
 */
public interface IExecutarRotinaPresenter extends IGenecicPresenter {

	void setView(IExecutarRotinaActivity view);

    void setRotina(Rotina mRotina);

    void setPaciente(Paciente mPaciente);

    void reproduzir();
}