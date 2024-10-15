package com.example.cocktailheaven.Cocteles;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocktailheaven.R;
import com.example.cocktailheaven.Util;

import java.util.ArrayList;

public class RecipeDetail extends AppCompatActivity {

    private TextView recetaTextView;
    private RecyclerView recyclerView;
    private IngredienteAdapter adapter;
    private ImageView imagen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

    recyclerView=findViewById(R.id.inRecyclerView);
    imagen = findViewById(R.id.imagenCoctel);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));


       Intent intent = getIntent();
       String receta= intent.getStringExtra("strInstructions");
       String image = intent.getStringExtra("Imagen");


       ArrayList<String> ingredientes = intent.getStringArrayListExtra("Ingredientes");
       ArrayList<String> medidas = intent.getStringArrayListExtra("Medidas");

        recetaTextView = findViewById(R.id.receta);

       recetaTextView.setText(receta);


       adapter = new IngredienteAdapter(ingredientes,medidas);
       recyclerView.setAdapter(adapter);

        Util.downloadBitmapToImageView(image,imagen);

    }
}
