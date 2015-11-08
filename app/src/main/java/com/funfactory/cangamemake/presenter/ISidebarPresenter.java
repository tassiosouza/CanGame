package com.funfactory.cangamemake.presenter;

import com.funfactory.cangamemake.view.IPerfilPacienteActivity;

public interface ISidebarPresenter {

	void setView(IPerfilPacienteActivity view);
	
	void onSidebarOptionClicked(int viewOptionId);
	
}
