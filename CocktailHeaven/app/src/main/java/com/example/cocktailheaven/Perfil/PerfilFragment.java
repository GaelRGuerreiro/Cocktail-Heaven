package com.example.cocktailheaven.Perfil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.cocktailheaven.LoginRegister.LoginActivity;
import com.example.cocktailheaven.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerfilFragment extends Fragment {

    private TextView textViewUsername, textViewEmail;
    private RecyclerView favoritesRecyclerView;
    private FavoriteAdapter favoriteAdapter;
    private RequestQueue queue;
    private Context context;
    private ImageView loadingImage;
    private Button logoutButton;
    private List<FavoriteCocktail> favoritesList = new ArrayList<>();

    private static final String host = "http://10.0.2.2:8000";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.perfil_layout, container, false);

        textViewUsername = view.findViewById(R.id.nombreUsuario);
        textViewEmail = view.findViewById(R.id.correoUsuario);
        favoritesRecyclerView = view.findViewById(R.id.favorite_recycler);
        loadingImage = view.findViewById(R.id.loadingImage);
        logoutButton = view.findViewById(R.id.button_logout);

        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(loadingImage);
        Glide.with(this).load(R.drawable.loading).into(imageViewTarget);

        context = requireActivity().getApplicationContext();
        queue = Volley.newRequestQueue(context);

        // Configuración del RecyclerView
        favoriteAdapter = new FavoriteAdapter(favoritesList);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        favoritesRecyclerView.setAdapter(favoriteAdapter);

        setupLogoutButton();
        // Cargar los favoritos desde el backend
        loadFavorites();

        return view;
    }

    private void loadFavorites() {
        loadingImage.setVisibility(View.VISIBLE);
        String url = host + "/favorites";
        SharedPreferences preferences = context.getSharedPreferences("USER_SESSIONS_PREFS", Context.MODE_PRIVATE);
        String sessionToken = preferences.getString("VALID_TOKEN", null);

        if (sessionToken != null) {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            loadingImage.setVisibility(View.GONE);

                            try {
                                String username = response.getString("username");
                                String email = response.getString("email");

                                textViewUsername.setText(username);
                                textViewEmail.setText(email);

                                JSONArray favoritesArray = response.getJSONArray("favorites");
                                List<FavoriteCocktail> favoritesList = parseFavorites(favoritesArray);
                                favoriteAdapter.updateData(favoritesList);

                                if (favoritesList.isEmpty()) {
                                    Toast.makeText(context, "No favorites yet.", Toast.LENGTH_SHORT).show();
                                }



                            } catch (JSONException e) {
                                Toast.makeText(context, "Error processing response.", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error al cargar favoritos", Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "Error al obtener los datos del perfil", Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Sesion-Token", sessionToken);
                    return headers;

                }
            };

            queue.add(request);
        }



    }
    private List<FavoriteCocktail> parseFavorites(JSONArray favoritesArray) throws JSONException {
        List<FavoriteCocktail> favoritesList = new ArrayList<>();
        for (int i = 0; i < favoritesArray.length(); i++) {
            JSONObject cocktailJson = favoritesArray.getJSONObject(i);
            String id = cocktailJson.getString("cocktail_id");
            String name = cocktailJson.getString("cocktail_name");
            String imageUrl = cocktailJson.getString("cocktail_image_url");
            favoritesList.add(new FavoriteCocktail(id, name, imageUrl));
        }
        return favoritesList;
    }


    private void setupLogoutButton(){

        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SharedPreferences preferences = context.getSharedPreferences("USER_SESSIONS_PREFS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("VALID_TOKEN");
                editor.apply();

                Toast.makeText(context, "Session closed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();

            }

        });

    }
}
