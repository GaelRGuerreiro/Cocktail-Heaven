package com.example.cocktailheaven.Cocteles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cocktailheaven.R;
import com.example.cocktailheaven.Util;

public class CoctelDetail extends AppCompatActivity {

    private ImageView coctelImageView;
    private TextView coctelNameTextView;
    private TextView coctelAlcohol;
    private TextView coctelGlass;
    private TextView coctelCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coctel_detail);



        coctelImageView = findViewById(R.id.coctelImageView);
        coctelNameTextView = findViewById(R.id.coctelNameTextView);
        coctelAlcohol = findViewById(R.id.alcoholic_detail);
        coctelGlass = findViewById(R.id.coctail_glass);
        coctelCategory = findViewById(R.id.coctel_category_detail);



        Intent intent = getIntent();
        String nombreCoctel = intent.getStringExtra("strDrink");
        String image = intent.getStringExtra("strDrinkThumb");
        String alcohol = intent.getStringExtra("strAlcoholic");
        String glass = intent.getStringExtra("strGlass");
        String category =intent.getStringExtra("strCategory");

        coctelNameTextView.setText(nombreCoctel);
        coctelCategory.setText(category);
        coctelGlass.setText(glass);
        coctelAlcohol.setText(alcohol);
        Util.downloadBitmapToImageView(image,coctelImageView);




    }
}