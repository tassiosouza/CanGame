package com.funfactory.cangamemake.view.impl;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.model.entity.Ranking;
import com.funfactory.cangamemake.presenter.IGenericActivity;
import com.funfactory.cangamemake.presenter.IPECSPresenter;
import com.funfactory.cangamemake.presenter.impl.PECSPresenter;
import com.funfactory.cangamemake.util.PhotoUtil;
import com.funfactory.cangamemake.view.IPECSActivity;

public class PECSActivity extends GenericActivity implements IPECSActivity {

    /**
     * Componentes da View
     */
    private ImageView      photoImage;
    private ImageButton    buttonPhotoImage;

    private ImageButton    buttonVideoPlay;
    private ImageButton    buttonVideoRec;
    private ImageButton    buttonVideoDelete;

    private ImageButton    buttonAudioPlay;
    private ImageButton    buttonAudioRec;
    private ImageButton    buttonAudioDelete;

    private EditText       textoPecs;
    private Button         buttonCategoria;

    private RatingBar      ratingBar;

    /**
     * Presenters
     */
    private IPECSPresenter mPECSPresenter = new PECSPresenter();

    /**
     * Components de UI
     */
    private PopupMenu      popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setParentDependencies(this, mPECSPresenter);
        setContentView(R.layout.activity_edicao_pecs);

        /*
         * Set the view
         */
        mPECSPresenter.setView(this);

        photoImage = (ImageView) findViewById(R.id.photo_image);
        photoImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        textoPecs = (EditText) findViewById(R.id.textoPecs);
        buttonCategoria = (Button) findViewById(R.id.button_categoria_pecs);

        buttonVideoPlay = (ImageButton) findViewById(R.id.buttonPlayVideo);
        buttonVideoRec = (ImageButton) findViewById(R.id.buttonRecVideo);
        buttonVideoDelete = (ImageButton) findViewById(R.id.buttonRemoveVideo);

        buttonAudioPlay = (ImageButton) findViewById(R.id.buttonPlayAudio);
        buttonAudioRec = (ImageButton) findViewById(R.id.buttonRecAudio);
        buttonAudioDelete = (ImageButton) findViewById(R.id.buttonRemoveAudio);

        buttonPhotoImage = (ImageButton) findViewById(R.id.btnCallPhoto);

        ratingBar = (RatingBar) findViewById(R.id.pontuacao);

        configureActions();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
    }

    private void configureActions() {
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
    }

    @Override
    public void enableMenuContext(boolean enable) {

        popupMenu = new PopupMenu(PECSActivity.this, photoImage);
        popupMenu.getMenuInflater().inflate(R.menu.foto, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mPECSPresenter.onOptionSeleted(item.getItemId());
                return true;
            }
        });

        popupMenu = new PopupMenu(PECSActivity.this, buttonPhotoImage);
        popupMenu.getMenuInflater().inflate(R.menu.foto, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mPECSPresenter.onOptionSeleted(item.getItemId());
                return true;
            }
        });

        if (enable) {
            photoImage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });

            buttonPhotoImage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });

        } else {
            photoImage.setOnClickListener(null);
            buttonPhotoImage.setOnClickListener(null);
        }
    }

    public void onOptionSelected(View view) {
        saveScreen();
        mPECSPresenter.onOptionSeleted(view.getId());
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        mPECSPresenter.onOptionSeleted(item.getItemId());
        return super.onMenuItemSelected(featureId, item);
    }

    public void salvar() {
        String legenda = textoPecs.getText().toString();
        mPECSPresenter.salvarPECS(legenda);
    }

    @Override
    public IGenericActivity getGenericMethods() {
        return this;
    }

    @Override
    public void configComponents(boolean edicao, PECS pecs, Ranking ranking) {

        setComponentesOff();

        if (edicao) {
            setComponentesEdicao(pecs, ranking);
        } else {
            setComponentesVisualizacao(pecs, ranking);
        }
    }

    public void setComponentesOff() {

        buttonVideoDelete.setVisibility(View.GONE);
        buttonAudioDelete.setVisibility(View.GONE);

        buttonVideoRec.setVisibility(View.GONE);
        buttonAudioRec.setVisibility(View.GONE);

        buttonAudioPlay.setVisibility(View.GONE);
        buttonVideoPlay.setVisibility(View.GONE);

        textoPecs.setInputType(0);
        buttonCategoria.setClickable(false);
    }

    private void setComponentesEdicao(PECS pecs, Ranking ranking) {

        textoPecs.setInputType(InputType.TYPE_CLASS_TEXT);
        buttonCategoria.setClickable(true);

        if (ranking == null) {
            ratingBar.setVisibility(View.GONE);
        }

        if (pecs.getPathSound() != null && !pecs.getPathSound().isEmpty()) {
            buttonAudioPlay.setVisibility(View.VISIBLE);
            buttonAudioDelete.setVisibility(View.VISIBLE);
        } else {
            buttonAudioRec.setVisibility(View.VISIBLE);
        }

        if (pecs.getPathVideo() != null && !pecs.getPathVideo().isEmpty()) {
            buttonVideoPlay.setVisibility(View.VISIBLE);
            buttonVideoDelete.setVisibility(View.VISIBLE);
        } else {
            buttonVideoRec.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void saveScreen() {
        String legenda = textoPecs.getText().toString();
        mPECSPresenter.salvarTela(legenda);
    }

    private void setComponentesVisualizacao(PECS pecs, Ranking ranking) {

        if (ranking == null) {
            ratingBar.setVisibility(View.GONE);
        }

        if (pecs.getPathSound() != null && !pecs.getPathSound().isEmpty()) {
            buttonAudioPlay.setVisibility(View.VISIBLE);
        }

        if (pecs.getPathVideo() != null && !pecs.getPathVideo().isEmpty()) {
            buttonVideoPlay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPECSPresenter.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void inserirConteudo(PECS pecs, Ranking ranking) {

        if (pecs.getImagePath() != null && !pecs.getImagePath().equals("")) {

            Bitmap btmp = PhotoUtil.getInstance().createBitmap(pecs.getImagePath());
            if (btmp == null) {
                btmp = PhotoUtil.getInstance().decodeBitmap(pecs.getImagePath());
            }
            if (btmp != null) {
                photoImage.setImageBitmap(btmp);
            }
        }

        buttonCategoria.setText(pecs.getCategoria().getDescricao());
        textoPecs.setText(pecs.getLegenda());

        if (ranking != null) {
            ratingBar.setRating(ranking.getPontuacao());
        }
    }

    @Override
    public void configComponents(boolean edicao, Object object) {
        // No implementation
    }

    @Override
    public void inserirConteudo(Object object) {
        // No implementation
    }

    @Override
    public void activeModeRecording(final boolean active) {
        if (buttonAudioRec != null) {

            if (active) {
                buttonAudioRec.setImageResource(R.drawable.btn_round_stop);
            } else {
                buttonAudioRec.setImageResource(R.drawable.btn_round_voice);
            }
        }
    }

}
