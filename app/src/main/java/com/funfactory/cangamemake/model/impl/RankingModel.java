package com.funfactory.cangamemake.model.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.funfactory.cangamemake.model.IPECSModel;
import com.funfactory.cangamemake.model.IRankingModel;
import com.funfactory.cangamemake.model.IRotinaPECSModel;
import com.funfactory.cangamemake.model.dao.IRankingDAO;
import com.funfactory.cangamemake.model.dao.impl.RankingDAO;
import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.model.entity.Ranking;
import com.funfactory.cangamemake.model.entity.RotinaPECS;

public class RankingModel implements IRankingModel {

    private IRankingDAO      rankingDAO       = new RankingDAO();
    private IRotinaPECSModel mRotinaPECSModel = new RotinaPECSModel();
    private IPECSModel       mPECSModel       = new PECSModel();

    @Override
    public List<Ranking> getAllByPaciente(Long idPaciente) {
        return rankingDAO.getAllByPaciente(idPaciente);
    }

    @Override
    public List<Ranking> getAllByRotinaPaciente(int idRotina, Long idPaciente) {
        return rankingDAO.getAllByRotinaPaciente(idRotina, idPaciente);
    }

    @Override
    public List<Ranking> getAllByPecPaciente(Long idPec, Long idPaciente) {
        return rankingDAO.getAllByPecPaciente(idPec, idPaciente);
    }

    @Override
    public Ranking getRankingById(long id) {
        return rankingDAO.getEntityById(id);
    }

    @Override
    public void saveOrUpdate(Ranking object) {
        rankingDAO.save(object);
    }

    @Override
    public void remove(Ranking object) {
        rankingDAO.delete(object);
    }

    @Override
    public void removeRakingByPaciente(Long idPaciente) {
        rankingDAO.removeAllByPaciente(idPaciente);
    }

    @Override
    public void removeRakingByPEC(Long idPec) {
        rankingDAO.removeRakingByPEC(idPec);
    }

    @Override
    public void removeRakingByPECPaciente(Long pecEntityId, Long pacienteEntityId) {
        rankingDAO.removeRakingByPECPaciente(pecEntityId, pacienteEntityId);
    }

    @Override
    public void removeRakingByRotinaPaciente(Long idRotina, Long pacienteEntityId) {
        rankingDAO.removeRakingByRotinaPaciente(idRotina, pacienteEntityId);
    }

    @Override
    public boolean isAssociacaoPECSExiste(Long idPECS) {
        return rankingDAO.isAssociacaoPECSExiste(idPECS);
    }

    @Override
    public boolean isAssociacaoRotinaExiste(Long idRotina) {
        return rankingDAO.isAssociacaoRotinaExiste(idRotina);
    }

    @Override
    public double getPontuacaoRotina(Long idRotina, Long idPaciente) {

        List<RotinaPECS> listaRotinaPECS = mRotinaPECSModel.recuperarPorRotina(idRotina);
        double pontuacao = 0;

        if (listaRotinaPECS != null) {
            for (RotinaPECS rotinaPECS : listaRotinaPECS) {

                long pecsId = rotinaPECS.getPecsId();
                PECS pecs = mPECSModel.getPECSById(pecsId);
                List<Ranking> rankings = getAllByRotinaPecPaciente(idRotina, pecs.getIdEntity(), idPaciente);

                if (rankings != null && rankings.size() != 0) {
                    Ranking ranking = rankings.get(0);
                    pontuacao += ranking.getPontuacao();
                }
            }

            // calcula média dos pontos
            BigDecimal pontuacaoBig = new BigDecimal(pontuacao);
            BigDecimal qtdPECS = new BigDecimal(listaRotinaPECS.size());
            BigDecimal media = pontuacaoBig.divide(qtdPECS, 1, RoundingMode.UP);

            pontuacao = media.doubleValue();
        }

        return pontuacao;
    }

    @Override
    public List<Ranking> getAllByRotinaPecPaciente(Long idRotina, Long idPec, Long idPaciente) {
        return rankingDAO.getAllByRotinaPecPaciente(idRotina, idPec, idPaciente);
    }
}
