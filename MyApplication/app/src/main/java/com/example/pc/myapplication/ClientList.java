package com.example.pc.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientList extends AppCompatActivity implements ValueEventListener, AdapterView.OnItemClickListener {

    TextView textView;
    ListView listView;
    List<AddUserRead> addUserReadList;
    List<ClientDetails> clientlist;
    String email,pass,key;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        email = this.getIntent().getExtras().getString("email");
        key = this.getIntent().getExtras().getString("key");
        textView = (TextView) this.findViewById(R.id.textView);
        textView.setText("Welcome: "+email);
        //Toast.makeText(this, "Key is: "+key, Toast.LENGTH_SHORT).show();
        listView = (ListView) this.findViewById(R.id.fetchclientlistview);
        listView.setOnItemClickListener(this);
        addUserReadList = new ArrayList<>();
        clientlist = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("clientsDB").child(key);
        reference.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        clientlist.clear();
        for(DataSnapshot mysnapshot:dataSnapshot.getChildren()){
            ClientDetails clientDetails=mysnapshot.getValue(ClientDetails.class);
            clientlist.add(clientDetails);
        }
        listView.setAdapter(new ClientListView(ClientList.this,clientlist));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ClientDetails clientDetails = clientlist.get(position);
        String clientnames = clientDetails.getClientnames();
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("clientname",clientnames);
        startActivity(intent);
    }
}
