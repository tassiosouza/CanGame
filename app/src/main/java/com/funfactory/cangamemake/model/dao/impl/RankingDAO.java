package com.funfactory.cangamemake.model.dao.impl;

import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.funfactory.cangamemake.db.Table;
import com.funfactory.cangamemake.model.dao.AbstractDAO;
import com.funfactory.cangamemake.model.dao.IRankingDAO;
import com.funfactory.cangamemake.model.entity.Ranking;

public class RankingDAO extends AbstractDAO<Ranking> implements IRankingDAO {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public RankingDAO() {
        super(Table.RANKING.getName());
    }

    @Override
    public List<Ranking> getAllByPaciente(Long idPaciente) {
        return getListSQL("SELECT * FROM " + getTable() + " WHERE " + Ranking.ID_PACIENTE + " = " + idPaciente,
                new HashMap<String, String>(), null);
    }

    @Override
    public List<Ranking> getAllByRotinaPaciente(int idRotina, Long idPaciente) {
        return getListSQL("SELECT * FROM " + getTable() + " WHERE " + Ranking.ID_ROTINA + " = " + idRotina + " AND "
                + Ranking.ID_PACIENTE + " = " + idPaciente, new HashMap<String, String>(), null);
    }

    @Override
    public List<Ranking> getAllByPecPaciente(Long idPec, Long idPaciente) {
        return getListSQL("SELECT * FROM " + getTable() + " WHERE " + Ranking.ID_PECS + " = " + idPec + " AND "
                + Ranking.ID_PACIENTE + " = " + idPaciente, new HashMap<String, String>(), null);
    }
    
    @Override
    public List<Ranking> getAllByRotinaPecPaciente(Long idRotina, Long idPec, Long idPaciente) {
        return getListSQL("SELECT * FROM " + getTable() + " WHERE " + Ranking.ID_PECS + " = " + idPec + " AND "
                + Ranking.ID_PACIENTE + " = " + idPaciente + " AND "
                        + Ranking.ID_ROTINA + " = " + idRotina, new HashMap<String, String>(), null);
    }
    
    @Override
    public void removeAllByPaciente(Long idPaciente) {
        
        StringBuffer buffer = new StringBuffer();
        buffer.append("DELETE FROM ");
        buffer.append(Table.RANKING.getName());
        buffer.append(" WHERE ");
        buffer.append(Ranking.ID_PACIENTE);
        buffer.append("=");
        buffer.append(idPaciente);
        buffer.append(";");
        
        executeSQL(buffer.toString());
    }
    
    @Override
    public void removeRakingByPEC(Long idPec) {
       
        StringBuffer buffer = new StringBuffer();
        buffer.append("DELETE FROM ");
        buffer.append(Table.RANKING.getName());
        buffer.append(" WHERE ");
        buffer.append(Ranking.ID_PECS);
        buffer.append("=");
        buffer.append(idPec);
        buffer.append(";");
        
        executeSQL(buffer.toString());
    }
    

    @Override
    public void removeRakingByPECPaciente(Long pecEntityId, Long pacienteEntityId) {
       
        StringBuffer buffer = new StringBuffer();
        buffer.append("DELETE FROM ");
        buffer.append(Table.RANKING.getName());
        buffer.append(" WHERE ");
        buffer.append(Ranking.ID_PECS);
        buffer.append("=");
        buffer.append(pecEntityId);
        buffer.append(" AND ");
        buffer.append(Ranking.ID_PACIENTE);
        buffer.append("=");
        buffer.append(pacienteEntityId);
        buffer.append(";");
        
        executeSQL(buffer.toString());
    }

    @Override
    protected Ranking cursorToObject(Cursor cursor) {

        final Ranking obj = new Ranking();

        int column = cursor.getColumnIndex(Ranking.ID_COLUMN);
        if (column >= 0) {
            obj.setIdEntity(cursor.getLong(column));
        }

        column = cursor.getColumnIndex(Ranking.ID_PACIENTE);
        if (column >= 0) {
            obj.setIdPaciente(cursor.getLong(column));
        }

        column = cursor.getColumnIndex(Ranking.ID_PECS);
        if (column >= 0) {
            obj.setIdPECS(cursor.getLong(column));
        }

        column = cursor.getColumnIndex(Ranking.ID_ROTINA);
        if (column >= 0) {
            obj.setIdRotina(cursor.getLong(column));
        }

        column = cursor.getColumnIndex(Ranking.PONTUACAO);
        if (column >= 0) {
            obj.setPontuacao(cursor.getInt(column));
        }

        column = cursor.getColumnIndex(Ranking.DATA_HORA_CRIACAO);
        if (column >= 0) {
            obj.setDataHoraCriacao(parse(cursor.getString(column)));
        }

        return obj;
    }

    @Override
    protected ContentValues objectToContentValues(Ranking entity) {

        final ContentValues values = new ContentValues();

        if (entity.getIdEntity() != null) {
            values.put(Ranking.ID_COLUMN, entity.getIdEntity());
        }

        if (entity.getIdPaciente() != null) {
            values.put(Ranking.ID_PACIENTE, entity.getIdPaciente());
        }

        if (entity.getIdPECS() != null) {
            values.put(Ranking.ID_PECS, entity.getIdPECS());
        }

        if (entity.getIdRotina() != null) {
            values.put(Ranking.ID_ROTINA, entity.getIdRotina());
        }

        if (entity.getPontuacao() != null) {
            values.put(Ranking.PONTUACAO, entity.getPontuacao());
        }

        if (entity.getDataHoraCriacao() != null) {
            values.put(Ranking.DATA_HORA_CRIACAO, format(entity.getDataHoraCriacao()));
        }

        return values;
    }

    @Override
    public void removeRakingByRotina(Long idRotina) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("DELETE FROM ");
        buffer.append(Table.RANKING.getName());
        buffer.append(" WHERE ");
        buffer.append(Ranking.ID_ROTINA);
        buffer.append("=");
        buffer.append(idRotina);
        buffer.append(";");
        
        executeSQL(buffer.toString());
        
    }

    @Override
    public void removeRakingByRotinaPaciente(Long idRotina, Long pacienteEntityId) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("DELETE FROM ");
        buffer.append(Table.RANKING.getName());
        buffer.append(" WHERE ");
        buffer.append(Ranking.ID_ROTINA);
        buffer.append("=");
        buffer.append(idRotina);
        buffer.append(" AND ");
        buffer.append(Ranking.ID_PACIENTE);
        buffer.append("=");
        buffer.append(pacienteEntityId);
        buffer.append(";");
        
        executeSQL(buffer.toString());
        
    }

	@Override
	public boolean isAssociacaoPECSExiste(Long idPECS) {
		List<Ranking> ranks = getListSQL("SELECT * FROM " + getTable() + " WHERE " + Ranking.ID_PECS + " = " + idPECS, new HashMap<String, String>(), null);
		return ranks != null && !ranks.isEmpty();
	}

	@Override
	public boolean isAssociacaoRotinaExiste(Long idRotina) {
		List<Ranking> ranks = getListSQL("SELECT * FROM " + getTable() + " WHERE " + Ranking.ID_ROTINA + " = " + idRotina, new HashMap<String, String>(), null);
		return ranks != null && !ranks.isEmpty();
	}
    
    


  

   

}
