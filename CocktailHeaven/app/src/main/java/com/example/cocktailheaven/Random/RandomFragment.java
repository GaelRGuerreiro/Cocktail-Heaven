package com.example.cocktailheaven.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cocktailheaven.Cocteles.CoctelDetail;
import com.example.cocktailheaven.Cocteles.CoctelesData;
import com.example.cocktailheaven.Cocteles.CoctelesViewAdapter;
import com.example.cocktailheaven.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RandomFragment extends Fragment {

    private Button randomButton;

    private Activity activity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        activity = getActivity();

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.random_layout, container, false);

        randomButton= view.findViewById(R.id.randomButton);

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomCocktail();
            }
        });
        return view;

    }




    public void getRandomCocktail(){

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://www.thecocktaildb.com/api/json/v1/1/random.php",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {




                            List<CoctelesData> coctelesDataArray = null;
                            coctelesDataArray = parseJson(response);


                            if (coctelesDataArray == null) {
                                Toast.makeText(activity, "Error al parsear la respuesta JSON", Toast.LENGTH_SHORT).show();
                            }else{

                                CoctelesData coctel = coctelesDataArray.get(0);


                                Intent intent = new Intent(activity, CoctelDetail.class);
                                intent.putExtra("strDrink", coctel.getNombreCoctel());
                                intent.putExtra("strDrinkThumb", coctel.getImageUrl());
                                intent.putExtra("strAlcoholic", coctel.getAlcohol());
                                intent.putExtra("strGlass", coctel.getVaso());
                                intent.putExtra("strCategory", coctel.getCategoria());
                                intent.putExtra("strInstructions", coctel.getInstrucciones());
                                intent.putExtra("Ingredientes", coctel.getIngredientes());
                                intent.putExtra("Medidas", coctel.getMedidas());

                                startActivity(intent);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(activity, "Error en la solicitud: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
            );

            RequestQueue queue = Volley.newRequestQueue(activity);
            queue.add(request);

        }


    private List<CoctelesData> parseJson(JSONObject response)  {
        List<CoctelesData> coctelesList = new ArrayList<>();

        try {
            JSONArray drinks = response.getJSONArray("drinks");




                JSONObject drink = drinks.getJSONObject(0);
                String id = drink.getString("idDrink");
                String nombreCoctel = drink.getString("strDrink");
                String imageUrl = drink.getString("strDrinkThumb");
                String categoriaCoctel = drink.getString("strCategory");
                String alcoholCoctel = drink.getString("strAlcoholic");
                String vasoCoctel = drink.getString("strGlass");
                String receta = drink.getString("strInstructions");

            ArrayList<String> ingredientes = new ArrayList<>();
            ArrayList<String> medidas = new ArrayList<>();

            for (int j = 1; j <= 15; j++) {
                String ingrediente = drink.optString("strIngredient" + j, null);
                String medida = drink.optString("strMeasure" + j, null);

                if (ingrediente != null && !ingrediente.isEmpty() && ingrediente != "null") {
                    ingredientes.add(ingrediente);
                }
                if (medida != null && !medida.isEmpty() && medida!="null") {

                    medidas.add(medida);
                }
            }

                // Crear objeto CoctelesData con las listas llenas
                CoctelesData coctel = new CoctelesData(id, nombreCoctel, imageUrl, categoriaCoctel, alcoholCoctel, vasoCoctel, receta, ingredientes, medidas);
                coctelesList.add(coctel);
            } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }

        return coctelesList;
    }

}


