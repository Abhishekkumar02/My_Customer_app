package com.example.pc.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText txtlogin,txtpass,txtusername;
    Button btnlogin;
    Spinner spinner;
    FirebaseAuth auth;
    String key;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtlogin = (EditText)this.findViewById(R.id.txtlogin);
        txtpass = (EditText)this.findViewById(R.id.txtpassword);
        txtusername = (EditText)this.findViewById(R.id.txtloginusername);
        btnlogin = (Button)this.findViewById(R.id.btnlogin);
        spinner = (Spinner)this.findViewById(R.id.spinnerselect);
        requestQueue = Volley.newRequestQueue(this);
        btnlogin.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        if(btnlogin == v){
            if(txtlogin.getText().toString().toLowerCase().equals("admin") && txtpass.getText().toString().equals("admin")){
                if(spinner.getSelectedItem().toString().equals("Administrator")){
                  startActivity(new Intent(this,AddUsers.class));
                }
                else{
                    Toast.makeText(this, "Invalid Selection", Toast.LENGTH_SHORT).show();
                }
            }
            else if(spinner.getSelectedItem().toString().equals("User")) {
                final String email = txtlogin.getText().toString();
                final String pass = txtpass.getText().toString();
                final String username = txtusername.getText().toString();
                auth.signInWithEmailAndPassword(email, pass).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Toast.makeText(Login.this, "success", Toast.LENGTH_SHORT).show();
                                    JsonObjectRequest jsonObjectRequest = new
                                            JsonObjectRequest("https://fir-cloudmessagingprac.firebaseio.com/.json", null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                String email2 = response.getJSONObject("usersDB").getJSONObject(username).getString("email");
                                                if (email2.equals(email)) {
                                                    key = response.getJSONObject("usersDB").getJSONObject(username).getString("username");
                                                    Intent intent = new Intent(getApplicationContext(), ClientList.class);
                                                    intent.putExtra("email",email);
                                                    intent.putExtra("key", key);
                                                    startActivity(intent);
                                                    //Toast.makeText(Login.this, "Key is :" + key, Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(Login.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    requestQueue.add(jsonObjectRequest);


                                }
                            }
                        }).
                        addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error:  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else {
                Toast.makeText(this, "Invalid user or pass", Toast.LENGTH_SHORT).show();
                txtlogin.setText("");
                txtpass.setText("");
                txtlogin.requestFocus();
            }
        }
    }
}
