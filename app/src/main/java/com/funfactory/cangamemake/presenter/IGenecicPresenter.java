package com.funfactory.cangamemake.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public interface IGenecicPresenter {
    
    public void onCreate(Bundle savedInstanceState);

    public void onResume();
    
    public void onPause();

    public void onDestroy();
    
    public void onActivityResult(int requestCode, int resultCode, Intent data);
    
    public boolean onMenuItemSelected(int featureId, MenuItem item);
    
    public boolean onCreateOptionsMenu(Menu menu);
    
}
