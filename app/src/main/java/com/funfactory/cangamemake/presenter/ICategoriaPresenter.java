package com.funfactory.cangamemake.presenter;

import android.view.View;

import com.funfactory.cangamemake.view.ICategoriaActivity;

public interface ICategoriaPresenter {
    
    /**
     * Set view
     */
    void setView(ICategoriaActivity view);
    
    boolean isDadosValidos(String nome);

    void onClick(View view);

}
