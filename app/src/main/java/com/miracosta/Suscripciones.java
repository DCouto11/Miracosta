package com.miracosta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;

public class Suscripciones extends AppCompatActivity {

    private static final String TAG = " ";
    ArrayList<String> suscripciones = new ArrayList<>();
    Spinner spinnerPlayas, spinnerCamaras;
    ImageView imagenPlaya;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suscripciones);
        final Context context = this;
        SharedPreferences sharedPre = getSharedPreferences("camaras", context.MODE_PRIVATE);
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
                String imagenMapa = item[0];
                Context context = imagenPlaya.getContext();
                int id1 = context.getResources().getIdentifier(imagenMapa, "drawable", context.getPackageName());
                imagenPlaya.setImageResource(id1);


                ArrayList<String> elegidas = new ArrayList<>();
                for (String e : camaras) {
                    if (e.contains(item[0])){
                        elegidas.add(e);
                    }
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Suscripciones.this, R.layout.support_simple_spinner_dropdown_item,elegidas);

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
                SharedPreferences suscripciones = getPreferences(context.MODE_PRIVATE);
                SharedPreferences.Editor editor = suscripciones.edit();
                editor.putString("MiDato", lista);
                editor.commit();
                startActivity(i);
                finish();
            }
        });
    }
}
