package com.funfactory.cangamemake.view.impl;

import android.os.Bundle;

import com.funfactory.cangamemake.presenter.IGenericActivity;
import com.funfactory.cangamemake.presenter.impl.IBaseActivityInterface;
import com.funfactory.cangamemake.presenter.impl.StartActivityPresenter;

public class StartActivity extends GenericActivity implements IBaseActivityInterface {

    StartActivityPresenter mPresenter = new StartActivityPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setParentDependencies(this, mPresenter);
        mPresenter.setView(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public IGenericActivity getGenericMethods() {
        return this;
    }

}
