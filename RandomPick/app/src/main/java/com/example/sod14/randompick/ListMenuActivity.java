package com.example.sod14.randompick;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sod14.randompick.Logic.ElementList;
import com.example.sod14.randompick.Logic.ElementListManager;
import com.example.sod14.randompick.Logic.OrderedArrayList;
import com.example.sod14.randompick.MainActivityElements.MainListItem;
import com.example.sod14.randompick.MainActivityElements.MainListItemsAdapter;
import com.example.sod14.randompick.Persistence.ActiveData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class ListMenuActivity extends AppCompatActivity {

    //Common app data
    private ActiveData activeData;
    private ElementListManager manager;

    //UI elements
    private MainListItemsAdapter adapter;
    private List<MainListItem> mainListItems;
    private RecyclerView recyclerView;

    //Request IDs
    private static final int FILE_SELECT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //UI stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_menu);

        //Add the toolbar always after the setcontentview method
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarListMenu);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Load app data
        activeData = ActiveData.getInstance();
        activeData.setMainActivity(this);
        manager = activeData.getManager();

        //Prepare elements for the UI
        mainListItems = activeData.getMainListItems();
        adapter = activeData.getMainListItemsAdapter();
        recyclerView.setAdapter(adapter);

        loadData();
    }

    //When coming back to the Activity from another one
    @Override
    protected void onResume() {
        super.onResume();

        mainListItems = activeData.getMainListItems();
        adapter = activeData.getMainListItemsAdapter();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadData();

    }

    //Loads UI data
    private void loadData() {
        mainListItems.clear();
        OrderedArrayList<ElementList<String>> lists = this.manager.getLists();
        MainListItem item;
        for (ElementList<String> e : lists) {
            item = new MainListItem(e.getName(), e.getDescription(), e.getElements().size(), e.getColor());
            mainListItems.add(item);
            adapter.notifyItemInserted(mainListItems.indexOf(item));
        }
    }

    //Handler for list adding
    public void AddListClick(View v) {
        startActivity(new Intent(this, AddListActivity.class));
    }

    //Handler for the list elements
    public void ElementClick(String name) {
        OrderedArrayList<ElementList<String>> lists = manager.getLists();
        int a = 0;
        while (a < lists.size()) {
            if (lists.get(a).getName() == name) break;
            a++;
        }
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    //Adding menu options to the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    //Handlers for the menu options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.importBtn:

                if (isExternalStorageReadable()) {
                    showFileChooser();
                }

                return true;
            case R.id.settings:
                startActivity(new Intent(this,SettingsActivity.class));

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    //For importing, not working yet
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //same
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    try {
                        manager.importList(getContentResolver().openInputStream(uri));
                        loadData();
                    }catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
        }
    }


}
