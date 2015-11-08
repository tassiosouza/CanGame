package com.funfactory.cangamemake.view.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.funfactory.cangamemake.view.listener.IChoicesHandle;

@SuppressLint("UseSparseArrays")
public class ListSelectorHandler  {
    
    private IChoicesHandle     mHandle;

    private ActionMode         mActionMode;

    private HashMap<Integer, Integer> mList = new HashMap<Integer, Integer>();
    
    public ListSelectorHandler(IChoicesHandle handle) {
        this.mHandle = handle;
    }
    
    public void setListView(ListView listView){
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(multiChoiceModeListener);
        listView.setOnItemClickListener(itemClickListener);
    }
    
    public void setListView(ListView listView, int selectionMode){
        listView.setChoiceMode(selectionMode);
        listView.setMultiChoiceModeListener(multiChoiceModeListener);
        listView.setOnItemClickListener(itemClickListener);
    }
    
    public ArrayList<Integer> getMultipleChoiceList(){
        return new ArrayList<Integer>(mList.values());
    }
    
    public void exitMode(){
        mList.clear();
        mActionMode.finish();
    }
    
    private MultiChoiceModeListener multiChoiceModeListener = new MultiChoiceModeListener() {
        
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
        
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // No Implementation
        }
        
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mActionMode = mode;
            return mHandle.onCreateActionMode(mode, menu);
        }
        
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return mHandle.onActionItemClicked(mode, item);
        }
        
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        
            if (!mList.containsKey(position)){
                mList.put(position, position);
            } 
            else {
                mList.remove(position);
            }
            
            mode.setTitle(Integer.toString(mList.size()));
        }
    };
    
    public AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mHandle.onItemSelected(position);
        }
    }; 
}
