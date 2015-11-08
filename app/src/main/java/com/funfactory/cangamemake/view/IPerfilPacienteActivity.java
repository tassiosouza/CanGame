package com.funfactory.cangamemake.view;

import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.presenter.impl.IBaseActivityInterface;

public interface IPerfilPacienteActivity extends IBaseActivityInterface {

    void setContent(Paciente paciente);

}
