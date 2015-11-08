package com.funfactory.cangamemake.presenter.impl;

import java.util.ArrayList;
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
import com.funfactory.cangamemake.model.IConfigModel;
import com.funfactory.cangamemake.model.IPacienteModel;
import com.funfactory.cangamemake.model.entity.Config;
import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.model.impl.ConfigModel;
import com.funfactory.cangamemake.model.impl.PacienteModel;
import com.funfactory.cangamemake.presenter.IListarPacientePresenter;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.util.DialogUtil;
import com.funfactory.cangamemake.util.DialogUtil.DialogCallback;
import com.funfactory.cangamemake.view.IListarActivity;
import com.funfactory.cangamemake.view.adapter.ListSelectorHandler;
import com.funfactory.cangamemake.view.adapter.ListarPacienteAdapter;
import com.funfactory.cangamemake.view.impl.ListarPacienteActivity;
import com.funfactory.cangamemake.view.impl.PacienteActivity;
import com.funfactory.cangamemake.view.impl.PerfilPacienteActivity;
import com.funfactory.cangamemake.view.listener.IChoicesHandle;

public class ListarPacientePresenter extends GenericPresenter implements IListarPacientePresenter {

    /**
     * Instancias da View
     */
    private Activity              mContext;
    private IListarActivity       mView;

    /**
     * Models utilizados do presenter
     */
    private IPacienteModel        mPacienteModel    = new PacienteModel();
    private IConfigModel          mConfigModel      = new ConfigModel();

    /**
     * Instruções para listagem de Pacientes
     */
    private String                mInstrucao        = null;
    private Bundle                mBundle           = null;

    /**
     * Dependencias das instruções
     */
    private long                  mPacienteCorrenteId;
    private int                   mEmptyViewMessage = R.string.sem_pacientes_cadastrados;

    /**
     * Controlador da lista
     */
    private List<Paciente>        mPacientes        = new ArrayList<Paciente>();
    private ListarPacienteAdapter mAdapter;

    private boolean               mIsMudandoPaciente;

    /**
     * Listeners do presenter
     */
    private ListSelectorHandler   mChoicesHandle    = new ListSelectorHandler(new IChoicesHandle() {

                                                        @Override
                                                        public boolean onActionItemClicked(ActionMode mode,
                                                                MenuItem item) {
                                                            switch (item.getItemId()) {
                                                            case R.id.action_delete:
                                                                confirmarRemocao(mChoicesHandle.getMultipleChoiceList());
                                                                mChoicesHandle.exitMode();
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
                                                            Paciente paciente = mPacientes.get(position);
                                                            abrirPerfil(position, paciente);
                                                        }
                                                    });

    @Override
    public void setView(final IListarActivity view) {
        mView = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = mView.getGenericMethods().getContext();
        filtrarInstrucoes();
        super.onCreate(savedInstanceState);
    }

    private void filtrarInstrucoes() {

        Intent intent = mView.getGenericMethods().getContext().getIntent();

        if (intent != null) {
            mBundle = intent.getExtras();
        }

        if (mBundle != null && mBundle.containsKey(Constants.INSTRUCAO)) {
            mInstrucao = mBundle.getString(Constants.INSTRUCAO);
        }
    }

    private void processarInstrucao() {

        if (mInstrucao != null) {
            if (mInstrucao.equals(IListarPacientePresenter.INTRUCAO_MUDAR_PACIENTE)) {
                mEmptyViewMessage = R.string.sem_outros_pacientes;
                mIsMudandoPaciente = true;
                removerPacienteCorrenteDaLista();
            } else if (mInstrucao.equals(IListarPacientePresenter.INTRUCAO_AUTO_SELECIONAR_PACIENTE)) {
                int position = (Integer) mBundle.get(Constants.PACIENTE);
                Paciente paciente = mPacientes.get(position);
                mInstrucao = IListarPacientePresenter.INTRUCAO_MUDAR_PACIENTE;
                abrirPerfil(0, paciente);
            }

        }
    }

    private void removerPacienteCorrenteDaLista() {

        if (mPacienteCorrenteId == 0) {
            Config config = mConfigModel.getConfiguration();
            mPacienteCorrenteId = config.getPacienteCorrente();
        }

        for (Paciente paciente : mPacientes) {
            if (paciente.getIdEntity() == mPacienteCorrenteId) {
                paciente.setCurrentSelect(true);
                break;
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onResume() {
        updateView();
        super.onResume();
    }

    private void updateView() {

        mPacientes.clear();
        mPacientes.addAll(mPacienteModel.listAll());

        processarInstrucao();

        if (mPacientes.isEmpty()) {
            mView.setEmptyView(mEmptyViewMessage);
        } else {
            setListContent();
        }
    }

    public void setListContent() {

        if (mAdapter == null) {
            mAdapter = new ListarPacienteAdapter(mContext, R.layout.paciente_list_item, mPacientes);
        }
        mView.setChoicesHandle(mChoicesHandle);
        mView.setAdapter(mAdapter);
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_new) {
            criarPaciente();
        }
        return false;
    }

    private void criarPaciente() {
        Intent cadastrarPacienteIntent = new Intent(mContext, PacienteActivity.class);
        mView.getGenericMethods().executarIntent(cadastrarPacienteIntent);
    }

    public void abrirPerfil(int posicao, Paciente paciente) {
        Intent perfilPacienteIntent = new Intent(mContext, PerfilPacienteActivity.class);
        perfilPacienteIntent.putExtra(Constants.PACIENTE, paciente);

        if (mIsMudandoPaciente) {
            perfilPacienteIntent.putExtra(Constants.PACIENTE, posicao);
            perfilPacienteIntent.putExtra(Constants.INSTRUCAO,
                    IListarPacientePresenter.INTRUCAO_AUTO_SELECIONAR_PACIENTE);
            perfilPacienteIntent.setClass(mContext, ListarPacienteActivity.class);
            perfilPacienteIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            
            mView.getGenericMethods().executarIntent(perfilPacienteIntent);
            mView.getGenericMethods().finalizarActivity();

        } else {
            mView.getGenericMethods().executarIntent(perfilPacienteIntent);
        }
    }

    private void confirmarRemocao(final List<Integer> positions) {

        int messagem = positions.size() > 1 ? R.string.confirmar_remover_pacientes
                : R.string.confirmar_remover_um_paciente;

        DialogUtil.generateDialog(mContext, R.string.deletar, messagem, new DialogCallback() {

            @Override
            public void onResult(boolean result) {
                if (result) {
                    removerPaciente(positions);
                    mChoicesHandle.exitMode();
                }
            }
        }).show();

    }

    private void removerPaciente(List<Integer> positions) {
        for (Integer pos : positions) {
            Paciente paciente = mPacientes.get((int) pos);
            mPacienteModel.remove(paciente);
        }

        updateView();
    }
    
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonNewRegister) {
           criarPaciente();
        }
    }
}
