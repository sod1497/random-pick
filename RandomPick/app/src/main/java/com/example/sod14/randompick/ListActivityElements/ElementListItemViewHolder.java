package com.example.sod14.randompick.ListActivityElements;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.sod14.randompick.ListActivity;
import com.example.sod14.randompick.Logic.ElementList;
import com.example.sod14.randompick.Logic.ElementListManager;
import com.example.sod14.randompick.Persistence.ActiveData;
import com.example.sod14.randompick.R;

/**
 * Created by sod14 on 31/01/2018.
 */

public class ElementListItemViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public ElementListItemsAdapter adapter;
    public String item;
    public ElementList<String> items;
    public Button button;
    private ActiveData activeData;
    private ElementListManager manager;


    public ElementListItemViewHolder(final View itemView) {
        super(itemView);

        activeData = ActiveData.getInstance();
        manager = activeData.getManager();

        name = itemView.findViewById(R.id.tvElementName);
        button = itemView.findViewById(R.id.itemListButton);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(activeData.getListActivity(), v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.item_context_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.itemRemove:
                                int index = items.getElements().indexOf(item);
                                items.getElements().remove(item);
                                manager.saveList(items);

                                adapter.notifyItemRemoved(index);
                                break;

                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        };
        button.setOnClickListener(onClickListener);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                item = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //If not adding
                if (items != null) {
                    if (!items.getElements().contains(s.toString())) {
                        int originalIndex = items.getElements().indexOf(item);
                        items.getElements().remove(item);
                        items.getElements().add(s.toString());
                        int newIndex = items.getElements().indexOf(s.toString());

                        adapter.notifyItemMoved(originalIndex, newIndex);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


}
