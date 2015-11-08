package com.funfactory.cangamemake.presenter.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.IPECSModel;
import com.funfactory.cangamemake.model.IRankingModel;
import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.model.entity.Ranking;
import com.funfactory.cangamemake.model.entity.Rotina;
import com.funfactory.cangamemake.model.impl.PECSModel;
import com.funfactory.cangamemake.model.impl.RankingModel;
import com.funfactory.cangamemake.presenter.IListarPECSPresenter;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.util.DialogUtil;
import com.funfactory.cangamemake.util.DialogUtil.DialogCallback;
import com.funfactory.cangamemake.view.IListarPECSActivity;
import com.funfactory.cangamemake.view.adapter.ListSelectorHandler;
import com.funfactory.cangamemake.view.adapter.ListarPECSAdapter;
import com.funfactory.cangamemake.view.impl.ListarPECSActivity;
import com.funfactory.cangamemake.view.impl.PECSActivity;
import com.funfactory.cangamemake.view.listener.IChoicesHandle;

public class ListarPECSPresenter extends GenericPresenter implements IListarPECSPresenter {

    /**
     * Instancias da View
     */
    private Activity            mContext;
    private IListarPECSActivity mView;

    /**
     * Models utilizados do presenter
     */
    private IPECSModel          mPecsModel        = new PECSModel();
    private IRankingModel       mRankingModel     = new RankingModel();

    /**
     * Instruções para listagem de PECS
     */
    private String              mInstrucao        = IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_SISTEMA;

    /**
     * Dependencias das instruções
     */
    private Paciente            mPaciente;
    private Rotina              mRotina;
    private int                 mEmptyViewMessage = R.string.sem_pecs_cadastradas;

    /**
     * Controlador da lista
     */
    private List<PECS>          mPECS             = new ArrayList<PECS>();
    private ListarPECSAdapter   mAdapter;

    /**
     * Listeners do presenter
     */

    private ListSelectorHandler mChoicesHandle    = new ListSelectorHandler(new IChoicesHandle() {

                                                      @Override
                                                      public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                                                          switch (item.getItemId()) {
                                                          case R.id.action_insert:
                                                          case R.id.action_delete:
                                                              tratarMultiSelecao(item.getItemId(),
                                                                      mChoicesHandle.getMultipleChoiceList());
                                                              break;
                                                          default:
                                                              mChoicesHandle.exitMode();
                                                              break;
                                                          }
                                                          return false;
                                                      }

                                                      @Override
                                                      public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                                                          MenuInflater inflater = mContext.getMenuInflater();
                                                          int menuResource = getActionBarResourceParaMultiSelecao();
                                                          inflater.inflate(menuResource, menu);
                                                          return true;
                                                      }

                                                      @Override
                                                      public void onItemSelected(int position) {
                                                          tratarSelecaoSimples(position);
                                                      }

                                                  });

    @Override
    public void setView(final IListarPECSActivity view) {
        mView = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = mView.getGenericMethods().getContext();
        filtrarInstrucoes();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        updateView();
        super.onResume();
    }

    private void updateView() {

        recuperarPECS();

        if (mPECS.isEmpty()) {
            generateEmptyViewMessage();
            mView.setEmptyView(mEmptyViewMessage);
        } else {
            setListContent();
        }

        configComponentes();
    }

    private void configComponentes() {
        if (mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_PACIENTE)
                || mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_ROTINA)) {
            mView.enableCreateButton(false);
        }
    }

    private void generateEmptyViewMessage() {
        if (mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_PACIENTE)
                || mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_ROTINA)) {
            mEmptyViewMessage = R.string.sem_pecs_para_selecao;
        } else {
            mEmptyViewMessage = R.string.sem_pecs_cadastradas;
        }
    }

    private void setListContent() {
        if (mAdapter == null) {
            mAdapter = new ListarPECSAdapter(mContext, R.layout.pecs_list_item, mPECS);
        }
        mView.setChoicesHandle(mChoicesHandle);
        mView.setAdapter(mAdapter);
    }

    // =========================================================================================================
    // Gerenciamento de conteudo da lista
    // ==========================================================================================================

    private void recuperarPECS() {

        List<PECS> temp = null;

        if (mInstrucao == null) {
            return;
        }

        if (mInstrucao.equals(IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_SISTEMA)) {
            temp = mPecsModel.listAll();
        } else if (mInstrucao.equals(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_ROTINA)) {
            temp = recuperarPECSRotina(mRotina);
        } else if (mInstrucao.equals(IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_PACIENTE)) {
            temp = mPecsModel.getPECSByPaciente(mPaciente.getIdEntity());
        } else if (mInstrucao.equals(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_PACIENTE)) {
            temp = subtrairItensDaLista(mPecsModel.listAll(), mPecsModel.getPECSByPaciente(mPaciente.getIdEntity()));
        }

        mPECS.clear();

        if (temp != null) {
            mPECS.addAll(temp);
        }
    }

    private List<PECS> recuperarPECSRotina(Rotina rotina) {
        List<PECS> pecs = null;

        if (rotina != null && rotina.getListaPECS() != null && !rotina.getListaPECS().isEmpty()) {
            pecs = subtrairItensDaLista(mPecsModel.listAll(), rotina.getListaPECS());
        } else {
            pecs = mPecsModel.listAll();
        }

        return pecs;
    }

    private List<PECS> subtrairItensDaLista(List<PECS> origem, List<PECS> remocao) {

        if (origem != null && remocao != null && origem.size() > 0) {
            for (PECS pec : remocao) {
                for (int i = 0; i < origem.size(); i++) {
                    if (origem.get(i).getIdEntity().longValue() == pec.getIdEntity().longValue()) {
                        origem.remove(i);
                        break;
                    }
                }
            }
        }

        return origem;
    }

    // =========================================================================================================
    // Filtro de Comandos e recuperação de instancias
    // ==========================================================================================================

    private void filtrarInstrucoes() {

        Intent intent = mView.getGenericMethods().getContext().getIntent();
        Bundle bundle = null;

        if (intent != null) {
            bundle = intent.getExtras();
        }

        if (bundle != null && bundle.containsKey(Constants.INSTRUCAO)) {
            mInstrucao = bundle.getString(Constants.INSTRUCAO);
            pegarReferenciasPorComando(bundle);
        }
    }

    private void pegarReferenciasPorComando(Bundle bundle) {
        if (mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_ROTINA)) {
            mRotina = (Rotina) bundle.get(Constants.ROTINA);
            mContext.setTitle(R.string.selecione_para_incluir);
        } else if (mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_PACIENTE)) {
            mPaciente = (Paciente) bundle.get(Constants.PACIENTE);
            mContext.setTitle(R.string.pecs_do_paciente);
        } else if (mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_PACIENTE)) {
            mPaciente = (Paciente) bundle.get(Constants.PACIENTE);
            mContext.setTitle(R.string.selecione_para_incluir);
        }
    }

    // =========================================================================================================
    // Configuração e ações para a actionbar
    // =========================================================================================================

    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public int getActionBarResourcePadrao() {
        int resource = 0;

        if (mInstrucao != null) {

            if (mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_SISTEMA)
                    || mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_PACIENTE)) {
                resource = R.menu.criar;
            }
        }

        return resource;
    }

    public int getActionBarResourceParaMultiSelecao() {
        int resource = 0;

        if (mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_SISTEMA)
                || mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_PACIENTE)) {
            resource = R.menu.excluir;
        } else if (mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_ROTINA)
                || mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_PACIENTE)) {
            resource = R.menu.incluir;
        }

        return resource;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return false;
    }

    private void tratarActionNew() {
        Intent intent = null;

        if (mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_SISTEMA)) {
            intent = new Intent(mContext, PECSActivity.class);
        } else if (mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_PACIENTE)) {
            intent = new Intent(mContext, ListarPECSActivity.class);
            intent.putExtra(Constants.PACIENTE, mPaciente);
            intent.putExtra(Constants.INSTRUCAO, IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_PACIENTE);
        }

        mView.getGenericMethods().executarIntent(intent);
    }

    // =========================================================================================================
    // Tratamento de retorno multiplo
    // =========================================================================================================

    private void tratarMultiSelecao(int itemId, ArrayList<Integer> positions) {

        switch (itemId) {
        case R.id.action_insert:
            tratarActionInserir(positions);
            break;
        case R.id.action_delete:
            tratarActionDelete(positions);
            break;
        default:
            break;
        }
    }

    private void tratarActionInserir(final List<Integer> positions) {

        if (mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_PACIENTE)) {
            adicionarAPaciente(positions);
            retornarAposAdicaoEmPaciente();
        } else if (mInstrucao.equals(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_ROTINA)) {
            adicionarARotina(positions);
            retornarAposAdicaoEmRotina();
        }

        mChoicesHandle.exitMode();
    }

    private void adicionarARotina(List<Integer> positions) {
        List<PECS> list = new ArrayList<PECS>();
        for (Integer pos : positions) {
            list.add(mPECS.get((int) pos));
        }
        mRotina.setListaPECS(list);
    }

    private void retornarAposAdicaoEmRotina() {
        Intent intent = new Intent();
        intent.putExtra(Constants.ROTINA, mRotina);
        mView.getGenericMethods().getContext().setResult(Activity.RESULT_OK, intent);
        mView.getGenericMethods().finalizarActivity();
    }

    private void adicionarAPaciente(List<Integer> positions) {
        for (Integer pos : positions) {
            PECS pecs = mPECS.get((int) pos);

            Ranking ranking = new Ranking();
            ranking.setIdPaciente(mPaciente.getIdEntity());
            ranking.setIdPECS(pecs.getIdEntity());
            ranking.setDataHoraCriacao(new Date());

            mRankingModel.saveOrUpdate(ranking);
        }
    }

    private void retornarAposAdicaoEmPaciente() {
        Intent intent = new Intent();
        mView.getGenericMethods().getContext().setResult(Activity.RESULT_OK, intent);
        mView.getGenericMethods().finalizarActivity();
    }

    private boolean isPECSAssociada(final ArrayList<Integer> positions) {
        for (Integer p : positions) {
            if (mRankingModel.isAssociacaoPECSExiste(mPECS.get(p).getIdEntity())) {
                return true;
            }
        }
        return false;
    }

    private void tratarActionDelete(final ArrayList<Integer> positions) {

        boolean isPECSAssociada = isPECSAssociada(positions);

        int mensagem = isPECSAssociada ? R.string.deletar_pecs_associadas : R.string.deletar_multiplas_pecs_mensagem;

        if (mInstrucao.equals(IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_SISTEMA)) {

            DialogUtil.generateDialog(mContext, R.string.deletar, mensagem, new DialogCallback() {

                @Override
                public void onResult(boolean result) {
                    if (result) {
                        removerDoSistema(positions);
                        mChoicesHandle.exitMode();
                    }
                }
            }).show();

        } else if (mInstrucao.equals(IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_PACIENTE)) {

            DialogUtil.generateDialog(mContext, R.string.deletar, R.string.deletar_multiplas_pecs_mensagem,
                    new DialogCallback() {

                        @Override
                        public void onResult(boolean result) {
                            if (result) {
                                removerDoPaciente(positions);
                                mChoicesHandle.exitMode();
                            }
                        }
                    }).show();
        }
    }

    private void removerDoSistema(final ArrayList<Integer> positions) {
        for (Integer pos : positions) {
            PECS pecs = mPECS.get((int) pos);
            mPecsModel.remove(pecs);
        }
        updateView();
    }

    private void removerDoPaciente(final ArrayList<Integer> positions) {
        for (Integer pos : positions) {
            PECS pecs = mPECS.get((int) pos);
            mRankingModel.removeRakingByPECPaciente(pecs.getIdEntity(), mPaciente.getIdEntity());
        }
        updateView();
    }

    // =========================================================================================================
    // Tratamento de seleção simples
    // =========================================================================================================

    private void tratarSelecaoSimples(int position) {

        if (mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_SISTEMA)) {

            PECS pecs = mPECS.get(position);
            visualizarPECS(pecs);

        } else if (mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_PACIENTE)
                || mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_ROTINA)) {

            mView.getGenericMethods().toastMessage(R.string.selecoes_com_long_press);

            // Não há tratamento de seleção simples para os modos de incluir
        } else if (mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_PACIENTE)) {
            PECS pecs = mPECS.get(position);
            visualizarPECSPaciente(pecs);
        }
    }

    public void addPECStoPaciente(Long idPECS, Long idPaciente) {
        Ranking ranking = new Ranking();
        ranking.setDataHoraCriacao(new Date());
        ranking.setIdPaciente(idPaciente);
        ranking.setIdPECS(idPECS);
        ranking.setPontuacao(0);

        mRankingModel.saveOrUpdate(ranking);
    }

    public List<PECS> listAllPECSByPaciente(Long idPaciente) {
        return mPecsModel.getPECSByPaciente(idPaciente);
    }

    private void visualizarPECS(PECS pecs) {
        Intent intent = new Intent(mContext, PECSActivity.class);
        intent.putExtra(Constants.INSTRUCAO, Constants.INSTRUCAO_VISUALIZAR);
        intent.putExtra(Constants.PECS, pecs);
        mView.getGenericMethods().executarIntent(intent);
    }

    private void visualizarPECSPaciente(PECS pecs) {
        Intent intent = new Intent(mContext, PECSActivity.class);
        intent.putExtra(Constants.INSTRUCAO, Constants.INSTRUCAO_VISUALIZAR);
        intent.putExtra(Constants.PECS, pecs);
        intent.putExtra(Constants.PACIENTE, mPaciente);
        mView.getGenericMethods().executarIntent(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonNewRegister) {
            tratarActionNew();
        }
    }
}