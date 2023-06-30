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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AuthActivity extends AppCompatActivity {

    private EditText emailEditText, txtNombre, txtApellido, txtCelular, txtDni;
    private EditText passwordEditText;
    private Button signUpButton;
    private Button loginButton;

    //NOS BRINDA EL PAQUETE DE AUTENTICACIÓN FIRBASE
    FirebaseAuth mAuth;
    DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        //ANIMACIÓN
        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.registercheck);
        LottieAnimationView animationView2 = (LottieAnimationView) findViewById(R.id.carga);

        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // La animación ha comenzado
                animationView.setVisibility(View.VISIBLE); // Hacer que el elemento aparezca
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // La animación ha terminado
                animationView.setVisibility(View.GONE); // Hacer que el elemento desaparezca
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

        animationView2.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // La animación ha comenzado
                animationView2.setVisibility(View.VISIBLE); // Hacer que el elemento aparezca
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // La animación ha terminado
                animationView2.setVisibility(View.GONE); // Hacer que el elemento desaparezca
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

        //INSTANCIAS DE CADA UNO DE LOS TEXT,BUTTON, ETC
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtDni = findViewById(R.id.txtDni);
        txtCelular = findViewById(R.id.txtCelular);
        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(view -> {
            RegistrarCuenta();
        });
    }

    public void RegistrarCuenta() {
        String nombre = txtNombre.getText().toString();
        String apellido = txtApellido.getText().toString();
        String celular = txtCelular.getText().toString();
        String dni = txtDni.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        //VALIDACIÓN PARA EL USUARIO CUMPLIENDO LOS REQUISITOS
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(celular) || TextUtils.isEmpty(dni) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            LottieAnimationView animationView2 = (LottieAnimationView) findViewById(R.id.carga);
            animationView2.setVisibility(View.VISIBLE);
            animationView2.playAnimation();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AuthActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }, 2*1000);
            return; // Salir del método sin registrar el usuario
        } else {
            //Crea el usuario en Firebase Auth
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            //Envia correo de verificacion al usuario registrado
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(AuthActivity.this, "Se ha enviado un correo de verificación. Por favor, verifica tu cuenta.", Toast.LENGTH_LONG).show();
                                                Intent obj1=new Intent(AuthActivity.this,StartActivity.class);
                                                startActivity(obj1);
                                                finish();
                                            }
                                        }, 3*1000);
                                        //Toast.makeText(StartActivity.class,"Verifique e ingrese con sus credenciales",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AuthActivity.this, "No se pudo enviar el correo de verificación.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            // Datos para guardar en la base de datos
                            //MAP proporciona una estructura de datos que permite almacenar pares clave-valor
                            Map<String, Object> map = new HashMap<>();
                            map.put("nombre", nombre);
                            map.put("apellido", apellido);
                            map.put("celular", celular);
                            map.put("dni", dni);
                            map.put("email", email);
                            map.put("password", password);

                            String id = user.getUid();

                            //Guarda los datos del usuario en la base de datos
                            mDataBase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //ANIMACION
                                        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.registercheck);
                                        animationView.playAnimation();
                                        Toast.makeText(AuthActivity.this, "Registro exitoso. Por favor, verifica tu cuenta antes de iniciar sesión.", Toast.LENGTH_LONG).show();
                                        mAuth.signOut(); //Cerrar sesión
                                    } else {
                                        Toast.makeText(AuthActivity.this, "No se pudieron crear los datos correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        Log.w("TAG", "Error", task.getException());
                    }
                }
            });
        }
    }
}
