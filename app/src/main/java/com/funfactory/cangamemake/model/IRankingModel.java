package com.funfactory.cangamemake.model;

import java.util.List;

import com.funfactory.cangamemake.model.entity.Ranking;

public interface IRankingModel {
	
	List<Ranking> getAllByPaciente(Long idPaciente);
	
	List<Ranking> getAllByRotinaPecPaciente(Long idRotina, Long idPec, Long idPaciente);
	
	List<Ranking> getAllByRotinaPaciente(int idRotina, Long idPaciente);
	
	List<Ranking> getAllByPecPaciente(Long idPec, Long idPaciente);
	
	Ranking getRankingById(long id);
	
	/**
	 * Save or update
	 * 
	 * @param object
	 */
	void saveOrUpdate(Ranking object);

	/**
	 * Remove/delete
	 * 
	 * @param object
	 */
	void remove(Ranking object);
	
	void removeRakingByPECPaciente(Long idRotina, Long pacienteId);

    void removeRakingByPEC(Long idPec);
    
    void removeRakingByPaciente(Long idPaciente);
    
    void removeRakingByRotinaPaciente(Long pecEntity, Long pacienteEntity);
    
    boolean isAssociacaoPECSExiste(Long idPECS);
    
    boolean isAssociacaoRotinaExiste(Long idRotina);

	double getPontuacaoRotina(Long idRotina, Long idPaciente);    
}

