package com.example.smarthealthconsultant.smarthealthconsultant.Doctor;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.Login_Activity.LoginActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.Profile.PatientProfile;
import com.example.smarthealthconsultant.smarthealthconsultant.chat.ChatActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.PatientDirectory.PatientHome;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorDetails extends AppCompatActivity {

    public static final String TAG="DoctorDetails";
    private Toolbar mToolbar;
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // database
    private DatabaseReference mDatabase;
    private Query query;

    public String ctg;
    public String name;
    public String imageUrl;
    public String doctorId;
    public TextView email;
    public FloatingActionButton floatingActionButton;
    //Widget
    CircleImageView profileImage;
    TextView doctorName,doctorCategory;
    ImageView chatSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        Intent i = getIntent();
        name = i.getStringExtra("doctor_name");
        ctg = i.getStringExtra("doctor_category");
        imageUrl = i.getStringExtra("doctor_image");
        doctorId = i.getStringExtra("doctor_id");

        //Widget init
        floatingActionButton=findViewById(R.id.fab);
        profileImage =  findViewById(R.id.doctorImage);
        doctorName = findViewById(R.id.doctorName);
        doctorCategory =  findViewById(R.id.doctorSpel);
        chatSymbol = findViewById(R.id.chatnav);
        email=findViewById(R.id.email);


        Picasso.with(getApplicationContext()).load(imageUrl).placeholder(R.drawable.doctor_icon).into(profileImage);
        doctorName.setText(name);


        floatingActionButton.setEnabled(false);
        floatingActionButton.setVisibility(View.GONE);


        doctorCategory.setText(ctg);
        //mProfilePicture imageview init
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        //Firebase init
        //Firebase Auth init
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
//                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
//                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };
//
        //Toolbar initialization
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Doctor info");
//
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
//
        chatSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUser.getUid();
                Intent msgActivity = new Intent(DoctorDetails.this, ChatActivity.class);
                msgActivity.putExtra("doctor_name",name);
                msgActivity.putExtra("doctor_id",doctorId);
                startActivity(msgActivity);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.patient_menu_bar,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(DoctorDetails.this, PatientHome.class));
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_profile){
            startActivity(new Intent(DoctorDetails.this, PatientProfile.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_setting){
         //   startActivity(new Intent(DoctorDetails.this, PatientProfileSetting.class));
           // finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_logout){
            mAuth.signOut();
            startActivity(new Intent(DoctorDetails.this, LoginActivity.class));
            finish();
            return true;
        }

        return true;
    }
}
