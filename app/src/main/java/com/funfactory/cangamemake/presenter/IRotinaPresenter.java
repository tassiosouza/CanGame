package com.funfactory.cangamemake.presenter;

import com.funfactory.cangamemake.model.entity.Rotina;
import com.funfactory.cangamemake.view.IRotinaActivity;

public interface IRotinaPresenter {

    void setView(IRotinaActivity view);

    void updateView();

    boolean salvar();

    boolean isDadosValidos();

    void onOptionSeleted(int viewOptionId);

    public Rotina getRotina();

    public void setRotina(Rotina mRotina);
}
