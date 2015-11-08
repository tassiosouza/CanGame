package com.funfactory.cangamemake.view.impl;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.presenter.IGenericActivity;
import com.funfactory.cangamemake.presenter.IPerfilPacientePresenter;
import com.funfactory.cangamemake.presenter.ISidebarPresenter;
import com.funfactory.cangamemake.presenter.impl.PerfilPacientePresenter;
import com.funfactory.cangamemake.presenter.impl.SidebarPresenter;
import com.funfactory.cangamemake.util.PhotoUtil;
import com.funfactory.cangamemake.view.IPerfilPacienteActivity;

@SuppressWarnings("deprecation")
public class PerfilPacienteActivity extends GenericActivity implements IPerfilPacienteActivity {

    /**
     * Referencies to view
     */
    private DrawerLayout             mDrawerLayout;

    private ActionBarDrawerToggle    mDrawerToggle;

    private TextView                 mTextName;
    private ImageView                mImageView;

    /**
     * Presenters
     */
    private ISidebarPresenter        mSidebarPresenter        = new SidebarPresenter();
    private IPerfilPacientePresenter mPerfilPacientePresenter = new PerfilPacientePresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_perfil_paciente);

        /*
         * Set Parent Dependencies
         */
        setParentDependencies(this, mSidebarPresenter);
        setParentDependencies(this, mPerfilPacientePresenter);

        /*
         * Set the view
         */
        mSidebarPresenter.setView(this);
        mPerfilPacientePresenter.setView(this);

        mTextName = (TextView) findViewById(R.id.text_menu);
        mImageView = (ImageView) findViewById(R.id.imageViewFotoPerfilTopBar);
        mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer, 0, 0) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                invalidateOptionsMenu();
            }
        };

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.excluir_editar, menu);
        return true;
    }

    public void onOptionClicked(View view) {
        mSidebarPresenter.onSidebarOptionClicked(view.getId());
    }

    public void onPacientContentClick(View view) {
        mPerfilPacientePresenter.onOptionSelected(view.getId());
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            if (mDrawerToggle.isDrawerIndicatorEnabled()) {
                mDrawerToggle.onOptionsItemSelected(item);
            }
            break;
        default:
            mPerfilPacientePresenter.onOptionSelected(item.getItemId());
            super.onOptionsItemSelected(item);
            break;
        }

        return true;
    }

    @Override
    public IGenericActivity getGenericMethods() {
        return this;
    }

    @Override
    public void setContent(final Paciente paciente) {

        if (paciente.getImagePath() != null && !paciente.getImagePath().equals("")) {

            Bitmap btmp = PhotoUtil.getInstance().createBitmap(paciente.getImagePath());
            if (btmp == null) {
                btmp = PhotoUtil.getInstance().decodeBitmap(paciente.getImagePath());
            }
            if (btmp != null) {
                mImageView.setImageBitmap(btmp);
            }
        }

        mTextName.setText(paciente.getNome());
    }

}