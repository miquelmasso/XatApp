package com.example.damxat.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.damxat.R;
import com.example.damxat.Views.Fragments.GroupsFragment;
import com.example.damxat.Views.Fragments.MyXatsFragment;
import com.example.damxat.Views.Fragments.UserFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.i("miquel", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.i("miquel", token);
                        setToken(token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyXatsFragment()).commit();

        //reconeix l'usuari conectant-ho amb el firebase,
        //una mica de personalització el toolbar
        //hi ha el botom navigation per cambiar de pantalla a sota amb els diferents camps
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.inflateMenu(R.menu.top_menu);


        BottomNavigationView bottomNav = findViewById(R.id.main_menu);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.nav_xats:
                    selectedFragment = new MyXatsFragment();
                    break;

                case R.id.nav_group:
                    selectedFragment = new GroupsFragment();
                    break;

                case R.id.nav_users:
                    selectedFragment = new UserFragment();

                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        });
    }

    private void status(String status){
        //mostra els usuaris i l'estat dels mateixos respecte el HashMap
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        ref.updateChildren(hashMap);
    }

    private void setToken(String token){
        //mostra els usuaris i l'estat dels mateixos respecte el HashMap
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        ref.updateChildren(hashMap);
    }

    //Funcions per marcar l'estat onResume
    @Override
    protected void onResume(){
        super.onResume();
        status("online");
    }

    //Funcions per marcar l'estat onPause
    @Override
    protected void onPause(){
        super.onPause();
        status("offline");
    }

    //bool de mini Menu superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    //bool per marcar l'item que s'ha sel·leccionat i acaba la sessió
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("TESTMENU", "hola" + item.getItemId());
        switch(item.getItemId()){
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                finish();
                return true;
        }
        return false;
    }
}