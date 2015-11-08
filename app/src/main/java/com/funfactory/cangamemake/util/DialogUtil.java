package com.funfactory.cangamemake.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.funfactory.cangamemake.R;
import com.google.inject.Singleton;

/**
 * Class to manage the appliation alerts and dialogs.
 * 
 */
@Singleton
public class DialogUtil {

    public interface DialogCallback {
        public void onResult(boolean result);
    }

    public static AlertDialog generateDialog(Activity activity, int title, int message, final DialogCallback callback) {

        return new AlertDialog.Builder(activity).setTitle(title).setCancelable(false).setMessage(message)
                .setCancelable(false).setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (callback != null) {
                            callback.onResult(true);
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (callback != null) {
                            callback.onResult(false);
                        }
                        dialog.dismiss();
                    }
                }).create();
    }
}
