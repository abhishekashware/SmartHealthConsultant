package com.example.smarthealthconsultant.smarthealthconsultant.PatientDirectory;



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
import com.example.smarthealthconsultant.smarthealthconsultant.Login_Activity.LoginActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.Profile.DoctorProfile;
import com.example.smarthealthconsultant.smarthealthconsultant.Doctor.DoctorSetting;
import com.example.smarthealthconsultant.smarthealthconsultant.chat.Message1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class PatientInbox extends AppCompatActivity {

    public static final String TAG="PatientInbox";
    private Toolbar mToolbar;

    //RecyclerView
    RecyclerView mMessageView;
    List<Message1> mMessageList = new ArrayList<>();
    public LinearLayoutManager mLinearLayout;
    public PatientInboxListAdapter mAdapter;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // database
    private DatabaseReference mUserRef,mDatabase;
    private Query query;

    public String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_inbox);

        //Firebase init
        //Firebase Auth init
        mAuth = FirebaseAuth.getInstance();

        //Firebase init
        //Firebase Auth init
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        patientId = mAuth.getCurrentUser().getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        //Toolbar initialization
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Inbox");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //RecyclerView

        mAdapter = new PatientInboxListAdapter(PatientInbox.this,mMessageList);
        mMessageView = (RecyclerView) findViewById(R.id.patient_message_list_recycler);
        mLinearLayout = new LinearLayoutManager(this);
        mMessageView.setHasFixedSize(true);
        mMessageView.setLayoutManager(mLinearLayout);
        msgInfo();
        mMessageView.setAdapter(mAdapter);
    }

    public void msgInfo(){

        mAuth = FirebaseAuth.getInstance();
        final List<String> ptId = new ArrayList<>();
        final String pId = mAuth.getCurrentUser().getUid();

        final DatabaseReference retriveId = FirebaseDatabase.getInstance().getReference().child("Chat");
        retriveId.child(pId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
           //     Log.d("key:",dataSnapshot.getKey());
                String ss = String.valueOf(dataSnapshot.getKey());
                final String pId = mAuth.getCurrentUser().getUid();

                DatabaseReference retrieveMsg = FirebaseDatabase.getInstance().getReference()
                        .child("Messages").child(pId).child(ss);
                query = retrieveMsg.limitToLast(1);
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Message1 message = dataSnapshot.getValue(Message1.class);
                        mMessageList.add(message);
                        mAdapter.notifyDataSetChanged();
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
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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

            startActivity(new Intent(PatientInbox.this, PatientHome.class));
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_profile){
            startActivity(new Intent(PatientInbox.this, DoctorProfile.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_setting){
            startActivity(new Intent(PatientInbox.this, DoctorSetting.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_logout){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            mAuth.signOut();
            startActivity(new Intent(PatientInbox.this, LoginActivity.class));
            finish();
            return true;
        }

        return true;
    }
}
