package com.funfactory.cangamemake.model;

import java.util.List;

import com.funfactory.cangamemake.model.entity.Categoria;

public interface ICategoriaModel {

	List<Categoria> listAll();

	void saveOrUpdate(Categoria object);

	void remove(Categoria object);

	Categoria getCategoriaById(long entityId);
	
	Categoria getCategoriaGeral();
}
