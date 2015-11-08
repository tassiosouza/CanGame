package com.funfactory.cangamemake.pecs;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.util.DateUtil;
import com.funfactory.cangamemake.view.impl.ExecutarPECSActivity;

/**
 * Classe Executora de PECS responsável pela implementação do pattern Chain of
 * Responsability.
 * 
 * @author Silvino Neto
 */
public abstract class Executor {

	protected Steps mStep;
	protected View view;
	protected TextView cronometro;
	protected int seconds;

	private Executor next;
	private Executor previous;

	protected Handler handler;
	protected CronometroPECS timer;

	/**
	 * Construtor da classe.
	 * 
	 * @param step
	 * @param view
	 * @param seconds
	 */
	public Executor(TextView cronometro, Steps step, View view, int seconds) {
		this.cronometro = cronometro;
		this.mStep = step;
		this.view = view;
		this.seconds = seconds;
	}

	public void setNext(Executor next) {
		this.next = next;
		next.setPrevious(this);
	}

	public void setPrevious(Executor previous) {
		this.previous = previous;
	}

	/**
	 * Executa PECS a partir da Activity.
	 * 
	 * @param activity
	 * @param step
	 * @param pecs
	 * @param seconds
	 */
	public void run(final ExecutarPECSActivity activity, final PECS pecs, final boolean rewind) {
		if (!isRunnable(pecs)) {
			runNext(activity, pecs);
			return;
		}

		PECSChainOfResponsability.setCurrent(this);
		handler = new Handler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				PECSChainOfResponsability.setCurrent(Executor.this);
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						runStep(activity, pecs);
					}
				});

				long timeout = seconds * 1000;

				if (timer != null && timer.secondsRemaining > 0 && !rewind) {
					timeout = timer.secondsRemaining * 1000;
					timer.resume();
				}
				else
				{
					timer = new CronometroPECS(timeout, 1000, activity, pecs);
					timer.start();
				}
			}
		});
	}

	public void previousStep(ExecutarPECSActivity activity, PECS pecs) {
		if (previous != null) {
			stopStep();
			cancelaOperacoes(false);
			PECSChainOfResponsability.setCurrent(previous);
			previous.run(activity, pecs, true);
		}
	}

	public void skipStep(ExecutarPECSActivity activity, PECS pecs) {
		cancelaOperacoes(false);
		this.runStep(activity, pecs);

		if (next != null) {
			PECSChainOfResponsability.setCurrent(next);
			next.run(activity, pecs, false);
		}
	}

	public void cancelaOperacoes(boolean pause) {
	    
		if (timer != null) {
			if (pause) {
				timer.pause();
			} else {
				timer.cancel();
				timer = null;
			}
		}
		
		if(this instanceof AudioExecutor) {
			((AudioExecutor)this).stopStep();
		}

		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
		}

		if (previous != null && previous.handler != null) {
			previous.handler.removeCallbacksAndMessages(null);

			if (previous.timer != null)
			{
				if (pause) {
					previous.timer.pause();
				} else {
					previous.timer.cancel();
					previous.timer = null;
				}
			}
		}

		if (next != null && next.handler != null) {
			next.handler.removeCallbacksAndMessages(null);

			if (next.timer != null)
			{
				if (pause) {
					next.timer.pause();
				} else {
					next.timer.cancel();
					next.timer = null;
				}
			}
		}
	}

	protected void runNext(ExecutarPECSActivity activity, PECS pecs) {
		if (next != null) {
			cancelaOperacoes(false);
			next.run(activity, pecs, false);
		} else {
			PECSChainOfResponsability.setCurrent(null);
			activity.finalizarPECS();
		}
	}

	protected abstract boolean isRunnable(PECS pecs);

	/**
	 * Implementa a rotina específica da fase da PECS.
	 * 
	 * @param pecs
	 */
	protected abstract void runStep(ExecutarPECSActivity activity, PECS pecs);

	/**
	 * Interrompe a execução do Passo de forma abrupta.
	 */
	public abstract void stopStep();

	public Steps getStep() {
		return mStep;
	}

	public Executor getPrevious() {
		return previous;
	}

	class CronometroPECS extends PECSCountDownTimer {
		long secondsRemaining;
		ExecutarPECSActivity activity;
		PECS pecs;
		static final String ZERO = "00:00";

		public CronometroPECS(long millisInFuture, long countDownInterval, ExecutarPECSActivity activity, PECS pecs) {
			super(millisInFuture+1000, countDownInterval);
			this.activity = activity;
			this.pecs = pecs;
		}

		@Override
		public void onFinish() {
			cronometro.setText(ZERO);

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					Executor.this.runNext(activity, pecs);
				}
			}, 1000);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			secondsRemaining = millisUntilFinished / 1000;
			cronometro.setText(DateUtil.format(secondsRemaining));
		}
	}
}
