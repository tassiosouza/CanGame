package com.funfactory.cangamemake.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.IConfigModel;
import com.funfactory.cangamemake.model.IPacienteModel;
import com.funfactory.cangamemake.model.entity.Config;
import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.model.impl.ConfigModel;
import com.funfactory.cangamemake.model.impl.PacienteModel;
import com.funfactory.cangamemake.presenter.IListarPECSPresenter;
import com.funfactory.cangamemake.presenter.IListarRotinaPresenter;
import com.funfactory.cangamemake.presenter.IPerfilPacientePresenter;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.util.DialogUtil.DialogCallback;
import com.funfactory.cangamemake.view.IPerfilPacienteActivity;
import com.funfactory.cangamemake.view.impl.ListarPECSActivity;
import com.funfactory.cangamemake.view.impl.ListarRotinaActivity;
import com.funfactory.cangamemake.view.impl.PacienteActivity;
import com.funfactory.cangamemake.view.impl.StartActivity;

public class PerfilPacientePresenter extends GenericPresenter implements IPerfilPacientePresenter {

    /**
     * Instancias da View
     */
    private IPerfilPacienteActivity mView;
    private Activity                mContext;
    
    /**
     * Models utilizados do presenter
     */
    private IPacienteModel          mPacienteModel = new PacienteModel();
    private IConfigModel            mConfigModel   = new ConfigModel();

    /**
     * Dependencias do presenter
     */
    private Paciente                mPaciente;

    @Override
    public void setView(IPerfilPacienteActivity view) {
        mView = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = mView.getGenericMethods().getContext();

        Bundle extras = null;
        Intent intent = mView.getGenericMethods().getContext().getIntent();

        if (intent != null) {
            extras = intent.getExtras();
        }

        if (extras != null && extras.containsKey(Constants.PACIENTE)) {
            mPaciente = (Paciente) extras.get(Constants.PACIENTE);
            setarConteudo();
            salvarPerfilCorrente();
        }
    }
    
    @Override
    public void onResume() {
        if (!isPacienteExiste()){
            escolherNovoPaciente();
        }
        super.onResume();
    }
    
    private boolean isPacienteExiste(){
        Paciente paciente = mPacienteModel.getPacienteById(mPaciente.getIdEntity());
        return paciente != null;
    }
    
    private void escolherNovoPaciente(){
        Intent intent = new Intent(mContext, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mView.getGenericMethods().executarIntent(intent);
        mView.getGenericMethods().toastMessage(R.string.choose_a_pacient);
        mView.getGenericMethods().finalizarActivity();
    }

    @Override
    public void onOptionSelected(int viewIdOption) {
        switch (viewIdOption) {

        case R.id.action_delete:
            confirmDelecao(); 
            break;

        case R.id.action_edit:
            editar();
            break;

        case R.id.btn_pecs:
            mView.getGenericMethods().toastMessage(R.string.pecs_do_paciente);
            mostrarPECSdoPaciente();
            break;

        case R.id.btn_rotinas:
            mView.getGenericMethods().toastMessage(R.string.rotinas_do_paciente);
            mostrarRotinasdoPaciente();
            break;

        default:
            break;
        }
    }

    private void mostrarRotinasdoPaciente() {
        Intent intent = new Intent(mContext, ListarRotinaActivity.class);
        intent.putExtra(Constants.PACIENTE, mPaciente);
        intent.putExtra(Constants.INSTRUCAO, IListarRotinaPresenter.INTRUCAO_LISTAR_ROTINA_PACIENTE);
        mView.getGenericMethods().executarIntent(intent);        
    }

    private void mostrarPECSdoPaciente() {
       Intent intent = new Intent(mContext, ListarPECSActivity.class);
       intent.putExtra(Constants.PACIENTE, mPaciente);
       intent.putExtra(Constants.INSTRUCAO, IListarPECSPresenter.INSTRUCAO_LISTAR_PECS_PACIENTE);
       mView.getGenericMethods().executarIntent(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ACAO_EDICAO && resultCode == Activity.RESULT_OK) {
            mPaciente = (Paciente) data.getExtras().get(Constants.PACIENTE);
            setarConteudo();
        }
    };

    @Override
    public void listarPecs() {
        mView.getGenericMethods().toastMessage(R.string.pecs_usuario);
    }

    @Override
    public void listarRotinas() {
        mView.getGenericMethods().toastMessage(R.string.rotinas_usuario);
    }

    private void salvarPerfilCorrente() {
        Config configuration = mConfigModel.getConfiguration();
        configuration.setPacienteCorrente(mPaciente.getIdEntity());
        mConfigModel.saveOrUpdate(configuration);
    }

    private void editar() {
        Intent intent = new Intent(mContext, PacienteActivity.class);
        intent.putExtra(Constants.PACIENTE, mPaciente);
        mView.getGenericMethods().getContext().startActivityForResult(intent, Constants.ACAO_EDICAO);
    }

    private void setarConteudo() {
        mView.setContent(mPaciente);
    }

    public void confirmDelecao() {
        mView.getGenericMethods().confirmarAcao(R.string.excluir, R.string.excluir_mensagem, new DialogCallback() {

            @Override
            public void onResult(boolean result) {
                if (result) {
                    deletarPaciente();
                    sairDoModoPerfil();
                }
            }
        });
    }

    public void deletarPaciente() {
        mPacienteModel.remove(mPaciente);
        resetarPerfilCorrente();
    }

    private void resetarPerfilCorrente() {
        Config config = mConfigModel.getConfiguration();
        config.setPacienteCorrente(0);
        mConfigModel.saveOrUpdate(config);
    }

    private void sairDoModoPerfil() {
        Context context = mView.getGenericMethods().getContext();
        Intent intent = new Intent(context, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mView.getGenericMethods().executarIntent(intent);
        mView.getGenericMethods().finalizarActivity();
    }

}
