package com.example.cocktailheaven.Perfil;

public class FavoriteCocktail {
    private String cocktailId;
    private String cocktailName;
    private String cocktailImageUrl;

    public FavoriteCocktail(String cocktailId, String cocktailName, String cocktailImageUrl) {
        this.cocktailId = cocktailId;
        this.cocktailName = cocktailName;
        this.cocktailImageUrl = cocktailImageUrl;
    }

    // Getters
    public String getCocktailId() {
        return cocktailId;
    }

    public String getCocktailName() {
        return cocktailName;
    }

    public String getCocktailImageUrl() {
        return cocktailImageUrl;
    }
}

