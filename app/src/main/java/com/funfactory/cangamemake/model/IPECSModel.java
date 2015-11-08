package com.funfactory.cangamemake.model;

import java.util.List;

import com.funfactory.cangamemake.model.entity.PECS;

public interface IPECSModel {

	List<PECS> listAll();

	void saveOrUpdate(PECS object);

	void remove(PECS object);

	PECS getPECSById(long entityId);

	List<PECS> getPECSByPaciente(long idPaciente);
	
	List<PECS> getPECSByRotina(long idRotina);
	
	void trocarCategoria(long previousCategoriaId, long newCategoriaId);
	
}
