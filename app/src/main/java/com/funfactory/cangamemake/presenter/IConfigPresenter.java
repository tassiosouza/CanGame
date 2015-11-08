package com.funfactory.cangamemake.presenter;

import com.funfactory.cangamemake.view.IEdicaoActivity;

public interface IConfigPresenter {
	
	/**
	 * Set view
	 * 
	 * @param view
	 */
	void setView(IEdicaoActivity view);

	void salvarConfig(int tempoExibicaoTxt, int tempoReproducaoAudio, int tempoReproducaoVideo);

	boolean isDadosValidos(int tempoExibicaoTxt, int tempoReproducaoAudio, int tempoReproducaoVideo);
}
