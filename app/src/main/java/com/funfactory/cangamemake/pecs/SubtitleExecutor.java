package com.funfactory.cangamemake.pecs;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.view.impl.ExecutarPECSActivity;

/**
 * Classe de exibição da Legenda da PECS.
 * 
 * @author Silvino Neto
 */
public class SubtitleExecutor extends Executor {

	/**
	 * Construtor da classe.
	 * 
	 * @param step
	 * @param view
	 * @param seconds
	 */
	public SubtitleExecutor(TextView cronometro, Steps step, View view, int seconds) {
		super(cronometro, step, view, seconds);
	}

	@Override
	protected boolean isRunnable(PECS pecs) {
		//Código comentado em virtude de bug no cadastro de PECS, o qual não está momentaneamente armazenando
		//a Legenda vinculada a imagem.
		return pecs.getLegenda() != null;
	}

	@Override
	protected void runStep(ExecutarPECSActivity activity, PECS pecs) {
		TextView txtLegenda = (TextView) view;
		RelativeLayout legendaPanel = (RelativeLayout) txtLegenda.getParent();
		legendaPanel.setVisibility(View.VISIBLE);
		txtLegenda.setText(pecs.getLegenda());
		txtLegenda.setVisibility(View.VISIBLE);
	}

	@Override
	public void stopStep() {
		TextView txtLegenda = (TextView) view;
		RelativeLayout legendaPanel = (RelativeLayout) txtLegenda.getParent();
		legendaPanel.setVisibility(View.INVISIBLE);
		txtLegenda.setVisibility(View.INVISIBLE);
	}
}
