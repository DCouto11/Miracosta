package com.miracosta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SesionConfirmada extends AppCompatActivity {
    Button btn_cerrar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_confirmada);
        btn_cerrar = findViewById(R.id.btn_cerrarsesion);
        //Usuario autenticado
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.btn_suscripciones).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SesionConfirmada.this, Suscripciones.class));
                finish();
            }
        });
        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SesionConfirmada.this, MainActivity.class));
                finish();
            }
        });
    }
}
