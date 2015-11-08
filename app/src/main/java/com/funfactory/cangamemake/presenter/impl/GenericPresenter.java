package com.funfactory.cangamemake.presenter.impl;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.funfactory.cangamemake.presenter.IGenecicPresenter;

public abstract class GenericPresenter implements IGenecicPresenter {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // No Implementation
    }

    @Override
    public void onResume() {
        // No Implementation
    }

    @Override
    public void onPause() {
        // No Implementation
    }

    @Override
    public void onDestroy() {
        // No Implementation
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // No Implementation
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
    
}
