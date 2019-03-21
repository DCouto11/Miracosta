package com.miracosta;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private static final String TAG =" " ;
    EditText mCorreo,mClave;
    Button mInicioSesion;

    //Variables Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCorreo = findViewById(R.id.txt_usuario);
        mClave =  findViewById(R.id.pw_contrasena);
        mInicioSesion = findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();
        mInicioSesion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email,password;
                email = mCorreo.getText().toString().trim();
                password = mClave.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this, "Introduzca su correo",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "Introduzca su contraseña",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    Intent intent = new Intent(MainActivity.this,SesionConfirmada.class);
                                    startActivity(intent);
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this, task.getException().toString().trim(),
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser usuario) {
        if(usuario != null){
            Intent intent = new Intent(MainActivity.this,SesionConfirmada.class);
            startActivity(intent);
            finish();
        }
    }
}
