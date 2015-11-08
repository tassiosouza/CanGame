package com.funfactory.cangamemake.model.entity;

public class Categoria extends AbstractBaseEntity {

	private static final long serialVersionUID = 1L;
	
	public static final String DESCRICAO = "descricao";
	
	private String descricao = "";

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Override
	public boolean equals(final Object obj) {
		return this.descricao.equals(obj);
	}

	@Override
	public String toString() {
		return this.descricao.toString();
	}
}
