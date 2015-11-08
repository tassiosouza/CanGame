package com.funfactory.cangamemake.model;

import java.util.List;

import com.funfactory.cangamemake.model.entity.RotinaPECS;


public interface IRotinaPECSModel {

    void removerTodasAsRelacoes(long rotinaId);
    void vincularRotinaPECS(long rotinaId, long pecsId, int posicao);
    void removerPorPECS(long pecsId);
    void atualizarPosicao(long rotinaId, long pecsId, int posicao);
    List<RotinaPECS> recuperarPorRotina(long rotinaId);
}
