package com.example.cocktailheaven.Perfil;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cocktailheaven.R;
import com.squareup.picasso.Picasso;

public class FavoriteViewHolder extends RecyclerView.ViewHolder {

    private ImageView cocktailImageView;
    private TextView cocktailNameTextView;

    public FavoriteViewHolder(@NonNull View itemView) {
        super(itemView);
        cocktailImageView = itemView.findViewById(R.id.favoriteCocktailImage);
        cocktailNameTextView = itemView.findViewById(R.id.favoriteCocktailName);
    }

    public void bind(FavoriteCocktail favoriteCocktail) {
        cocktailNameTextView.setText(favoriteCocktail.getCocktailName());
        Picasso.get().load(favoriteCocktail.getCocktailImageUrl()).into(cocktailImageView);
    }
}
