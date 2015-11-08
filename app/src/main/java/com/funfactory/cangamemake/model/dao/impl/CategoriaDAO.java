package com.funfactory.cangamemake.model.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.funfactory.cangamemake.db.Table;
import com.funfactory.cangamemake.model.dao.AbstractDAO;
import com.funfactory.cangamemake.model.dao.ICategoriaDAO;
import com.funfactory.cangamemake.model.entity.Categoria;

public class CategoriaDAO extends AbstractDAO<Categoria> implements ICategoriaDAO {

    private static final long serialVersionUID = 1L;

    public CategoriaDAO() {
        super(Table.CATEGORIA.getName());
    }

    @Override
    protected Categoria cursorToObject(Cursor cursor) {

        final Categoria obj = new Categoria();

        int column = cursor.getColumnIndex(Categoria.ID_COLUMN);
        if (column >= 0) {
            obj.setIdEntity(cursor.getLong(column));
        }

        column = cursor.getColumnIndex(Categoria.DESCRICAO);
        if (column >= 0) {
            obj.setDescricao(cursor.getString(column));
        }

        return obj;
    }

    @Override
    protected ContentValues objectToContentValues(Categoria entity) {

        final ContentValues values = new ContentValues();

        if (entity != null) {
            values.put(Categoria.ID_COLUMN, entity.getIdEntity());
            values.put(Categoria.DESCRICAO, entity.getDescricao());
        }

        return values;
    }
    
    @Override
    public int delete(Categoria entity) throws SQLException {
        
        
        
        return super.delete(entity);
    }

}
