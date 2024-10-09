package com.example.cocktailheaven.Cocteles;

import java.util.ArrayList;

public class CoctelesData {

    private String id;
    private String nombreCoctel;
    private String imageUrl;
    private String categoria;
    private String alcohol;
    private String vaso;
    private String instrucciones;
    private ArrayList<String> ingredientes;
    private ArrayList<String> medidas;

    public CoctelesData(String id, String nombreCoctel, String imageUrl, String categoria, String alcohol, String vaso, String instrucciones, ArrayList<String> ingredientes, ArrayList<String> medidas) {
        this.id = id;
        this.nombreCoctel = nombreCoctel;
        this.imageUrl = imageUrl;
        this.categoria = categoria;
        this.alcohol = alcohol;
        this.vaso = vaso;
        this.instrucciones = instrucciones;
        this.ingredientes = ingredientes;
        this.medidas = medidas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreCoctel() {
        return nombreCoctel;
    }

    public void setNombreCoctel(String nombreCoctel) {
        this.nombreCoctel = nombreCoctel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }

    public String getVaso() {
        return vaso;
    }

    public void setVaso(String vaso) {
        this.vaso = vaso;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public ArrayList<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public ArrayList<String> getMedidas() {
        return medidas;
    }

    public void setMedidas(ArrayList<String> medidas) {
        this.medidas = medidas;
    }
}
