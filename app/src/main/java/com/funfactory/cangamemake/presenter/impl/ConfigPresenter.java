package com.funfactory.cangamemake.presenter.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.IConfigModel;
import com.funfactory.cangamemake.model.entity.Config;
import com.funfactory.cangamemake.model.impl.ConfigModel;
import com.funfactory.cangamemake.presenter.IConfigPresenter;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.view.IEdicaoActivity;

public class ConfigPresenter extends GenericPresenter implements IConfigPresenter {

	private IEdicaoActivity mView;
	protected Activity mContext;

	private IConfigModel mConfigModel = new ConfigModel();

	private Config mConfig;

	@Override
	public void setView(final IEdicaoActivity view) {
		this.mView = view;
		mContext = mView.getGenericMethods().getContext();
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = mView.getGenericMethods().getContext();
        super.onCreate(savedInstanceState);

        mConfig = mConfigModel.getConfiguration();
        mView.inserirConteudo(mConfig);
    }

	@Override
	public void salvarConfig(int tempoExibicaoTxt, int tempoReproducaoAudio, int tempoReproducaoVideo) {
        if (!isDadosValidos(tempoExibicaoTxt, tempoReproducaoAudio, tempoReproducaoVideo)) {
            return;
        }

        mConfig.setTempoExibicaoTxt(tempoExibicaoTxt);
        mConfig.setTempoReproducaoAudio(tempoReproducaoAudio);
        mConfig.setTempoReproducaoVideo(tempoReproducaoVideo);

        mConfigModel.saveOrUpdate(mConfig);

        mView.getGenericMethods().toastMessage(R.string.operacao_executada_com_sucesso);

        informarDadosDeRetorno();

        mView.getGenericMethods().finalizarActivity();
	}

    private void informarDadosDeRetorno() {
        Intent intent = new Intent();
        intent.putExtra(Constants.CONFIG, mConfig);
        mView.getGenericMethods().getContext().setResult(Activity.RESULT_OK, intent);
    }

	@Override
	public boolean isDadosValidos(int tempoExibicaoTxt,	int tempoReproducaoAudio, int tempoReproducaoVideo) {
        if (tempoExibicaoTxt <= 0) {
            mView.getGenericMethods().toastMessage(R.string.erro_nome_vazio);
            return false;
        }

        if (tempoReproducaoAudio <= 0) {
        	mView.getGenericMethods().toastMessage(R.string.erro_nome_vazio);
            return false;
        }

        if (tempoReproducaoVideo <= 0) {
        	mView.getGenericMethods().toastMessage(R.string.erro_nome_vazio);
            return false;
        }

        return true;
	}
}
