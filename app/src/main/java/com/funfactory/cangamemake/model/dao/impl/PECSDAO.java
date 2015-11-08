package com.funfactory.cangamemake.model.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.funfactory.cangamemake.db.Table;
import com.funfactory.cangamemake.model.dao.AbstractDAO;
import com.funfactory.cangamemake.model.dao.ICategoriaDAO;
import com.funfactory.cangamemake.model.dao.IPECSDAO;
import com.funfactory.cangamemake.model.entity.Categoria;
import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.model.entity.Ranking;

public class PECSDAO extends AbstractDAO<PECS> implements IPECSDAO {

    private static final long serialVersionUID = 1L;
    private ICategoriaDAO     categoriaDAO;

    public PECSDAO() {
        super(Table.PECS.getName());
        categoriaDAO = new CategoriaDAO();
    }

    @Override
    protected PECS cursorToObject(Cursor cursor) {
        final PECS obj = new PECS();

        int column = cursor.getColumnIndex(PECS.ID_COLUMN);
        if (column >= 0) {
            obj.setIdEntity(cursor.getLong(column));
        }

        column = cursor.getColumnIndex(PECS.LEGENDA);
        if (column >= 0) {
            obj.setLegenda(cursor.getString(column));
        }

        column = cursor.getColumnIndex(PECS.DATA_HORA_CRIACAO);
        if (column >= 0) {
            obj.setDataHoraCriacao(parse(cursor.getString(column)));
        }

        column = cursor.getColumnIndex(PECS.ID_CATEGORIA);
        if (column >= 0) {
            int idCategoria = cursor.getInt(column);
            Categoria categoria = categoriaDAO.getEntityById(idCategoria);
            obj.setCategoria(categoria);
        }

        column = cursor.getColumnIndex(PECS.IMAGE_PATH);
        if (column >= 0) {
            obj.setImagePath(cursor.getString(column));
        }

        column = cursor.getColumnIndex(PECS.SOUND_PATH);
        if (column >= 0) {
            obj.setPathSound(cursor.getString(column));
        }

        column = cursor.getColumnIndex(PECS.VIDEO_PATH);
        if (column >= 0) {
            obj.setPathVideo(cursor.getString(column));
        }

        return obj;
    }

    @Override
    protected ContentValues objectToContentValues(PECS entity) {
        final ContentValues values = new ContentValues();

        if (entity.getIdEntity() != null) {
            values.put(PECS.ID_COLUMN, entity.getIdEntity());
        }

        if (entity.getLegenda() != null) {
            values.put(PECS.LEGENDA, entity.getLegenda());
        }

        if (entity.getDataHoraCriacao() != null) {
            values.put(PECS.DATA_HORA_CRIACAO, format(entity.getDataHoraCriacao()));
        }

        if (entity.getCategoria() != null) {
            values.put(PECS.ID_CATEGORIA, entity.getCategoria().getIdEntity().intValue());
        }

        if (entity.getImagePath() != null) {
            values.put(PECS.IMAGE_PATH, entity.getImagePath());
        }

        if (entity.getPathSound() != null) {
            values.put(PECS.SOUND_PATH, entity.getPathSound());
        }

        if (entity.getPathVideo() != null) {
            values.put(PECS.VIDEO_PATH, entity.getPathVideo());
        }

        return values;
    }

    @Override
    public List<PECS> listAllByPaciente(long idPaciente) {
        String query = "SELECT " + getTable() + ".* FROM " + getTable() + ", " + Table.RANKING.getName() + " WHERE "
                + getTable() + "." + PECS.ID_COLUMN + " = " + Table.RANKING.getName() + "." + Ranking.ID_PECS + " AND "
                + Table.RANKING.getName() + "." + Ranking.ID_PACIENTE + " = " + idPaciente;

        return getListSQL(query, new HashMap<String, String>(), getTable() + "." + PECS.ID_COLUMN + " ASC");
    }

    @Override
    public void associarPECPaciente(long pecId, long pacientId) {
        RankingDAO dao = new RankingDAO();

        Ranking ranking = recuperarRaking(dao, pecId, pacientId);

        if (ranking == null) {
            criarAssociacao(dao, pecId, pacientId);
        }
    }

    private Ranking recuperarRaking(RankingDAO dao, long pecId, long pacientId) {
        HashMap<String, String> fields = new HashMap<String, String>();
        fields.put(Ranking.ID_PACIENTE, Long.toString(pacientId));
        fields.put(Ranking.ID_PECS, Long.toString(pecId));
        return dao.getEntityByFilter(fields);
    }

    private void criarAssociacao(RankingDAO dao, long pecId, long pacientId) {
        Ranking ranking = new Ranking();
        ranking.setDataHoraCriacao(new Date());
        ranking.setPontuacao(0);
        dao.save(ranking);
    }

    @Override
    public void trocarCategoria(long previousCategoriaId, long newCategoriaId) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("UPDATE ");
        buffer.append(Table.PECS.getName());
        buffer.append(" SET ");
        buffer.append(PECS.ID_CATEGORIA);
        buffer.append("=");
        buffer.append(newCategoriaId);
        buffer.append(" WHERE ");
        buffer.append(PECS.ID_CATEGORIA);
        buffer.append("=");
        buffer.append(previousCategoriaId);
        buffer.append(";");

        executeSQL(buffer.toString());
    }

    @Override
    public List<PECS> getPECSByRotina(long idRotina) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT tb_pecs.id,  " + "tb_pecs.legenda, " + "tb_pecs.id_categoria, " + "tb_pecs.image_path, "
                + "tb_pecs.sound_path, " + "tb_pecs.video_path, " + "tb_pecs.dt_hr_criacao, "
                + "tb_rotina_pecs.posicao " + "FROM tb_pecs,tb_rotina_pecs" + " WHERE id_rotina=" + idRotina
                + " AND id_pecs=tb_pecs.id " + "order by tb_rotina_pecs.posicao asc");

        Cursor cursor = executeSelectQuery(buffer.toString());
        return parseCursorToList(cursor);
    }

}
