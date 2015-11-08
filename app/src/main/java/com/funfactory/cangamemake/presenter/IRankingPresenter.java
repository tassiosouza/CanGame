package com.funfactory.cangamemake.presenter;

import java.util.List;

import com.funfactory.cangamemake.model.entity.Ranking;
import com.funfactory.cangamemake.view.IRankingActivity;

public interface IRankingPresenter {
	
	/**
	 * Set view
	 * 
	 * @param view
	 */
	void setView(IRankingActivity view);
	
	void salvarRanking(Long idPaciente, Long idPec, Long idRotina, int pontuacao);
	
	Ranking getRankingById(long id);
	
	List<Ranking> getAllByPaciente(Long idPaciente);
	
	List<Ranking> getAllByRotinaPaciente(int idRotina, Long idPaciente);
	
	List<Ranking> getAllByPecPaciente(Long idPec, Long idPaciente);

}
