package com.funfactory.cangamemake.presenter.impl;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.ICategoriaModel;
import com.funfactory.cangamemake.model.IPECSModel;
import com.funfactory.cangamemake.model.IRankingModel;
import com.funfactory.cangamemake.model.entity.Categoria;
import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.model.entity.Ranking;
import com.funfactory.cangamemake.model.impl.CategoriaModel;
import com.funfactory.cangamemake.model.impl.PECSModel;
import com.funfactory.cangamemake.model.impl.RankingModel;
import com.funfactory.cangamemake.presenter.IPECSPresenter;
import com.funfactory.cangamemake.util.AudioManager;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.util.DialogUtil;
import com.funfactory.cangamemake.util.DialogUtil.DialogCallback;
import com.funfactory.cangamemake.util.FileUtil;
import com.funfactory.cangamemake.util.PhotoUtil;
import com.funfactory.cangamemake.util.VideoUtil;
import com.funfactory.cangamemake.view.IPECSActivity;
import com.funfactory.cangamemake.view.impl.CategoriaActivity;
import com.funfactory.cangamemake.view.impl.ExecutarPECSActivity;
import com.funfactory.cangamemake.view.impl.PECSActivity;

public class PECSPresenter extends GenericPresenter implements IPECSPresenter {

    public static final String INSTRUCAO_SELECIONAR = "Selecionar";

    /**
     * Instancias da View
     */
    private IPECSActivity      mView;
    private Activity           mContext;

    /**
     * Models utilizados do presenter
     */
    private IPECSModel         mPECSModel           = new PECSModel();
    private IRankingModel      mRankingModel        = new RankingModel();
    private ICategoriaModel    mCategoriaModel      = new CategoriaModel();

    /**
     * Instruções para listagem de Pacientes
     */
    private String             mInstrucao           = Constants.INSTRUCAO_EDITAR;

    /**
     * Dependencias do presenter
     */
    private PECS               mPECSInicial;
    private PECS               mPECS;
    private Paciente           mPaciente;
    private Ranking            mRanking;

    private String             mAudioUri            = null;
    private String             mPhotoUri            = null;
    private String             mVideoUri            = null;

    private boolean            mDeletePreviousAudio;
    private boolean            mDeletePreviousVideo;

    private String             mPreviousAudio       = null;
    private String             mPreviousVideo       = null;

    private AudioManager       mAudioManager        = new AudioManager();

    @Override
    public void setView(final IPECSActivity view) {
        mView = view;
        mContext = mView.getGenericMethods().getContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = null;
        Intent intent = mView.getGenericMethods().getContext().getIntent();

        if (intent != null) {
            extras = intent.getExtras();
        }

        if (extras != null && extras.containsKey(Constants.PECS)) {
            mPECS = (PECS) extras.get(Constants.PECS);

            if (extras.containsKey(Constants.PACIENTE)) {
                mPaciente = (Paciente) extras.get(Constants.PACIENTE);
                getPecPaciente();
            }

            updateView();
        } else {
            mPECS = new PECS();
            mPECS.setCategoria(mCategoriaModel.getCategoriaGeral());
        }

        filtrarInstrucoes(intent);
        configurarPorInstrucao(extras);

        validarMenuDeContext();
    }

    private void getPecPaciente() {
        List<Ranking> rankings = mRankingModel.getAllByPecPaciente(mPECS.getIdEntity(), mPaciente.getIdEntity());
        if (rankings != null && rankings.size() > 0) {
            mRanking = rankings.get(0);
        }
    }

    @Override
    public void onResume() {
        updateView();
        super.onResume();
    }

    @Override
    public void onPause() {
        mView.saveScreen();

        if (mAudioManager != null) {

            if (mAudioManager.isRecording()) {
                mAudioManager.pararGravacao();
            }

            if (mAudioManager.isPlaying()) {
                mAudioManager.pararExecAudio();
            }

            mAudioManager.release();
        }

        super.onPause();
    }

    private void updateView() {
        mView.inserirConteudo(mPECS, mRanking);

        boolean isEdicao = mInstrucao.equalsIgnoreCase(Constants.INSTRUCAO_EDITAR);
        mView.configComponents(isEdicao, mPECS, mRanking);
    }

    private void configurarPorInstrucao(Bundle bundle) {
        if (mInstrucao.equalsIgnoreCase(Constants.INSTRUCAO_EDITAR)) {
            mPECSInicial = mPECS.clone();
        }
    }

    private void validarMenuDeContext() {
        mView.enableMenuContext(mInstrucao.equals(Constants.INSTRUCAO_EDITAR));
    }

    private void filtrarInstrucoes(Intent intent) {

        Bundle bundle = null;

        if (intent != null) {
            bundle = intent.getExtras();
        }

        if (bundle != null && bundle.containsKey(Constants.INSTRUCAO)) {
            mInstrucao = bundle.getString(Constants.INSTRUCAO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = mContext.getMenuInflater();
        inflater.inflate(getOptionsMenu(), menu);
        return true;
    }

    private int getOptionsMenu() {

        int resource = 0;

        if (mInstrucao != null && mInstrucao.equalsIgnoreCase(Constants.INSTRUCAO_EDITAR)) {
            resource = R.menu.salvar;
        } else if (mInstrucao != null && mInstrucao.equalsIgnoreCase(Constants.INSTRUCAO_VISUALIZAR)) {
            resource = R.menu.executar_excluir_editar;
        }

        return resource;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            mView.salvar();
        } else if (item.getItemId() == R.id.action_edit) {
            editarPECS();
        } else if (item.getItemId() == R.id.action_delete) {
            removerPECS();
        } else if (item.getItemId() == R.id.action_executar) {
            executarPECS();
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void removerPECS() {
        DialogUtil.generateDialog(mContext, R.string.deletar, R.string.deletar_pecs_mensagem, new DialogCallback() {

            @Override
            public void onResult(boolean result) {
                if (result) {
                    mPECSModel.remove(mPECS);
                    mView.getGenericMethods().finalizarActivity();
                }
            }
        }).show();
    }

    private void editarPECS() {
        Intent intent = new Intent(mContext, PECSActivity.class);
        intent.putExtra(Constants.PECS, mPECS);
        intent.putExtra(Constants.INSTRUCAO, Constants.INSTRUCAO_EDITAR);
        mView.getGenericMethods().getContext().startActivityForResult(intent, Constants.ACAO_EDICAO);
    }

    private void executarPECS() {
        Intent intent = new Intent(mContext, ExecutarPECSActivity.class);
        intent.putExtra(Constants.PECS, mPECS);
        intent.putExtra(Constants.PACIENTE, mPaciente);
        mView.getGenericMethods().getContext().startActivityForResult(intent, Constants.ACAO_EXECUTAR);
    }

    @Override
    public void salvarTela(String legenda) {
        mPECS.setLegenda(legenda);
    }

    @Override
    public void salvarPECS(String legenda) {

        if (!isDadosValidos(legenda)) {
            return;
        }

        if (mPECS.getDataHoraCriacao() == null) {
            mPECS.setDataHoraCriacao(new Date());
        }

        mPECS.setLegenda(legenda);

        String audio = mAudioUri == null ? "" : mAudioUri;

        if ((audio != null && !audio.isEmpty()) && (mPECS.getPathSound() != null && mPECS.getPathSound().isEmpty())) {
            mPECS.setPathSound(audio);
        }

        String video = mVideoUri == null ? "" : mVideoUri;
        if ((video != null && !video.isEmpty()) && (mPECS.getPathVideo() != null && mPECS.getPathVideo().isEmpty())) {
            mPECS.setPathVideo(video);
        }

        mPECSModel.saveOrUpdate(mPECS);

        limparVariaveisTemporarias();

        if (mPECSInicial != null) {
            removerArquivosAnteriores();
        }

        if (mDeletePreviousAudio && mPreviousAudio != null) {
            FileUtil.removerArquivo(mPreviousAudio.replace("file://", ""));
        }

        if (mDeletePreviousVideo && mPreviousVideo != null) {
            FileUtil.removerArquivo(mPreviousVideo.replace("file://", ""));
        }

        mView.getGenericMethods().toastMessage(R.string.operacao_executada_com_sucesso);

        informarDadosDeRetorno();

        mView.getGenericMethods().finalizarActivity();
    }

    private void removerArquivosAnteriores() {
        removerPathInicial(mPECSInicial.getPathSound(), mPECSInicial.getPathSound());
        removerPathInicial(mPECSInicial.getPathVideo(), mPECSInicial.getPathVideo());
    }

    private void removerPathInicial(String pathFinal, String pathInicial) {
        if (pathFinal != null && !pathFinal.isEmpty() && !pathFinal.equalsIgnoreCase(mPECS.getPathVideo())) {
            FileUtil.removerArquivo(pathFinal);
        }
    }

    @Override
    public boolean isDadosValidos(String legenda) {

        if (legenda.trim().isEmpty()) {
            mView.getGenericMethods().toastMessage(R.string.erro_nome_vazio);
            return false;
        }

        if (mPECS.getImagePath() == null || mPECS.getImagePath().isEmpty()) {
            mView.getGenericMethods().toastMessage(R.string.precisa_de_imagem);
            return false;
        }

        return true;
    }

    @Override
    public void onOptionSeleted(int viewOptionId) {
        switch (viewOptionId) {
        case R.id.menu_camera:
            tirarPhoto();
            break;
        case R.id.menu_album:
            getFotoDaGaleria();
            break;
        case R.id.buttonRecAudio:
            gravarAudio();
            break;
        case R.id.buttonPlayAudio:
            executarAudio();
            break;
        case R.id.buttonRemoveAudio:
            removerAudio();
            break;
        case R.id.buttonRecVideo:
            gravarVideo();
            break;
        case R.id.buttonPlayVideo:
            executarVideo();
            break;
        case R.id.buttonRemoveVideo:
            removerVideo();
            break;
        case R.id.button_categoria_pecs:
            selecionarCategoria();
            break;
        default:
            break;
        }
    }

    private void removerVideo() {

        if (mVideoUri != null) {
            FileUtil.removerArquivo(mVideoUri.replace("file://", ""));
            mVideoUri = null;
        } else if (mPECS.getPathVideo() != null) {
            mDeletePreviousVideo = true;
            mPreviousVideo = mPECS.getPathVideo();
            mPECS.setPathVideo(null);
            mPECS.setPathVideo("");
        }

        updateView();
    }

    private void removerAudio() {

        if (mVideoUri != null) {
            FileUtil.removerArquivo(mVideoUri.replace("file://", ""));
            mVideoUri = null;
        } else if (mPECS.getPathSound() != null) {
            mDeletePreviousAudio = true;
            mPreviousAudio = mPECS.getPathSound();
            mPECS.setPathSound(null);
        }

        mView.activeModeRecording(false);
        updateView();
    }

    private void selecionarCategoria() {
        Intent intentCategoriaActivity = new Intent(mContext, CategoriaActivity.class);
        intentCategoriaActivity.putExtra(Constants.INSTRUCAO, INSTRUCAO_SELECIONAR);
        mView.getGenericMethods().getContext().startActivityForResult(intentCategoriaActivity, Constants.ACAO_SELECAO);
    }

    private void gravarAudio() {
        if (mAudioManager.isRecording()) {
            mAudioUri = mAudioManager.pararGravacao();
            mPECS.setPathSound(mAudioUri);
            updateView();
        } else {
            mView.activeModeRecording(true);
            mAudioManager.gravar(mContext);
        }
    }

    private void tirarPhoto() {
        mPhotoUri = PhotoUtil.getInstance().tirarFoto(mContext);
    }

    private void getFotoDaGaleria() {
        mPhotoUri = PhotoUtil.getInstance().getFotoDaGaleria(mContext);
    }

    private void gravarVideo() {
        mVideoUri = VideoUtil.getInstance().gravar(mContext);
    }

    private void executarVideo() {
        String path = mPECS.getPathVideo();
        if (path == null) {
            path = mVideoUri;
        }

        if (!mAudioManager.isPlaying()) {
            VideoUtil.getInstance().executar(mContext, path);
        }
    }

    private void executarAudio() {
        if (mAudioManager.isPlaying()) {
            mAudioManager.pararExecAudio();
        } else {

            String path = mPECS.getPathSound();
            if (path == null) {
                path = mAudioUri;
            }

            mAudioManager.executar(path);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PhotoUtil.CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            mPECS.setImagePath(mPhotoUri);
            updateView();
        } else if (requestCode == VideoUtil.CAPTURE_VIDEO && resultCode == Activity.RESULT_OK) {
            mPECS.setPathVideo(mVideoUri);
            updateView();
        } else if (requestCode == PhotoUtil.RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = mContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            mPECS.setImagePath(picturePath);
            updateView();

        } else if (requestCode == Constants.ACAO_SELECAO && resultCode == Activity.RESULT_OK) {
            Categoria categoria = (Categoria) data.getExtras().get(Constants.CATEGORIA);
            mPECS.setCategoria(categoria);
            updateView();
        } else if (requestCode == Constants.ACAO_EDICAO && resultCode == Activity.RESULT_OK) {
            mPECS = (PECS) data.getExtras().get(Constants.PECS);
            updateView();
        } else if (requestCode == Constants.ACAO_EXECUTAR) {
            if (mPECS != null && mPaciente != null) {
                List<Ranking> rankings = mRankingModel
                        .getAllByPecPaciente(mPECS.getIdEntity(), mPaciente.getIdEntity());
                if (rankings != null && rankings.size() > 0) {
                    mRanking = rankings.get(0);
                }
            }
            updateView();
        }
    }

    public void onDestroy() {
        cancelarAcoesCorrentes();
        removerArquivosTemporarios();
        super.onDestroy();
    }

    private void limparVariaveisTemporarias() {
        mPhotoUri = null;
        mVideoUri = null;
        mAudioUri = null;
    }

    private void removerArquivosTemporarios() {
        if (mVideoUri != null) {
            FileUtil.removerArquivo(mVideoUri);
        }

        if (mAudioUri != null) {
            FileUtil.removerArquivo(mAudioUri);
        }
    }

    private void cancelarAcoesCorrentes() {
        if (mAudioManager.isPlaying()) {
            mAudioManager.pararExecAudio();
        }

        if (mAudioManager.isRecording()) {
            mAudioManager.pararGravacao();
        }
    }

    private void informarDadosDeRetorno() {
        Intent intent = new Intent();
        intent.putExtra(Constants.PECS, mPECS);
        mView.getGenericMethods().getContext().setResult(Activity.RESULT_OK, intent);
    }

    public void associarPEC(long pecId, long pacientId) {

        Ranking ranking = new Ranking();
        ranking.setDataHoraCriacao(new Date());
        ranking.setIdPaciente(pacientId);
        ranking.setIdPECS(pecId);
        ranking.setPontuacao(0);

        mRankingModel.saveOrUpdate(ranking);
    }

    @Override
    public boolean onBackPressed() {

        if (mInstrucao != null && mInstrucao.equalsIgnoreCase(Constants.INSTRUCAO_EDITAR)) {

            DialogUtil.generateDialog(mContext, R.string.sair, R.string.sair_da_edicao_mensagem, new DialogCallback() {

                @Override
                public void onResult(boolean result) {
                    if (result) {
                        mView.getGenericMethods().finalizarActivity();
                    }
                }
            }).show();

            return false;
        }
        return true;
    }

}
