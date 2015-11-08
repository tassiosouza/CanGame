package com.funfactory.cangamemake.view.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funfactory.cangamemake.R;
import com.funfactory.cangamemake.model.entity.Paciente;
import com.funfactory.cangamemake.util.DateUtil;
import com.funfactory.cangamemake.util.PhotoUtil;

public class ListarPacienteAdapter extends ArrayAdapter<Paciente> {

    private List<Paciente> mData;

    private int            mLayoutResourceId;

    public ListarPacienteAdapter(Context context, int layoutResourceId, List<Paciente> data) {
        super(context, layoutResourceId, data);
        mData = data;
        mLayoutResourceId = layoutResourceId;
    }

    @Override
    public Paciente getItem(int position) {
        Paciente object = null;
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

        PacienteHolder holder = new PacienteHolder();

        if (view == null) {

            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            view = inflater.inflate(mLayoutResourceId, parent, false);
            
            holder.image = (ImageView) view.findViewById(R.id.imageView);
			holder.image.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            holder.textName = (TextView) view.findViewById(R.id.textViewUserName);
            holder.textNascimento = (TextView) view.findViewById(R.id.textViewBirthday);
            
            view.setTag(holder);

        } else {
            holder = (PacienteHolder) view.getTag();
        }

        final Paciente paciente = (Paciente) getItem(position);

        
        if (paciente.getImagePath() != null && !paciente.getImagePath().equals("")) {

            Bitmap btmp = PhotoUtil.getInstance().createBitmap(paciente.getImagePath());
            if (btmp == null) {
                btmp = PhotoUtil.getInstance().decodeBitmap(paciente.getImagePath());
            }
            if (btmp != null) {
                holder.image.setImageBitmap(btmp);
            }
        }
        
        holder.textName.setText(paciente.getNome());
        holder.textNascimento.setText(DateUtil.format(paciente.getDataNascimento()));
        
        if (paciente.isCurrentSelected()){
            view.setBackgroundColor(Color.GRAY);
        }

        return view;
    }

    static class PacienteHolder {
        ImageView image;
        TextView  textName;
        TextView  textNascimento;
    }

}
