package com.funfactory.cangamemake.view.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.entity.PECS;
import com.funfactory.cangamemake.util.PhotoUtil;
import com.terlici.dragndroplist.DragNDropSimpleAdapter;

public class DragDropAdapter extends DragNDropSimpleAdapter {

    public static final String NAME   = "name";
    public static final String ID     = "_id";
    public static final String OBJECT = "object";

    private Context            mContext;
    private int                mLayoutResourceId;
    private DeleteDragDropListClick    mDeleteListener;

    public DragDropAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,
            int handler) {
        super(context, data, resource, from, to, handler);
        mContext = context;
        mLayoutResourceId = resource;
    }

    public void setDeleleListener(DeleteDragDropListClick listener) {
        mDeleteListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        

        DragDropPECSHolder holder = new DragDropPECSHolder();

        if (row == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder.pecLegenda = (TextView) row.findViewById(R.id.legenda);
            holder.image = (ImageView) row.findViewById(R.id.imageView);
            holder.image.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            holder.delete = (ImageButton) row.findViewById(R.id.delete);

            row.setTag(holder);

        } else {
            holder = (DragDropPECSHolder) row.getTag();
        }

        @SuppressWarnings("unchecked")
        HashMap<String, Object> item = (HashMap<String, Object>) getItem(position);

        final PECS object = (PECS) item.get(OBJECT);
        holder.position = getLibPosition(position);
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

        if (mDeleteListener != null){
            holder.delete.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    mDeleteListener.delete(object.getIdEntity());
                }
            });
        }

        return row;
    }
    
    public interface DeleteDragDropListClick {
        public void delete(Long position);
    }

    public static class DragDropPECSHolder {
        public int         position;
        TextView    pecLegenda;
        ImageView   image;
        ImageButton delete;
    }
    
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
