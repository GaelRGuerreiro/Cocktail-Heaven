package com.example.cocktailheaven.Cocteles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cocktailheaven.R;

import java.util.ArrayList;

public class IngredienteAdapter extends RecyclerView.Adapter<IngredienteViewHolder> {

    private ArrayList<String> ingredientes;
    private ArrayList<String> medidas;

    public IngredienteAdapter(ArrayList<String> ingredientes, ArrayList<String> medidas) {
        this.ingredientes = ingredientes;
        this.medidas = medidas;
    }

    @NonNull
    @Override
    public IngredienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.in_recycler_cell, parent, false);
        return new IngredienteViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull IngredienteViewHolder holder, int position) {
        String ingrediente = ingredientes.get(position);
        String medida = medidas.get(position);

        holder.Ingrediente.setText(ingrediente);
        holder.Medida.setText(medida);
    }

    @Override
    public int getItemCount() {
        return Math.min(ingredientes.size(), medidas.size());
    }
}


