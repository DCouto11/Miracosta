package com.miracosta;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


public class VisorImagen extends Activity {

    ImageView imagen;
    Button visto;
    TextView titulo,cuerpo;
    Button FakePositive, NotPerson, Call,swapImg;
    boolean vista = true;
    String url_alerta, sector_camara;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "Miracosta";

    public void onResume(){
        super.onResume();
        setContentView(R.layout.activity_visor_imagen);

        visto = findViewById(R.id.btn_visto);
        imagen = findViewById(R.id.img_foto);
        titulo = findViewById(R.id.txt_title);
        cuerpo = findViewById(R.id.txt_body);
        FakePositive = findViewById(R.id.btn_FP);
        NotPerson = findViewById(R.id.btn_notPerson);
        Call = findViewById(R.id.btn_112);
        swapImg = findViewById(R.id.btn_swapImg);

        String urlImagen = getIntent().getExtras().getString("imagenCaso");
        final String txtTitulo = getIntent().getExtras().getString("tituloCaso");
        String txtCuerpo = getIntent().getExtras().getString("bodyCaso");

        sector_camara = txtTitulo;
        url_alerta = urlImagen;

        Picasso.with(getApplicationContext()).invalidate("");
        Picasso.with(this)
                .load(urlImagen)
                .into(imagen);
        vista = true;

        titulo.setText(txtTitulo);
        cuerpo.setText(txtCuerpo);

        visto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp time = new Timestamp(System.currentTimeMillis());
                Map<String, Object> alerta = new HashMap<>();
                alerta.put("fakePositive", false);
                alerta.put("notPerson", false);
                alerta.put("sector", txtTitulo);
                alerta.put("timestamp",time);
                alerta.put("url_imagen",url_alerta);
                db.collection("detecciones").document(txtTitulo+"_"+time)
                        .set(alerta)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
                startActivity(new Intent(VisorImagen.this, MainActivity.class));
            }
        });

        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri num = Uri.parse("tel:112");
                Intent i = new Intent(Intent.ACTION_DIAL, num);
                startActivity(i);
            }
        });
        swapImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vista){
                    StorageReference islandRef = storageRef.child("playas/"+sector_camara+".png");
                    islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(getApplicationContext())
                                    .load(uri)
                                    .into(imagen);
                        }
                    });
                    vista=false;
                }
                else{
                    Picasso.with(getApplicationContext())
                            .load(url_alerta)
                            .into(imagen);
                    vista=true;
                }
            }
        });
        FakePositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp time = new Timestamp(System.currentTimeMillis());
                Map<String, Object> alerta = new HashMap<>();
                alerta.put("fakePositive", true);
                alerta.put("notPerson", false);
                alerta.put("sector", txtTitulo);
                alerta.put("timestamp",time);
                alerta.put("url_imagen",url_alerta);
                db.collection("detecciones").document(txtTitulo+"_"+time)
                        .set(alerta)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
                startActivity(new Intent(VisorImagen.this, MainActivity.class));
            }
        });
        NotPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp time = new Timestamp(System.currentTimeMillis());
                Map<String, Object> alerta = new HashMap<>();
                alerta.put("fakePositive", false);
                alerta.put("notPerson", true);
                alerta.put("sector", txtTitulo);
                alerta.put("timestamp",time);
                alerta.put("url_imagen",url_alerta);
                db.collection("detecciones").document(txtTitulo+"_"+time)
                        .set(alerta)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
                startActivity(new Intent(VisorImagen.this, MainActivity.class));
            }
        });
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }
}
