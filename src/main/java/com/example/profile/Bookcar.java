package com.example.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Bookcar extends AppCompatActivity implements View.OnClickListener {
    private EditText ed1,ed2,ed3,ed4,ed5;
    private TextView txt1,txt2,txt3,txt4,txt5;
    private CircleImageView img1;
    private FirebaseDatabase dB;
    private DatabaseReference dR;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    public static final String DATABASE_PATH_FB = "Customer";
    private FirebaseUser user;
private int mYear, mMonth, mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcar);
        firebaseAuth = FirebaseAuth.getInstance();
        dB = FirebaseDatabase.getInstance();
        dR = dB.getReference(DATABASE_PATH_FB);
        user = firebaseAuth.getCurrentUser();

        //CHECKING USER LOGIN
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user == null){
                    finish();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }
            }
        };


        //DEFINE LAYOUT OBJECTS
        ed1 = (EditText) findViewById(R.id.editText8);
        ed2 = (EditText) findViewById(R.id.editText10);
        ed3 = (EditText) findViewById(R.id.editText7);
        ed4 = (EditText) findViewById(R.id.editText6);
        ed4.setOnClickListener( this);

        ed5 = (EditText) findViewById(R.id.editText5);
        img1 = (CircleImageView) findViewById(R.id.imageView3);
        txt1 = (TextView) findViewById(R.id.exitBtn);
        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        txt2 = (TextView) findViewById(R.id.textView10);
        txt3 = (TextView) findViewById(R.id.textView11);
        txt4 = (TextView) findViewById(R.id.textView12);
        txt5 = (TextView) findViewById(R.id.textView);

        /**
         * THIS METHOD USE FOR RETRIEVE DATA AND DISPLAY IT TO THE APPLICATION
         */
        //GET INTENT
        Intent i = this.getIntent();

        //RECEIVE DATA
        String pict = i.getStringExtra("pict");
        String name = i.getExtras().getString("name");
        String merk = i.getExtras().getString("merk");
        String plat = i.getExtras().getString("plat");
        String contact = i.getExtras().getString("contact");

        //BIND DATA
        Picasso.get().load(pict).resize(280,280).into(img1);
        txt2.setText(name);
        txt3.setText(merk);
        txt4.setText(plat);
        txt5.setText(contact);
    }

    public void Cancel(View v){
        finish();
        startActivity(new Intent (Bookcar.this, selectCar.class));
    }

    //BOOKING BUTTON
    public void rentNow(View v){
        String fullname = ed1.getText().toString().trim();
        String contact = ed2.getText().toString().trim();
        String usetime = ed3.getText().toString().trim();
        String bookingTime = ed4.getText().toString().trim();
        String licenceNumber = ed5.getText().toString().trim();

        if (TextUtils.isEmpty(fullname)){
            ed1.setError("Field cannot be empty");
        }
        if (TextUtils.isEmpty(contact)){
            ed2.setError("Field cannot be empty");
        }
        if (TextUtils.isEmpty(usetime)){
            ed3.setError("Field cannot be empty");
        }
        if (TextUtils.isEmpty(bookingTime)){
            ed4.setError("Field cannot be empty");
        }
        if (TextUtils.isEmpty(licenceNumber)){
            ed5.setError("Field cannot be empty");
        }

        if (!TextUtils.isEmpty(fullname)
                && !TextUtils.isEmpty(contact)
                && !TextUtils.isEmpty(usetime)
                && !TextUtils.isEmpty(bookingTime)
                && !TextUtils.isEmpty(licenceNumber)){

            // UPLOAD DATA TO FIREBASE DATABASE ON TABLE CUSTOMER
            String savedID = dR.push().getKey();
            dR = dB.getReference(DATABASE_PATH_FB);
            Customer customer = new Customer(fullname,contact,usetime,bookingTime,licenceNumber);
            dR.child(savedID).setValue(customer);
            Toast.makeText(this, "Succesful Book",Toast.LENGTH_LONG).show();

            //INTENT ACTIVITY TO PAGEMAIN
            finish();
            Intent intent = new Intent(Bookcar.this, MainActivity.class);
            startActivity(intent);
        }
    }

    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener !=null){
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    public void onClick(View view) {
    if(view == ed4){
        final Calendar c = Calendar.getInstance();
        mYear= c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay= c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog =new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int dayOfMonth, int monthOfYear, int year) {
                ed4.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

            }
        }, mYear, mMonth, mDay);
    datePickerDialog.show();
    }
    }
    }


