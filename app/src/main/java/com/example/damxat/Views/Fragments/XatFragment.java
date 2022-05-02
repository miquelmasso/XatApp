package com.example.damxat.Views.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damxat.Adapter.RecyclerXatAdapter;
import com.example.damxat.Model.User;
import com.example.damxat.Model.Xat;
import com.example.damxat.Model.XatGroup;
import com.example.damxat.R;
import com.example.damxat.Views.Activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class XatFragment extends Fragment {

    private static final int RESULT_OK = -1;
    DatabaseReference ref;
    View view;
    FirebaseUser firebaseUser;
    String userid;
    Bundle bundle;
    Boolean isXatUser;
    ArrayList<Xat> arrayXats;
    ArrayList<String> arrayUsers;

    XatGroup group;
    String groupName;

    int RecordAudioRequestCode;
    ImageButton button;
    TextView text;

    public XatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_xat, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //conecta con firebase y llama a la clase getArguments que la asigna a un bundle
        //comprueba el string y si es correcto busca el usuario
        //si no es correcto reasigna el nombre de grupo
        bundle = getArguments();

        if(bundle.getString("type").equals("xatuser")){
            isXatUser = true;
            getUserXat();
        }else{
            isXatUser = false;
            groupName = bundle.getString("group");
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(groupName);
            readGroupMessages(groupName);
        }


        ImageButton btnMessage = view.findViewById(R.id.btnMessage);
        EditText txtMessage = view.findViewById(R.id.txtMessage);

        //hay dos botones, para la imagen y para el texto
        //asigna el mensage
        //si el mensaje NO esta vacío, lo envía, en caso contrario muestra un toast de error
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = txtMessage.getText().toString();

                if(!msg.isEmpty()){
                    sendMessage(firebaseUser.getUid(), msg, isXatUser);
                }else{
                    Toast.makeText(getContext(), "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                txtMessage.setText("");
            }
        });


        //voice recording

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
            }
        }

        button= (ImageButton)view.findViewById(R.id.ic_mic_off);
        button.setOnClickListener(imgButtonHandler);

        text = (TextView)view.findViewById(R.id.textView);




        return view;
    }

    //retorna el xat del usuario respecto su id
    public void getUserXat(){
        if(getArguments()!=null) {
            userid = bundle.getString("user");

            //se conecta el firebase com el id del usuario para tener la referencia
            ref = FirebaseDatabase.getInstance().getReference("Users").child(userid);

            //añade los valores a la referencia
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //crea un usuario y le asigna los valores de la snapshot
                    User user = dataSnapshot.getValue(User.class);

                    //se junta todo, el titulo con el usuario
                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(user.getUsername());

                    //lee el mensaje del usuario
                    readUserMessages();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }



    public void sendMessage(String sender, String message, boolean isXatUser){
        //metodo para enviar mensaje
        //primero comprueba el usuario para usar el firebase
        //si es correcto crea el xat con los valores recibidos
        //sino, s'assigna une grup base al ref i s'assinga el xat sense el receiver
        //comprueba tambien que el xat no esté vacío, y que el grupo no sea null
        if(isXatUser==true){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            String receiver = userid;
            Xat xat = new Xat(sender, receiver, message);
            ref.child("Xats").push().setValue(xat);
            //crear retrofit amb la url
            //cridar la api amb l'instance
        }else{
            ref = FirebaseDatabase.getInstance().getReference("Groups").child(groupName);

            Xat xat = new Xat(sender, message);

            if(arrayXats==null) {
                arrayXats = new ArrayList<Xat>();
                arrayXats.add(xat);
            }else{
                arrayXats.add(xat);
            }

            if(group.getUsers()==null){
                arrayUsers = new ArrayList<String>();
                arrayUsers.add(firebaseUser.getUid());
            }else{
                if(!group.getUsers().contains(firebaseUser.getUid())){
                    arrayUsers.add(firebaseUser.getUid());
                }
            }

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("xats", arrayXats);
            hashMap.put("users", arrayUsers);
            ref.updateChildren(hashMap);
        }
    }

    public void readUserMessages(){
        arrayXats = new ArrayList<>();

        //método para leer mensajes de usuarios
        //assigna el firebase de xats a la referencia
        ref = FirebaseDatabase.getInstance().getReference("Xats");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayXats.clear();

                //para cada snapshot se asigna el xat
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Xat xat = postSnapshot.getValue(Xat.class);
                    //comprueba el userid con el id de firebase y añade el xat
                    if(xat.getReceiver().equals(userid) && xat.getSender().equals(firebaseUser.getUid()) ||
                            xat.getReceiver().equals(firebaseUser.getUid()) && xat.getSender().equals(userid)){
                        arrayXats.add(xat);
                        Log.i("logTest",xat.getMessage());
                    }
                }

                //llama la clase para actualizar el reycler
                updateRecycler();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("damxat", "Failed to read value.", error.toException());
            }
        });
    }


    public void readGroupMessages(String groupName){

        //Comentar
        ref = FirebaseDatabase.getInstance().getReference("Groups").child(groupName);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                group = dataSnapshot.getValue(XatGroup.class);

                arrayXats = group.getXats();

                if(arrayXats!=null) {
                    updateRecycler();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("damxat", "Failed to read value.", error.toException());
            }
        });
    }

    public void updateRecycler(){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerXat);
        RecyclerXatAdapter adapter = new RecyclerXatAdapter(arrayXats, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    View.OnClickListener imgButtonHandler = new View.OnClickListener() {

        public void onClick(View v) {
            button.setImageResource(R.drawable.ic_mic_off);

            Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hola, digues quelcom!");

            startActivityForResult(speechRecognizerIntent,12);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(getContext(),"Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == RESULT_OK) {
            ArrayList<String> result=data.getStringArrayListExtra( RecognizerIntent.EXTRA_RESULTS );
            text.setText(result.get(0));
            button.setImageResource(R.drawable.ic_mic_off);
        }
    }
}