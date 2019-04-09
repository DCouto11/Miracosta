package com.miracosta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Map;

public class SesionConfirmada extends AppCompatActivity {
    private static final String TAG = "Miracosta";
    Button btn_cerrar, btn_refresh;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion_confirmada);
        btn_cerrar = findViewById(R.id.btn_cerrarsesion);
        btn_refresh = findViewById(R.id.btn_update);

        SharedPreferences preferences = getSharedPreferences("altas", Context.MODE_PRIVATE);
        Map<String, ?> predef = preferences.getAll();
        String TAG="Miracosta";
        Log.d(TAG,"Numero de cámaras en suscripción:  "+predef.size());
        String altas="";
        for (String key : predef.keySet()) {
            FirebaseMessaging.getInstance().subscribeToTopic(key);
            altas = altas.concat(" "+key);
        }
        if (predef.size()>0)
            Toast.makeText(getApplicationContext(),"Suscrito a "+altas,Toast.LENGTH_LONG).show();
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
                logOff();
                startActivity(new Intent(SesionConfirmada.this, MainActivity.class));
                finish();
            }
        });
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar();
            }
        });
    }
    private void logOff(){
        SharedPreferences preferences = getSharedPreferences("altas", Context.MODE_PRIVATE);
        Map<String, ?> predef = preferences.getAll();
        for (String key : predef.keySet()) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(key);
        }

    }
    public void actualizar(){
        //Actualizar playas y camaras
        SharedPreferences prefPlayas = getSharedPreferences("playas", Context.MODE_PRIVATE);
        SharedPreferences prefCamaras = getSharedPreferences("camaras", Context.MODE_PRIVATE);
        final SharedPreferences.Editor EditorPlayas = prefPlayas.edit();
        final SharedPreferences.Editor EditorCamaras = prefCamaras.edit();
        EditorPlayas.clear();
        EditorPlayas.apply();
        EditorCamaras.clear();
        EditorCamaras.apply();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("camaras")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String playa = document.getData().get("playa").toString();
                                EditorPlayas.putString(playa,"1");
                                EditorPlayas.apply();
                                String sector = document.getData().get("sector").toString();
                                String result = playa+"_"+sector;
                                EditorCamaras.putString(result,"1");
                                EditorCamaras.apply();
                                Log.d(TAG, result);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        //Actualizar fotos de playas
        //Actualizar fotos de sectores
        Toast.makeText(getApplicationContext(),"Actualización completa",Toast.LENGTH_LONG).show();
    }




}
