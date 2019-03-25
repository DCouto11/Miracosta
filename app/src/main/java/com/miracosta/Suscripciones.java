package com.miracosta;

import android.content.Context;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Suscripciones extends AppCompatActivity {

    ArrayList<String> suscripciones = new ArrayList<>();
    Spinner spinnerPlayas, spinnerCamaras;
    ImageView imagenPlaya;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suscripciones);
        final Context context = this;
        final String[] item = new String[1];
        item[0] = "";
        spinnerPlayas = findViewById(R.id.spin_playas);
        spinnerCamaras = findViewById(R.id.spin_camaras);
        imagenPlaya = findViewById(R.id.img_playas);

        if(!suscripciones.isEmpty()){
            String lista=" ";
            for(String a: suscripciones) {
                FirebaseMessaging.getInstance().subscribeToTopic(a);
                lista += a + " ";
            }
            Toast.makeText(getApplicationContext(),"Suscrito a: "+lista,Toast.LENGTH_LONG).show();
        }

        spinnerPlayas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] camaras = getResources().getStringArray(R.array.camaras);
                String[] playas = getResources().getStringArray(R.array.playas);
                item[0] = playas[position];
                /*
                *Carga de imágenes en estático.
                *String imagenMapa = item[0];
                *Context context = imagenPlaya.getContext();
                *int id1 = context.getResources().getIdentifier(imagenMapa, "drawable", context.getPackageName());
                *imagenPlaya.setImageResource(id1);
                *
                */

                //Carga de imágenes via BBDD
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
                if(!suscripciones.contains(topicAltas)) {
                    suscripciones.add(topicAltas);
                }
                String lista=" ";
                for(String a: suscripciones) {
                    lista += a + " ";
                }
                Toast.makeText(getApplicationContext(),"Suscrito a: "+lista,Toast.LENGTH_LONG).show();

            }
        });

        findViewById(R.id.btn_unsubscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topicBajas = spinnerCamaras.getSelectedItem().toString();
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topicBajas);
                if(suscripciones.contains(topicBajas))
                    suscripciones.remove(topicBajas);
                String lista=" ";
                for(String a: suscripciones) {
                    lista += a + " ";
                }
                Toast.makeText(getApplicationContext(),"Suscrito a: "+lista,Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.btn_backMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lista=" ";
                for(String a: suscripciones) {
                    lista += a + " ";
                }
                Intent i = new Intent(Suscripciones.this, MainActivity.class);
                /*SharedPreferences suscripciones = getPreferences(context.MODE_PRIVATE);
                SharedPreferences.Editor editor = suscripciones.edit();
                editor.putString("MiDato", lista);
                editor.apply();
                */
                startActivity(i);
                finish();
            }
        });
    }
}
