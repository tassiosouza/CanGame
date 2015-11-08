/**
 * 
 */
package com.funfactory.cangamemake.pecs;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.util.PhotoUtil;
import com.funfactory.cangamemake.view.impl.ExecutarPECSActivity;

/**
 * Classe de execução da Carga da Foto.
 * 
 * @author Silvino Neto
 */
public class PhotoExecutor extends Executor {

	/**
	 * Construtor da classe.
	 * 
	 * @param cronometro
	 * @param step
	 * @param view
	 * @param seconds
	 */
	public PhotoExecutor(TextView cronometro, Steps step, View view, int seconds) {
		super(cronometro, step, view, seconds);
	}

	@Override
	protected boolean isRunnable(PECS pecs) {
		return true;
	}

	@Override
	protected void runStep(ExecutarPECSActivity activity, PECS pecs) {
		Bitmap bitmap = PhotoUtil.getInstance().createBitmap(
				pecs.getImagePath());
		if (bitmap == null) {
			bitmap = PhotoUtil.getInstance().decodeBitmap(pecs.getImagePath());
		}

		if (bitmap != null) {
			((ImageView) view).setImageBitmap(bitmap);
		}
	}

	@Override
	public void stopStep() {
	}
}
