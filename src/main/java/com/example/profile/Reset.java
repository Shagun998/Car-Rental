package com.example.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset extends AppCompatActivity {
    private EditText Email;
    private Button Reset, register;
    private FirebaseAuth auth;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        Email = (EditText) findViewById(R.id.email);
        Reset = (Button) findViewById(R.id.reset);
        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Reset.this, signUp.class);
                startActivity(intent1);
                finish();
            }
        });
        auth = FirebaseAuth.getInstance();
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String email = Email.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Email.setError("Field cannot be empty");
                return;
            }

        auth.sendPasswordResetEmail(Email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Reset.this, "We sent you an e-mail", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Reset.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        }
}