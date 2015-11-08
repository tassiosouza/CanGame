package com.funfactory.cangamemake.view.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.entity.Categoria;

public class ListarCategoriaAdapter extends ArrayAdapter<Categoria> {

    private List<Categoria> mData;

    private int             mLayoutResourceId;

    public ListarCategoriaAdapter(Context context, int layoutResourceId, List<Categoria> data) {
        super(context, layoutResourceId, data);
        mData = data;
        mLayoutResourceId = layoutResourceId;
    }

    @Override
    public Categoria getItem(int position) {
        Categoria object = null;
        if (mData != null) {
            object = mData.get(position);
        }
        return object;
    }

    @Override
    public int getCount() {
        int count = 0;

        if (mData != null) {
            return mData.size();
        }
        return count;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View view = convertView;

        CategoriaHolder holder = new CategoriaHolder();

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            view = inflater.inflate(mLayoutResourceId, parent, false);
            holder.categoriaName = (TextView) view.findViewById(R.id.textViewNome);
            view.setTag(holder);

        } else {
            holder = (CategoriaHolder) view.getTag();
        }

        final Categoria categoria = (Categoria) getItem(position);

        holder.categoriaName.setText(categoria.getDescricao());

        return view;
    }

    static class CategoriaHolder {
        TextView categoriaName;
    }
}
