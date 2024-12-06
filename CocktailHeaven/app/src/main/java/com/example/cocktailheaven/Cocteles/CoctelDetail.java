package com.example.cocktailheaven.Cocteles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cocktailheaven.R;
import com.example.cocktailheaven.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CoctelDetail extends AppCompatActivity {

    static String host = "http://10.0.2.2:8000"; // Dirección del backend

    private ImageView coctelImageView;
    private TextView coctelNameTextView;
    private TextView coctelAlcohol;
    private TextView coctelGlass;
    private TextView coctelCategory;
    private Button recipeButton;
    private SwitchCompat favSwitch;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coctel_detail);

        // Inicializar componentes de la UI
        recipeButton = findViewById(R.id.button_recipe);
        coctelImageView = findViewById(R.id.coctelImageView);
        coctelNameTextView = findViewById(R.id.coctelNameTextView);
        coctelAlcohol = findViewById(R.id.alcoholic_detail);
        coctelGlass = findViewById(R.id.coctail_glass);
        coctelCategory = findViewById(R.id.coctel_category_detail);
        favSwitch = findViewById(R.id.switch_favorite);

        queue = Volley.newRequestQueue(this);

        // Obtener datos enviados por el intent
        Intent intent = getIntent();
        String nombreCoctel = intent.getStringExtra("strDrink");
        String image = intent.getStringExtra("strDrinkThumb");
        String alcohol = intent.getStringExtra("strAlcoholic");
        String glass = intent.getStringExtra("strGlass");
        String category = intent.getStringExtra("strCategory");
        String receta = intent.getStringExtra("strInstructions");
        ArrayList<String> ingredientes = intent.getStringArrayListExtra("Ingredientes");
        ArrayList<String> medidas = intent.getStringArrayListExtra("Medidas");
        String cocktailId = intent.getStringExtra("idDrink");

        // Mostrar datos en la UI
        coctelNameTextView.setText(nombreCoctel);
        coctelCategory.setText(category);
        coctelGlass.setText(glass);
        coctelAlcohol.setText(alcohol);
        Util.downloadBitmapToImageView(image, coctelImageView);

        // Verificar si el cóctel ya está marcado como favorito
        favSwitch.setOnCheckedChangeListener(null);

        checkIfFavorite(cocktailId);

        // Escuchar cambios en el switch de favoritos
        favSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                markAsFavorite(cocktailId, nombreCoctel, image);
            } else {
                unmarkAsFavorite(cocktailId);
            }
        });

        // Navegar a la pantalla de receta
        recipeButton.setOnClickListener(v -> {
            Intent intent2 = new Intent(CoctelDetail.this, RecipeDetail.class);
            intent2.putExtra("strInstructions", receta);
            intent2.putExtra("Ingredientes", ingredientes);
            intent2.putExtra("Medidas", medidas);
            intent2.putExtra("Imagen", image);
            startActivity(intent2);
        });
    }

    private void markAsFavorite(String cocktailId, String cocktailName, String imageUrl) {
        String url = host + "/favorites/" + cocktailId;
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("cocktail_name", cocktailName);
            requestBody.put("cocktail_image_url", imageUrl);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                requestBody,
                response -> Toast.makeText(this, "Marked as favorite", Toast.LENGTH_SHORT).show(),
                error -> handleErrorResponse(error)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return getAuthHeaders();
            }
        };

        queue.add(request);
    }

    private void unmarkAsFavorite(String cocktailId) {
        String url = host + "/favorites/" + cocktailId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> Toast.makeText(this, "Unmarked as favorite", Toast.LENGTH_SHORT).show(),
                error -> handleErrorResponse(error)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return getAuthHeaders();
            }
        };

        queue.add(request);
    }

    private void checkIfFavorite(String cocktailId) {
        String url = host + "/favorites/" + cocktailId;

        favSwitch.setOnCheckedChangeListener(null);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        boolean isFavorite = response.getBoolean("favorite");
                        favSwitch.setChecked(isFavorite);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> handleErrorResponse(error)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return getAuthHeaders();
            }
        };

        queue.add(request);
    }


    private Map<String, String> getAuthHeaders() {
        SharedPreferences preferences = getSharedPreferences("USER_SESSIONS_PREFS", MODE_PRIVATE);
        String sessionToken = preferences.getString("VALID_TOKEN", null);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Sesion-Token", sessionToken);
        return headers;
    }

    private void handleErrorResponse(VolleyError error) {
        if (error.networkResponse == null) {
            Toast.makeText(this, "Red Error", Toast.LENGTH_SHORT).show();
        } else {
            int serverCode = error.networkResponse.statusCode;
            if (serverCode == 401) {
                Toast.makeText(this, "Not autenticated", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
