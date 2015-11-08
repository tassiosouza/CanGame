package com.funfactory.cangamemake.view.impl;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.entity.Rotina;
import com.funfactory.cangamemake.presenter.IGenericActivity;
import com.funfactory.cangamemake.presenter.IRotinaPresenter;
import com.funfactory.cangamemake.presenter.impl.RotinaPresenter;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.view.IRotinaActivity;
import com.funfactory.cangamemake.view.adapter.DragDropAdapter;
import com.funfactory.cangamemake.view.adapter.ListSelectorHandler;
import com.terlici.dragndroplist.DragNDropListView;
import com.terlici.dragndroplist.DragNDropListView.OnItemDragNDropListener;

public class RotinaActivity extends GenericActivity implements IRotinaActivity {

    /**
     * Componentes da View
     */
    private EditText            mEditTextNome;
    private DragNDropListView   mListview;
    private Button              mButtonCategoria;

    private ListSelectorHandler mSelectorHandler;
    private TextView            tituloRating;
    private RatingBar           mRatingBar;

    /**
     * Presenters
     */
    private IRotinaPresenter    mPresenter = new RotinaPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_rotina);

        setParentDependencies(this, mPresenter);
        mPresenter.setView(this);

        mListview = (DragNDropListView) findViewById(android.R.id.list);
        mEditTextNome = (EditText) findViewById(R.id.textViewNome);
        mButtonCategoria = (Button) findViewById(R.id.button_categoria_rotina);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBarExecRotina);
        tituloRating = (TextView) findViewById(R.id.TextView02);

        if (savedInstanceState != null)
            setFields(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        configureActions();
    }

    private void setFields(Bundle savedInstanceState) {

        Object nomeRotina = savedInstanceState.get(Constants.BUNDLE_DS_ROTINA);
        if (nomeRotina != null)
            mEditTextNome.setText(nomeRotina.toString());

        Object categoria = savedInstanceState.get(Constants.BUNDLE_DS_CATEGORIA);
        if (categoria != null)
            mButtonCategoria.setText(categoria.toString());

    }

    public IGenericActivity getGenericMethods() {
        return this;
    }

    private void configureActions() {
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setAdapter(ArrayAdapter adapter) {
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }

    @Override
    public void setEmptyView(int stringID) {

        setContentView(R.layout.no_content_activity_rotina);
        mEditTextNome = (EditText) findViewById(R.id.textViewNome);

        mButtonCategoria = (Button) findViewById(R.id.button_categoria_rotina);

        TextView textView = (TextView) findViewById(R.id.title);
        textView.setText(stringID);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        if (item.getItemId() == R.id.action_save) {
            salvar();
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onPause() {
        salvarNome();
        super.onPause();
    }

    private void salvar() {
        String nome = mEditTextNome.getEditableText().toString();

        Rotina rotina = mPresenter.getRotina();
        rotina.setNome(nome);

        mPresenter.setRotina(rotina);

        mPresenter.salvar();
    }

    private void salvarNome() {
        String nome = mEditTextNome.getEditableText().toString();
        Rotina rotina = mPresenter.getRotina();
        rotina.setNome(nome);
        mPresenter.setRotina(rotina);
    }

    @Override
    public void setChoicesHandle(ListSelectorHandler handle) {
        mSelectorHandler = handle;
        mSelectorHandler.setListView(mListview, ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void setRotina(Rotina rotina) {
        if (rotina != null) {

            mEditTextNome.setText(rotina.getNome());

            if (rotina.getCategoria() != null)
                mButtonCategoria.setText(rotina.getCategoria().getDescricao());
        }
    }

    public void onOptionSelected(View view) {
        mPresenter.onOptionSeleted(view.getId());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        String nome = mEditTextNome.getEditableText().toString();

        Rotina rotina = mPresenter.getRotina();
        rotina.setNome(nome);

        mPresenter.setRotina(rotina);
    }

    @Override
    public void setSelectedItem(int position) {
        if (mListview != null) {
            mListview.setSelection(position);
            mSelectorHandler.setListView(mListview);
        }
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void setAdapterDrapDrop(DragDropAdapter adapter, OnItemDragNDropListener listener) {
        mListview.setDragNDropAdapter(adapter);
        mListview.setOnItemDragNDropListener(listener);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setPontuacao(Double pontuacao) {
        if (mRatingBar != null && pontuacao != null) {
            mRatingBar.setRating(new Float(pontuacao));
        }
    }

    @Override
    public void configComponents(boolean edicao) {

        setComponentesOff();

        if (edicao) {
            setComponentesEdicao();
        } else {
            setComponentesVisualizacao();
        }
    }

    public void setComponentesOff() {
        mRatingBar.setVisibility(View.GONE);
        tituloRating.setVisibility(View.GONE);
    }

    private void setComponentesEdicao() {
        mRatingBar.setVisibility(View.GONE);
        tituloRating.setVisibility(View.GONE);
    }

    private void setComponentesVisualizacao() {
        mRatingBar.setVisibility(View.VISIBLE);
        tituloRating.setVisibility(View.VISIBLE);
    }

}
