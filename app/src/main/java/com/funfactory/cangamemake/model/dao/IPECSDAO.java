
package com.funfactory.cangamemake.model.dao;

import java.util.List;

import com.funfactory.cangamemake.model.entity.PECS;

public interface IPECSDAO extends IGenericDAO<PECS> {

    public void associarPECPaciente(long pecId, long pacientId);
    
    public void trocarCategoria(long previousCategoriaId, long newCategoriaId);
    
    public List<PECS> getPECSByRotina(long idRotina);
    
    public List<PECS> listAllByPaciente(long idPaciente);
}
