package com.example.proyectoua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnIngresar;
    private  Button btnRegistrar;
    private FirebaseAuth mAuth;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //ANIMACIÓN
        LottieAnimationView logeo = (LottieAnimationView) findViewById(R.id.logeo);
        LottieAnimationView nologeo = (LottieAnimationView) findViewById(R.id.nologeo);
        LottieAnimationView carga = (LottieAnimationView) findViewById(R.id.carga);

        logeo.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // La animación ha comenzado
                int nuevoAncho = 800; // Nuevo ancho deseado en píxeles
                int nuevoAlto = 800; // Nuevo alto deseado en píxeles
                ViewGroup.LayoutParams params = logeo.getLayoutParams();
                params.width = nuevoAncho;
                params.height = nuevoAlto;
                logeo.setLayoutParams(params);
                float velocidad = 1.5f; // Valor mayor a 1 para acelerar la animación
                nologeo.setSpeed(velocidad);
                logeo.setVisibility(View.VISIBLE); // Hacer que el elemento aparezca
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // La animación ha terminado
                logeo.setVisibility(View.GONE); // Hacer que el elemento desaparezca
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // La animación ha sido cancelada
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // La animación se ha repetido
            }
        });

        nologeo.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // La animación ha comenzado
                int nuevoAncho = 400; // Nuevo ancho deseado en píxeles
                int nuevoAlto = 400; // Nuevo alto deseado en píxeles
                ViewGroup.LayoutParams params = nologeo.getLayoutParams();
                params.width = nuevoAncho;
                params.height = nuevoAlto;
                nologeo.setLayoutParams(params);
                float velocidad = 1.75f; // Valor mayor a 1 para acelerar la animación
                nologeo.setSpeed(velocidad);
                nologeo.setVisibility(View.VISIBLE); // Hacer que el elemento aparezca
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // La animación ha terminado
                nologeo.setVisibility(View.GONE); // Hacer que el elemento desaparezca
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // La animación ha sido cancelada
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // La animación se ha repetido
            }
        });

        carga.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                float velocidad = 1.75f; // Valor mayor a 1 para acelerar la animación
                carga.setSpeed(velocidad);
                carga.setVisibility(View.VISIBLE); // Hacer que el elemento aparezca
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // La animación ha terminado
                carga.setVisibility(View.GONE); // Hacer que el elemento desaparezca
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // La animación ha sido cancelada
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // La animación se ha repetido
            }
        });

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword=findViewById(R.id.txtPassword);
        btnIngresar=findViewById(R.id.btnIngresar);
        btnRegistrar=findViewById(R.id.btnRegistrar);
        mAuth=FirebaseAuth.getInstance();
        btnIngresar.setOnClickListener(view ->{
            IngresarCuenta();
        });
        btnRegistrar.setOnClickListener(View ->{
            CrearCuenta();
        });
    }

    public void IngresarCuenta(){
        String email= txtEmail.getText().toString();
        String password= txtPassword.getText().toString();
        LottieAnimationView carga = (LottieAnimationView) findViewById(R.id.carga);
        carga.setVisibility(View.VISIBLE);
        carga.playAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(StartActivity.this,"Ingrese un correo",Toast.LENGTH_SHORT).show();
                    txtEmail.requestFocus();
                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(StartActivity.this,"Ingrese una constraseña",Toast.LENGTH_SHORT).show();
                    txtPassword.requestFocus();
                }else{
                    //Verifica la base de datos en FireBase, si esta ok ingresa
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                LottieAnimationView logeo = (LottieAnimationView) findViewById(R.id.logeo);
                                logeo.setVisibility(View.VISIBLE);
                                logeo.playAnimation();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(StartActivity.this,"Bienvenid@",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(StartActivity.this,HomeActivity.class));
                                    }
                                }, 25*100);
                            }else {
                                LottieAnimationView nologeo = (LottieAnimationView) findViewById(R.id.nologeo);
                                nologeo.setVisibility(View.VISIBLE);
                                nologeo.playAnimation();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(StartActivity.this,"Usuario o contraseña incorrecto/s",Toast.LENGTH_SHORT).show();
                                    }
                                }, 1*1000);
                                Log.w("TAG","Error",task.getException());
                            }
                        }
                    });
                }
            }
        }, 1*1000);
    }

    public void CrearCuenta(){
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }
}