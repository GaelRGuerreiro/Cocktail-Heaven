package com.example.cocktailheaven.Cocteles;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cocktailheaven.R;

public class IngredienteViewHolder extends RecyclerView.ViewHolder {

        TextView Ingrediente;
        TextView Medida;

        public IngredienteViewHolder(View itemView){
            super(itemView);

            Ingrediente= itemView.findViewById(R.id.ingrediente);
            Medida=itemView.findViewById(R.id.medida);
        }

}
