package com.funfactory.cangamemake.view.impl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.presenter.ICategoriaPresenter;
import com.funfactory.cangamemake.presenter.IGenericActivity;
import com.funfactory.cangamemake.presenter.impl.CategoriaPresenter;
import com.funfactory.cangamemake.view.ICategoriaActivity;
import com.funfactory.cangamemake.view.ICategoriaCallcack;
import com.funfactory.cangamemake.view.adapter.ListSelectorHandler;

public class CategoriaActivity extends GenericActivity implements ICategoriaActivity {

    /**
     * Componentes da View
     */
    private ListView            mListview;
    private AlertDialog         mAlertDialog;
    private ListSelectorHandler mSelectorHandler;

    /**
     * Presenters
     */
    private ICategoriaPresenter mPresenter = new CategoriaPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_listar);

        setParentDependencies(this, mPresenter);
        mPresenter.setView(this);

        mListview = (ListView) findViewById(R.id.listView);

        super.onCreate(savedInstanceState);
        configureActions();
    }

    public IGenericActivity getGenericMethods() {
        return this;
    }

    private void configureActions() {
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setAdapter(ArrayAdapter adapter) {
        setContentView(R.layout.activity_listar);
        mListview = (ListView) findViewById(R.id.listView);
        mSelectorHandler.setListView(mListview);
        mListview.setItemsCanFocus(false);
        mListview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setEmptyView(int stringID) {
        setContentView(R.layout.no_content_activity);
        TextView textView = (TextView) findViewById(R.id.title);
        textView.setText(stringID);
    }

    @Override
    public void setChoicesHandle(ListSelectorHandler handle) {
        mSelectorHandler = handle;
        mSelectorHandler.setListView(mListview);
    }

    @Override
    public void requererInputDeCategoria(final String preloadText, final ICategoriaCallcack callcack) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.nome_categoria);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        if (preloadText != null) {
            input.setText(preloadText);
        }

        builder.setView(input);

        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String descricao = input.getText().toString();
                if (callcack != null) {
                    callcack.descricao(mAlertDialog, descricao);
                }
            }
        });

        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        mAlertDialog = builder.show();
    }

    @Override
    public void onClick(View view) {
        mPresenter.onClick(view);
    }

}
