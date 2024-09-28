package com.example.cocktailheaven.LoginRegister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cocktailheaven.MainActivity;
import com.example.cocktailheaven.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText nombreUsuario;
    private EditText password;

    private Button registrarse;

    private Button iniciarSesion;

    private Context context = this;

    private RequestQueue queue;
    private Activity activity = this;

    static String host = "http://10.0.2.2:8000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nombreUsuario = findViewById(R.id.editTextEmailLogin);
        password = findViewById(R.id.editTextPasswordLogin);
        iniciarSesion = findViewById(R.id.buttonLogin);
        registrarse = findViewById(R.id.buttonRegister);
        queue = Volley.newRequestQueue(this);


        //Verificar si ya existe una sesión válida
        SharedPreferences preferences = getSharedPreferences("USER_SESSIONS_PREFS", MODE_PRIVATE);
        String validToken = preferences.getString("VALID_TOKEN", null);
        if (validToken != null) {
            // Si el token es valido,se redirige al usuario a la MainActivity
            Intent intent = new Intent(activity, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navegar a la pantalla de registro
                Intent intent = new Intent(activity, RegisterActivity.class);
                activity.startActivity(intent);
            }
        });

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("password", password.getText().toString());
            requestBody.put("username", nombreUsuario.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Crear una solicitud JsonObjectRequest para iniciar sesión
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                host + "/sessions",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String receivedToken;
                        try {
                            // Obtener el token de sesión de la respuesta JSON
                            receivedToken = response.getString("sessionToken");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(context, "Token de sesión: " + receivedToken, Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "Iniciando sesión...", Toast.LENGTH_SHORT).show();

                        SharedPreferences preferences = context.getSharedPreferences("USER_SESSIONS_PREFS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("VALID_USERNAME", nombreUsuario.getText().toString());
                        editor.putString("VALID_TOKEN", receivedToken);
                        editor.commit();

                        // Navegar a la actividad principal después de iniciar sesión
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);

                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        } else {
                            int serverCode = error.networkResponse.statusCode;
                            // Manejar diferentes códigos de error del servidor
                            if (serverCode == 404) {
                                Toast.makeText(context, "Usuario no válido", Toast.LENGTH_SHORT).show();
                            }

                            if (serverCode == 401) {
                                Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }

                            if (serverCode == 500) {
                                Toast.makeText(context, "Problemas con el servidor", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );

        // Agregar la solicitud a la cola de solicitudes
        this.queue.add(request);
    }
}

    

