package com.funfactory.cangamemake.model.dao.impl;

import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.funfactory.cangamemake.db.Table;
import com.funfactory.cangamemake.model.dao.AbstractDAO;
import com.funfactory.cangamemake.model.dao.IPacienteDAO;
import com.funfactory.cangamemake.model.entity.Paciente;

public class PacienteDAO extends AbstractDAO<Paciente> implements IPacienteDAO {
    private static final long serialVersionUID = 1L;

    /**
     * Public constructor.
     */
    public PacienteDAO() {
        super(Table.PACIENTE.getName());
    }

    @Override
    protected Paciente cursorToObject(final Cursor cursor) {
        final Paciente obj = new Paciente();

        int column = cursor.getColumnIndex(Paciente.ID_COLUMN);
        if (column >= 0) {
            obj.setIdEntity(cursor.getLong(column));
        }

        column = cursor.getColumnIndex(Paciente.NOME);
        if (column >= 0) {
            obj.setNome(cursor.getString(column));
        }

        column = cursor.getColumnIndex(Paciente.NOME_PAI);
        if (column >= 0) {
            obj.setNomePai(cursor.getString(column));
        }

        column = cursor.getColumnIndex(Paciente.NOME_MAE);
        if (column >= 0) {
            obj.setNomeMae(cursor.getString(column));
        }

        column = cursor.getColumnIndex(Paciente.CONTATO_PAI);
        if (column >= 0) {
            obj.setContatoPai(cursor.getString(column));
        }

        column = cursor.getColumnIndex(Paciente.CONTATO_MAE);
        if (column >= 0) {
            obj.setContatoMae(cursor.getString(column));
        }

        column = cursor.getColumnIndex(Paciente.SEXO);
        if (column >= 0) {
            obj.setSexo(cursor.getString(column));
        }

        column = cursor.getColumnIndex(Paciente.DATA_NASCIMENTO);
        if (column >= 0) {
            obj.setDataNascimento(parse(cursor.getString(column)));
        }

        column = cursor.getColumnIndex(Paciente.IMAGE_PATH);
        if (column >= 0) {
            obj.setImagePath(cursor.getString(column));
        }

        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see br.org.sidi.spod.model.dao.AbstractDAO#objectToContentValues(br.org.sidi
     * .spod.model.entity.AbstractBaseEntity)
     */
    @Override
    protected ContentValues objectToContentValues(final Paciente entity) {
        final ContentValues values = new ContentValues();

        if (entity.getIdEntity() != null) {
            values.put(Paciente.ID_COLUMN, entity.getIdEntity());
        }

        if (entity.getNome() != null) {
            values.put(Paciente.NOME, entity.getNome());
        }

        if (entity.getNomePai() != null) {
            values.put(Paciente.NOME_PAI, entity.getNomePai());
        }

        if (entity.getNomeMae() != null) {
            values.put(Paciente.NOME_MAE, entity.getNomeMae());
        }

        if (entity.getContatoPai() != null) {
            values.put(Paciente.CONTATO_PAI, entity.getContatoPai());
        }

        if (entity.getContatoMae() != null) {
            values.put(Paciente.CONTATO_MAE, entity.getContatoMae());
        }

        if (entity.getSexo() != null) {
            values.put(Paciente.SEXO, entity.getSexo());
        }

        if (entity.getDataNascimento() != null) {
            values.put(Paciente.DATA_NASCIMENTO, format(entity.getDataNascimento()));
        }

        if (entity.getImagePath() != null) {
            values.put(Paciente.IMAGE_PATH, entity.getImagePath());
        }

        return values;
    }

    @Override
    public List<Paciente> getAllByName(String name) {
        return getListSQL("SELECT * FROM " + getTable() + " WHERE " + Paciente.NOME + " LIKE %" + name + "%",
                new HashMap<String, String>(), Paciente.NOME + " ASC");
    }
}
