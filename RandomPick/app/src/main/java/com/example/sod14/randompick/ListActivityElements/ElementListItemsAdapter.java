package com.example.sod14.randompick.ListActivityElements;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.example.sod14.randompick.Logic.ElementList;
import com.example.sod14.randompick.Logic.ElementListManager;
import com.example.sod14.randompick.Persistence.ActiveData;
import com.example.sod14.randompick.R;

import java.util.List;

/**
 * Created by sod14 on 31/01/2018.
 */

public class ElementListItemsAdapter extends RecyclerView.Adapter {

    private ElementList<String> items;
    private ActiveData activeData;
    private ElementListManager manager;
    private Context mainContext;

    public ElementListItemsAdapter(ElementList<String> items) {
        this.items = items;
        activeData = ActiveData.getInstance();
        mainContext = activeData.getMainActivity();
        manager=activeData.getManager();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ElementListItemViewHolder(
                LayoutInflater.from(mainContext).inflate(R.layout.list_item_list,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String item = (String) items.getElements().get(position);
        ElementListItemViewHolder viewHolder = (ElementListItemViewHolder) holder;
        viewHolder = new ElementListItemViewHolder(viewHolder.itemView);
        viewHolder.name.setText(item);
        viewHolder.adapter=this;
        viewHolder.items=items;
        viewHolder.item=item;
    }

    @Override
    public int getItemCount() {
        return items.getElements().size();
    }
}
