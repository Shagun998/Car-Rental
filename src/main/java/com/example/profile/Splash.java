package com.example.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import pl.droidsonroids.gif.GifImageView;

public class Splash extends AppCompatActivity {
        private static int Splash=4000;
        Animation bottomanim;
        TextView name, slogan;
        GifImageView image;
    private FirebaseAuth mAuth;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);
            image = findViewById(R.id.image);
            name = findViewById(R.id.name);
            slogan = findViewById(R.id.slogan);
            bottomanim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
            name.setAnimation(bottomanim);
            slogan.setAnimation(bottomanim);
         mAuth = FirebaseAuth.getInstance();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
finish();
                    Intent intent = new Intent(Splash.this, SignInCust.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            },Splash);
        }
    }