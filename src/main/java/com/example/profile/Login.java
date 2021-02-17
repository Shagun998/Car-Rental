package com.example.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    private EditText ed1, ed2;
    private Button txtForgot;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener mAuth;
    Button registration, Login ;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), signUp.class);
                    startActivity(intent);
                }
            }
        };
        firebaseAuth = FirebaseAuth.getInstance();
        ed1 = (EditText) findViewById(R.id.ed_username);
        ed2 = (EditText) findViewById(R.id.ed_password);
        txtForgot = (Button) findViewById(R.id.reset);
        registration = (Button) findViewById(R.id.registration);
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Reset.class);
                startActivity(intent);
                finish();
            }
        });
        registration.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent1 = new Intent(Login.this, signUp.class);
                startActivity(intent1);
                finish();
            }
        });
    }

    //LOGIN BUTTON
    public void LoginUser(View v) {
        String email = ed1.getText().toString().trim();
        String password = ed2.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            ed1.setError("Field cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ed2.setError("Field cannot be empty");
            return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(Login.this, "Please wait ", "Verify user....", true);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(Login.this, "Succesful Sign In.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
