package com.funfactory.cangamemake.pecs;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.util.AudioManager;
import com.funfactory.cangamemake.view.impl.ExecutarPECSActivity;

/**
 * Classe de reprodução de Áudio da PECS.
 * 
 * @author Silvino Neto
 */
public class AudioExecutor extends Executor {

	private AudioManager mAudioManager = new AudioManager();

	/**
	 * Construtor da classe.
	 * 
	 * @param step
	 * @param view
	 * @param seconds
	 */
	public AudioExecutor(TextView cronometro, Steps step, View view, int seconds) {
		super(cronometro, step, view, seconds);
	}

	@Override
	protected boolean isRunnable(PECS pecs) {
		return pecs.getPathSound() != null && pecs.getPathSound().length() > 0;
	}

	/**
	 * Executa PECS a partir da Activity.
	 * 
	 * @param activity
	 * @param step
	 * @param pecs
	 * @param seconds
	 */
	public void run(final ExecutarPECSActivity activity, final PECS pecs,
			final boolean rewind) {
		if (!isRunnable(pecs)) {
			runNext(activity, pecs);
			return;
		}

		PECSChainOfResponsability.setCurrent(this);
		handler = new Handler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				PECSChainOfResponsability.setCurrent(AudioExecutor.this);
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						runStep(activity, pecs);
					}
				});

				long timeout = seconds * 1000;

				if (timer != null && timer.secondsRemaining > 0 && !rewind) {
					timeout = timer.secondsRemaining * 1000;
				} else {
					timer = new CronometroPECS(timeout - 900, 1000, activity,
							pecs);
				}
			}
		});
	}

	@Override
	protected void runStep(ExecutarPECSActivity activity, PECS pecs) {
		if (mAudioManager.isPlaying()) {
			mAudioManager.pararExecAudio();
		} else {
			String path = pecs.getPathSound();
			mAudioManager.executar(path, new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					if (timer != null) {
						if (timer.secondsRemaining > 0) {
							timer.resume();
						} else {
							timer.start();
						}
					}
				}
			});
		}
	}

	@Override
	public void stopStep() {
		mAudioManager.pararExecAudio();
	}
}
