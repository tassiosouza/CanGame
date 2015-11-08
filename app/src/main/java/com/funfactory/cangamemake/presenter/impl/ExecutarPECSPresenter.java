package com.funfactory.cangamemake.presenter.impl;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.IConfigModel;
import com.funfactory.cangamemake.model.IRankingModel;
import com.funfactory.cangamemake.model.entity.Config;
import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.model.entity.Ranking;
import com.funfactory.cangamemake.model.entity.Rotina;
import com.funfactory.cangamemake.model.impl.ConfigModel;
import com.funfactory.cangamemake.model.impl.RankingModel;
import com.funfactory.cangamemake.pecs.Executor;
import com.funfactory.cangamemake.pecs.PECSChainOfResponsability;
import com.funfactory.cangamemake.pecs.Steps;
import com.funfactory.cangamemake.presenter.IExecutarPECSPresenter;
import com.funfactory.cangamemake.view.IExecutarPECSActivity;

public class ExecutarPECSPresenter extends GenericPresenter implements IExecutarPECSPresenter {

    private IExecutarPECSActivity mView;
    protected Activity            mContext;

    private IConfigModel          mConfigModel  = new ConfigModel();
    private IRankingModel         mRankingModel = new RankingModel();

    private Config                mConfig;
    private PECS                  mPecs;
    private Rotina                mRotina;
    private Paciente              mPaciente;

    @Override
    public void setView(final IExecutarPECSActivity view) {
        this.mView = view;
        mContext = mView.getGenericMethods().getContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = mView.getGenericMethods().getContext();
        super.onCreate(savedInstanceState);

        mConfig = mConfigModel.getConfiguration();
        mView.preparar(mConfig);
    }

    @Override
    public void onOptionSeleted(int viewOptionId) {
        switch (viewOptionId) {
        case R.id.buttonRewind:
            mView.retornarPasso();
            break;
        case R.id.buttonPlay:
            mView.reproduzir();
            break;
        case R.id.buttonForward:
            mView.pularPasso();
            break;
        case R.id.buttonOK:
            atualizarRankingPaciente();
            onBackPressed();
            break;
        default:
            break;
        }
    }

    public void onBackPressed() {
        Executor executor = PECSChainOfResponsability.getCurrent();
        if (executor != null) {
            executor.cancelaOperacoes(true);
            if (executor.getPrevious() != null) {
                executor.getPrevious().stopStep();
            }
        }

        mView.finalizarPECS();
        mView.getGenericMethods().finalizarActivity();
    }

    private void atualizarRankingPaciente() {
        if (mPaciente != null) {
            int pontuacao = getPontuacao();

            Ranking ranking = null;
            List<Ranking> listRankingPecPaciente = getRaking();
            if (listRankingPecPaciente != null && listRankingPecPaciente.size() != 0) {
                ranking = listRankingPecPaciente.get(0);
            }

            if (ranking == null) {
                ranking = new Ranking();
                ranking.setDataHoraCriacao(new Date());
                ranking.setIdPaciente(mPaciente.getIdEntity());
                ranking.setIdPECS(mPecs.getIdEntity());
                
                if (mRotina != null) {
                    ranking.setIdRotina(mRotina.getIdEntity());
                }
            }
            ranking.setPontuacao(pontuacao);

            mRankingModel.saveOrUpdate(ranking);
        }
    }

    private List<Ranking> getRaking() {
        if (mRotina == null) {
            return mRankingModel.getAllByPecPaciente(mPecs.getIdEntity(), mPaciente.getIdEntity());
        } else {
            return mRankingModel.getAllByRotinaPecPaciente(mRotina.getIdEntity(), mPecs.getIdEntity(), mPaciente.getIdEntity());
        }
    }

    private int getPontuacao() {

        Executor currentStep = PECSChainOfResponsability.getCurrent();

        if (currentStep == null)
            return 1;

        Steps step = currentStep.getStep();

        int pontuacao = 0;
        switch (step) {
        case TEXT:
            pontuacao = 3;
            break;
        case AUDIO:
            pontuacao = 2;
            break;
        case VIDEO:
            pontuacao = 1;
            break;
        default: // step == null -> ok na foto
            pontuacao = 4;
            break;
        }

        return pontuacao;
    }

    public void setPecs(PECS mPecs) {
        this.mPecs = mPecs;
    }

    public void setPaciente(Paciente mPaciente) {
        this.mPaciente = mPaciente;
    }

    public void setRotina(Rotina mRotina) {
        this.mRotina = mRotina;
    }
}
