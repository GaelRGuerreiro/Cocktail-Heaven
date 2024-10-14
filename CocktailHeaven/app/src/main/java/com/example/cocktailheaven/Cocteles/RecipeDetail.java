package com.example.cocktailheaven.Cocteles;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocktailheaven.R;

public class RecipeDetail extends AppCompatActivity {

    private TextView recetaTextView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

       Intent intent = getIntent();
       String receta= intent.getStringExtra("strInstructions");


       if(receta == null){

           receta="Textotootototototooooooooo";

       }
       recetaTextView = findViewById(R.id.receta);

       recetaTextView.setText(receta);
    }
}
