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
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;




/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, ValueEventListener, AdapterView.OnItemClickListener {
    TextView textView;
    ListView listView;
    List<AddUserRead> addUserReadList;
    List<ClientDetails> clientlist;
    String email,pass,key;
    DatabaseReference reference;

    private static final int STORAGE_PERMISSION_CODE = 1;
    private EditText txtName,txtId,txtPhone,txtPhone2,txtCity,txtAddress,txtDevice,txtAge,txtDate,txtNotes,txtPrice,txtPaymentNumber,txtPaymentMethod,txtNote1,txtNote2,txtNote3,txtNote4,txtNote5;
    Button btnSave;
    DatabaseReference databaseReference;
    ProgressDialog dialog;
    LinearLayout linearLayout;
    Button btnS;
    Bitmap bitmap;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("CustomersDB");
        key=getActivity().getIntent().getExtras().getString("clientname");
        txtName = (EditText) view.findViewById(R.id.txtName);
        txtName.setText(key);
        txtId = (EditText) view.findViewById(R.id.txtID);
        txtPhone = (EditText) view.findViewById(R.id.txtPhone);
        txtPhone2 = (EditText) view.findViewById(R.id.txtPhone2);
        txtCity = (EditText) view.findViewById(R.id.txtCIty);
        txtAddress = (EditText) view.findViewById(R.id.txtAddress);
        txtDevice = (EditText) view.findViewById(R.id.txtDevice);
        txtAge = (EditText) view.findViewById(R.id.txtAge);
        txtDate = (EditText)view.findViewById(R.id.txtDate);
        txtNotes = (EditText) view.findViewById(R.id.txtNotes);
        txtPrice = (EditText) view.findViewById(R.id.txtPrice);
        txtPaymentNumber = (EditText) view.findViewById(R.id.txtPaymentNumber);
        txtPaymentMethod = (EditText) view.findViewById(R.id.txtPaymentMethod);
        txtNote1 = (EditText) view.findViewById(R.id.txtNote1);
        txtNote2 = (EditText)view.findViewById(R.id.txtNote2);
        txtNote3 = (EditText) view.findViewById(R.id.txtNote3);
        txtNote4 = (EditText) view.findViewById(R.id.txtNote4);
        txtNote5 = (EditText)view.findViewById(R.id.txtNote5);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        txtDate.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnS = (Button) view.findViewById(R.id.btnS);
        linearLayout = (LinearLayout) view.findViewById(R.id.print);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }
        btnS.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                bitmap = loadBitmapFromView(linearLayout, linearLayout.getWidth(), linearLayout.getHeight());
                createPdf();
            }
        });
        dialog = new ProgressDialog(getContext());
        return view;
    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(getContext())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdf(){
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);
        File direct = new File(Environment.getExternalStorageDirectory() + "/CRM");
        if (!direct.exists()) {
            File pdf = new File("/sdcard/CRM/");
            pdf.mkdirs();
        }
        File directt = new File(Environment.getExternalStorageDirectory() + "/sdcard/CRM");
        if (!directt.exists()) {
            File pdf = new File("/sdcard/CRM/CRMPDF/");
            pdf.mkdirs();
        }

        File file = new File("/sdcard/CRM/CRMPDF/", txtName.getText().toString()+ ".pdf");
        if (file.exists()) {
            file.delete();
        }

        //String targetPdf ="//sdcard"+txtName.getText().toString()+".pdf";
        try {
            document.writeTo(new FileOutputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        document.close();
        Toast.makeText(getContext(), "PDF is created!!!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        if(v == btnSave) {
            //String key = databaseReference.push().getKey();
            String name = txtName.getText().toString();
            String id = txtId.getText().toString();
            String phone = txtPhone.getText().toString();
            String phone2 = txtPhone2.getText().toString();
            String city = txtCity.getText().toString();
            String address = txtAddress.getText().toString();
            String device = txtDevice.getText().toString();
            String age = txtAge.getText().toString();
            String date = txtDate.getText().toString();
            String notes = txtNotes.getText().toString();
            String price = txtPrice.getText().toString();
            String  payment_number = txtPaymentNumber.getText().toString();
            String payment_method = txtPaymentMethod.getText().toString();
            String note1 = txtNote1.getText().toString();
            String note2 = txtNote2.getText().toString();
            String note3 = txtNote3.getText().toString();
            String note4 = txtNote4.getText().toString();
            String note5 = txtNote5.getText().toString();
            dialog.setMessage("Saving data..please wait");
            dialog.show();
            CustomerDetails cd = new CustomerDetails(key, name, id, phone, phone2, city, address, device, age, date, notes, price,payment_number,payment_method,note1,note2,note3,note4,note5);
            databaseReference.child(key).setValue(cd).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Data Stored", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).
                    addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        if(v == txtDate){
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog =new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    int m = month+1;
                    txtDate.setText(dayOfMonth+" - "+m+" - "+year);
                }
            },year, month, dayofmonth);
            datePickerDialog.show();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
