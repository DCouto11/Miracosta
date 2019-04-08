package com.miracosta;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class VisorImagen extends Activity {

    ImageView imagen;
    Button visto;
    TextView titulo,cuerpo;
    Button FakePositive, NotPerson, Call,swapImg;
    boolean vista = true;
    String url_alerta, sector_camara;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

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
        String txtTitulo = getIntent().getExtras().getString("tituloCaso");
        String txtCuerpo = getIntent().getExtras().getString("bodyCaso");

        sector_camara = txtTitulo;
        url_alerta = urlImagen;

        Picasso.with(getApplicationContext()).invalidate("");
        Picasso.with(this)
                .load(urlImagen)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imagen);
        vista = true;

        titulo.setText(txtTitulo);
        cuerpo.setText(txtCuerpo);

        visto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if(vista==true){
                    StorageReference islandRef = storageRef.child("playas/"+sector_camara+".png");
                    islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(getApplicationContext())
                                    .load(uri)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .into(imagen);
                        }
                    });
                    vista=false;
                }
                else{
                    Picasso.with(getApplicationContext())
                            .load(url_alerta)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(imagen);
                    vista=true;
                }
            }
        });
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }
}
