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
import android.widget.Toast;
import android.widget.EditText;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.cocteles_fragment, container, false);

        campoTexto = layout.findViewById(R.id.busqueda);
        loadingImage = layout.findViewById(R.id.loading);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(loadingImage);
        Glide.with(requireActivity()).load(R.drawable.loading).into(imageViewTarget);
        lupa = layout.findViewById(R.id.lupa);
        alcohol = layout.findViewById(R.id.alcolSwitch);

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

        alcohol = view.findViewById(R.id.alcolSwitch);

        alcohol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String peticion;

                    // If the switch is checked, it's alcoholic, otherwise non-alcoholic
                    if (isChecked) {
                        peticion = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic";
                    } else {
                        peticion = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Non_Alcoholic";
                    }

                    sendCoctelRequest(peticion);
                }
            });

            return layout;
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
    private List<CoctelesData> parseJson(JSONObject response)  {
        List<CoctelesData> coctelesList = new ArrayList<>();

        try {
            JSONArray drinks = response.getJSONArray("drinks");


            for (int i = 0; i < drinks.length(); i++) {
                JSONObject drink = drinks.getJSONObject(i);
                String id = drink.getString("idDrink");
                String nombreCoctel = drink.getString("strDrink");
                String imageUrl = drink.getString("strDrinkThumb");

                // Crea un objeto CoctelData solo con los datos necesarios
                CoctelesData coctel = new CoctelesData(id, nombreCoctel, imageUrl);

                // Añade el cóctel a la lista
                coctelesList.add(coctel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return coctelesList;
    }






}




