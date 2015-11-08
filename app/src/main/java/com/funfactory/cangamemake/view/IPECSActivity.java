package com.funfactory.cangamemake.view;

import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.model.entity.Ranking;

public interface IPECSActivity extends IEdicaoActivity {

    void inserirConteudo(PECS pecs, Ranking ranking);
    
    void activeModeRecording(boolean active);
    
    void configComponents(boolean edicao, PECS pecs, Ranking ranking);
    
}
