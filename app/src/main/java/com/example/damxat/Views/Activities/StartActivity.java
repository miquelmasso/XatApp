package com.example.damxat.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.damxat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
//Inicialitzacio de firebaseUser
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        //Comproba que tot estigui correcte i hi hagi usuari, per començar i inicialitza l'Intent
        //si tot esta correcta comença
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button startLogin = findViewById(R.id.startLogin);
        Button startRegister = findViewById(R.id.startRegister);

        //Es mou a la pantalla de activity_start, on hi ha els botons startLogin i startRegister
        startLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Quan s'activa el botó de login va a Intent de l'Activity(pantalla) de "intentLogin"
                Intent intentLogin = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intentLogin);
            }
        });

        //Igual que abans, espera que s'activi un botó, en aquest cas nomès queda el de register
        startRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Quan s'activa el botó de register va a Intent de l'Activity(pantalla) de "intentRegister"
                Intent intentRegister = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
            }
        });
    }
}