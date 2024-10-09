package com.example.cocktailheaven.Cocteles;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocktailheaven.R;
import com.example.cocktailheaven.Util;

public class CoctelesViewHolder extends RecyclerView.ViewHolder {

    private TextView coctelNameTextView;
    private ImageView coctelImageView;

    public CoctelesViewHolder(@NonNull View itemView){
        super(itemView);

        coctelImageView = itemView.findViewById(R.id.coctelNameTextView);
        coctelNameTextView = itemView.findViewById(R.id.coctelNameTextView);



    }

    public void bind(CoctelesData dataForThisCell){

        coctelNameTextView.setText(dataForThisCell.getNombreCoctel());

        Util.downloadBitmapToImageView(dataForThisCell.getImageUrl(), coctelImageView);

    }
}
