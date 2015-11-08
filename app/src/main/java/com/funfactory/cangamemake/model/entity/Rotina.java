package com.funfactory.cangamemake.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade representativa das Rotinas do Paciente.
 * 
 */
public class Rotina extends AbstractBaseEntity {

    private static final long  serialVersionUID = 1L;

    /**
     * Table Columns
     */
    public static final String NOME             = "nome";
    public static final String ID_CATEGORIA     = "id_categoria";

    private String             nome             = "";

    private Categoria          categoria;

    private List<PECS>         listaPECS;

    public Rotina() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!super.equals(obj))
            return false;

        if (getClass() != obj.getClass())
            return false;

        Rotina other = (Rotina) obj;

        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;

        if (!categoria.equals(other.categoria))
            return false;

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Rotina [nome=" + nome + ", idCategoria=  " + categoria + "]";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<PECS> getListaPECS() {
        if (listaPECS == null) {
            listaPECS = new ArrayList<PECS>();
        }
        return listaPECS;
    }

    public void setListaPECS(List<PECS> listaPECS) {
        this.listaPECS = listaPECS;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
