package com.example.pc.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AssignClient extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    DatabaseReference databaseReference;
    TextView textView;
    EditText editText;
    Button button;
    List<ClientDetails> listclientdetails;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_client);
        textView = (TextView) this.findViewById(R.id.lblusername);
        //String key = this.getIntent().getExtras().getString("key");
        String name = this.getIntent().getExtras().getString("username");
        textView.setText("Username is:  " + name);
        databaseReference = FirebaseDatabase.getInstance().getReference("clientsDB").child(name);
        databaseReference.addValueEventListener(this);
        editText = (EditText) this.findViewById(R.id.txtaddclient);
        button = (Button) this.findViewById(R.id.btnaddclient);
        listView = (ListView) this.findViewById(R.id.listviewclients);
        button.setOnClickListener(this);
        listclientdetails = new ArrayList<>();
    }
    @Override
    public void onClick(View v) {
        String key = databaseReference.push().getKey();
        String clientnames = editText.getText().toString();
        databaseReference.child(key).setValue(new ClientDetails(key,clientnames)).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AssignClient.this, "Client Assign Successfully", Toast.LENGTH_SHORT).show();
                            editText.setText("");
                        }
                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AssignClient.this, "Error :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
     listclientdetails.clear();
        for(DataSnapshot mysnapshot:dataSnapshot.getChildren()){
            ClientDetails cd = mysnapshot.getValue(ClientDetails.class);
            listclientdetails.add(cd);
        }
        listView.setAdapter(new ClientListView(this,listclientdetails));

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
