package com.funfactory.cangamemake.model.dao;

import java.util.List;

import com.funfactory.cangamemake.model.entity.Paciente;

public interface IPacienteDAO extends IGenericDAO<Paciente> {

	/**
     * Search by name
     * 
     * @return a list of Paciente
     */
    List<Paciente> getAllByName(String name);
	
}
