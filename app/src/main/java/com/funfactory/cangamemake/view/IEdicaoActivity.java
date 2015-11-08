package com.funfactory.cangamemake.view;

import com.funfactory.cangamemake.presenter.impl.IBaseActivityInterface;

public interface IEdicaoActivity extends IBaseActivityInterface {

    void salvar();
    
    void saveScreen();
    
    void inserirConteudo(Object object);
    
    void configComponents(boolean edicao, Object object);
    
    void enableMenuContext(boolean enable);

}
