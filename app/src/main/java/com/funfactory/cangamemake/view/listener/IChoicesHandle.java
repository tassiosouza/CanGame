package com.funfactory.cangamemake.view.listener;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Handle users choices into a Listview
 */
public interface IChoicesHandle {

    /**
     * Callback method to be invoked when an item has been clicked.
     */
    void onItemSelected(int position);

    /**
     * Called to report a user click on an action button.
     */
    boolean onActionItemClicked(ActionMode mode, MenuItem item);

    /**
     * Called when action mode is first created. The menu supplied will be used to generate action
     * buttons for the action mode.
     */
    boolean onCreateActionMode(ActionMode mode, Menu menu);
}