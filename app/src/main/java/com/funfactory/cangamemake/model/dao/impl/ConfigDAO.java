package com.funfactory.cangamemake.model.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;

import com.funfactory.cangamemake.db.Table;
import com.funfactory.cangamemake.model.dao.AbstractDAO;
import com.funfactory.cangamemake.model.dao.IConfigDAO;
import com.funfactory.cangamemake.model.entity.Config;
import com.funfactory.cangamemake.model.entity.Paciente;

public class ConfigDAO extends AbstractDAO<Config> implements IConfigDAO {

    private static final long serialVersionUID = 1L;

    public ConfigDAO() {
        super(Table.CONFIG.getName());
    }

    @Override
    protected Config cursorToObject(Cursor cursor) {
        final Config obj = new Config();

        int column = cursor.getColumnIndex(Paciente.ID_COLUMN);
        if (column >= 0) {
            obj.setIdEntity(cursor.getLong(column));
        }

        column = cursor.getColumnIndex(Config.TEMPO_EXIBICAO_TXT);
        if (column >= 0) {
            obj.setTempoExibicaoTxt(cursor.getInt(column));
        }

        column = cursor.getColumnIndex(Config.TEMPO_REPRODUCAO_AUDIO);
        if (column >= 0) {
            obj.setTempoReproducaoAudio(cursor.getInt(column));
        }

        column = cursor.getColumnIndex(Config.TEMPO_REPRODUCAO_VIDEO);
        if (column >= 0) {
            obj.setTempoReproducaoVideo(cursor.getInt(column));
        }
        
        column = cursor.getColumnIndex(Config.PACIENTE_CORRENTE);
        if (column >= 0) {
            obj.setPacienteCorrente(cursor.getInt(column));
        }

        return obj;
    }

    @Override
    protected ContentValues objectToContentValues(Config entity) {

        final ContentValues values = new ContentValues();

        if (entity != null) {
            values.put(Paciente.ID_COLUMN, entity.getIdEntity());
            values.put(Config.TEMPO_EXIBICAO_TXT, entity.getTempoExibicaoTxt());
            values.put(Config.TEMPO_REPRODUCAO_AUDIO, entity.getTempoReproducaoAudio());
            values.put(Config.TEMPO_REPRODUCAO_VIDEO, entity.getTempoReproducaoVideo());
            values.put(Config.PACIENTE_CORRENTE, entity.getPacienteCorrente());
        }

        return values;
    }

    @Override
    public long saveAndReturnId(Config entity) {
        return saveAndReturnId(entity);
    }

}
