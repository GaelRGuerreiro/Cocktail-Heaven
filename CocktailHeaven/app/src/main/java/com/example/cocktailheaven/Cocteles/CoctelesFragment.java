package com.example.cocktailheaven.Cocteles;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.cocktailheaven.MainActivity;
import com.example.cocktailheaven.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.widget.ImageView;

public class CoctelesFragment extends Fragment{

    private RecyclerView recyclerView;
    private Activity activity;
    private ImageButton lupa;
    private String busqueda;
    public EditText campoTexto;
    private ImageView loadingImage;
    private SwitchCompat alcohol;
    private static final String url="https://www.thecocktaildb.com/api/json/v1/1/search.php?f=";


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }





    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.cocteles_fragment, container, false);

        setupLetterClickListener(layout,R.id.letter_A, "A");
        setupLetterClickListener(layout,R.id.letter_B, "B");
        setupLetterClickListener(layout,R.id.letter_C, "C");
        setupLetterClickListener(layout,R.id.letter_D, "D");
        setupLetterClickListener(layout,R.id.letter_E, "E");
        setupLetterClickListener(layout,R.id.letter_F, "F");
        setupLetterClickListener(layout,R.id.letter_G, "G");
        setupLetterClickListener(layout,R.id.letter_H, "H");
        setupLetterClickListener(layout,R.id.letter_I, "I");
        setupLetterClickListener(layout,R.id.letter_J, "J");
        setupLetterClickListener(layout,R.id.letter_K, "K");
        setupLetterClickListener(layout,R.id.letter_L, "L");
        setupLetterClickListener(layout,R.id.letter_M, "M");
        setupLetterClickListener(layout,R.id.letter_N, "N");
        setupLetterClickListener(layout,R.id.letter_O, "O");
        setupLetterClickListener(layout,R.id.letter_P, "P");
        setupLetterClickListener(layout,R.id.letter_Q, "Q");
        setupLetterClickListener(layout,R.id.letter_R, "R");
        setupLetterClickListener(layout,R.id.letter_S, "S");
        setupLetterClickListener(layout,R.id.letter_T, "T");
        setupLetterClickListener(layout,R.id.letter_U, "U");
        setupLetterClickListener(layout,R.id.letter_V, "V");
        setupLetterClickListener(layout,R.id.letter_W, "W");
        setupLetterClickListener(layout,R.id.letter_X, "X");
        setupLetterClickListener(layout,R.id.letter_Y, "Y");
        setupLetterClickListener(layout,R.id.letter_Z, "Z");



        campoTexto = layout.findViewById(R.id.busqueda);
        loadingImage = layout.findViewById(R.id.loading);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(loadingImage);
        Glide.with(requireActivity()).load(R.drawable.loading).into(imageViewTarget);
        lupa = layout.findViewById(R.id.lupa);




        lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busqueda = campoTexto.getText().toString();
                sendSearchRequest(busqueda);
            }
        });

        campoTexto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                Activity activity = getActivity();
                if (activity instanceof MainActivity) {

                    ((MainActivity) activity).onBackPressedCallback.setEnabled(false);

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                busqueda = campoTexto.getText().toString();
                sendSearchRequest(busqueda);
           }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {

                    Activity activity = getActivity();
                    if (activity instanceof MainActivity) {

                        ((MainActivity) activity).onBackPressedCallback.setEnabled(false);

                    }
                } else {
                    Activity activity = getActivity();
                    if (activity instanceof MainActivity) {
                        ((MainActivity) activity).onBackPressedCallback.setEnabled(true);
                    }
                }
            }
        });

        recyclerView = layout.findViewById(R.id.coctel_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        View view = inflater.inflate(R.layout.cocteles_fragment, container, false);

            return layout;
        }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sendCoctelRequest(url+"a");
    }
    private void setupLetterClickListener(View layout,int letraId, final String letra) {

        TextView letraTextView = layout.findViewById(letraId);
        letraTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCoctelRequest(url+letra.toLowerCase());
            }
        });
    }

        public void sendCoctelRequest (String url){

            loadingImage.setVisibility(View.VISIBLE);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            loadingImage.setVisibility(View.GONE);



                            // Llamar al método parseJson para parsear la respuesta JSON
                            List<CoctelesData> coctelesDataArray = null;
                            coctelesDataArray = parseJson(response);


                            if (coctelesDataArray != null) {

                                CoctelesViewAdapter adapter = new CoctelesViewAdapter(coctelesDataArray, activity);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));

                            } else {
                                Toast.makeText(activity, "Error al parsear la respuesta JSON", Toast.LENGTH_SHORT).show();
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



    public void sendSearchRequest(String search){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "https://www.thecocktaildb.com/api/json/v1/1/search.php?s="+search,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        List<CoctelesData> coctelesDataArray = null;
                        coctelesDataArray = parseJson(response);



                        if (coctelesDataArray != null) {

                            CoctelesViewAdapter adapter = new CoctelesViewAdapter(coctelesDataArray, activity);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(activity,2));

                        } else {

                            Toast.makeText(activity, "Error al parsear la respuesta JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity, "No existen cocteles con dicho nombre.",Toast.LENGTH_LONG).show();
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




            for (int i = 0; i < drinks.length(); i++) {
                JSONObject drink = drinks.getJSONObject(i);
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
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return coctelesList;
    }






}




