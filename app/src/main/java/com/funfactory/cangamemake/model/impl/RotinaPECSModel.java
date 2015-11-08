package com.funfactory.cangamemake.model.impl;

import java.util.List;

import android.util.Log;

import com.funfactory.cangamemake.model.IRotinaPECSModel;
import com.funfactory.cangamemake.model.dao.IRotinaPECSDAO;
import com.funfactory.cangamemake.model.dao.impl.RotinasPECSDAO;
import com.funfactory.cangamemake.model.entity.RotinaPECS;

public class RotinaPECSModel implements IRotinaPECSModel {

    public IRotinaPECSDAO rotinaPECSDAO = new RotinasPECSDAO();

    @Override
    public void vincularRotinaPECS(long rotinaId, long pecsId, int posicao) {
        RotinaPECS entrada = recuperarEntrada(rotinaId, pecsId);

        if (entrada == null) {
            entrada = new RotinaPECS();
            entrada.setPecsId(pecsId);
            entrada.setPosicao(posicao);
            entrada.setRotinaId(rotinaId);
        }
        
        entrada.setPosicao(posicao);
        rotinaPECSDAO.save(entrada);
    }

    public void atualizarPosicao(long rotinaId, long pecsId, int posicao) {
        RotinaPECS entrada = recuperarEntrada(rotinaId, pecsId);
        entrada.setPosicao(posicao);
        rotinaPECSDAO.save(entrada);
    }

    private RotinaPECS recuperarEntrada(long rotinaId, long pecsId) {
        return rotinaPECSDAO.recuperarEntrada(rotinaId, pecsId);
    }

    @Override
    public void removerPorPECS(long pecsId) {
        rotinaPECSDAO.removerPorPECS(pecsId);
    }

    @Override
    public void removerTodasAsRelacoes(long rotinaId) {
        rotinaPECSDAO.removerPorRotina(rotinaId);
    }

    @Override
    public List<RotinaPECS> recuperarPorRotina(long rotinaId) {
        return rotinaPECSDAO.recuperarPorRotina(rotinaId);
    }

}
