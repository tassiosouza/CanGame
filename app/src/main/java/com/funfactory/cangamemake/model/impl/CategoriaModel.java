package com.funfactory.cangamemake.model.impl;

import java.util.List;

import com.funfactory.cangamemake.CanGameMake;
import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.ICategoriaModel;
import com.funfactory.cangamemake.model.IPECSModel;
import com.funfactory.cangamemake.model.IRotinaModel;
import com.funfactory.cangamemake.model.dao.ICategoriaDAO;
import com.funfactory.cangamemake.model.dao.impl.CategoriaDAO;
import com.funfactory.cangamemake.model.entity.Categoria;

public class CategoriaModel implements ICategoriaModel {

    /**
     * Propriedades de categoria
     */
    public static final int CATEGORIA_GERAL_DESCRICAO = R.string.categoria_permanente;
    public static final Long CATEGORIA_GERAL_ID        = 1L;

    /**
     * DAO
     */
    private ICategoriaDAO   categoriaDAO              = new CategoriaDAO();

    /**
     * Dependencias
     */
    private IPECSModel      mPECSModel;
    private IRotinaModel    mRotinaModel;

    @Override
    public List<Categoria> listAll() {
    	List<Categoria> list = categoriaDAO.getAll();
        if(list == null || list.isEmpty()) {
        	Categoria cat = new Categoria();
        	cat.setDescricao("Geral");
        	saveOrUpdate(cat);
        	list = categoriaDAO.getAll();
        }
        
        return list;
    }

    @Override
    public void saveOrUpdate(Categoria object) {
        categoriaDAO.save(object);
    }

    @Override
    public void remove(Categoria categoria) {

        if (categoria.getIdEntity() == CATEGORIA_GERAL_ID) {

            String descricao = CanGameMake.getContext().getResources().getString(CATEGORIA_GERAL_DESCRICAO);
            String formato = CanGameMake.getContext().getResources().getString(R.string.delecao_categoria_obrigatoria);
            String mensagem = String.format(formato, descricao);

            throw new IllegalArgumentException(mensagem);
        }

        removerRelacionamentosExistentes(categoria);
        categoriaDAO.delete(categoria);
    }

    private void removerRelacionamentosExistentes(Categoria categoria) {
        if (mPECSModel == null) {
            mPECSModel = new PECSModel();
        }

        if (mRotinaModel == null) {
            mRotinaModel = new RotinaModel();
        }

        mPECSModel.trocarCategoria(categoria.getIdEntity(), CATEGORIA_GERAL_ID);
        mRotinaModel.trocarCategoria(categoria.getIdEntity(), CATEGORIA_GERAL_ID);
    }

    @Override
    public Categoria getCategoriaById(long entityId) {
        return categoriaDAO.getEntityById(entityId);
    }

    public Categoria criarCategoriaGeral() {

        Categoria categoria = getCategoriaById(1);
        return categoria;
    }
    @Override
    public Categoria getCategoriaGeral() {
        return categoriaDAO.getEntityById(CATEGORIA_GERAL_ID);
    }
}
