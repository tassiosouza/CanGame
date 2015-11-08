package com.funfactory.cangamemake.view.impl;

import roboguice.inject.ContentView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.entity.Config;
import com.funfactory.cangamemake.presenter.IConfigPresenter;
import com.funfactory.cangamemake.presenter.IGenericActivity;
import com.funfactory.cangamemake.presenter.impl.ConfigPresenter;
import com.funfactory.cangamemake.view.IEdicaoActivity;

@ContentView(R.layout.activity_config)
public class ConfigActivity extends GenericActivity implements IEdicaoActivity {

	private static final String SUFIXO_SEGUNDOS = " seg";

	private IConfigPresenter mConfigPresenter = new ConfigPresenter();

	private SeekBar tempoTxt;
	
	private SeekBar tempoAudio;

	private SeekBar tempoVideo;

	private TextView txtViewIndicadorTempoAudio;

	private TextView txtViewIndicadorTempoTexto;

	private TextView txtViewIndicadorTempoVideo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setParentDependencies(this, mConfigPresenter);
        setContentView(R.layout.activity_config);

        /*
         * Set the view
         */
        mConfigPresenter.setView(this);

        OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (seekBar == tempoTxt) {
					txtViewIndicadorTempoTexto.setText(progress + SUFIXO_SEGUNDOS);
				}
				else if (seekBar == tempoAudio) {
					txtViewIndicadorTempoAudio.setText(progress + SUFIXO_SEGUNDOS);
				}
				else if (seekBar == tempoVideo) {
					txtViewIndicadorTempoVideo.setText(progress + SUFIXO_SEGUNDOS);
				}
			}
		};

        tempoTxt = (SeekBar) findViewById(R.id.tempoTxt);
        tempoTxt.setOnSeekBarChangeListener(onSeekBarChangeListener);
        tempoAudio = (SeekBar) findViewById(R.id.tempoAudio);
        tempoAudio.setOnSeekBarChangeListener(onSeekBarChangeListener);
        tempoVideo = (SeekBar) findViewById(R.id.tempoVideo);
        tempoVideo.setOnSeekBarChangeListener(onSeekBarChangeListener);
        txtViewIndicadorTempoAudio = (TextView) findViewById(R.id.txtViewIndicadorTempoAudio);
        txtViewIndicadorTempoTexto = (TextView) findViewById(R.id.txtViewIndicadorTempoTexto);
        txtViewIndicadorTempoVideo = (TextView) findViewById(R.id.txtViewIndicadorTempoVideo);

        configureActions();

        super.onCreate(savedInstanceState);
	}

    private void configureActions() {
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
    }
	
	@Override
	public IGenericActivity getGenericMethods() {
		return this;
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.salvar, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            salvar();
        }
        return super.onMenuItemSelected(featureId, item);
    }

	@Override
	public void salvar() {
        int strTempoTxt = tempoTxt.getProgress();
        int strTempoAudio = tempoAudio.getProgress();
        int strTempoVideo = tempoVideo.getProgress();

        mConfigPresenter.salvarConfig(strTempoTxt, strTempoAudio, strTempoVideo);
	}

	@Override
	public void inserirConteudo(Object object) {
        if (object != null) {
            Config config = (Config) object;

			tempoTxt.setProgress(config.getTempoExibicaoTxt());
			tempoAudio.setProgress(config.getTempoReproducaoAudio());
			tempoVideo.setProgress(config.getTempoReproducaoVideo());

			txtViewIndicadorTempoTexto.setText(config.getTempoExibicaoTxt() + SUFIXO_SEGUNDOS);
			txtViewIndicadorTempoAudio.setText(config.getTempoReproducaoAudio() + SUFIXO_SEGUNDOS);
			txtViewIndicadorTempoVideo.setText(config.getTempoReproducaoVideo() + SUFIXO_SEGUNDOS);
        }
	}

	@Override
	public void enableMenuContext(boolean enable) {
	}

    @Override
    public void configComponents(boolean edicao, Object object) {
    }

    @Override
    public void saveScreen() {
    }
}
