package com.example.sod14.randompick;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.sod14.randompick.Logic.ElementList;
import com.example.sod14.randompick.Logic.ElementListManager;
import com.example.sod14.randompick.Logic.OrderedArrayList;
import com.example.sod14.randompick.Persistence.ActiveData;

import java.util.Random;

public class RandomActivity extends AppCompatActivity {

    private ElementList<String> elementList;
    private Random random;
    private TextView textView;
    private ElementListManager manager;
    private ActiveData activeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);
        textView = findViewById(R.id.tvRandom);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbarRandom);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        getWindow().setEnterTransition(null);

        //Get the list
        String name = getIntent().getStringExtra("elementList");
        OrderedArrayList<ElementList<String>> lists = manager.getLists();
        int a = 0;
        while (a < lists.size()) {
            if (lists.get(a).getName().equals(name)) break;
            a++;
        }
        elementList = lists.get(a); //Found it!

        //Update the UI to the list
        this.setTitle(elementList.getName());

        newRandom();
    }

    public RandomActivity() {
        random = new Random();
        activeData = ActiveData.getInstance();
        manager=activeData.getManager();
    }

    public void RandomClick(View v)
    {
        newRandom();
    }

    private void newRandom()
    {
        textView.setText((String) elementList.getElements().get(random.nextInt(elementList.getElements().size())));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
