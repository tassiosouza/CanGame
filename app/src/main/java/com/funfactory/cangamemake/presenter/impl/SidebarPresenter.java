package com.funfactory.cangamemake.presenter.impl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.presenter.IListarPacientePresenter;
import com.funfactory.cangamemake.presenter.ISidebarPresenter;
import com.funfactory.cangamemake.util.Constants;
import com.funfactory.cangamemake.view.IPerfilPacienteActivity;
import com.funfactory.cangamemake.view.impl.CategoriaActivity;
import com.funfactory.cangamemake.view.impl.ConfigActivity;
import com.funfactory.cangamemake.view.impl.ListarPECSActivity;
import com.funfactory.cangamemake.view.impl.ListarPacienteActivity;
import com.funfactory.cangamemake.view.impl.ListarRotinaActivity;

public class SidebarPresenter extends GenericPresenter implements ISidebarPresenter {

    /**
     * Instancias da View
     */
    private Activity                mContext;
    private IPerfilPacienteActivity mView;

    @Override
    public void setView(final IPerfilPacienteActivity view) {
        mView = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = mView.getGenericMethods().getContext();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSidebarOptionClicked(int viewOptionId) {
        Intent intent = null;

        if (viewOptionId == R.id.btn_pacientes) {

            intent = new Intent(mContext, ListarPacienteActivity.class);
            intent.putExtra(Constants.INSTRUCAO, IListarPacientePresenter.INTRUCAO_MUDAR_PACIENTE);

        } else if (viewOptionId == R.id.btn_pecs) {

            intent = new Intent(mContext, ListarPECSActivity.class);

        } else if (viewOptionId == R.id.btn_rotinas) {

            intent = new Intent(mContext, ListarRotinaActivity.class);

        } else if (viewOptionId == R.id.btn_categorias) {

            intent = new Intent(mContext, CategoriaActivity.class);

        } else if (viewOptionId == R.id.btn_config) {

            intent = new Intent(mContext, ConfigActivity.class);

        }
        else if (viewOptionId == R.id.btn_informacoes) {

            String url = mView.getGenericMethods().getContext().getString(R.string.link_informacoes);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            
        }

        mView.getGenericMethods().executarIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = mContext.getMenuInflater();
        inflater.inflate(R.menu.excluir_editar, menu);
        return true;
    }
}
