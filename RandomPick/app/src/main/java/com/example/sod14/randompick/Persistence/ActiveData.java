package com.example.sod14.randompick.Persistence;

import android.content.Context;

import com.example.sod14.randompick.Logic.ElementList;
import com.example.sod14.randompick.Logic.ElementListManager;
import com.example.sod14.randompick.MainActivityElements.MainListItem;
import com.example.sod14.randompick.MainActivityElements.MainListItemsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sod14 on 31/01/2018.
 */

public class ActiveData {
    private static ActiveData instance;

    private Context mainActivity;
    private Context listActivity;
    private Context randomActivity;
    private ElementListManager manager;

    private MainListItemsAdapter mainListItemsAdapter;
    private List<MainListItem> mainListItems;

    private ActiveData()
    {

    }

    public static ActiveData getInstance()
    {
        if(instance==null) instance = new ActiveData();
        return instance;
    }

    public Context getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Context mainActivity) {
        this.mainActivity = mainActivity;
    }

    public Context getListActivity() {
        return listActivity;
    }

    public void setListActivity(Context listActivity) {
        this.listActivity = listActivity;
    }

    public Context getRandomActivity() {
        return randomActivity;
    }

    public void setRandomActivity(Context randomActivity) {
        this.randomActivity = randomActivity;
    }

    public ElementListManager getManager() {
        if(manager==null){
            manager = new ElementListManager(this.mainActivity);
        }
        return manager;
    }

    public void setManager(ElementListManager manager) {
        this.manager = manager;
    }

    public MainListItemsAdapter getMainListItemsAdapter() {
        if(mainListItemsAdapter==null) mainListItemsAdapter = new MainListItemsAdapter(mainListItems);
        return mainListItemsAdapter;
    }

    public void setMainListItemsAdapter(MainListItemsAdapter mainListItemsAdapter) {
        this.mainListItemsAdapter = mainListItemsAdapter;
    }

    public List<MainListItem> getMainListItems() {
        if(mainListItems==null) mainListItems = new ArrayList<>();
        return mainListItems;
    }

    public void setMainListItems(List<MainListItem> mainListItems) {
        this.mainListItems = mainListItems;
    }
}
