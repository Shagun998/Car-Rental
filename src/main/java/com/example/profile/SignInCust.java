package com.example.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

public class SignInCust extends AppCompatActivity {
    EditText phone;
    CountryCodePicker ccp;
    Button Sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_cust);
        phone = findViewById(R.id.phone);
        Sign = findViewById(R.id.Sign);
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phone);
        Sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = ccp.getSelectedCountryCodeWithPlus();
                String   numb = phone.getText().toString().trim();
                if(numb.isEmpty()||numb.length()<10){
                    phone.setError("Valid number is required");
                    phone.requestFocus();
                    return;
                }
                String phonenumber = number + numb;
                Intent intent = new Intent(SignInCust.this, VerifyPhone.class);
                intent.putExtra("phonenumber", phonenumber);
                startActivity(intent);
            }
        });
    }
}