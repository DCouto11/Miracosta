package com.miracosta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

public class Suscripciones extends AppCompatActivity {

    Spinner spinnerAltas,spinnerBajas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suscripciones);

        spinnerAltas = findViewById(R.id.spin_subscribe);
        spinnerBajas = findViewById(R.id.spin_unsubscribe);

        findViewById(R.id.btn_subscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topicAltas = spinnerAltas.getSelectedItem().toString();
                FirebaseMessaging.getInstance().subscribeToTopic(topicAltas);
                Toast.makeText(getApplicationContext(),"Suscrito a "+topicAltas,Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.btn_unsubscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topicBajas = spinnerBajas.getSelectedItem().toString();
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topicBajas);
                Toast.makeText(getApplicationContext(),"De baja de "+topicBajas,Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.btn_backMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Suscripciones.this, MainActivity.class));
                finish();
            }
        });
    }
}
