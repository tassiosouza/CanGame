package com.funfactory.cangamemake.presenter.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.model.entity.Rotina;
import com.funfactory.cangamemake.presenter.IExecutarRotinaPresenter;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.view.IExecutarRotinaActivity;
import com.funfactory.cangamemake.view.impl.ExecutarPECSActivity;

public class ExecutarRotinaPresenter extends GenericPresenter implements IExecutarRotinaPresenter {

	private IExecutarRotinaActivity mView;
	protected Activity mContext;

	private Rotina mRotina;
	private Paciente mPaciente;

	private int pecsIndex = 0;

	@Override
	public void setView(final IExecutarRotinaActivity view) {
		this.mView = view;
		mContext = mView.getGenericMethods().getContext();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContext = mView.getGenericMethods().getContext();
		super.onCreate(savedInstanceState);
		
		mView.reproduzir();
	}

	@Override
	public void reproduzir() {
		if (!mRotina.getListaPECS().isEmpty()) {
			PECS mPECS = mRotina.getListaPECS().get(pecsIndex);
	        Intent intent = new Intent(mContext, ExecutarPECSActivity.class);
	        intent.putExtra(Constants.PECS, mPECS);
	        intent.putExtra(Constants.PACIENTE, mPaciente);
	        intent.putExtra(Constants.ROTINA, mRotina);
	        intent.putExtra(Constants.INSTRUCAO, Constants.INSTRUCAO_REPRODUZIR);
	        mView.getGenericMethods().getContext().startActivityForResult(intent, Constants.ACAO_EXECUTAR);
		} else {
			mView.getGenericMethods().toastMessage("Não há PECS associadas a esta Rotina!");
			mView.getGenericMethods().finalizarActivity();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.ACAO_EXECUTAR) {
        	pecsIndex++;
        	if (mRotina.getListaPECS().size() > pecsIndex)
        	{
        		reproduzir();
        	} else {
        		mView.getGenericMethods().finalizarActivity();
        	}
        }
        else {
        	mView.getGenericMethods().finalizarActivity();
        }
	}

	public void setRotina(Rotina mRotina) {
		this.mRotina = mRotina;
	}

	public void setPaciente(Paciente mPaciente) {
		this.mPaciente = mPaciente;
	}
	
}
