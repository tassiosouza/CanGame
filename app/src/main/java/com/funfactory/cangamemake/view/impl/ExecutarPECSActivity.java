package com.funfactory.cangamemake.view.impl;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.entity.Config;
import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.model.entity.Rotina;
import com.funfactory.cangamemake.pecs.Executor;
import com.funfactory.cangamemake.pecs.PECSChainOfResponsability;
import com.funfactory.cangamemake.pecs.Steps;
import com.funfactory.cangamemake.presenter.IExecutarPECSPresenter;
import com.funfactory.cangamemake.presenter.IGenericActivity;
import com.funfactory.cangamemake.presenter.impl.ExecutarPECSPresenter;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.view.IExecutarPECSActivity;

/**
 * Classe Activity de Execução das PECS
 * 
 * @author Silvino Neto
 */
public class ExecutarPECSActivity extends GenericActivity implements IExecutarPECSActivity {

    private ImageView              imgPECS;
    protected ImageButton          buttonRewind;
    protected ImageButton          buttonForward;
    protected ImageButton          buttonPlay;
    private TextView               txtLegenda;
    private TextView               cronometro;

    private IExecutarPECSPresenter mExecutarPECSPresenter = new ExecutarPECSPresenter();

    private int                    tempoExibicaoLegenda;
    private int                    tempoReproducaoAudio;
    private int                    tempoReproducaoVideo;

    private boolean                isRunning;

    private PECS                   pecs;
    private Paciente               paciente;
    private Rotina                 rotina; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setParentDependencies(this, mExecutarPECSPresenter);
        setContentView(R.layout.activity_executar_rotinas);

        mExecutarPECSPresenter.setView(this);

        imgPECS = (ImageView) findViewById(R.id.imagePECS);
        imgPECS.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        txtLegenda = (TextView) findViewById(R.id.legenda);
        buttonRewind = (ImageButton) findViewById(R.id.buttonRewind);
        buttonForward = (ImageButton) findViewById(R.id.buttonForward);
        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);
        cronometro = (TextView) findViewById(R.id.cronometro);

        pecs = (PECS) getIntent().getExtras().get(Constants.PECS);
        mExecutarPECSPresenter.setPecs(pecs);
        paciente = (Paciente) getIntent().getExtras().get(Constants.PACIENTE);
        mExecutarPECSPresenter.setPaciente(paciente);
        rotina = (Rotina) getIntent().getExtras().get(Constants.ROTINA);
        mExecutarPECSPresenter.setRotina(rotina);

        isRunning = false;
        PECSChainOfResponsability.setCurrent(null);

        configureActions();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        pararExecucao();
        super.onPause();
    }

    private void pararExecucao() {
        if (isRunning) {
            for (int i = 0; i < Steps.values().length; i++) {
                retornarPasso();
            }
            if (isRunning) {
                reproduzir();
            }
        }
    }

    private void configureActions() {
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
    }

    @Override
    public void preparar(Config config) {
        tempoExibicaoLegenda = config.getTempoExibicaoTxt();
        tempoReproducaoAudio = config.getTempoReproducaoAudio();
        tempoReproducaoVideo = config.getTempoReproducaoVideo();
    }

    public void onOptionSelected(View view) {
        mExecutarPECSPresenter.onOptionSeleted(view.getId());
    }

    @Override
    public void reproduzir() {
        isRunning = !isRunning;

        Executor executor = PECSChainOfResponsability.getCurrent();
        if (executor == null) {
            executor = PECSChainOfResponsability.createChain(cronometro, tempoExibicaoLegenda, tempoReproducaoAudio,
                    tempoReproducaoVideo, imgPECS, txtLegenda);
            PECSChainOfResponsability.setCurrent(executor);
        }

        if (isRunning) {
            buttonPlay.setImageResource(R.drawable.ic_white_pause);
            executor.run(this, pecs, false);
        } else {
            buttonPlay.setImageResource(R.drawable.ic_white_play);
            executor.cancelaOperacoes(true);
        }
    }

    @Override
    public void pularPasso() {
        Executor executor = PECSChainOfResponsability.getCurrent();
        if (executor != null) {
            executor.skipStep(this, pecs);
        }
    }

    @Override
    public void retornarPasso() {
        Executor executor = PECSChainOfResponsability.getCurrent();
        if (executor != null) {
            executor.previousStep(this, pecs);
        }
    }

    @Override
    public void finalizarPECS() {
        isRunning = false;
        buttonPlay.setImageResource(R.drawable.ic_white_play);
        RelativeLayout legendaPanel = (RelativeLayout) txtLegenda.getParent();
        legendaPanel.setVisibility(View.INVISIBLE);
        txtLegenda.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        pararExecucao();
        mExecutarPECSPresenter.onBackPressed();
        super.onBackPressed();
    }

    @Override
    public IGenericActivity getGenericMethods() {
        return this;
    }
    
    
}
