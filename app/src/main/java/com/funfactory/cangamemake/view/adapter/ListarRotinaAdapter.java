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
import com.funfactory.cangamemake.model.entity.Rotina;

public class ListarRotinaAdapter extends ArrayAdapter<Rotina> {

    private List<Rotina> mData;

    private int          mLayoutResourceId;

    public ListarRotinaAdapter(Context context, int layoutResourceId, List<Rotina> data) {
        super(context, layoutResourceId, data);
        mData = data;
        mLayoutResourceId = layoutResourceId;
    }

    @Override
    public Rotina getItem(int position) {
        Rotina object = null;
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

        RotinaHolder holder = new RotinaHolder();

        if (view == null) {

            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            view = inflater.inflate(mLayoutResourceId, parent, false);
            holder.rotinaName = (TextView) view.findViewById(R.id.textViewNome);
            view.setTag(holder);

        } else {
            holder = (RotinaHolder) view.getTag();
        }

        final Rotina rotina = (Rotina) getItem(position);

        holder.rotinaName.setText(rotina.getNome());

        return view;
    }

    static class RotinaHolder {
        TextView rotinaName;
    }
}
