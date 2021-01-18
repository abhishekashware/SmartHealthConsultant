package com.example.smarthealthconsultant.smarthealthconsultant.Appointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.Login_Activity.LoginActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.Doctor.DoctorList;
import com.example.smarthealthconsultant.smarthealthconsultant.chat.Doctors;
import com.example.smarthealthconsultant.smarthealthconsultant.PatientDirectory.PatientHome;
import com.example.smarthealthconsultant.smarthealthconsultant.Profile.PatientProfile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AvailableDoctorsForAppointment extends AppCompatActivity {
    // public static final String TAG="DoctorList";
    private Toolbar mToolbar;
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // database
    private DatabaseReference mDatabase;
    private Query query;

    public String name;
    public String email;

    //RecyclerView
    private RecyclerView mDoctorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        Intent i = getIntent();
        name = i.getStringExtra("name");
        email=i.getStringExtra("email");

        //mProfilePicture imageview init
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        //Retrive from database
      //  String hos_name=getIntent().getStringExtra("hos_name");
        mDatabase =  FirebaseDatabase.getInstance().getReference("Doctor");
      //  query = mDatabase.orderByChild("hosname").equalTo(hos_name);

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

        //Toolbar initialization
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Available Doctors");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Recycler view set up for doctor list
        mDoctorList =  findViewById(R.id.doctor_list_recycler);
        mDoctorList.setHasFixedSize(false);
        mDoctorList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Doctors> options=new FirebaseRecyclerOptions.Builder<Doctors>().setQuery(mDatabase,Doctors.class).build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Doctors, DoctorList.DoctorsViewHolder>(options) {
            @NonNull
            @Override
            public DoctorList.DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_doctor_online_list,parent,false);
                return new DoctorList.DoctorsViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull DoctorList.DoctorsViewHolder viewHolder, int position, @NonNull final Doctors doctors) {
                viewHolder.setName(doctors.getName());
                viewHolder.setCategory(doctors.getCategory());
                viewHolder.setImage(doctors.getImage(), getApplicationContext());
                viewHolder.setDoc_id(doctors.getDoc_id());
//                viewHolder.showOnline(doctor.getOnline());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(AvailableDoctorsForAppointment.this, FinalAppointment.class);

                        i.putExtra("doc_id",doctors.getDoc_id());
                        i.putExtra("doc_name",doctors.getName());
                        i.putExtra("doc_image",doctors.getImage());
                        i.putExtra("doc_cat",doctors.getCategory());

//                        Appointment_Dialog appointment_dialog=new Appointment_Dialog(AvailableDoctorsForAppointment.this);
//                        appointment_dialog.setDoc_id(doctors.getDoc_id());
//                        appointment_dialog.setDoc_name(doctors.getName());
//                        appointment_dialog.setImage(getApplicationContext(),doctors.getImage());
//                        appointment_dialog.setCat(doctors.getCategory());
//                        appointment_dialog.show();

//                        i.putExtra("doctor_name",doctors.getName());
//                        i.putExtra("doctor_category",doctors.getCategory());
//                        i.putExtra("doctor_image",doctors.getImage());
//                        i.putExtra("doctor_id",doctor.getId());
                        startActivity(i);
                    }
                });
            }


        };
        mDoctorList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
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
            startActivity(new Intent(AvailableDoctorsForAppointment.this, PatientHome.class));
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_profile){
            startActivity(new Intent(AvailableDoctorsForAppointment.this, PatientProfile.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_setting){
            //  startActivity(new Intent(DoctorList.this, PatientProfileSetting.class));
            //finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_logout){
            mAuth.signOut();
            startActivity(new Intent(AvailableDoctorsForAppointment.this, LoginActivity.class));
            finish();
            return true;
        }

        return true;
    }
}