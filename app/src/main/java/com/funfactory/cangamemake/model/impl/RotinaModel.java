package com.funfactory.cangamemake.model.impl;

import java.util.List;

import android.util.Log;

import com.funfactory.cangamemake.model.IRankingModel;
import com.funfactory.cangamemake.model.IRotinaModel;
import com.funfactory.cangamemake.model.IRotinaPECSModel;
import com.funfactory.cangamemake.model.dao.IRotinaDAO;
import com.funfactory.cangamemake.model.dao.impl.RotinaDAO;
import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.model.entity.Rotina;

public class RotinaModel implements IRotinaModel {

    private IRotinaDAO       mRotinaDAO       = new RotinaDAO();
    private IRotinaPECSModel mRotinaPECSModel = new RotinaPECSModel();
    
    private IRankingModel    mRankingModel;

    @Override
    public List<Rotina> listAll() {
        return mRotinaDAO.getAll();
    }

    @Override
    public void saveOrUpdate(Rotina rotina) {
        
        long entId = mRotinaDAO.saveAndReturnId(rotina);
        rotina.setIdEntity(entId);

        mRotinaPECSModel.removerTodasAsRelacoes(rotina.getIdEntity());
        int posicao = 1;
        for (PECS pecs : rotina.getListaPECS()) {
            mRotinaPECSModel.vincularRotinaPECS(rotina.getIdEntity(), pecs.getIdEntity(), posicao++);
        }
    }

    @Override
    public void remove(Rotina object) {
        
        if (mRankingModel == null) {
            mRankingModel = new RankingModel();
        }

        mRankingModel.removeRakingByPEC(object.getIdEntity());
        mRotinaDAO.delete(object);
    }

    @Override
    public Rotina getRotinaById(long entityId) {
        return mRotinaDAO.getEntityById(entityId);
    }

    @Override
    public List<Rotina> getRotinasByPaciente(long idPaciente) {
        return mRotinaDAO.listAllByPaciente(idPaciente);
    }

    @Override
    public void trocarCategoria(long previousCategoriaId, long newCategoriaId) {
        mRotinaDAO.trocarCategoria(previousCategoriaId, newCategoriaId);
    }

    @Override
    public long saveAndReturnId(Rotina object) {
        return mRotinaDAO.saveAndReturnId(object);
    }
}
