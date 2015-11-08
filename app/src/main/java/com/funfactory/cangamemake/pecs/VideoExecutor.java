package com.funfactory.cangamemake.pecs;

import android.view.View;
import android.widget.TextView;

import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.util.VideoUtil;
import com.funfactory.cangamemake.view.impl.ExecutarPECSActivity;

/**
 * Classe de reprodução do vídeo da PECS.
 * 
 * @author Silvino Neto
 */
public class VideoExecutor extends Executor {

	/**
	 * Construtor da classe.
	 * 
	 * @param step
	 * @param view
	 * @param seconds
	 */
	public VideoExecutor(TextView cronometro, Steps step, View view, int seconds) {
		super(cronometro, step, view, seconds);
	}

	@Override
	protected boolean isRunnable(PECS pecs) {
		return pecs.getPathVideo() != null && pecs.getPathVideo().length() > 0;
	}

	@Override
	protected void runStep(ExecutarPECSActivity activity, PECS pecs) {
		VideoUtil.getInstance().executar(activity, pecs.getPathVideo());
	}

	@Override
	public void stopStep() {
	}
}
