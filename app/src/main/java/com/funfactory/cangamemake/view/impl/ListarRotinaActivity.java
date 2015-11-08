package com.funfactory.cangamemake.view.impl;

import roboguice.inject.ContentView;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.presenter.IGenericActivity;
import com.funfactory.cangamemake.presenter.IListarRotinaPresenter;
import com.funfactory.cangamemake.presenter.impl.ListarRotinaPresenter;
import com.funfactory.cangamemake.view.IListarRotinaActivity;
import com.funfactory.cangamemake.view.adapter.ListSelectorHandler;

@ContentView(R.layout.activity_listar)
public class ListarRotinaActivity extends GenericActivity implements
		IListarRotinaActivity {

	/**
	 * Componentes da View
	 */
	private ListView mListview;
	private ListSelectorHandler mSelectorHandler;

	/**
	 * Presenters
	 */
	private IListarRotinaPresenter mPresenter = new ListarRotinaPresenter();

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
		ImageView imageView = (ImageView) findViewById(R.id.no_content_icon);
		imageView.setImageResource(R.drawable.big_icon_for_emp_list_rotinas);
	}

	@Override
	public void setChoicesHandle(ListSelectorHandler handle) {
		mSelectorHandler = handle;
		mSelectorHandler.setListView(mListview);
	}

	@Override
	public void finalizar() {
		this.finish();
	}

	@Override
	public void onClick(View view) {
		mPresenter.onClick(view);
	}

	@Override
	public void enableCreateButton(boolean enable) {
		ImageButton createButton = (ImageButton) findViewById(R.id.buttonNewRegister);
		createButton.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

}
