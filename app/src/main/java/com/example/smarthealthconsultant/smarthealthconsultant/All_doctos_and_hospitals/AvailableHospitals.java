package com.example.smarthealthconsultant.smarthealthconsultant.All_doctos_and_hospitals;

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
import com.example.smarthealthconsultant.smarthealthconsultant.Appointment.AvailableDoctorsForAppointment;
import com.example.smarthealthconsultant.smarthealthconsultant.Login_Activity.LoginActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.ModelClass.Hospitals;
import com.example.smarthealthconsultant.smarthealthconsultant.PatientDirectory.PatientHome;
import com.example.smarthealthconsultant.smarthealthconsultant.Profile.PatientProfile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvailableHospitals extends AppCompatActivity {


  //  public static final String TAG="DoctorList";
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
        setContentView(R.layout.activity_available_hospitals);

        Intent i = getIntent();
    //    ctg = i.getStringExtra("doctor_category");
//        mProfilePicture imageview init
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        //Retrive from database
        mDatabase =  FirebaseDatabase.getInstance().getReference("Hospitals");
      //  query = mDatabase.orderByChild("city").equalTo(ctg);

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
                  //  Log.d("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };

        //Toolbar initialization
        mToolbar =  findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Available Hospitals");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Recycler view set up for doctor list
        mDoctorList =  findViewById(R.id.hos_recycler);
        mDoctorList.setHasFixedSize(false);
        mDoctorList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Hospitals> options=new FirebaseRecyclerOptions.Builder<Hospitals>().setQuery(mDatabase,Hospitals.class).build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Hospitals, AvailableHospitals.HospitalsViewHolder>(options) {
            @NonNull
            @Override
            public AvailableHospitals.HospitalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_hospitals_list,parent,false);
                return new AvailableHospitals.HospitalsViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull AvailableHospitals.HospitalsViewHolder viewHolder, int position, @NonNull final Hospitals hospitals) {
                viewHolder.setHosname(hospitals.getHosname());
                viewHolder.setHoscity(hospitals.getHoscity());
                viewHolder.setHosimage(hospitals.getHosimage(), getApplicationContext());
//                viewHolder.showOnline(doctor.getOnline());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String name=getIntent().getStringExtra("name");
                        final String email=getIntent().getStringExtra("email");
                        Intent i = new Intent(AvailableHospitals.this, AvailableDoctorsForAppointment.class);
                        i.putExtra("hos_name",hospitals.getHosname());
                        i.putExtra("name",name);
                        i.putExtra("email",email);
//                        i.putExtra("pa_cat",getIntent().getStringExtra("pa_cat"));

//                        i.putExtra("doctor_category",doctor.getHos());
//                        i.putExtra("doctor_image",doctor.getImage());
////                        i.putExtra("doctor_id",doctor.getId());
                        startActivity(i);
                    }
                });
            }

        };
        mDoctorList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public class HospitalsViewHolder extends RecyclerView.ViewHolder{

        View mItem;
        public HospitalsViewHolder(View itemView) {
            super(itemView);
            this.mItem = itemView;
        }

        public void setHosname(String name){
           TextView mDoctorName =  mItem.findViewById(R.id.hos_name);
          mDoctorName.setText(name);
        }
        public void setHoscity(String ctg){
            TextView mDoctorName = (TextView) mItem.findViewById(R.id.hos_city);
            mDoctorName.setText(ctg);
        }
        public void setHosimage(String url, Context con){
            CircleImageView mDoctorImage = (CircleImageView) mItem.findViewById(R.id.hos_image);
          //  Picasso.with(con).load(url).placeholder(R.drawable.doctor_icon).into(mDoctorImage);
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
        getMenuInflater().inflate(R.menu.patient_menu_bar,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(AvailableHospitals.this, PatientHome.class));
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_profile){
            startActivity(new Intent(AvailableHospitals.this, PatientProfile.class));
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
            startActivity(new Intent(AvailableHospitals.this, LoginActivity.class));
            finish();
            return true;
        }

        return true;
    }
}