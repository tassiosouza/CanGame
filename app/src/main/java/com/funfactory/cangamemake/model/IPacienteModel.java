package com.funfactory.cangamemake.model;

import java.util.List;

import com.funfactory.cangamemake.model.entity.Paciente;

public interface IPacienteModel {

    /**
     * List all
     * 
     * @return A list of all Paciente from the database
     */
    List<Paciente> listAll();

    /**
     * List all, filtering by name
     * 
     * @param name
     * @return A list of all Paciente from the database, filtered by name
     */
    List<Paciente> getAllByName(String name);

    /**
     * Save or update
     * 
     * @param object
     * 
     * @return Return the id of the entity
     */
    long saveOrUpdate(Paciente object);

    /**
     * Remove/delete
     * 
     * @param object
     */
    void remove(Paciente object);

    /**
     * 
     * @param entityId
     * @return
     */
    Paciente getPacienteById(long entityId);

    /**
     * @return Number of pacientes
     */
    int count();

}
