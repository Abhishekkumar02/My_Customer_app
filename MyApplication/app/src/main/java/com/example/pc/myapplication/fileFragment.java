package com.example.pc.myapplication;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class fileFragment extends Fragment implements View.OnClickListener{

    FloatingActionButton fab3,fab4;
    Uri filepath;
    TextView textViewpath;
    AlertDialog.Builder builder;
    public fileFragment() {
        // Required empty public constructor
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK && data!=null){
            filepath = data.getData();
            textViewpath.setText(filepath.getPath());
        }
        if(requestCode == 124 && resultCode == RESULT_OK && data!=null){
            filepath=data.getData();
            //builder.show();
            //textViewpath.setText(filepath.getEncodedPath());
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_file_upload, container, false);
        fab3 = (FloatingActionButton)view.findViewById(R.id.floatingActionButton3);
        fab4 = (FloatingActionButton)view.findViewById(R.id.floatingActionButton4);
        fab3.setOnClickListener( this);
        fab4.setOnClickListener( this);
        builder = new AlertDialog.Builder(getActivity());
        return view;
    }
    public void onClick(View v) {

        if(v == fab3){
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.fragment_file,null,false);
            builder.setView(view);
            Button btngallery = (Button)view.findViewById(R.id.buttonopengallery);
            textViewpath = (TextView)view.findViewById(R.id.textViewfilepath);
            btngallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Choose Files..."),123);
                }
            });
            EditText editTextDes = (EditText)view.findViewById(R.id.editTextDescription);
            builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
        if(v == fab4){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,124);
        }
    }
}

