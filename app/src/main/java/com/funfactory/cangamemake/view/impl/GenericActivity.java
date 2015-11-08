package com.funfactory.cangamemake.view.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.funfactory.cangamemake.presenter.IGenericActivity;
import com.funfactory.cangamemake.presenter.impl.GenericPresenter;
import com.funfactory.cangamemake.util.DialogUtil;
import com.funfactory.cangamemake.util.DialogUtil.DialogCallback;
import com.funfactory.cangamemake.util.ToastUtil;

public abstract class GenericActivity extends Activity implements IGenericActivity {

    private Activity                    mContext;

    private ArrayList<GenericPresenter> mPresenters = new ArrayList<GenericPresenter>();

    public void setParentDependencies(Activity activity, Object presenter) {
        mContext = activity;

        if (presenter instanceof GenericPresenter) {
            mPresenters.add((GenericPresenter) presenter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (GenericPresenter presenter : mPresenters) {
            presenter.onCreate(savedInstanceState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            for (GenericPresenter presenter : mPresenters) {
                presenter.onResume();
            }
        } catch (Exception e) {
            Log.e("CanGameMaker", "Error on loading activity");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (GenericPresenter presenter : mPresenters) {
            presenter.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (GenericPresenter presenter : mPresenters) {
            presenter.onDestroy();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (GenericPresenter presenter : mPresenters) {
            presenter.onActivityResult(requestCode, resultCode, data);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = false;

        for (GenericPresenter presenter : mPresenters) {
            result = result || presenter.onCreateOptionsMenu(menu);
        }

        return result;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        boolean result = false;

        for (GenericPresenter presenter : mPresenters) {
            result = result || presenter.onMenuItemSelected(featureId, item);
        }

        return result;
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void toastMessage(int resId) {
        ToastUtil.getInstance().callToast(mContext, resId);
    }

    @Override
    public void toastMessage(String message) {
        ToastUtil.getInstance().callToast(mContext, message);
    }

    @Override
    public Activity getContext() {
        return mContext;
    }

    @Override
    public void finalizarActivity() {
        mContext.finish();
    }

    @Override
    public void executarIntent(Intent intent) {
        mContext.startActivity(intent);
    }

    @Override
    public void confirmarAcao(int titulo, int mensagem, DialogCallback callback) {
        AlertDialog dialog = DialogUtil.generateDialog(mContext, titulo, mensagem, callback);
        dialog.show();
    }

}
