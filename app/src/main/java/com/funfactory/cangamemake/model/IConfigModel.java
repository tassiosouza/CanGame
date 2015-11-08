package com.funfactory.cangamemake.model;

import java.util.List;

import com.funfactory.cangamemake.model.entity.Config;

public interface IConfigModel {

    /**
     * List all
     * 
     * @return A list of all Paciente from the database
     */
    List<Config> listAll();

    /**
     * Save or update
     * 
     * @param object
     */
    void saveOrUpdate(Config object);

    /**
     * Remove/delete
     * 
     * @param object
     */
    void remove(Config object);

    /**
     * Recupera instancia da Configuracao corrente
     * 
     * @return Instancia de Config ou uma nova instancia se não houver nada.
     */
    Config getConfiguration();
}
