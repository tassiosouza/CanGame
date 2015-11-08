package com.funfactory.cangamemake.model.dao;

import java.util.List;

import com.funfactory.cangamemake.model.entity.RotinaPECS;

public interface IRotinaPECSDAO extends IGenericDAO<RotinaPECS> {

    public void removerPorPECS(long pecsId);

    public void removerPorRotina(long rotinaId);

    public RotinaPECS recuperarEntrada(long rotinaId, long pecsId);

    public List<RotinaPECS> recuperarPorRotina(long rotinaId);
    
}
