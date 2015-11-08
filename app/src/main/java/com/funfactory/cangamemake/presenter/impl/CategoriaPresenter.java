package com.funfactory.cangamemake.presenter.impl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.ICategoriaModel;
import com.funfactory.cangamemake.model.entity.Categoria;
import com.funfactory.cangamemake.model.impl.CategoriaModel;
import com.funfactory.cangamemake.presenter.ICategoriaPresenter;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.util.DialogUtil.DialogCallback;
import com.funfactory.cangamemake.view.ICategoriaActivity;
import com.funfactory.cangamemake.view.ICategoriaCallcack;
import com.funfactory.cangamemake.view.adapter.ListSelectorHandler;
import com.funfactory.cangamemake.view.adapter.ListarCategoriaAdapter;
import com.funfactory.cangamemake.view.listener.IChoicesHandle;

public class CategoriaPresenter extends GenericPresenter implements ICategoriaPresenter {

	public static final String INSTRUCAO_SELECIONAR = "Selecionar";
	public static final String INSTRUCAO_EDITAR = "Editar";
	
	
    /**
     * Instancias da View
     */
    private Activity              mContext;
    private ICategoriaActivity    mView;

    /**
     * Models utilizados do presenter
     */
    private ICategoriaModel       mCategoriaModel   = new CategoriaModel();

    /**
     * Instruções para listagem de Pacientes
     */
    private String                mInstrucao              = null;

    /**
     * Dependencias das instruções
     */
    private int                   mEmptyViewMessage       = R.string.sem_categorias_cadastradas;

    /**
     * Controlador da lista
     */
    private List<Categoria>        mCategorias              = new ArrayList<Categoria>();
    private ListarCategoriaAdapter mAdapter;

    /**
     * Listeners do presenter
     */
    private ListSelectorHandler   mChoicesHandle          = new ListSelectorHandler(new IChoicesHandle() {

          @Override
          public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
              switch (item.getItemId()) {
              case R.id.action_delete:
                  confirmarEExecutarRemocao(mChoicesHandle.getMultipleChoiceList());
                  break;

              default:
                  break;
              }
              return false;
          }

      

        @Override
          public boolean onCreateActionMode(ActionMode mode, Menu menu) {
              MenuInflater inflater = mContext.getMenuInflater();
              inflater.inflate(R.menu.excluir, menu);
              return true;
          }

          @Override
          public void onItemSelected(int position) {
        	  if(mInstrucao == null) {
        		  mInstrucao = INSTRUCAO_EDITAR;
        	  }
              tratarSelecao(position);
          }



        private void tratarSelecao(int position) {
            if (!mInstrucao.equalsIgnoreCase(INSTRUCAO_SELECIONAR)) {
                Categoria categoria = mCategorias.get(position);
                editarCategoria(categoria);
            } else {
                Intent intent = new Intent();
                intent.putExtra(Constants.CATEGORIA, mCategorias.get(position));
                mView.getGenericMethods().getContext().setResult(Activity.RESULT_OK, intent);
                mView.getGenericMethods().finalizarActivity();
            }
        }
      });
    
    @Override
    public void setView(ICategoriaActivity view) {
        this.mView = view;
    }

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = mView.getGenericMethods().getContext();
        super.onCreate(savedInstanceState);
        filtrarInstrucoes();
    }
    
    private void filtrarInstrucoes() {

        Intent intent = mView.getGenericMethods().getContext().getIntent();
        Bundle bundle = null;
        
        if (intent != null) {
            bundle = intent.getExtras();
        }

        if (bundle != null && bundle.containsKey(Constants.INSTRUCAO)) {
            mInstrucao = bundle.getString(Constants.INSTRUCAO);
        }
    }


    @Override
    public void onResume() {
        updateView();
        super.onResume();
    }
    
    private void updateView() {
        
        mCategorias.clear();
        mCategorias.addAll(mCategoriaModel.listAll());
        
        if (mCategorias.isEmpty()) {
            mView.setEmptyView(mEmptyViewMessage);
        } else {
            setListContent();
        }
    }

    public void setListContent() {

        if (mAdapter == null) {
            mAdapter = new ListarCategoriaAdapter(mContext, R.layout.categoria_list_item, mCategorias);
        }
        mView.setChoicesHandle(mChoicesHandle);
        mView.setAdapter(mAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_new) {
           criarCategoria();
        }
        return false;
    }
    
    private void editarCategoria(final Categoria categoria) {
        
        if (categoria.getIdEntity() == CategoriaModel.CATEGORIA_GERAL_ID) {
            return;
        }
        
        mView.requererInputDeCategoria(categoria.getDescricao(), new ICategoriaCallcack() {
            
            @Override
            public void descricao(AlertDialog alert, String descricao) {
                
                if(!isDadosValidos(descricao) && !descricao.equalsIgnoreCase(categoria.getDescricao())){
                    
                    mView.getGenericMethods().toastMessage(R.string.inserir_categorias_duplicadas);
                    
                } else  if(descricao.trim().isEmpty()){
                    mView.getGenericMethods().toastMessage(R.string.erro_nome_vazio);  
                } else {
                    atualizarCategoria(categoria, descricao);
                    alert.dismiss();
                }
            }
        });
    }
    
    private void criarCategoria() {
        mView.requererInputDeCategoria(null, new ICategoriaCallcack() {
            
            @Override
            public void descricao(AlertDialog alert, String descricao) {
                
                if(!isDadosValidos(descricao)){
                    
                    mView.getGenericMethods().toastMessage(R.string.inserir_categorias_duplicadas);
                    
                } else  if(descricao.trim().isEmpty()){
                    
                    mView.getGenericMethods().toastMessage(R.string.erro_nome_vazio);
                    
                } else {
                    criarCategoria(descricao);
                    alert.dismiss();
                }
            }
        });
    }
    
    @Override
    public boolean isDadosValidos(String nome){
    	
        for (Categoria categoria : mCategorias){
            if (categoria.getDescricao().equalsIgnoreCase(nome)){
                return false;
            }
        }
        return true;
    }
    
    private void criarCategoria(String nome){
        Categoria categoria = new Categoria();
        categoria.setDescricao(nome);
        mCategoriaModel.saveOrUpdate(categoria);
        updateView();
    }
    
    private void atualizarCategoria(Categoria categoria, String novaDescricao){
        categoria.setDescricao(novaDescricao);
        mCategoriaModel.saveOrUpdate(categoria);
        updateView();
    }
    
    private void confirmarEExecutarRemocao(final List<Integer> positions) {
        
        int mensagem = positions.size() > 1 ? R.string.excluir_categorias_mensagem
                : R.string.excluir_uma_categoria_mensagem;
        
        mView.getGenericMethods().confirmarAcao(R.string.excluir_categorias, mensagem,
                new DialogCallback() {

                    @Override
                    public void onResult(boolean result) {
                        if (result) {
                            removerCategorias(positions);
                        }
                        mChoicesHandle.exitMode();
                    }
                });
    }
    
    private void removerCategorias(List<Integer> positions) {
        
        for (Integer pos : positions) {
            Categoria categoria = mCategorias.get((int) pos);

            try {
                mCategoriaModel.remove(categoria);
                
            } catch (IllegalArgumentException e) {
                mView.getGenericMethods().toastMessage(e.getMessage());
            }
        }
        
        updateView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonNewRegister) {
            criarCategoria();
        }
    }

}
