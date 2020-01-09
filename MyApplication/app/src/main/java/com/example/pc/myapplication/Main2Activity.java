package com.example.pc.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements ValueEventListener {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    DatabaseReference databaseReference;
    ProgressDialog dialog;
    List<CustomerDetails> customerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        databaseReference = FirebaseDatabase.getInstance().getReference("Customers");
        databaseReference.addValueEventListener(this);
        recyclerView = (RecyclerView)this.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading details please wait...");
        dialog.show();
        customerList = new ArrayList<>();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        customerList.clear();
        for(DataSnapshot mysnapshot:dataSnapshot.getChildren()) {
            CustomerDetails customerDetails = mysnapshot.getValue(CustomerDetails.class);
            customerList.add(customerDetails);
        }
        adapter = new CustomerListView(this,customerList);
        recyclerView.setAdapter(adapter);
        dialog.dismiss();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
