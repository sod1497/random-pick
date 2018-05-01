package com.example.sod14.randompick.MainActivityElements;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sod14.randompick.Persistence.ActiveData;
import com.example.sod14.randompick.R;

import java.util.List;

/**
 * Created by sod14 on 31/01/2018.
 */

public class MainListItemsAdapter extends RecyclerView.Adapter {

    private List<MainListItem> items;
    private ActiveData activeData;
    private Context mainContext;

    public MainListItemsAdapter(List<MainListItem> items) {
        this.items = items;
        activeData = ActiveData.getInstance();
        mainContext = activeData.getMainActivity();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainListItemViewHolder(
                LayoutInflater.from(mainContext).inflate(R.layout.list_item_main,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainListItem item = items.get(position);
        MainListItemViewHolder viewHolder = (MainListItemViewHolder) holder;

        viewHolder.name.setText(item.getName());
        viewHolder.description.setText(item.getDescription());
        int aux = item.getElementCount();
        String mystring;
        switch (aux){
            case 0:
                mystring = mainContext.getResources().getString(R.string.no_elements);
                break;
            case 1:
                mystring = mainContext.getResources().getString(R.string.element);
                mystring = aux + " " + mystring;
                break;
            default:
                mystring = mainContext.getResources().getString(R.string.elements);
                mystring = aux + " " + mystring;
                break;
        }
        viewHolder.num.setText(mystring);
        viewHolder.colorBar.setImageResource(item.getColor());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
