package com.funfactory.cangamemake.model.entity;

import java.util.Date;

import com.funfactory.cangamemake.util.DateUtil;

/**
 * Ranking
 * 
 * @author fernando.scalia
 */
public class Ranking extends AbstractBaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Table Columns
	 */
	public static final String ID_PACIENTE = "id_paciente";
	public static final String ID_PECS = "id_pec";
	public static final String ID_ROTINA = "id_rotina";
	public static final String DATA_HORA_CRIACAO = "dt_hr_criacao";
	public static final String PONTUACAO = "pontuacao";

	/**
	 * Attributes
	 */
	private Long idPaciente;
	private Long idPECS;
	private Long idRotina;
	private Date dataHoraCriacao;
	private Integer pontuacao = 0;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((dataHoraCriacao == null) ? 0 : dataHoraCriacao.hashCode());
		result = prime * result + ((idPECS == null) ? 0 : idPECS.hashCode());
		result = prime * result
				+ ((idPaciente == null) ? 0 : idPaciente.hashCode());
		result = prime * result
				+ ((idRotina == null) ? 0 : idRotina.hashCode());
		result = prime * result
				+ ((pontuacao == null) ? 0 : pontuacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ranking other = (Ranking) obj;
		if (dataHoraCriacao == null) {
			if (other.dataHoraCriacao != null)
				return false;
		} else if (!dataHoraCriacao.equals(other.dataHoraCriacao))
			return false;
		if (idPECS == null) {
			if (other.idPECS != null)
				return false;
		} else if (!idPECS.equals(other.idPECS))
			return false;
		if (idPaciente == null) {
			if (other.idPaciente != null)
				return false;
		} else if (!idPaciente.equals(other.idPaciente))
			return false;
		if (idRotina == null) {
			if (other.idRotina != null)
				return false;
		} else if (!idRotina.equals(other.idRotina))
			return false;
		if (pontuacao == null) {
			if (other.pontuacao != null)
				return false;
		} else if (!pontuacao.equals(other.pontuacao))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Paciente{id=\"");
		sb.append(super.getIdEntity());
		sb.append(", idPaciente=").append(this.idPaciente);
		sb.append(", idPECS=").append(this.idPECS);
		sb.append(", idRotina=").append(this.idRotina);
		sb.append(", pontuacao=").append(this.pontuacao);
		sb.append(", dataHoraCriacao=\"").append(
				DateUtil.format(this.dataHoraCriacao));
		sb.append("\"}");
		return sb.toString();
	}

	public Long getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(Long idPaciente) {
		this.idPaciente = idPaciente;
	}

	public Long getIdPECS() {
		return idPECS;
	}

	public void setIdPECS(Long idPECS) {
		this.idPECS = idPECS;
	}

	public Long getIdRotina() {
		return idRotina;
	}

	public void setIdRotina(Long idRotina) {
		this.idRotina = idRotina;
	}

	public Date getDataHoraCriacao() {
		return dataHoraCriacao;
	}

	public void setDataHoraCriacao(Date dataHoraCriacao) {
		this.dataHoraCriacao = dataHoraCriacao;
	}

	public Integer getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(Integer pontuacao) {
		this.pontuacao = pontuacao;
	}

}
