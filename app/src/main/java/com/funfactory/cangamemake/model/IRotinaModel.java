package com.funfactory.cangamemake.model;

import java.util.List;

import com.funfactory.cangamemake.model.entity.Rotina;

public interface IRotinaModel {

    List<Rotina> listAll();

    void saveOrUpdate(Rotina object);
    
    long saveAndReturnId(Rotina object);

    void remove(Rotina object);

    Rotina getRotinaById(long entityId);

    List<Rotina> getRotinasByPaciente(long idPaciente);

    void trocarCategoria(long previousCategoriaId, long newCategoriaId);

}
