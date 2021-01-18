package com.example.smarthealthconsultant.smarthealthconsultant.Doctor;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.Login_Activity.LoginActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.Profile.DoctorProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorSetting extends AppCompatActivity {

    public static final String TAG="DoctorSetting";
    private Toolbar mToolbar;
    String uid;
    FirebaseUser currentUser;
    //Firbase storage
    private StorageReference mImageStorage;
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // database
    private DatabaseReference mUserRef,mDatabase;
    private Query query;

    public String doctorId;
    //CircularImageView
    CircleImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_setting);

        //Firebase init
        //Firebase Auth init
        mAuth = FirebaseAuth.getInstance();

        //Firebase init
        //Firebase Auth init
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        doctorId = mAuth.getCurrentUser().getUid();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctorId);

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    //For checking isOnline
                    mUserRef.child("online").onDisconnect().setValue(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        //CircularImageView init
        profilePicture = (CircleImageView)findViewById(R.id.profile_image);
        //mProfilePicture imageview init
        currentUser = mAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();
        //Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctor").child(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String imageUrl = dataSnapshot.child("image").getValue().toString();

                if(imageUrl.equals("Default")){
                    profilePicture.setImageResource(R.drawable.doctor_icon);
                }
                else {
                    Picasso.with(getApplicationContext()).load(imageUrl).into(profilePicture);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Toolbar initialization
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile Setting");
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

        }
        else {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Doctor").child(doctorId);

            mUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null) {
                        //For checking isOnline
                   //     mUserRef.child("online").setValue(true);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Doctor").child(doctorId);

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    //For checking isOnline
           //         mUserRef.child("online").setValue(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_bar_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {

            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_profile){
            startActivity(new Intent(DoctorSetting.this, DoctorProfile.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_setting){
            startActivity(new Intent(DoctorSetting.this, DoctorSetting.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_history){
            startActivity(new Intent(DoctorSetting.this, DoctorPrescriptionList.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_logout){
            FirebaseUser currentUser = mAuth.getCurrentUser();

//            if(currentUser == null){
//
//            }
//            else {
//                mUserRef.child("online").setValue(false);
//            }
            mAuth.signOut();
            startActivity(new Intent(DoctorSetting.this, LoginActivity.class));
            finish();
            return true;
        }

        return true;
    }
}
