package com.example.smarthealthconsultant.smarthealthconsultant.Doctor;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.Login_Activity.LoginPage;
import com.example.smarthealthconsultant.smarthealthconsultant.Profile.DoctorProfile;
import com.example.smarthealthconsultant.smarthealthconsultant.chat.DoctorMessageListAdapter;
import com.example.smarthealthconsultant.smarthealthconsultant.chat.Message1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorHome extends AppCompatActivity {

    public static final String TAG="DoctorHome";
    private Toolbar mToolbar;

    //RecyclerView
    RecyclerView mMessageView;
    ArrayList<Message1> mMessage1List = new ArrayList<>();
    public LinearLayoutManager mLinearLayout;
    public DoctorMessageListAdapter mAdapter;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // database
    private DatabaseReference mUserRef,mDatabase;
    private Query query;

    public String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        //Firebase init
        //Firebase Auth init
        mAuth = FirebaseAuth.getInstance();

        //Firebase init
        //Firebase Auth init
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        doctorId = mAuth.getCurrentUser().getUid();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Doctor").child(doctorId);

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    //For checking isOnline
                //    mUserRef.child("online").onDisconnect().setValue(false);
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
               //     Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                //    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        //Toolbar initialization
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //RecyclerView

        mAdapter = new DoctorMessageListAdapter(this, mMessage1List);
        mMessageView = (RecyclerView) findViewById(R.id.doctor_message_list_recycler);
        mLinearLayout = new LinearLayoutManager(this);
        mMessageView.setHasFixedSize(true);
        mMessageView.setLayoutManager(mLinearLayout);
        msgInfo();
        mMessageView.setAdapter(mAdapter);

    }

    public void msgInfo(){

        mAuth = FirebaseAuth.getInstance();
        final List<String> ptId = new ArrayList<>();
        final String dId = mAuth.getCurrentUser().getUid();

        final DatabaseReference retriveId = FirebaseDatabase.getInstance().getReference().child("Chat");
        retriveId.child(dId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            //    Log.d("key:",dataSnapshot.getKey());
                String ss = String.valueOf(dataSnapshot.getKey());
                final String dId = mAuth.getCurrentUser().getUid();

                DatabaseReference retriveMsg = FirebaseDatabase.getInstance().getReference()
                        .child("Messages").child(dId).child(ss);
                query = retriveMsg.limitToLast(1);
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Message1 message1 = dataSnapshot.getValue(Message1.class);
                        mMessage1List.add(message1);
                        mAdapter.notifyDataSetChanged();
//                        startActivity(new Intent(getApplicationContext(),DoctorHome.class));
//                        finish();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if(currentUser == null){
//
//        }
//        else {
//            mUserRef.child("online").setValue(true);
//        }
    }


    @Override
    public void onStop() {
        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//        mUserRef.child("online").setValue(false);
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
            startActivity(new Intent(DoctorHome.this, DoctorProfile.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_setting){
            startActivity(new Intent(DoctorHome.this, DoctorSetting.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_history){
            startActivity(new Intent(DoctorHome.this, DoctorPrescriptionList.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_logout){
           // FirebaseUser currentUser = mAuth.getCurrentUser();
//
//            if(currentUser == null){
//
//            }
//            else {
//                mUserRef.child("online").setValue(false);
//            }
            mAuth.signOut();
            startActivity(new Intent(DoctorHome.this, LoginPage.class));
            finish();
            return true;
        }
        return true;
    }
}
