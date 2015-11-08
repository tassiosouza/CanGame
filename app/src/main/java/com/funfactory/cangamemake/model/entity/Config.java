package com.funfactory.cangamemake.model.entity;

public class Config extends AbstractBaseEntity {

    private static final long  serialVersionUID       = 1L;

    /**
     * Table Columns
     */
    public static final String TEMPO_EXIBICAO_TXT     = "tempo_exibicao_txt";
    public static final String TEMPO_REPRODUCAO_AUDIO = "tempo_reproducao_audio";
    public static final String TEMPO_REPRODUCAO_VIDEO = "tempo_reproducao_video";
    public static final String PACIENTE_CORRENTE      = "paciente_corrente";

    private int                tempoExibicaoTxt = 5; // Valor padrão de 5 segundos, quando a configuração não foi realizada.
    private int                tempoReproducaoAudio = 10; // Valor padrão de 10 segundos, quando a configuração não foi realizada. 
    private int                tempoReproducaoVideo = 10; // Valor padrão de 10 segundos, quando a configuração não foi realizada.
    private long               pacienteCorrente;

    public int getTempoExibicaoTxt() {
        return tempoExibicaoTxt;
    }

    public void setTempoExibicaoTxt(int tempoExibicaoTxt) {
        this.tempoExibicaoTxt = tempoExibicaoTxt;
    }

    public int getTempoReproducaoAudio() {
        return tempoReproducaoAudio;
    }

    public void setTempoReproducaoAudio(int tempoReproducaoAudio) {
        this.tempoReproducaoAudio = tempoReproducaoAudio;
    }

    public int getTempoReproducaoVideo() {
        return tempoReproducaoVideo;
    }

    public void setTempoReproducaoVideo(int tempoReproducaoVideo) {
        this.tempoReproducaoVideo = tempoReproducaoVideo;
    }

    public void setPacienteCorrente(long pacienteCorrente) {
        this.pacienteCorrente = pacienteCorrente;
    }

    public long getPacienteCorrente() {
        return pacienteCorrente;
    }

    @Override
    public boolean equals(final Object obj) {
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("tempo_exibicao_txt = ").append(this.tempoExibicaoTxt);
        sb.append(", tempo_reproducao_audio = ").append(this.tempoReproducaoAudio);
        sb.append(", tempo_reproducao_video = ").append(this.tempoReproducaoVideo);
        sb.append(", paciente_corrente = ").append(this.pacienteCorrente);

        return sb.toString();
    }

}
