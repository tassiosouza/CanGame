package com.funfactory.cangamemake.model.dao;

import java.util.List;

import com.funfactory.cangamemake.model.entity.Rotina;

public interface IRotinaDAO extends IGenericDAO<Rotina> {

	/**
	 * Get all Rotinas by Paciente.
	 * 
	 * @param idPaciente
	 * @return
	 */
	public List<Rotina> listAllByPaciente(long idPaciente);
	
    public void trocarCategoria(long previousCategoriaId, long newCategoriaId);

}