package com.funfactory.cangamemake.model.entity;

import java.util.Date;

import com.funfactory.cangamemake.util.DateUtil;

public class Paciente extends AbstractBaseEntity {

    private static final long  serialVersionUID = 1L;
    private final int          PRIME            = 37;

    /**
     * Table Columns
     */
    public static final String NOME             = "nome";
    public static final String NOME_PAI         = "nome_pai";
    public static final String NOME_MAE         = "nome_mae";
    public static final String CONTATO_PAI      = "contato_pai";
    public static final String CONTATO_MAE      = "contato_mae";
    public static final String SEXO             = "sexo";
    public static final String DATA_NASCIMENTO  = "dt_nasc";
    public static final String IMAGE_PATH       = "image_path";

    /**
     * Attributes
     */
    private Date               dataNascimento;
    private String             nome;
    private String             nomePai;
    private String             nomeMae;
    private String             contatoPai;
    private String             contatoMae;
    private String             sexo;
    private String             imagePath;
    private boolean            isCurrentSelect;

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = PRIME * result + ((nome == null) ? 0 : nome.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Paciente other = (Paciente) obj;

        if ((getIdEntity() == null && other.getIdEntity() != null) || (nome == null && other.nome != null)
                || (sexo == null && other.sexo != null) || (dataNascimento == null && other.dataNascimento != null)
                // !=
                || (getIdEntity() != null && other.getIdEntity() == null) || (nome != null && other.nome == null)
                || (sexo != null && other.sexo == null) || (dataNascimento != null && other.dataNascimento == null)) {
            return false;
        }

        // Only need to check one entity becouse of above validation
        if (nome != null && !nome.equals(other.nome)) {
            return false;
        }
        if (sexo != null && !sexo.equals(other.sexo)) {
            return false;
        }
        if (dataNascimento != null && !dataNascimento.equals(other.dataNascimento)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Paciente{id=\"");
        sb.append(super.getIdEntity());
        sb.append(", nome=").append(this.nome);
        sb.append(", sexo=").append(this.sexo);
        sb.append(", dataNascimento=\"").append(DateUtil.format(this.dataNascimento));
        sb.append("\"}");
        return sb.toString();
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getContatoPai() {
        return contatoPai;
    }

    public void setContatoPai(String contatoPai) {
        this.contatoPai = contatoPai;
    }

    public String getContatoMae() {
        return contatoMae;
    }

    public void setContatoMae(String contatoMae) {
        this.contatoMae = contatoMae;
    }

    public boolean isCurrentSelected() {
        return this.isCurrentSelect;
    }

    public void setCurrentSelect(boolean isCurrentSelect) {
        this.isCurrentSelect = isCurrentSelect;
    }
}
