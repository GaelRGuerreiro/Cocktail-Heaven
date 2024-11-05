package com.example.cocktailheaven.Perfil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocktailheaven.R;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<FavoriteCocktail> favorites;

    public FavoriteAdapter(List<FavoriteCocktail> favorites) {
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_recycler_cell, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteCocktail favorite = favorites.get(position);
        holder.bind(favorite);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private TextView cocktailName;
        private ImageView cocktailImage;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            cocktailName = itemView.findViewById(R.id.favoriteCocktailName);
            cocktailImage = itemView.findViewById(R.id.favoriteCocktailImage);
        }

        public void bind(FavoriteCocktail favorite) {
            cocktailName.setText(favorite.getCocktailName());

            // Usamos Glide para cargar la imagen
            Glide.with(itemView.getContext())
                    .load(favorite.getCocktailImageUrl())
                    .into(cocktailImage);
        }
    }

    public void updateData(List<FavoriteCocktail> newFavorites) {
        favorites.clear();
        favorites.addAll(newFavorites);
        notifyDataSetChanged();
    }
}
