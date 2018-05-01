package com.example.sod14.randompick;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.example.sod14.randompick.Logic.ElementList;
import com.example.sod14.randompick.Logic.ElementListManager;
import com.example.sod14.randompick.Logic.OrderedArrayList;
import com.example.sod14.randompick.Persistence.ActiveData;

import java.util.List;

/*
This Activity lets you:
    -   Add a new list (will be called from a ListMenuActivity)
    -   Edit a list (will be called from a ListActivity)
It returns the name of the new list in case of success editing (in order to reload the list when
returning to ListActivity)
 */

public class AddListActivity extends AppCompatActivity {
    //Common data for the app
    private ActiveData activeData;
    private ElementListManager manager;

    //The list editing or creating. If it's null, it's a new list. Else we are editing an existing list.
    private ElementList<String> elementList;

    //UI components
    private EditText etName, etDescription;
    private ImageView imageView;
    private FloatingActionButton bAccept;
    private GridLayout gridLayout;
    private Button b1,b2,b3,b4,b5,b6,b7,b8;

    //Internal data to keep track of the selected options
    private int selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //UI stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        imageView = findViewById(R.id.imageView);
        bAccept = findViewById(R.id.bAdd);

        imageView.setImageResource(R.color.theme5);
        selectedColor=R.color.theme5;

        b1 = findViewById(R.id.bColor1);
        b2 = findViewById(R.id.bColor2);
        b3 = findViewById(R.id.bColor3);
        b4 = findViewById(R.id.bColor4);
        b5 = findViewById(R.id.bColor5);
        b6 = findViewById(R.id.bColor6);
        b7 = findViewById(R.id.bColor7);
        b8 = findViewById(R.id.bColor8);

        b1.setBackgroundColor(getResources().getColor(R.color.theme1));
        b2.setBackgroundColor(getResources().getColor(R.color.theme2));
        b3.setBackgroundColor(getResources().getColor(R.color.theme3));
        b4.setBackgroundColor(getResources().getColor(R.color.theme4));
        b5.setBackgroundColor(getResources().getColor(R.color.theme5));
        b6.setBackgroundColor(getResources().getColor(R.color.theme6));
        b7.setBackgroundColor(getResources().getColor(R.color.theme7));
        b8.setBackgroundColor(getResources().getColor(R.color.theme8));

        //Load common data
        activeData = ActiveData.getInstance();
        manager = activeData.getManager();

        //If we are editing, we adjust the elements to the existing list
        if (getIntent().hasExtra(Intent.EXTRA_TEXT)) {
            //Searching for the selected list
            String elementListName = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            OrderedArrayList<ElementList<String>> lists = manager.getLists();
            int a = 0;
            while (a < lists.size()) {
                if (lists.get(a).getName().equals(elementListName)) break;
                a++;
            }
            elementList = lists.get(a); //Found it!

            //Setting the UI to the list
            selectedColor=elementList.getColor();
            imageView.setImageResource(selectedColor);
            etName.setText(elementList.getName());
            etDescription.setText(elementList.getDescription());
            bAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bEditClick(v);
                }
            });
            setTitle(R.string.edit_list);
        }

    }

    /*
    Called only if this is a new list
    It will show errors if the data is incomplete or there is already a list with the name provided
     */
    public void bAcceptClic(View view) {
        Snackbar incompleteDataSnackbar = Snackbar.make(view, R.string.add_name_description, BaseTransientBottomBar.LENGTH_SHORT);
        Snackbar nameNotValidSnackbar = Snackbar.make(view, R.string.already_exists, BaseTransientBottomBar.LENGTH_SHORT);

        if (etName.getText().toString().length() == 0 || etDescription.getText().toString().length() == 0) {
            incompleteDataSnackbar.show();
        } else {
            ElementList<String> newElement = new ElementList<>();
            newElement.setName(etName.getText().toString());
            newElement.setDescription(etDescription.getText().toString());
            newElement.setColor(selectedColor);

            if (!manager.addList(newElement)) {
                nameNotValidSnackbar.show();
            } else {
                finish();
            }
        }
    }

    /*
    Called only if we are editing
     */
    public void bEditClick(View view) {
        Snackbar incompleteDataSnackbar = Snackbar.make(view, R.string.add_name_description, BaseTransientBottomBar.LENGTH_SHORT);
        Snackbar nameNotValidSnackbar = Snackbar.make(view, R.string.already_exists, BaseTransientBottomBar.LENGTH_SHORT);

        if (etName.getText().toString().length() == 0 || etDescription.getText().toString().length() == 0) {
            incompleteDataSnackbar.show();
        } else {
            try {
                ElementList<String> newElement = (ElementList<String>) elementList.clone();
                newElement.setName(etName.getText().toString());
                newElement.setDescription(etDescription.getText().toString());
                newElement.setColor(selectedColor);

                //If name changed it adds a new list and removes the old one, else it saves changes
                if (!manager.addList(newElement)) {
                    manager.saveList(newElement);
                } else {
                    manager.deleteList(elementList);
                }
                Intent result = new Intent();
                result.setData(Uri.parse(newElement.getName()));
                setResult(RESULT_OK,result);
                finish();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    //For better behaviour when going back
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Just in case, but shouldn't be necessary
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    //Some handlers
    public void colorClick1(View v)
    {
        imageView.setImageResource(R.color.theme1);
        selectedColor=R.color.theme1;
    }
    public void colorClick2(View v)
    {
        imageView.setImageResource(R.color.theme2);
        selectedColor=R.color.theme2;
    }
    public void colorClick3(View v)
    {
        imageView.setImageResource(R.color.theme3);
        selectedColor=R.color.theme3;
    }
    public void colorClick4(View v)
    {
        imageView.setImageResource(R.color.theme4);
        selectedColor=R.color.theme4;
    }
    public void colorClick5(View v)
    {
        imageView.setImageResource(R.color.theme5);
        selectedColor=R.color.theme5;
    }
    public void colorClick6(View v)
    {
        imageView.setImageResource(R.color.theme6);
        selectedColor=R.color.theme6;
    }
    public void colorClick7(View v)
    {
        imageView.setImageResource(R.color.theme7);
        selectedColor=R.color.theme7;
    }
    public void colorClick8(View v)
    {
        imageView.setImageResource(R.color.theme8);
        selectedColor=R.color.theme8;
    }
}
