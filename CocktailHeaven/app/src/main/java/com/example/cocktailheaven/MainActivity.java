package com.example.cocktailheaven;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.cocktailheaven.Cocteles.CoctelesFragment;
import com.example.cocktailheaven.Ingredientes.IngredientesFragment;
import com.example.cocktailheaven.Perfil.PerfilFragment;

import com.example.cocktailheaven.databinding.ActivityMainBinding;
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private Fragment activeFragment;
    public OnBackPressedCallback onBackPressedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new CoctelesFragment());


        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.Cocteles){
                replaceFragment(new CoctelesFragment());
            } else if (item.getItemId() == R.id.Perfil) {
                replaceFragment(new PerfilFragment());
            } else if (item.getItemId() == R.id.Ingredientes) {
                replaceFragment(new IngredientesFragment());
            }
            return true;
        });


        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (activeFragment instanceof CoctelesFragment) {
                    finish();
                } else {
                    replaceFragment(new CoctelesFragment());
                }
            }
        };

        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);


    }


    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        activeFragment=fragment;
        fragmentTransaction.commit();

    }
}