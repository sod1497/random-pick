package com.example.sod14.randompick;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sod14.randompick.ListActivityElements.ElementListItem;
import com.example.sod14.randompick.ListActivityElements.ElementListItemsAdapter;
import com.example.sod14.randompick.Logic.ElementList;
import com.example.sod14.randompick.Logic.ElementListManager;
import com.example.sod14.randompick.Logic.OrderedArrayList;
import com.example.sod14.randompick.Persistence.ActiveData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/*
This activity shows the name of the list, its elements and lets you add new elements through an
EditText and an Accept button. Will only be called under existing lists.

 */
public class ListActivity extends AppCompatActivity {
    //Common app data
    private ActiveData activeData;
    private ElementListManager manager;

    //List and its elements (and data adapters)
    private ElementList<String> elementList;
    private ElementListItemsAdapter adapter;
    private List<ElementListItem> elementListItem;

    //UI Components
    private Button addButton;
    private EditText etElement;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    //Internal data to keep track of selected objects
    private String listName;

    //Request IDs for Result data on new Activities
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int DETAILS_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //UI stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarListActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomButtonClick(view);
            }
        });

        //Load app data
        activeData = ActiveData.getInstance();
        activeData.setListActivity(this);
        manager = activeData.getManager();

        //Setting UI to the data loaded
        recyclerView = findViewById(R.id.recyclerViewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        etElement = findViewById(R.id.etElement);

        etElement.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            addButtonClick(etElement);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        //Makes the EditText not lose focus when adding a new element to a RecyclerView
        //Not suitable for using keyboard to navigate
        etElement.setNextFocusDownId(etElement.getId());
        etElement.setNextFocusUpId(etElement.getId());
        etElement.setNextFocusLeftId(etElement.getId());
        etElement.setNextFocusRightId(etElement.getId());

        //Load the list selected in the previous Activity
        listName = getIntent().getStringExtra("name");
        loadData();
    }

    //When coming back form another Activity we need to reload the data
    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadData();
    }

    //
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadData() {
        //Search for the list
        OrderedArrayList<ElementList<String>> lists = manager.getLists();
        int a = 0;
        while (a < lists.size()) {
            if (lists.get(a).getName().equals(listName)) break;
            a++;
        }
        elementList = lists.get(a); //Found it!

        //Update the UI to the list
        this.setTitle(elementList.getName());

        adapter = new ElementListItemsAdapter(elementList);
        recyclerView.setAdapter(adapter);
        elementListItem = new ArrayList<>();

        ElementListItem item;
        for (Object e : elementList.getElements()) {
            item = new ElementListItem((String) e);
            elementListItem.add(item);
            adapter.notifyItemInserted(elementListItem.indexOf(item));
        }
        //If there are no elements, the user is driven to add one
        if (elementList.getElements().size() == 0) etElement.requestFocus();
    }

    //Adds an element to the list
    public void addButtonClick(View v) {
        Snackbar snackbar = Snackbar.make(v, R.string.add_some_text, BaseTransientBottomBar.LENGTH_SHORT);
        if (etElement.getText().toString().length() == 0) snackbar.show();
        else if (elementList.getElements().contains(etElement.getText().toString())) {
            snackbar.setText(R.string.element_already_in_list);
            snackbar.show();
        } else {
            //Add the element
            String element = etElement.getText().toString();
            elementList.getElements().add(element);
            manager.saveList(elementList);

            //Get the index to update the recyclerview
            int index = elementList.getElements().indexOf(element);
            adapter.notifyItemInserted(index);

            //UI feedback to the user
            snackbar.setText(etElement.getText().toString() + " " + getString(R.string.added));
            etElement.setText("");
            snackbar.show();
        }

    }

    //Starts the RandomActivity with this list
    public void randomButtonClick(View v) {
        if(elementList.getElements().size()==0)
        {
            Snackbar.make(v,R.string.add_some_elements,Snackbar.LENGTH_LONG).show();
        }
        else {
            //Here i add the transition for the FAB (floating action button) https://www.youtube.com/watch?v=4L4fLrWDvAU
            Intent intent = new Intent(this, RandomActivity.class);
            intent.putExtra("elementList", elementList.getName());
            getWindow().setExitTransition(null);
            intent.setAction(Intent.ACTION_VIEW);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    this, fab, "randomFabTarget").toBundle());
        }
    }

    //Adding the menu to the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        return true;
    }

    //Handlers for the menu buttons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (item.getItemId()) {
            case R.id.detailsButton:
                Intent intent = new Intent(this, AddListActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, elementList.getName());
                startActivityForResult(intent, DETAILS_REQUEST);

                //recibir el nombre de la nueva lista y actualizar los datos actuales

                return true;
            case R.id.deleteList:
                //Show confirmation dialog
                // 1. Instantiate an AlertDialog.Builder with its constructor

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setTitle(R.string.confirmation_dialog);

                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Remove list and file
                        manager.deleteList(elementList);
                        //Finnish activity
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            case R.id.exportListButton:
                if (isExternalStorageWritable()) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    }

                    try {
                        manager.exportList(elementList);
                        Toast.makeText(this, R.string.export_completed, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //Toast.makeText(this, R.string.need_permission, Toast.LENGTH_LONG).show();
                    }

                }
                return true;
            case R.id.clearListMenuItem:
                //Show confirmation dialog

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setTitle(R.string.confirmation_dialog);

                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        elementList.getElements().clear();
                        manager.saveList(elementList);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // 3. Get the AlertDialog from create()
                AlertDialog dialogClear = builder.create();
                dialogClear.show();
                return true;
            case R.id.mergeListMenuItem:
                //Show the list picker dialog

                //Ask for confirmation

                //Do changes


                return true;
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

    //When the list gets edited
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == DETAILS_REQUEST) {
            if (resultCode == RESULT_OK) {
                listName = data.getData().toString();
            }
        }
    }

    public ElementList<String> getElementList() {
        return elementList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_PERMISSIONS_REQUEST_READ_CONTACTS)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                try {
                    manager.exportList(elementList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(this, R.string.need_permission, Toast.LENGTH_LONG).show();
            }
        }
    }
}
