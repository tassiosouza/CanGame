package com.funfactory.cangamemake.view;

import android.view.View;

import com.funfactory.cangamemake.model.entity.Rotina;
import com.funfactory.cangamemake.view.adapter.DragDropAdapter;
import com.terlici.dragndroplist.DragNDropListView.OnItemDragNDropListener;

public interface IRotinaActivity extends IListarActivity {

    public void setRotina(Rotina rotina);

    public void setAdapterDrapDrop(@SuppressWarnings("rawtypes") DragDropAdapter adapter,
            OnItemDragNDropListener listener);

    public void onOptionSelected(View view);

    void setSelectedItem(int position);
    
    void setPontuacao(Double pontuacao);
    
    void configComponents(boolean edicao);

}
