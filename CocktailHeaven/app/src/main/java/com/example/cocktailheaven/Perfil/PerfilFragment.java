package com.example.cocktailheaven.Perfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cocktailheaven.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class PerfilFragment extends Fragment {


    private TextView textViewUsername;

    private TextView textViewEmail;

    private RequestQueue queue;

    private Context context;

    static String host = "http://10.0.2.2:8000";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.perfil_layout, container, false);

        textViewUsername = view.findViewById(R.id.nombreUsuario);
        textViewEmail = view.findViewById(R.id.correoUsuario);


        context = getActivity();
        queue = Volley.newRequestQueue(context);



        return view;
    }





}