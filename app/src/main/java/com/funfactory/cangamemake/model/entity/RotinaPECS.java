package com.funfactory.cangamemake.model.entity;

public class RotinaPECS extends AbstractBaseEntity {

    private static final long  serialVersionUID = 1L;

    /**
     * Table Columns
     */
    public static final String ID_ROTINA        = "id_rotina";
    public static final String ID_PECS          = "id_pecs";
    public static final String POSICAO          = "posicao";

    private long               pecsId;
    private long               rotinaId;
    private int                posicao;

    public long getPecsId() {
        return pecsId;
    }

    public void setPecsId(long pecsId) {
        this.pecsId = pecsId;
    }

    public long getRotinaId() {
        return rotinaId;
    }

    public void setRotinaId(long rotinaId) {
        this.rotinaId = rotinaId;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
    
    @Override
    public String toString() {
        return "pecsId= " + pecsId + ", rotinaId= " + rotinaId + ", posicao= " + posicao;
    }

}
