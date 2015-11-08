package com.funfactory.cangamemake.view.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.util.PhotoUtil;

public class ListarPECSAdapter extends ArrayAdapter<PECS> {

    private List<PECS> mData;

    private int        mLayoutResourceId;

    public ListarPECSAdapter(Context context, int layoutResourceId, List<PECS> data) {
        super(context, layoutResourceId, data);
        mData = data;
        mLayoutResourceId = layoutResourceId;
    }

    @Override
    public PECS getItem(int position) {
        PECS object = null;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;

        PECSHolder holder = new PECSHolder();
        
        if (row == null) {
            
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
            
            holder.pecLegenda = (TextView) row.findViewById(R.id.legenda);
            holder.image = (ImageView) row.findViewById(R.id.imageView);
            holder.image.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            holder.bar = (RatingBar) row.findViewById(R.id.ranking);
            
            row.setTag(holder);
            
        } else {
            holder = (PECSHolder) row.getTag();
        }

        final PECS object = (PECS) getItem(position);

        holder.pecLegenda.setText(object.getLegenda());

        if (object.getImagePath() != null && !object.getImagePath().equals("")) {

            Bitmap btmp = PhotoUtil.getInstance().createBitmap(object.getImagePath());
            if (btmp == null) {
            	btmp = PhotoUtil.getInstance().decodeBitmap(object.getImagePath());
            }
            if (btmp != null) {
                holder.image.setImageBitmap(btmp);
            }
        }
        
        if (object.getRanking() != null){
            holder.bar.setRating(object.getRanking().getPontuacao());
        } else {
            holder.bar.setVisibility(View.GONE);
        }

        return row;
    }

    static class PECSHolder {
        TextView pecLegenda;
        ImageView image;
        RatingBar bar;
    }
}
