 package com.example.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static final float END_SCALE = 0.7f;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    LinearLayout contentView;
    private TextView txtV1, txtV2, txtV3, txtV4, txtV5, txtV6;
    private CircleImageView img1;
    private ListView listView;
    private List<Customer> customerList;
    private customerAdapter adapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ProgressDialog progressDialog;
    private FirebaseDatabase db;
    private DatabaseReference dR;
    private FirebaseUser user;
    private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menuIcon);
        contentView = findViewById(R.id.contentView);
        //DEFINE DATABASE INSTANCES
        firebaseAuth = FirebaseAuth.getInstance();
     user = firebaseAuth.getCurrentUser();

            UserID = user.getUid();

        db = FirebaseDatabase.getInstance();
        dR = db.getReference();

        //CHECKING USER ALREADY LOGIN OR NOT
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user == null){
                    finish();
                    firebaseAuth.signOut();
                    startActivity(new Intent(MainActivity.this, Login.class));
                }
            }
        };

        //DEFINE LAYOUT OBJECTS
        txtV1 = (TextView) findViewById(R.id.logutbtn);
        txtV1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });
        txtV2 = (TextView) findViewById(R.id.textView13);
        txtV3 = (TextView) findViewById(R.id.textView15);
        txtV4 = (TextView) findViewById(R.id.textView16);
        txtV5 = (TextView) findViewById(R.id.textView17);
        txtV6 = (TextView) findViewById(R.id.textView18);
        img1 = (CircleImageView) findViewById(R.id.img2);

        // RETRIEVE DATA FROM FIREBASE DATABASE
        DatabaseReference ref = db.getReference("UserDetails").child("Matric_ID").child(UserID);
        Query query = ref.orderByChild(UserID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    UserDetails uD = ds.getValue(UserDetails.class); //CALL DATA MODEL
                    Picasso.get().load(uD.getImage()).resize(110,110).into(img1);
                    txtV2.setText(uD.getFullName());
                    txtV3.setText(uD.getEmail());
                    txtV4.setText(uD.getMatricNumber());
                    txtV5.setText(uD.getInasis());
                    txtV6.setText(uD.getCourse());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        customerList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.ls1);


        // get database reference from rentmycar.class
        DatabaseReference reff = db.getReference(Bookcar.DATABASE_PATH_FB);

        //Displaying data into ListView on apps
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //FETCH DATA FROM FIREBASE DATABASE
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    //CALL DEFAULT CONSTRUCTOR FROM DATA MODEL TO RETRIEVE DATA FROM FIREBASE DATABASE
                    Customer i = snapshot.getValue(Customer.class);
                    customerList.add(i);
                }
                adapter = new customerAdapter(MainActivity.this,R.layout.customerlistbookacar,customerList);

                //SET ADAPTER FOR LIST VIEW
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //SET LIST VIEW WHEN GET SELECTED ITEM
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
            }
        });

    }


    public void rent_car(View v){
        finish();
        Intent intent = new Intent(this, rentmycar.class);
        startActivity(intent);
    }
    public void booAcar(View v){
        finish();
        Intent intent = new Intent(this, selectCar.class);
        startActivity(intent);
    }

    @Override
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
    navigationDrawer();
    }



private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.red));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);
                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_account:
                Intent intent = new Intent(this, Payment.class);
                this.startActivity(intent);
                break;
            case R.id.nav_support:
                Intent intent1 = new Intent(this, Support.class);
                this.startActivity(intent1);
                break;
            case R.id.nav_logout:
                finish();
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}

