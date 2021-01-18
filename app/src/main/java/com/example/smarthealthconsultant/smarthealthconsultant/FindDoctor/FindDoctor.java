package com.example.smarthealthconsultant.smarthealthconsultant.FindDoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.Login_Activity.LoginActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.Doctor.DoctorDetails;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindDoctor extends AppCompatActivity {
    public static final String TAG="DoctorList";
    private Toolbar mToolbar;
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // database
    private DatabaseReference mDatabase;
    private Query query;

    public String ctg;

    //RecyclerView
    private RecyclerView mDoctorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        Intent i = getIntent();
        ctg = i.getStringExtra("doctor_category");

        //mProfilePicture imageview init
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        //Retrive from database
        mDatabase =  FirebaseDatabase.getInstance().getReference("Doctor");
        query = mDatabase.orderByChild("category").equalTo(ctg);

        //Firebase init
        //Firebase Auth init
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                  //  Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    //Log.d("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };

        //Toolbar initialization
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
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

        FirebaseRecyclerOptions<Doctors> options=new FirebaseRecyclerOptions.Builder<Doctors>().setQuery(query,Doctors.class).build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Doctors, DoctorList.DoctorsViewHolder>(options) {
            @NonNull
            @Override
            public DoctorList.DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_doctor_online_list,parent,false);
                return new DoctorList.DoctorsViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull DoctorList.DoctorsViewHolder viewHolder, int position, @NonNull final Doctors doctor) {
                viewHolder.setName(doctor.getName());
                viewHolder.setCategory(doctor.getCategory());
                viewHolder.setImage(doctor.getImage(), getApplicationContext());
//                viewHolder.showOnline(doctor.getOnline());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(FindDoctor.this, DoctorDetails.class);
                        i.putExtra("doctor_name",doctor.getName());
                        i.putExtra("doctor_category",doctor.getCategory());
                        i.putExtra("doctor_image",doctor.getImage());
                        i.putExtra("doctor_id",doctor.getDoc_id());

                        startActivity(i);
                    }
                });
            }


        };
        mDoctorList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public class DoctorsViewHolder extends RecyclerView.ViewHolder{

        View mItem;
        public DoctorsViewHolder(View itemView) {
            super(itemView);
            mItem = itemView;
        }

        public void setName(String name){
            TextView mDoctorName = (TextView) mItem.findViewById(R.id.doctor_name);
            mDoctorName.setText(name);
        }
        public void setCategory(String ctg){
            TextView mDoctorName = (TextView) mItem.findViewById(R.id.doctor_category);
            mDoctorName.setText(ctg);
        }
        public void setImage(String url, Context con){
            CircleImageView mDoctorImage = (CircleImageView) mItem.findViewById(R.id.doctor_image);
            Picasso.with(con).load(url).placeholder(R.drawable.doctor_icon).into(mDoctorImage);
        }

        public void showOnline(Boolean b){
            ImageView onlineImage = mItem.findViewById(R.id.online_image);
            if(b){
                onlineImage.setImageResource(R.drawable.online);
            }

        }
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
            startActivity(new Intent(FindDoctor.this, PatientHome.class));
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_profile){
            startActivity(new Intent(FindDoctor.this, PatientProfile.class));
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
            startActivity(new Intent(FindDoctor.this, LoginActivity.class));
            finish();
            return true;
        }

        return true;
    }
}