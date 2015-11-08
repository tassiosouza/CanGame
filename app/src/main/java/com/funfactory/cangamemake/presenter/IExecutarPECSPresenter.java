package com.funfactory.cangamemake.presenter;

import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.model.entity.Rotina;
import com.funfactory.cangamemake.view.IExecutarPECSActivity;

/**
 * Interface Presenter para Execu��o de PECS.
 * 
 * @author Silvino Neto
 */
public interface IExecutarPECSPresenter extends IGenecicPresenter {

	void setView(IExecutarPECSActivity view);

    void onOptionSeleted(int viewOptionId);
    
    void setPecs(PECS mPecs);

    void setPaciente(Paciente mPaciente);
    
    void setRotina(Rotina mRotina);

    void onBackPressed();
}