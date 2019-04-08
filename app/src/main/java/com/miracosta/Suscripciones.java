package com.miracosta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Suscripciones extends AppCompatActivity {
    private static final String TAG = "Miracosta";
    Spinner spinnerPlayas, spinnerCamaras;
    ImageView imagenPlaya;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    String[] camaras, playas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suscripciones);
        final String[] item = new String[1];
        item[0] = "";
        spinnerPlayas = findViewById(R.id.spin_playas);
        spinnerCamaras = findViewById(R.id.spin_camaras);
        imagenPlaya = findViewById(R.id.img_playas);
        SharedPreferences prefPlayas = getSharedPreferences("playas", Context.MODE_PRIVATE);
        SharedPreferences prefCamaras = getSharedPreferences("camaras", Context.MODE_PRIVATE);
        Map<String, ?> pPlayas = prefPlayas.getAll();
        Map<String, ?> pCamaras = prefCamaras.getAll();
        Log.d(TAG,"Numero de playas en suscripción:  "+pPlayas.size());
        Log.d(TAG,"Numero de cámaras en suscripción:  "+pCamaras.size());
        String playaS="";
        for (String key : pPlayas.keySet()) {
            playaS = playaS.concat(key+" ");
        }
        String camaraS="";
        for (String key : pCamaras.keySet()) {
            camaraS = camaraS.concat(key+" ");
        }
        camaras = camaraS.trim().split(" ");
        playas = playaS.trim().split(" ");

        ArrayList<String> spinner1 = new ArrayList<>();
        spinner1.addAll(Arrays.asList(playas));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Suscripciones.this, R.layout.support_simple_spinner_dropdown_item,spinner1);
        spinnerPlayas.setAdapter(adapter1);

        spinnerPlayas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item[0] = playas[position];
                StorageReference islandRef = storageRef.child("playas/"+item[0]+".png");
                islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getApplicationContext())
                                .load(uri)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(imagenPlaya);
                    }
                });
                ArrayList<String> elegidas = new ArrayList<>();
                for (String e : camaras) {
                    if (e.contains(item[0])){
                        elegidas.add(e);
                    }
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Suscripciones.this, R.layout.support_simple_spinner_dropdown_item,elegidas);
                spinnerCamaras.setAdapter(adapter1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        findViewById(R.id.btn_subscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topicAltas = spinnerCamaras.getSelectedItem().toString();
                FirebaseMessaging.getInstance().subscribeToTopic(topicAltas);
                SharedPreferences preferences = getSharedPreferences("altas",Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_Editor = preferences.edit();
                Obj_Editor.putString(topicAltas,"1");
                Obj_Editor.apply();
                Log.d(TAG, "Cámara nueva");
                Toast.makeText(getApplicationContext(),"Suscrito a "+topicAltas,Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.btn_unsubscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topicBajas = spinnerCamaras.getSelectedItem().toString();
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topicBajas);
                SharedPreferences preferences = getSharedPreferences("altas", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_Editor = preferences.edit();
                Obj_Editor.remove(topicBajas);
                Obj_Editor.apply();
                Log.d(TAG, "Cámara fuera");
                Toast.makeText(getApplicationContext(),"De baja de "+topicBajas,Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.btn_backMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Suscripciones.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
