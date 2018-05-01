package com.example.sod14.randompick.MainActivityElements;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sod14.randompick.ListMenuActivity;
import com.example.sod14.randompick.Persistence.ActiveData;
import com.example.sod14.randompick.R;


/**
 * Created by sod14 on 31/01/2018.
 */

public class MainListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView name,description,num;
    public ImageView colorBar;
    public CardView cardView;
    public ActiveData activeData;

    public MainListItemViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.tvName);
        description = itemView.findViewById(R.id.tvDetails);
        num = itemView.findViewById(R.id.tvNumber);
        colorBar = itemView.findViewById(R.id.colorBar);
        cardView = itemView.findViewById(R.id.card);
        activeData = ActiveData.getInstance();
        cardView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        ((ListMenuActivity)activeData.getMainActivity()).ElementClick(name.getText().toString());
    }


}
