package com.funfactory.cangamemake.db;

import java.util.ArrayList;
import java.util.List;

import com.funfactory.cangamemake.CanGameMake;
import com.funfactory.cangamemake.model.entity.Categoria;
import com.funfactory.cangamemake.model.impl.CategoriaModel;

public class InicialInputData {

    private static List<String> queries;

    public static List<String> getConfigurationQueries() {

        queries = new ArrayList<String>();

        createCategoryQueries();

        return queries;
    }

    private static void createCategoryQueries() {

        String descricao = CanGameMake.getContext().getResources().getString(CategoriaModel.CATEGORIA_GERAL_DESCRICAO);

        StringBuffer buffer = new StringBuffer();
        buffer.append("INSERT INTO ");
        buffer.append(Table.CATEGORIA.getName());
        buffer.append("(");
        buffer.append(Categoria.ID_COLUMN);
        buffer.append(",");
        buffer.append(Categoria.DESCRICAO);
        buffer.append(")");
        buffer.append(" VALUES ");
        buffer.append("(");
        buffer.append(CategoriaModel.CATEGORIA_GERAL_ID);
        buffer.append(",");
        buffer.append("\"" + descricao + "\"");
        buffer.append(")");
        buffer.append(";");

        queries.add(buffer.toString());
    }

}
