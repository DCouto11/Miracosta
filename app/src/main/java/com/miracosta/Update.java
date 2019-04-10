package com.miracosta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Update extends AppCompatActivity {
    private static final String TAG = "Miracosta";
    Spinner ciudades;
    Button incluir,excluir,actualizar;
    String[] cities;
    Map<String, String> eleccion = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        ciudades = findViewById(R.id.spin_ciudades);
        incluir = findViewById(R.id.btn_include);
        excluir = findViewById(R.id.btn_exclude);
        actualizar = findViewById(R.id.btn_modify);

        SharedPreferences setCiudades = getSharedPreferences("ciudades", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editorCiudades = setCiudades.edit();
        editorCiudades.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("camaras")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String[] cadena = document.getId().split("_");
                                editorCiudades.putString(cadena[0],"1");
                                editorCiudades.apply();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        Map<String, ?> predef = setCiudades.getAll();
        Log.d(TAG,"Numero: "+predef.size());

        String citieS="";
        for (String key : predef.keySet()) {
            citieS = citieS.concat(key+" ");
        }
        cities = citieS.trim().split(" ");
        ArrayList<String> spinner1 = new ArrayList<>();
        spinner1.addAll(Arrays.asList(cities));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Update.this, R.layout.support_simple_spinner_dropdown_item,spinner1);
        ciudades.setAdapter(adapter1);

        incluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ciudadIn = ciudades.getSelectedItem().toString();
                eleccion.put(ciudadIn,"1");
                Log.d(TAG, "Entra "+ciudadIn);
                Log.d(TAG, "Numero "+eleccion.size());
            }
        });
        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ciudadOut = ciudades.getSelectedItem().toString();
                eleccion.remove(ciudadOut);
                Log.d(TAG, "Sale "+ciudadOut);
                Log.d(TAG, "Numero "+eleccion.size());
            }
        });
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                        String[] cadena = document.getId().split("_");
                                        if(eleccion.keySet().contains(cadena[0])){
                                            EditorPlayas.putString(cadena[1],"1");
                                            EditorPlayas.apply();
                                            EditorCamaras.putString(cadena[1]+"_"+cadena[2],"1");
                                            EditorCamaras.apply();
                                        }
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                Toast.makeText(getApplicationContext(),"Actualización completa",Toast.LENGTH_LONG).show();
                startActivity(new Intent(Update.this, MainActivity.class));
            }
        });
    }
}

