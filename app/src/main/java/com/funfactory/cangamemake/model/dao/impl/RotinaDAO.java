package com.funfactory.cangamemake.model.dao.impl;

import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.funfactory.cangamemake.db.Table;
import com.funfactory.cangamemake.model.dao.AbstractDAO;
import com.funfactory.cangamemake.model.dao.ICategoriaDAO;
import com.funfactory.cangamemake.model.dao.IRotinaDAO;
import com.funfactory.cangamemake.model.entity.Categoria;
import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.model.entity.Ranking;
import com.funfactory.cangamemake.model.entity.Rotina;
import com.funfactory.cangamemake.model.entity.RotinaPECS;

public class RotinaDAO extends AbstractDAO<Rotina> implements IRotinaDAO {

    private static final long serialVersionUID = 1L;
    private ICategoriaDAO     categoriaDAO;

    public RotinaDAO() {
        super(Table.ROTINA.getName());
        categoriaDAO = new CategoriaDAO();
    }

    @Override
    protected Rotina cursorToObject(Cursor cursor) {
        final Rotina obj = new Rotina();

        int column = cursor.getColumnIndex(Rotina.ID_COLUMN);
        if (column >= 0) {
            obj.setIdEntity(cursor.getLong(column));
        }

        column = cursor.getColumnIndex(Rotina.NOME);
        if (column >= 0) {
            obj.setNome(cursor.getString(column));
        }

        column = cursor.getColumnIndex(Rotina.ID_CATEGORIA);
        if (column >= 0) {

            int idCategoria = cursor.getInt(column);
            Categoria categoria = categoriaDAO.getEntityById(idCategoria);
            obj.setCategoria(categoria);
        }

        return obj;
    }

    @Override
    protected ContentValues objectToContentValues(Rotina entity) {
        final ContentValues values = new ContentValues();

        if (entity.getIdEntity() != null) {
            values.put(Rotina.ID_COLUMN, entity.getIdEntity());
        }

        if (entity.getNome() != null) {
            values.put(Rotina.NOME, entity.getNome());
        }

        if (entity.getCategoria() != null) {
            values.put(Rotina.ID_CATEGORIA, entity.getCategoria().getIdEntity());
        }

        return values;
    }

    @Override
    public List<Rotina> listAllByPaciente(long idPaciente) {

        String query = "SELECT " + getTable() + ".* FROM " + getTable() + ", " + Table.RANKING.getName() + " WHERE "
                + getTable() + "." + Rotina.ID_COLUMN + " = " + Table.RANKING.getName() + "." + Ranking.ID_ROTINA + " AND "
                + Table.RANKING.getName() + "." + Ranking.ID_PACIENTE + " = " + idPaciente + " AND "
                        + Table.RANKING.getName() + "." + Ranking.ID_PECS + " is null ";

        return getListSQL(query, new HashMap<String, String>(), getTable() + "." + Rotina.ID_COLUMN + " ASC");
    }

    @Override
    public void trocarCategoria(long previousCategoriaId, long newCategoriaId) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("UPDATE ");
        buffer.append(Table.ROTINA.getName());
        buffer.append(" SET ");
        buffer.append(Rotina.ID_CATEGORIA);
        buffer.append("=");
        buffer.append(newCategoriaId);
        buffer.append(" WHERE ");
        buffer.append(Rotina.ID_CATEGORIA);
        buffer.append("=");
        buffer.append(previousCategoriaId);
        buffer.append(";");

        executeSQL(buffer.toString());
    }

}
