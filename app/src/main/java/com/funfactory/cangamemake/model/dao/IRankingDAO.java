package com.funfactory.cangamemake.model.dao;

import java.util.List;

import com.funfactory.cangamemake.model.entity.Ranking;

public interface IRankingDAO extends IGenericDAO<Ranking> {

    List<Ranking> getAllByPaciente(Long idPaciente);

    void removeAllByPaciente(Long idPaciente);

    List<Ranking> getAllByRotinaPaciente(int idRotina, Long idPaciente);

    List<Ranking> getAllByPecPaciente(Long idPec, Long idPaciente);

    void removeRakingByPEC(Long idPec);

    void removeRakingByPECPaciente(Long pecEntityId, Long pacienteEntityId);

    void removeRakingByRotina(Long idRotina);
    
    void removeRakingByRotinaPaciente(Long idRotina, Long pacienteEntityId);
    
    boolean isAssociacaoPECSExiste(Long idPECS);
    
    boolean isAssociacaoRotinaExiste(Long idRotina);
    
    List<Ranking> getAllByRotinaPecPaciente(Long idRotina, Long idPec, Long idPaciente);
    
}
