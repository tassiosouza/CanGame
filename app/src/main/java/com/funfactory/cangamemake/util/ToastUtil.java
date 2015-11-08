package com.funfactory.cangamemake.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static ToastUtil sInstance;
    public static Toast     mToast;

    public synchronized static ToastUtil getInstance() {
        if (sInstance == null) {
            sInstance = new ToastUtil();
        }
        return sInstance;
    }

    public void callToast(Context context, String message) {
        cancelPreviousToast();
        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void callToast(Context context, int resourceId) {
        cancelPreviousToast();
        mToast = Toast.makeText(context, resourceId, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void cancelPreviousToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
