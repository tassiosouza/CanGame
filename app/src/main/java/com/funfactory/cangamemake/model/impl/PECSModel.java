package com.funfactory.cangamemake.model.impl;

import java.util.List;

import com.funfactory.cangamemake.model.IPECSModel;
import com.funfactory.cangamemake.model.IRankingModel;
import com.funfactory.cangamemake.model.IRotinaPECSModel;
import com.funfactory.cangamemake.model.dao.IPECSDAO;
import com.funfactory.cangamemake.model.dao.IRotinaPECSDAO;
import com.funfactory.cangamemake.model.dao.impl.PECSDAO;
import com.funfactory.cangamemake.model.dao.impl.RotinasPECSDAO;
import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.model.entity.Ranking;

public class PECSModel implements IPECSModel {

    /**
     * DAO
     */
    private IPECSDAO         pecsDAO       = new PECSDAO();
    private IRotinaPECSDAO   rotinaPECSDAO = new RotinasPECSDAO();

    /**
     * Dependencias
     */
    private IRankingModel    mRankingModel;
    private IRotinaPECSModel mRotinaPecsModel;

    @Override
    public List<PECS> listAll() {
        return pecsDAO.getAll();
    }

    @Override
    public void saveOrUpdate(PECS object) {
        pecsDAO.saveAndCommit(object);
    }

    @Override
    public void remove(PECS object) {

        if (mRankingModel == null) {
            mRankingModel = new RankingModel();
        }

        if (mRotinaPecsModel == null) {
            mRotinaPecsModel = new RotinaPECSModel();
        }
        mRotinaPecsModel.removerPorPECS(object.getIdEntity());
        mRankingModel.removeRakingByPEC(object.getIdEntity());
        pecsDAO.delete(object);
    }

    @Override
    public PECS getPECSById(long entityId) {
        return pecsDAO.getEntityById(entityId);
    }

    @Override
    public List<PECS> getPECSByPaciente(long idPaciente) {

        if (mRankingModel == null) {
            mRankingModel = new RankingModel();
        }

        List<PECS> list = pecsDAO.listAllByPaciente(idPaciente);

        for (PECS pecs : list) {
            List<Ranking> ranking = mRankingModel.getAllByPecPaciente(pecs.getIdEntity(), idPaciente);
            if (ranking != null && ranking.size() > 0) {
                pecs.setRanking(ranking.get(0));
            }
        }

        return list;
    }

    @Override
    public void trocarCategoria(long previousCategoriaId, long newCategoriaId) {
        pecsDAO.trocarCategoria(previousCategoriaId, newCategoriaId);
    }

    @Override
    public List<PECS> getPECSByRotina(long idRotina) {
        return pecsDAO.getPECSByRotina(idRotina);
    }

}
