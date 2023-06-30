package com.example.proyectoua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    Button maps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        maps=findViewById(R.id.btn_maps);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#03C988"));
        }
    }
    public void FragmentMap(View view){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment MapsFragment = new MapsFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainerView2, MapsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void FragmenChat(View view){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment ChatFragment = new ChatFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainerView2, ChatFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void FragmenLlamada(View view){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment LlamadaFragment= new LlamadaFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainerView2, LlamadaFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void FragmentConfiguracion(View view){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment ConfiguracionFragment = new ConfiguracionFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainerView2, ConfiguracionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



}