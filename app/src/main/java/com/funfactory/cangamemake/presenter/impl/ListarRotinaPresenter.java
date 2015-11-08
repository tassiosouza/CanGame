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
import com.funfactory.cangamemake.model.IRankingModel;
import com.funfactory.cangamemake.model.IRotinaModel;
import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.model.entity.Ranking;
import com.funfactory.cangamemake.model.entity.Rotina;
import com.funfactory.cangamemake.model.impl.RankingModel;
import com.funfactory.cangamemake.model.impl.RotinaModel;
import com.funfactory.cangamemake.presenter.IListarPECSPresenter;
import com.funfactory.cangamemake.presenter.IListarRotinaPresenter;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.util.DialogUtil;
import com.funfactory.cangamemake.util.DialogUtil.DialogCallback;
import com.funfactory.cangamemake.view.IListarRotinaActivity;
import com.funfactory.cangamemake.view.adapter.ListSelectorHandler;
import com.funfactory.cangamemake.view.adapter.ListarRotinaAdapter;
import com.funfactory.cangamemake.view.impl.ListarRotinaActivity;
import com.funfactory.cangamemake.view.impl.RotinaActivity;
import com.funfactory.cangamemake.view.listener.IChoicesHandle;

public class ListarRotinaPresenter extends GenericPresenter implements IListarRotinaPresenter {

    /**
     * Instancias da View
     */
    private Activity              mContext;
    private IListarRotinaActivity mView;

    /**
     * Models utilizados do presenter
     */
    private IRotinaModel          mRotinaModel      = new RotinaModel();
    private IRankingModel         mRankingModel     = new RankingModel();

    /**
     * Instruções para listagem de Rotinas
     */
    private String                mInstrucao        = IListarRotinaPresenter.INTRUCAO_LISTAR_ROTINA_SISTEMA;

    /**
     * Dependencias das instruções
     */
    private Paciente              mPaciente;
    private int                   mEmptyViewMessage = R.string.sem_rotinas_cadastradas;

    /**
     * Controlador da lista
     */
    private List<Rotina>          mRotinas          = new ArrayList<Rotina>();
    private ListarRotinaAdapter   mAdapter;

    /**
     * Listeners do presenter
     */
    private ListSelectorHandler   mChoicesHandle    = new ListSelectorHandler(new IChoicesHandle() {

                                                        @Override
                                                        public boolean onActionItemClicked(ActionMode mode,
                                                                MenuItem item) {
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
    public void setView(IListarRotinaActivity view) {
        this.mView = view;
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

        recuperarRotinas();

        if (mRotinas.isEmpty()) {
            generateEmptyViewMessage();
            mView.setEmptyView(mEmptyViewMessage);
        } else {
            setListContent();
        }
        configComponentes();
    }

    private void configComponentes() {
        if (mInstrucao.equalsIgnoreCase(IListarRotinaPresenter.INTRUCAO_INCLUIR_EM_PACIENTE)
                || mInstrucao.equalsIgnoreCase(IListarPECSPresenter.INSTRUCAO_INCLUIR_EM_ROTINA)) {
            mView.enableCreateButton(false);
        }
    }

    private void generateEmptyViewMessage() {
        if (mInstrucao.equalsIgnoreCase(IListarRotinaPresenter.INTRUCAO_INCLUIR_EM_PACIENTE)) {
            mEmptyViewMessage = R.string.sem_rotinas_para_selecao;
        } else {
            mEmptyViewMessage = R.string.sem_rotinas_cadastradas;
        }
    }

    public void setListContent() {
        if (mAdapter == null) {
            mAdapter = new ListarRotinaAdapter(mContext, R.layout.rotina_list_item, mRotinas);
        }
        mView.setChoicesHandle(mChoicesHandle);
        mView.setAdapter(mAdapter);
    }

    // =========================================================================================================
    // Gerenciamento de conteudo da lista
    // ==========================================================================================================

    private void recuperarRotinas() {

        List<Rotina> temp = null;

        if (mInstrucao == null) {
            return;
        }

        if (mInstrucao.equals(IListarRotinaPresenter.INTRUCAO_LISTAR_ROTINA_SISTEMA)) {
            temp = mRotinaModel.listAll();
        } else if (mInstrucao.equals(IListarRotinaPresenter.INTRUCAO_INCLUIR_EM_PACIENTE)) {
            temp = recuperarRotinaPaciente(mPaciente);
        } else if (mInstrucao.equals(IListarRotinaPresenter.INTRUCAO_LISTAR_ROTINA_PACIENTE)) {
            temp = mRotinaModel.getRotinasByPaciente(mPaciente.getIdEntity());
        }

        mRotinas.clear();

        if (temp != null) {
            mRotinas.addAll(temp);
        }
    }

    private List<Rotina> recuperarRotinaPaciente(Paciente paciente) {
        List<Rotina> rotinas = null;

        if (paciente.getIdEntity() != null) {
            rotinas = subtrairItensDaLista(mRotinaModel.listAll(),
                    mRotinaModel.getRotinasByPaciente(mPaciente.getIdEntity()));
        } else {
            rotinas = mRotinaModel.listAll();
        }

        return rotinas;
    }

    private List<Rotina> subtrairItensDaLista(List<Rotina> origem, List<Rotina> remocao) {

        if (origem != null && remocao != null && origem.size() > 0) {
            for (Rotina rotina : remocao) {
                for (int i = 0; i < origem.size(); i++) {
                    if (origem.get(i).getIdEntity().longValue() == rotina.getIdEntity().longValue()) {
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
        if (mInstrucao.equalsIgnoreCase(IListarRotinaPresenter.INTRUCAO_INCLUIR_EM_PACIENTE)) {
            mPaciente = (Paciente) bundle.get(Constants.PACIENTE);
            mContext.setTitle(R.string.selecione_para_incluir);
        } else if (mInstrucao.equalsIgnoreCase(IListarRotinaPresenter.INTRUCAO_LISTAR_ROTINA_PACIENTE)) {
            mPaciente = (Paciente) bundle.get(Constants.PACIENTE);
            mContext.setTitle(R.string.rotinas_do_paciente);
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

            if (mInstrucao.equalsIgnoreCase(IListarRotinaPresenter.INTRUCAO_LISTAR_ROTINA_SISTEMA)
                    || mInstrucao.equalsIgnoreCase(IListarRotinaPresenter.INTRUCAO_LISTAR_ROTINA_PACIENTE)) {
                resource = R.menu.criar;
            }
        }

        return resource;
    }

    public int getActionBarResourceParaMultiSelecao() {
        int resource = 0;

        if (mInstrucao.equalsIgnoreCase(INTRUCAO_LISTAR_ROTINA_SISTEMA)
                || mInstrucao.equalsIgnoreCase(INTRUCAO_LISTAR_ROTINA_PACIENTE)) {
            resource = R.menu.excluir;
        } else if (mInstrucao.equalsIgnoreCase(INTRUCAO_INCLUIR_EM_PACIENTE)) {
            resource = R.menu.incluir;
        }

        return resource;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new:
                tratarActionNew();
                break;
            default:
                break;
        }
        return false;

    }

    private void tratarActionNew() {
        Intent intent = null;

        if (mInstrucao.equalsIgnoreCase(INTRUCAO_LISTAR_ROTINA_SISTEMA)) {
            intent = new Intent(mContext, RotinaActivity.class);
            intent.putExtra(Constants.INSTRUCAO, Constants.INSTRUCAO_CRIAR);
        } else if (mInstrucao.equalsIgnoreCase(INTRUCAO_LISTAR_ROTINA_PACIENTE)) {
            intent = new Intent(mContext, ListarRotinaActivity.class);
            intent.putExtra(Constants.PACIENTE, mPaciente);
            intent.putExtra(Constants.INSTRUCAO, INTRUCAO_INCLUIR_EM_PACIENTE);
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

        if (mInstrucao.equalsIgnoreCase(INTRUCAO_INCLUIR_EM_PACIENTE)) {
            adicionarAPaciente(positions);
            retornarAposAdicaoEmPaciente();
        }

        mChoicesHandle.exitMode();
    }

    private void adicionarAPaciente(List<Integer> positions) {
        for (Integer pos : positions) {
            Rotina rotinas = mRotinas.get((int) pos);

            Ranking ranking = new Ranking();
            ranking.setIdPaciente(mPaciente.getIdEntity());
            ranking.setIdRotina(rotinas.getIdEntity());
            ranking.setDataHoraCriacao(new Date());

            mRankingModel.saveOrUpdate(ranking);
        }
    }

    private void retornarAposAdicaoEmPaciente() {
        Intent intent = new Intent();
        mView.getGenericMethods().getContext().setResult(Activity.RESULT_OK, intent);
        mView.getGenericMethods().finalizarActivity();
    }

    private boolean isRotinaAssociada(final ArrayList<Integer> positions) {
        for (Integer p : positions) {
            if (mRankingModel.isAssociacaoRotinaExiste(mRotinas.get(p).getIdEntity())) {
                return true;
            }
        }
        return false;
    }

    private void tratarActionDelete(final ArrayList<Integer> positions) {

        boolean isRotinaAssociada = isRotinaAssociada(positions);

        int mensagem = isRotinaAssociada ? R.string.deletar_rotinas_associadas
                : R.string.deletar_multiplas_rotinas_mensagem;

        if (mInstrucao.equals(INTRUCAO_LISTAR_ROTINA_SISTEMA)) {

            DialogUtil.generateDialog(mContext, R.string.deletar, mensagem, new DialogCallback() {

                @Override
                public void onResult(boolean result) {
                    if (result) {
                        removerDoSistema(positions);
                        mChoicesHandle.exitMode();
                    }
                }
            }).show();

        } else if (mInstrucao.equals(INTRUCAO_LISTAR_ROTINA_PACIENTE)) {

            DialogUtil.generateDialog(mContext, R.string.deletar, R.string.deletar_multiplas_rotinas_mensagem,
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
            Rotina rotina = mRotinas.get((int) pos);
            mRotinaModel.remove(rotina);
        }
        updateView();
    }

    private void removerDoPaciente(final ArrayList<Integer> positions) {
        for (Integer pos : positions) {
            Rotina rotina = mRotinas.get((int) pos);
            mRankingModel.removeRakingByRotinaPaciente(rotina.getIdEntity(), mPaciente.getIdEntity());
        }
        updateView();
    }

    // =========================================================================================================
    // Tratamento de seleção simples
    // =========================================================================================================

    private void tratarSelecaoSimples(int position) {

        if (mInstrucao.equalsIgnoreCase(INTRUCAO_LISTAR_ROTINA_SISTEMA)) {

            Rotina rotina = mRotinas.get(position);
            visualizarRotina(rotina);

        } else if (mInstrucao.equalsIgnoreCase(INTRUCAO_INCLUIR_EM_PACIENTE)) {

            mView.getGenericMethods().toastMessage(R.string.selecoes_com_long_press);

            // Não há tratamento de seleção simples para os modos de incluir
        } else if (mInstrucao.equalsIgnoreCase(INTRUCAO_LISTAR_ROTINA_PACIENTE)) {
            Rotina rotina = mRotinas.get(position);
            visualizarRotinaPaciente(rotina);
        }
    }

    private void visualizarRotina(Rotina rotina) {
        Intent intent = new Intent(mContext, RotinaActivity.class);
        intent.putExtra(Constants.INSTRUCAO, Constants.INSTRUCAO_VISUALIZAR);
        intent.putExtra(Constants.ROTINA, rotina);
        mView.getGenericMethods().executarIntent(intent);
    }

    private void visualizarRotinaPaciente(Rotina rotina) {
        Intent intent = new Intent(mContext, RotinaActivity.class);
        intent.putExtra(Constants.INSTRUCAO, Constants.INSTRUCAO_VISUALIZAR);
        intent.putExtra(Constants.ROTINA, rotina);
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
