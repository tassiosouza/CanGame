package com.funfactory.cangamemake.view;

import com.funfactory.cangamemake.model.entity.Config;
import com.funfactory.cangamemake.presenter.impl.IBaseActivityInterface;

public interface IExecutarPECSActivity extends IBaseActivityInterface {

	void preparar(Config config);

	void reproduzir();

	void pularPasso();

	void retornarPasso();

	void finalizarPECS();
}
