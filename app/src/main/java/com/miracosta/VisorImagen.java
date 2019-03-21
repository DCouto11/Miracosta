package com.miracosta;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class VisorImagen extends Activity {

    ImageView imagen;
    Button visto;
    TextView titulo,cuerpo;

    public void onResume(){
        super.onResume();
        setContentView(R.layout.activity_visor_imagen);

        visto = findViewById(R.id.btn_visto);
        imagen = findViewById(R.id.img_foto);
        titulo = findViewById(R.id.txt_title);
        cuerpo = findViewById(R.id.txt_body);

        String urlImagen = getIntent().getExtras().getString("imagenCaso");
        String txtTitulo = getIntent().getExtras().getString("tituloCaso");
        String txtCuerpo = getIntent().getExtras().getString("bodyCaso");

        double token;
        token=Math.random();

        Picasso.with(getApplicationContext()).invalidate("");
        Picasso.with(this)
                .load(urlImagen)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imagen);

        titulo.setText(txtTitulo);
        cuerpo.setText(txtCuerpo);

        visto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VisorImagen.this, MainActivity.class));
                getIntent().removeExtra("tituloCaso");
                getIntent().removeExtra("bodyCaso");
                getIntent().removeExtra("imagenCaso");
            }
        });
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }
}
