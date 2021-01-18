package com.example.smarthealthconsultant.smarthealthconsultant.PatientDirectory;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.Login_Activity.LoginActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.Prescription.PrescriptionList;
import com.example.smarthealthconsultant.smarthealthconsultant.Profile.PatientProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class PatientHome extends AppCompatActivity {

    public static final String TAG="PatientHome";
    private Toolbar mToolbar;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

        //init RecyclerView
        initRecyclerViewsForPatient();

        //Firebase init
        //Firebase Auth init
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                 //   Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
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

    public void initRecyclerViewsForPatient(){
        RecyclerView recyclerView = findViewById(R.id.patient_home_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<PatientHomeData> list = new ArrayList<>();
        list.add(new PatientHomeData("Acupuncturist",R.drawable.stethoscope));
        list.add(new PatientHomeData("Allergist",R.drawable.stethoscope));
        list.add(new PatientHomeData("Cardiologist",R.drawable.stethoscope));
        list.add(new PatientHomeData("Neurologist", R.drawable.stethoscope));
        list.add(new PatientHomeData("Oncologist",R.drawable.stethoscope));
        list.add(new PatientHomeData("Pathologist",R.drawable.stethoscope));
        list.add(new PatientHomeData("Hematologist",R.drawable.stethoscope));
        list.add(new PatientHomeData("Dermatologist",R.drawable.stethoscope));

        list.add(new PatientHomeData("Dietitian",R.drawable.stethoscope));
        list.add(new PatientHomeData("Dentist",R.drawable.stethoscope));
        list.add(new PatientHomeData("Ear Nose Throat Doctor",R.drawable.stethoscope));
        list.add(new PatientHomeData("Nutritionist",R.drawable.stethoscope));
        list.add(new PatientHomeData("Gastroenterologist",R.drawable.stethoscope));
        list.add(new PatientHomeData("General Practitioner",R.drawable.stethoscope));
        list.add(new PatientHomeData("Gynecologist",R.drawable.stethoscope));
        list.add(new PatientHomeData("Gynecologist",R.drawable.stethoscope));
        list.add(new PatientHomeData("Gynecologist",R.drawable.stethoscope));
        list.add(new PatientHomeData("Pulmonologist",R.drawable.stethoscope));
        list.add(new PatientHomeData("Psychiatrist",R.drawable.stethoscope));
        list.add(new PatientHomeData("Optometrist",R.drawable.stethoscope));
        list.add(new PatientHomeData("Urologist",R.drawable.stethoscope));

        PatientHomeViewAdapter adapter = new PatientHomeViewAdapter(getApplicationContext(),list);
        recyclerView.setAdapter(adapter);

        //Firebase Auth init
        mAuth = FirebaseAuth.getInstance();
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
            startActivity(new Intent(PatientHome.this, PatientHome.class));
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_profile){
            startActivity(new Intent(PatientHome.this, PatientProfile.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_setting){
            startActivity(new Intent(PatientHome.this, PatientProfile.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_inbox){
            startActivity(new Intent(PatientHome.this, PatientInbox.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_pres){
            startActivity(new Intent(PatientHome.this, PrescriptionList.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_logout){
            mAuth.signOut();
            startActivity(new Intent(PatientHome.this, LoginActivity.class));
            finish();
            return true;
        }

        return true;
    }
}
