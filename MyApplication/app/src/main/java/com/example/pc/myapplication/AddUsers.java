package com.example.pc.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddUsers extends AppCompatActivity implements View.OnClickListener, ValueEventListener, AdapterView.OnItemClickListener {

    FloatingActionButton floatingActionButton;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    List<AddUserRead> listadduserread;
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);
        floatingActionButton = (FloatingActionButton)this.findViewById(R.id.floatingActionButton2);
        listview = (ListView)this.findViewById(R.id.listView);
        listview.setOnItemClickListener(this);
        floatingActionButton.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usersDB");
        databaseReference.addValueEventListener(this);
        listadduserread = new ArrayList<>();
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.logout, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_logout_btn:
                logOut();
                return true;
            default:
                return false;


        }
    }
    private void logOut () {


        auth.signOut();
        sendToLogin();
    }

    private void sendToLogin () {

        Intent loginIntent = new Intent(AddUsers.this, Login.class);
        startActivity(loginIntent);
        finish();

    }

    @Override
    public void onClick(View v) {
        final EditText txtaddemail,txtaddpass,txtaddusername;
        Button btnuser;
        final LayoutInflater layoutInflater = this.getLayoutInflater();
        View view =layoutInflater.inflate(R.layout.add_user_dialog,null,false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Users");
        builder.setMessage("Click on add user button and save records");
        builder.setView(view);
        txtaddemail = (EditText) view.findViewById(R.id.txtaddemail);
        txtaddpass = (EditText) view.findViewById(R.id.txtaddpass);
        txtaddusername = (EditText) view.findViewById(R.id.txtaddusername);
        btnuser = (Button)view.findViewById(R.id.btnaddlogin);
        btnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = txtaddemail.getText().toString();
                final String pass = txtaddpass.getText().toString();
                final String username = txtaddusername.getText().toString();
                auth.createUserWithEmailAndPassword(email,pass).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //String key = databaseReference.push().getKey();
                                    AddUserRead read = new AddUserRead(username,email,pass);
                                    databaseReference.child(username).setValue(read).
                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(AddUsers.this, "User Added..", Toast.LENGTH_SHORT).show();
                                                        txtaddemail.setText("");
                                                        txtaddusername.setText("");
                                                        txtaddpass.setText("");
                                                    }
                                                }
                                            }).
                                            addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AddUsers.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        }).
                        addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddUsers.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        listadduserread.clear();
        for (DataSnapshot mysnapshot : dataSnapshot.getChildren()) {
            AddUserRead read = mysnapshot.getValue(AddUserRead.class);
            listadduserread.add(read);
        }
        UserListView userlist = new UserListView(this, listadduserread);
        listview.setAdapter(userlist);
    }
    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final AddUserRead read = listadduserread.get(position);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Assign Client to  " + read.getUsername());
        builder.setPositiveButton("Assign Client", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), AssignClient.class);
                //intent.putExtra("key",key);
                intent.putExtra("username",read.getUsername());
                startActivity(intent);
            }
        });
        builder.setNeutralButton("Delete User", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference dr1 = FirebaseDatabase.getInstance().getReference("usersDB").child(read.getUsername());
                DatabaseReference dr2 = FirebaseDatabase.getInstance().getReference("clientsDB").child(read.getUsername());
                dr1.removeValue();
                dr2.removeValue();
                Toast.makeText(AddUsers.this, "User deleted sucessfully", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
