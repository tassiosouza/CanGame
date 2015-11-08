package com.funfactory.cangamemake.model.entity;

import java.util.Date;

public class PECS extends AbstractBaseEntity implements Cloneable {

    private static final long  serialVersionUID  = 1L;

    /**
     * Table Columns
     */
    public static final String LEGENDA           = "legenda";
    public static final String DATA_HORA_CRIACAO = "dt_hr_criacao";
    public static final String ID_CATEGORIA      = "id_categoria";
    public static final String IMAGE_PATH        = "image_path";
    public static final String SOUND_PATH        = "sound_path";
    public static final String VIDEO_PATH        = "video_path";

    private String             legenda;
    private Date               dataHoraCriacao;
    private String             pathImage;
    private String             pathSound;
    private String             pathVideo;
    private Categoria          categoria;

    private Ranking            ranking;

    public PECS() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((dataHoraCriacao == null) ? 0 : dataHoraCriacao.hashCode());
        result = prime * result + categoria.getIdEntity().intValue();
        result = prime * result + ((legenda == null) ? 0 : legenda.hashCode());
        result = prime * result + ((pathImage == null) ? 0 : pathImage.hashCode());
        result = prime * result + ((pathSound == null) ? 0 : pathSound.hashCode());
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
        PECS other = (PECS) obj;
        if (dataHoraCriacao == null) {
            if (other.dataHoraCriacao != null)
                return false;
        } else if (!dataHoraCriacao.equals(other.dataHoraCriacao))
            return false;
        if (!categoria.equals(other.getCategoria()))
            return false;
        if (legenda == null) {
            if (other.legenda != null)
                return false;
        } else if (!legenda.equals(other.legenda))
            return false;
        if (pathImage == null) {
            if (other.pathImage != null)
                return false;
        } else if (!pathImage.equals(other.pathImage))
            return false;
        if (pathSound == null) {
            if (other.pathSound != null)
                return false;
        } else if (!pathSound.equals(other.pathSound))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PECS [legenda=" + legenda + ", dataHoraCriacao=" + dataHoraCriacao + ", categoria=" + categoria
                + ", pathImage=" + pathImage + ", pathSound=" + pathSound + "]";
    }

    public String getLegenda() {
        return legenda;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public Date getDataHoraCriacao() {
        return dataHoraCriacao;
    }

    public void setDataHoraCriacao(Date dataHoraCriacao) {
        this.dataHoraCriacao = dataHoraCriacao;
    }

    public String getImagePath() {
        return pathImage;
    }

    public void setImagePath(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getPathSound() {
        return pathSound;
    }

    public void setPathSound(String pathSound) {
        this.pathSound = pathSound;
    }

    public String getPathVideo() {
        return pathVideo;
    }

    public void setPathVideo(String pathVideo) {
        this.pathVideo = pathVideo;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

    public Ranking getRanking() {
        return ranking;
    }

    @Override
    public PECS clone() {
        try {
            return (PECS) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.getMessage());
        }
    }

}
