package com.funfactory.cangamemake.view;

import android.view.View;
import android.widget.ArrayAdapter;

import com.funfactory.cangamemake.presenter.impl.IBaseActivityInterface;
import com.funfactory.cangamemake.view.adapter.ListSelectorHandler;

public interface IListarActivity extends IBaseActivityInterface {

    public void setAdapter(@SuppressWarnings("rawtypes") ArrayAdapter adapter);

    public void setEmptyView(int stringID);

    public void setChoicesHandle(ListSelectorHandler handle);
    
    public void onClick(View view);

}
