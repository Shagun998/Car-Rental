package com.example.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class Support extends AppCompatActivity {
EditText etTo, etSubject, etMessage;
Button btSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
    etTo =  findViewById(R.id.et_To);
    btSend = findViewById(R.id.send);
    etTo.setText("uic.19mca8272@gmail.com");
    btSend.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent (Intent.ACTION_VIEW
                    ,Uri.parse("mailto:" +etTo.getText().toString()));
            startActivity(intent);

        }
    });
    }
}