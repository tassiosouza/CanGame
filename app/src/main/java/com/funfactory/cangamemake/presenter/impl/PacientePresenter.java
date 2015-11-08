package com.funfactory.cangamemake.presenter.impl;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.IPacienteModel;
import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.model.impl.PacienteModel;
import com.funfactory.cangamemake.presenter.IPacientePresenter;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.util.PhotoUtil;
import com.funfactory.cangamemake.view.IEdicaoActivity;

public class PacientePresenter extends GenericPresenter implements IPacientePresenter {

    /**
     * Instancias da View
     */
    private IEdicaoActivity mView;
    private Activity        mContext;

    /**
     * Models utilizados do presenter
     */
    private IPacienteModel  mPacienteModel = new PacienteModel();

    /**
     * Dependencias do presenter
     */
    private Paciente        mPaciente;

    private String          mTempUri       = null;

    @Override
    public void setView(final IEdicaoActivity view) {
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

        if (extras != null && extras.containsKey(Constants.PACIENTE)) {
            mPaciente = (Paciente) extras.get(Constants.PACIENTE);
            mView.inserirConteudo(mPaciente);
        } else {
            mPaciente = new Paciente();
        }
        
        mView.enableMenuContext(true);
    }
    
    @Override
    public void onPause() {
        mView.saveScreen();
        super.onPause();
    }

    @Override
    public void salvarPaciente(String nomePaciente, String nomePai, String nomeMae, String contatoPai,
                               String contatoMae, Date dataNascimento, Boolean isSexoMasculino) {

        if (!isDadosValidos(nomePaciente, nomePai, nomeMae, contatoPai, contatoMae, dataNascimento, isSexoMasculino)) {
            return;
        }

        mPaciente.setNome(nomePaciente.toString());
        mPaciente.setNomePai(nomePai);
        mPaciente.setNomeMae(nomeMae);
        mPaciente.setContatoPai(contatoPai);
        mPaciente.setContatoMae(contatoMae);
        mPaciente.setDataNascimento(dataNascimento);
        mPaciente.setSexo(isSexoMasculino ? Constants.SEXO_MASCULINO : Constants.SEXO_FEMINIMO);

        mPacienteModel.saveOrUpdate(mPaciente);

        limparVariaveisTemporarias();

        mView.getGenericMethods().toastMessage(R.string.operacao_executada_com_sucesso);

        informarDadosDeRetorno();

        mView.getGenericMethods().finalizarActivity();
    }

    @Override
    public boolean isDadosValidos(String nomePaciente, String nomePai, String nomeMae, String contatoPai,
                                  String contatoMae, Date dataNascimento, Boolean isSexoMasculino) {

        if (nomePaciente.trim().isEmpty()) {
            mView.getGenericMethods().toastMessage(R.string.erro_nome_vazio);
            return false;
        }

        if (dataNascimento == null) {
            mView.getGenericMethods().toastMessage(R.string.erro_data_vazia);
            return false;
        }
        
        if (isSexoMasculino == null) {
            mView.getGenericMethods().toastMessage(R.string.erro_sem_sexo);
            return false;
        }

        return true;
    }

    private void informarDadosDeRetorno() {
        Intent intent = new Intent();
        intent.putExtra(Constants.PACIENTE, mPaciente);
        mView.getGenericMethods().getContext().setResult(Activity.RESULT_OK, intent);
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
            default:
                break;
        }
    }

    private void tirarPhoto() {
        mTempUri = PhotoUtil.getInstance().tirarFoto(mContext);
    }
    
    private void getFotoDaGaleria() {
        mTempUri = PhotoUtil.getInstance().getFotoDaGaleria(mContext);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PhotoUtil.CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            mPaciente.setImagePath(mTempUri);
            mView.inserirConteudo(mPaciente);
            
        } else if (requestCode == PhotoUtil.RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
            
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = mContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            mTempUri = picturePath;
            mPaciente.setImagePath(picturePath);
            mView.inserirConteudo(mPaciente);
        } 
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void limparVariaveisTemporarias() {
        mTempUri = null;
    }

    @Override
    public void salvarTela(String nomePaciente, String nomePai, String nomeMae, String contatoPai, String contatoMae,
            Date dataNascimento, Boolean isSexoMasculino) {
        
        mPaciente.setNome(nomePaciente.toString());
        mPaciente.setNomePai(nomePai);
        mPaciente.setNomeMae(nomeMae);
        mPaciente.setContatoPai(contatoPai);
        mPaciente.setContatoMae(contatoMae);
        mPaciente.setDataNascimento(dataNascimento);
        
        if (isSexoMasculino != null){
            mPaciente.setSexo(isSexoMasculino ? Constants.SEXO_MASCULINO : Constants.SEXO_FEMINIMO);
        }
    }
}
