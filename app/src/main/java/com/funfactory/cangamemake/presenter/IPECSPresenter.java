package com.funfactory.cangamemake.presenter;

import com.funfactory.cangamemake.view.IPECSActivity;

public interface IPECSPresenter {

    void setView(IPECSActivity view);

    boolean onBackPressed();

    void salvarPECS(String legenda);

    void salvarTela(String legenda);

    boolean isDadosValidos(String legenda);

    void onOptionSeleted(int viewOptionId);

}
