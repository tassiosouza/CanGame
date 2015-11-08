package com.funfactory.cangamemake.presenter.impl;

import java.util.Date;
import java.util.List;

import com.funfactory.cangamemake.model.IRankingModel;
import com.funfactory.cangamemake.model.entity.Ranking;
import com.funfactory.cangamemake.presenter.IRankingPresenter;
import com.funfactory.cangamemake.view.IRankingActivity;
import com.google.inject.Inject;

public class RankingPresenter implements IRankingPresenter {
	
	@Inject
	private IRankingModel model;
	
	private IRankingActivity view;

	@Override
	public void setView(IRankingActivity view) {
		
		this.view = view;
	}

	@Override
	public void salvarRanking(Long idPaciente, Long idPec, Long idRotina, int pontuacao) {

		Ranking ranking = new Ranking();
		
		ranking.setIdPaciente(idPaciente);
		ranking.setIdPECS(idPec);
		ranking.setIdRotina(idRotina);
		ranking.setPontuacao(pontuacao);
		ranking.setDataHoraCriacao(new Date());
		
		model.saveOrUpdate(ranking);
		
		view.exibirMensagemSucesso();
		view.finalizarActivity();
		
	}

	@Override
	public Ranking getRankingById(long id) {
		
		return model.getRankingById(id);
	}

	@Override
	public List<Ranking> getAllByPaciente(Long idPaciente) {
		
		return model.getAllByPaciente(idPaciente);
	}

	@Override
	public List<Ranking> getAllByRotinaPaciente(int idRotina, Long idPaciente) {
		
		return model.getAllByRotinaPaciente(idRotina, idPaciente);
	}

	@Override
	public List<Ranking> getAllByPecPaciente(Long idPec, Long idPaciente) {

		return model.getAllByPecPaciente(idPec, idPaciente);
	}
}
