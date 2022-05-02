package com.example.damxat.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.damxat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Es marquen tots els camps que es poden editar i s'assigna l'autentificació de firebase
        auth = FirebaseAuth.getInstance();

        Button loginButton = findViewById(R.id.groupAdd);
        EditText loginEmail = findViewById(R.id.groupName);
        EditText loginPassword = findViewById(R.id.loginPassword);

        //Comentar
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                //s'activa el procès de registre, entrant en cada camp el valor corresponent
                //comprobant que no és null i si falla ho mostra per pantalla com a toast
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                }else{
                    //autentifica els camps de email i contrasenya
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //si és correcte segueix
                            if(task.isSuccessful()){
                                //Crea un nou intent i passa al Main activity (classe), si falla ho mostra
                                //per pantalla com a toast
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}