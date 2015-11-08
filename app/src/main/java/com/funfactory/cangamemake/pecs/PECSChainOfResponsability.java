package com.funfactory.cangamemake.pecs;

import android.view.View;
import android.widget.TextView;

/**
 * Classe de implementação do pattern Chain of Responsability para execução da
 * PECS.
 * 
 * @author Silvino Neto
 */
public class PECSChainOfResponsability {

	private static Executor currentExecutor;

	public static Executor createChain(TextView cronometro, int tempoExibicaoLegenda, int tempoReproducaoAudio, int tempoReproducaoVideo, View... views) {
		Executor executor1 = new PhotoExecutor(cronometro, Steps.PHOTO, views[0], tempoExibicaoLegenda);

		Executor executor2 = new SubtitleExecutor(cronometro, Steps.TEXT, views[1], tempoReproducaoAudio);
		executor1.setNext(executor2);

		Executor executor3 = new AudioExecutor(cronometro, Steps.AUDIO, null, tempoReproducaoVideo);
		executor2.setNext(executor3);

		Executor executor4 = new VideoExecutor(cronometro, Steps.VIDEO, null, 0);
		executor3.setNext(executor4);

		return executor1;
	}

	public static void setCurrent(Executor executor) {
		currentExecutor = executor;
	}

	public static Executor getCurrent() {
		return currentExecutor;
	}
}
