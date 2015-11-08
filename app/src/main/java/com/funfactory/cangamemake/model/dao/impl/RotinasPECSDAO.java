package com.funfactory.cangamemake.model.dao.impl;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.funfactory.cangamemake.db.Table;
import com.funfactory.cangamemake.model.dao.AbstractDAO;
import com.funfactory.cangamemake.model.dao.IRotinaPECSDAO;
import com.funfactory.cangamemake.model.entity.RotinaPECS;

public class RotinasPECSDAO extends AbstractDAO<RotinaPECS> implements IRotinaPECSDAO {

    private static final long serialVersionUID = 1L;

    public RotinasPECSDAO() {
        super(Table.ROTINA_PECS.getName());
    }
    
    @Override
    protected RotinaPECS cursorToObject(Cursor cursor) {
        RotinaPECS rotinaPecs = new RotinaPECS();

        int column = cursor.getColumnIndex(RotinaPECS.ID_COLUMN);
        if (column >= 0) {
            rotinaPecs.setIdEntity(cursor.getLong(column));
        }

        column = cursor.getColumnIndex(RotinaPECS.ID_PECS);
        if (column >= 0) {
            rotinaPecs.setPecsId(cursor.getLong(column));
        }

        column = cursor.getColumnIndex(RotinaPECS.ID_ROTINA);
        if (column >= 0) {
            rotinaPecs.setRotinaId(cursor.getLong(column));
        }

        column = cursor.getColumnIndex(RotinaPECS.POSICAO);
        if (column >= 0) {
            rotinaPecs.setPosicao(cursor.getInt(column));
        }

        return rotinaPecs;
    }

    @Override
    protected ContentValues objectToContentValues(RotinaPECS entity) {

        ContentValues contentValues = new ContentValues();

        if (entity.getIdEntity() != null) {
            contentValues.put(RotinaPECS.ID_COLUMN, entity.getIdEntity());
        }

        contentValues.put(RotinaPECS.ID_PECS, entity.getPecsId());
        contentValues.put(RotinaPECS.ID_ROTINA, entity.getRotinaId());
        contentValues.put(RotinaPECS.POSICAO, entity.getPosicao());
        
        return contentValues;
    }

    @Override
    public void removerPorPECS(long pecsId) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("DELETE FROM ");
        buffer.append(Table.ROTINA_PECS.getName());
        buffer.append(" WHERE ");
        buffer.append(RotinaPECS.ID_PECS);
        buffer.append("=");
        buffer.append(pecsId);
        buffer.append(";");

        executeSQL(buffer.toString());
    }

    @Override
    public void removerPorRotina(long rotinaId) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("DELETE FROM ");
        buffer.append(Table.ROTINA_PECS.getName());
        buffer.append(" WHERE ");
        buffer.append(RotinaPECS.ID_ROTINA);
        buffer.append("=");
        buffer.append(rotinaId);
        buffer.append(";");
        
        executeSQL(buffer.toString());
    }

    @Override
    public RotinaPECS recuperarEntrada(long rotinaId, long pecsId) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT * FROM ");
        buffer.append(Table.ROTINA_PECS.getName());
        buffer.append(" WHERE ");
        buffer.append(RotinaPECS.ID_PECS);
        buffer.append("=");
        buffer.append(pecsId);
        buffer.append(" AND ");
        buffer.append(RotinaPECS.ID_ROTINA);
        buffer.append("=");
        buffer.append(rotinaId);
        buffer.append(";");

        Cursor cursor = executeSelectQuery(buffer.toString());
        List<RotinaPECS> list = parseCursorToList(cursor);

        if (list != null && list.size() > 0) {
            return list.get(0);
        }

        return null;
    }
    
    @Override
    public List<RotinaPECS> recuperarPorRotina(long rotinaId) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT * FROM ");
        buffer.append(Table.ROTINA_PECS.getName());
        buffer.append(" WHERE ");
        buffer.append(RotinaPECS.ID_ROTINA);
        buffer.append("=");
        buffer.append(rotinaId);
        buffer.append(";");

        Cursor cursor = executeSelectQuery(buffer.toString());
        List<RotinaPECS> list = parseCursorToList(cursor);

        if (list != null && list.size() > 0) {
            return list;
        }

        return null;
    }

}
