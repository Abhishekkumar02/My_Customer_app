package com.example.pc.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity  {
    private HomeFragment homeFragment;
    private SignFragment signFragment;
    private fileFragment fieFragment;
    BottomNavigationView mainbottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainbottomNav = (BottomNavigationView) findViewById(R.id.mainBottomNav);
        homeFragment = new HomeFragment();
        signFragment = new SignFragment();
        fieFragment = new fileFragment();

        initializeFragment();
        mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);

                switch (item.getItemId()) {

                    case R.id.bottom_action_home:

                        replaceFragment(homeFragment, currentFragment);
                        return true;

                    case R.id.bottom_action_account:

                        replaceFragment(signFragment, currentFragment);
                        return true;

                    case R.id.bottom_action_notif:

                        replaceFragment(fieFragment, currentFragment);
                        return true;

                    default:
                        return false;


                }

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.menu1:
                startActivity(new Intent(this,Main2Activity.class));
                return true;

            case R.id.menu2:
                Toast.makeText(this, "Menu 2 Clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu3:
                Toast.makeText(this, "Menu 3 Clicked", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    private void initializeFragment () {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.main_container, homeFragment);
        fragmentTransaction.add(R.id.main_container, signFragment);
        fragmentTransaction.add(R.id.main_container, fieFragment);

        fragmentTransaction.hide(signFragment);
        fragmentTransaction.hide(fieFragment);

        fragmentTransaction.commit();

    }

    private void replaceFragment (Fragment fragment, Fragment currentFragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == homeFragment) {

            fragmentTransaction.hide(signFragment);
            fragmentTransaction.hide(fieFragment);

        }

        if (fragment == signFragment) {

            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(fieFragment);

        }

        if (fragment == fieFragment) {

            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(signFragment);

        }
        fragmentTransaction.show(fragment);

        //fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }
}
