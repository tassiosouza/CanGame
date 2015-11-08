package com.funfactory.cangamemake.model.impl;

import java.util.List;

import com.funfactory.cangamemake.model.IPacienteModel;
import com.funfactory.cangamemake.model.IRankingModel;
import com.funfactory.cangamemake.model.dao.IPacienteDAO;
import com.funfactory.cangamemake.model.dao.impl.PacienteDAO;
import com.funfactory.cangamemake.model.entity.Paciente;

public class PacienteModel implements IPacienteModel {

    private IPacienteDAO pacienteDAO = new PacienteDAO();
    
    /**
     * Dependencias
     */
    private IRankingModel mRankingModel;

    @Override
    public List<Paciente> listAll() {
        return pacienteDAO.getAll();
    }

    @Override
    public List<Paciente> getAllByName(String name) {
        return pacienteDAO.getAllByName(name);
    }

    @Override
    public long saveOrUpdate(Paciente object) {
        return pacienteDAO.saveAndReturnId(object);
    }

    @Override
    public void remove(Paciente paciente) {
        
        if (mRankingModel == null){
            mRankingModel = new RankingModel();
        }
        
        mRankingModel.removeRakingByPaciente(paciente.getIdEntity());
        pacienteDAO.delete(paciente);
    }

    @Override
    public Paciente getPacienteById(long entityId) {
        return pacienteDAO.getEntityById(entityId);
    }

    @Override
    public int count() {
        return pacienteDAO.count();
    }

}
