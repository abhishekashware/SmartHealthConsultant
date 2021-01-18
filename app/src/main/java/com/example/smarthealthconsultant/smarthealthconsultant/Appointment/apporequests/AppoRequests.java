package com.example.smarthealthconsultant.smarthealthconsultant.Appointment.apporequests;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.ModelClass.Patients;
import com.example.smarthealthconsultant.smarthealthconsultant.ModelClass.PatientsAdapter;
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

public class AppoRequests extends AppCompatActivity {


    private Toolbar mToolbar;
    //Firebase
    private FirebaseAuth mAuth;
   // public List<Appo> aplist;
//    public List<String> pid;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // database
    private DatabaseReference mDatabase;
    private Query query;

    public String ctg;

    //RecyclerView
    private RecyclerView mPatientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appo_requests);
        //Recycler view set up for doctor list
        mPatientList =  findViewById(R.id.pat_list_recycler);
        mPatientList.setHasFixedSize(false);
        mPatientList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        //mProfilePicture imageview init
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentUser.getUid();

        //Retrive from database
        mDatabase =  FirebaseDatabase.getInstance().getReference("Patient");
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Appointments").child(uid);
       // aplist=new ArrayList<>();
 //       query = mDatabase.orderByChild("hosname").equalTo(hos_name);
        //Firebase init
        //Firebase Auth init
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };

       // Toolbar initialization
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Appointment Requests");

       //  add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final ArrayList<AppoReqClass> patients=new ArrayList<>();

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                AppoReqClass patients1= snapshot.getValue(AppoReqClass.class);
                patients.add(patients1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        AppoReqAdapter adapter=new AppoReqAdapter(getApplicationContext(),patients);
        mPatientList.setAdapter(adapter);
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
//            startActivity(new Intent(AvailableDoctors.this, PatientHome.class));
//            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_profile){
//            startActivity(new Intent(AppoRequests.this, PatientProfile.class));
//            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_setting){
            //  startActivity(new Intent(DoctorList.this, PatientProfileSetting.class));
            //finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_logout){
//            mAuth.signOut();
//            startActivity(new Intent(AppoRequests.this, LoginActivity.class));
//            finish();
            return true;
        }

        return true;
    }
    
    
}