    package com.funfactory.cangamemake.presenter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.ICategoriaModel;
import com.funfactory.cangamemake.model.IPECSModel;
import com.funfactory.cangamemake.model.IRankingModel;
import com.funfactory.cangamemake.model.IRotinaModel;
import com.funfactory.cangamemake.model.entity.Categoria;
import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.model.entity.Rotina;
import com.funfactory.cangamemake.model.impl.CategoriaModel;
import com.funfactory.cangamemake.model.impl.PECSModel;
import com.funfactory.cangamemake.model.impl.RankingModel;
import com.funfactory.cangamemake.model.impl.RotinaModel;
import com.funfactory.cangamemake.presenter.IListarPECSPresenter;
import com.funfactory.cangamemake.presenter.IRotinaPresenter;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.view.IRotinaActivity;
import com.funfactory.cangamemake.view.adapter.DragDropAdapter;
import com.funfactory.cangamemake.view.adapter.DragDropAdapter.DeleteDragDropListClick;
import com.funfactory.cangamemake.view.impl.CategoriaActivity;
import com.funfactory.cangamemake.view.impl.ExecutarRotinasActivity;
import com.funfactory.cangamemake.view.impl.ListarPECSActivity;
import com.terlici.dragndroplist.DragNDropListView;
import com.terlici.dragndroplist.DragNDropListView.OnItemDragNDropListener;

public class RotinaPresenter extends GenericPresenter implements IRotinaPresenter {

    public static final String INSTRUCAO_SELECIONAR = "Selecionar";

    private IRankingModel      mRankingModel        = new RankingModel();

    /**
     * Instancias da View
     */
    private IRotinaActivity    mView;
    private Activity           mContext;

    /**
     * Models utilizados do presenter
     */
    private IRotinaModel       mRotinaModel         = new RotinaModel();
    private IPECSModel         mPECSModel           = new PECSModel();
    private ICategoriaModel    mCategoriaModel      = new CategoriaModel();

    /**
     * Instrucao
     */
    private String             mInstrucao           = Constants.INSTRUCAO_CRIAR;

    /**
     * Dependencias do presenter
     */
    protected int              mEmptyViewMessage    = R.string.sem_pecs_cadastradas;

    private Rotina             mRotina;
    private List<PECS>         mPECS                = new ArrayList<PECS>();
    private DragDropAdapter    mAdapter;
    private Paciente           mPaciente;

    @Override
    public void setView(IRotinaActivity view) {
        mView = view;
        mContext = mView.getGenericMethods().getContext();
    }

    private OnItemDragNDropListener listener       = new OnItemDragNDropListener() {

                                                       @Override
                                                       public void onItemDrop(DragNDropListView parent, View view,
                                                               int startPosition, int endPosition, long id) {

                                                           PECS pecsStart = mPECS.get(startPosition);
                                                           PECS pecsEnd = mPECS.get(endPosition);

                                                           mPECS.set(endPosition, pecsStart);
                                                           mPECS.set(startPosition, pecsEnd);

                                                           if (!mInstrucao.equalsIgnoreCase(Constants.INSTRUCAO_CRIAR)) {
                                                               salvar();
                                                           }

                                                       }

                                                       @Override
                                                       public void onItemDrag(DragNDropListView parent, View view,
                                                               int position, long id) {
                                                       }
                                                   };

    private DeleteDragDropListClick deleteListener = new DeleteDragDropListClick() {

                                                       @Override
                                                       public void delete(Long id) {
                                                           int pos = getPosition(id);
                                                           if (pos != -1) {
                                                               mPECS.remove(pos);
                                                               adpeterCreated = false;
                                                               setListContent();
                                                               salvar();
                                                           }

                                                       }
                                                   };

    private int getPosition(Long id) {
        for (int i = 0; i < mPECS.size(); i++) {
            if (mPECS.get(i).getIdEntity().equals(id)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = null;
        Intent intent = mView.getGenericMethods().getContext().getIntent();

        if (intent != null) {
            extras = intent.getExtras();
        }

        if (extras != null && extras.containsKey(Constants.ROTINA)) {
            mRotina = (Rotina) extras.get(Constants.ROTINA);
          
            mPECS = mPECSModel.getPECSByRotina(mRotina.getIdEntity());
            setListContent();
            mPaciente = (Paciente) extras.get(Constants.PACIENTE);

            if (extras.get(Constants.INSTRUCAO) != null) {
                mInstrucao = (String) extras.get(Constants.INSTRUCAO);
            }
            
            updateView();

        } else {
            mRotina = new Rotina();
            mRotina.setCategoria(mCategoriaModel.getCategoriaGeral());
            updateView();
        }

        setarParametrosInstrucao();
        atualizarRankingPaciente();
    }

    private void setarParametrosInstrucao() {
        if (mInstrucao.equalsIgnoreCase(Constants.INSTRUCAO_CRIAR)) {
            mEmptyViewMessage = R.string.sem_pecs_relacionadas;
        }
    }

    @Override
    public void onResume() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        
        if(mPECSModel != null) {
	        mPECS = mPECSModel.getPECSByRotina(mRotina.getIdEntity());
	        setListContent();
        }
        
        super.onResume();
    }

    @Override
    public void updateView() {
        boolean isEdicao = mPaciente == null;
        mView.configComponents(isEdicao);
        mView.setRotina(mRotina);
    }

    @Override
    public void onPause() {
        if (!mInstrucao.equalsIgnoreCase(Constants.INSTRUCAO_CRIAR)) {
            salvar();
        }  
        super.onPause();
    }
    boolean adpeterCreated = false;
    public void setListContent() {

        if(!adpeterCreated)
        {
            createAdapter();
            adpeterCreated = true;
            mView.setAdapterDrapDrop(mAdapter, listener);
            mAdapter.setDeleleListener(deleteListener);
            mAdapter.notifyDataSetChanged();
        }

    }

    private void createAdapter() {
        ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < mPECS.size(); ++i) {

            HashMap<String, Object> item = new HashMap<String, Object>();
            PECS p = mPECS.get(i);

            item.put(DragDropAdapter.NAME, p.getLegenda());
            item.put(DragDropAdapter.ID, p.getIdEntity());
            item.put(DragDropAdapter.OBJECT, p);
            items.add(item);
        }

        mAdapter = new DragDropAdapter(mContext, items, R.layout.pecs_rotina_list_item,
                new String[] { DragDropAdapter.NAME }, new int[] { R.id.legenda }, R.id.move);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = mContext.getMenuInflater();

        if (mInstrucao.equalsIgnoreCase(Constants.INSTRUCAO_CRIAR)) {
            inflater.inflate(R.menu.incluir_pecs, menu);
        } else {
            inflater.inflate(R.menu.executar, menu);
        }

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        if (item.getItemId() == R.id.action_save) {
            salvarEFechar();
        } else if (item.getItemId() == R.id.action_executar) {
            executarRotina();
        }

        return super.onMenuItemSelected(featureId, item);
    }

    private void selecionarPECSparaRotina() {
        Intent intent = new Intent(mContext, ListarPECSActivity.class);
        intent.putExtra(Constants.INSTRUCAO, IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_ROTINA);
        mRotina.setListaPECS(mPECS);
        intent.putExtra(Constants.ROTINA, mRotina);
        mView.getGenericMethods().getContext().startActivityForResult(intent, Constants.ACAO_SELECAO);
    }

    private void executarRotina() {
        if (mRotina.getListaPECS().isEmpty()) {
            mRotina.setListaPECS(mPECS);
        }

        Intent intent = new Intent(mContext, ExecutarRotinasActivity.class);
        intent.putExtra(Constants.INSTRUCAO, Constants.INSTRUCAO_REPRODUZIR);
        intent.putExtra(Constants.ROTINA, mRotina);
        intent.putExtra(Constants.PACIENTE, mPaciente);

        mView.getGenericMethods().getContext().startActivityForResult(intent, Constants.ACAO_EXECUTAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.ACAO_SELECAO && resultCode == Activity.RESULT_OK) {
            Rotina rotina = (Rotina) data.getExtras().get(Constants.ROTINA);
            adpeterCreated = false;
            adicionarRetornaARotina(rotina.getListaPECS());
        } else if (requestCode == Constants.ACAO_SELECAO_CATEGORIA && resultCode == Activity.RESULT_OK) {
            Categoria categoria = (Categoria) data.getExtras().get(Constants.CATEGORIA);
            mRotina.setCategoria(categoria);
            updateView();
        } else if (requestCode == Constants.ACAO_EXECUTAR) {
            if (mPECS != null && mPaciente != null) {
                atualizarRankingPaciente();
            }
            updateView();
        }
    }

    private void adicionarRetornaARotina(List<PECS> pecs) {
        mPECS.addAll(pecs);
        mRotina.setListaPECS(mPECS);
        setListContent();
        salvar();
    }

    @Override
    public boolean salvar() {

        if (!isDadosValidos()) {
            return false;
        }
        mRotina.setListaPECS(mPECS);
        mRotinaModel.saveOrUpdate(mRotina);
        return true;
    }

    private void salvarEFechar() {
        if (salvar()){
        mView.getGenericMethods().toastMessage(R.string.rotina_foi_salva);
        mView.getGenericMethods().finalizarActivity();
        }
    }

    @Override
    public boolean isDadosValidos() {
        if (mRotina.getNome().trim().isEmpty()) {
            mView.getGenericMethods().toastMessage(R.string.erro_nome_vazio);
            return false;
        }
        return true;
    }

    @Override
    public void onOptionSeleted(int viewOptionId) {
        switch (viewOptionId) {
        case R.id.button_categoria_rotina:
            selecionarCategoria();
            break;
        case R.id.button_selecionar_pecs:
            selecionarPECSparaRotina();
            break;

        default:
            break;
        }
    }

    private void selecionarCategoria() {
        Intent intentCategoriaActivity = new Intent(mContext, CategoriaActivity.class);
        intentCategoriaActivity.putExtra(Constants.INSTRUCAO, INSTRUCAO_SELECIONAR);
        mView.getGenericMethods().getContext()
                .startActivityForResult(intentCategoriaActivity, Constants.ACAO_SELECAO_CATEGORIA);
    }

    public Rotina getRotina() {
        return mRotina;
    }

    public void setRotina(Rotina mRotina) {
        this.mRotina = mRotina;
    }

    protected void atualizarRankingPaciente() {
        if (mPaciente != null) {
            double pontuacao = getPontuacao();
            mView.setPontuacao(pontuacao);
        }
    }

    private double getPontuacao() {
        double pontuacao = mRankingModel.getPontuacaoRotina(mRotina.getIdEntity(), mPaciente.getIdEntity());
        return pontuacao;
    }
}