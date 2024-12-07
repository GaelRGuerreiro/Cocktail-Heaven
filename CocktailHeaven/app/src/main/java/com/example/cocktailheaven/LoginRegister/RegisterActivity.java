package com.example.cocktailheaven.LoginRegister;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cocktailheaven.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private Button registerButton;
    private RequestQueue queue;
    private Context context;
   // static String host = "http://192.168.1.127:8000";
    static String host = "http://10.0.2.2:8000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_register);
        editTextUsername = findViewById(R.id.editTextNombre);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        registerButton = findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPostRegister();
            }
        });

    }
    private void sendPostRegister()  {

        // Construye el cuerpo de la solicitud con los datos del usuario
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", editTextUsername.getText().toString());
            requestBody.put("password", editTextPassword.getText().toString());
            requestBody.put("email", editTextEmail.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                host + "/users",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        SharedPreferences preferences = getSharedPreferences("USER_SESSIONS_PREFS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("VALID_USERNAME", editTextUsername.getText().toString());
                        editor.putString("VALID_EMAIL", editTextEmail.getText().toString());
                        editor.apply();

                        Toast.makeText(RegisterActivity.this, "Created user", Toast.LENGTH_LONG).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(RegisterActivity.this, "The connection has not been established", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                int serverCode = error.networkResponse.statusCode;
                                String errorMessage = new String(error.networkResponse.data, "UTF-8");
                                JSONObject errorJson = new JSONObject(errorMessage);
                                String errorText = errorJson.getString("error");

                                if (serverCode == 400) {
                                    Toast.makeText(RegisterActivity.this, "Complete all fields correctly.", Toast.LENGTH_SHORT).show();
                                } else if (serverCode == 409) {
                                    if (errorText.equals("Username already in use")) {
                                        Toast.makeText(RegisterActivity.this, "The username is already in use", Toast.LENGTH_SHORT).show();
                                    } else if (errorText.equals("Email already in use")) {
                                        Toast.makeText(RegisterActivity.this, "The email is already registered.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Unknown conflict.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Unexpected error. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(RegisterActivity.this, "Error processing the response.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }


                }
        );
        this.queue.add(request);

    }
}
