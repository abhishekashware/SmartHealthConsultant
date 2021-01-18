package com.example.smarthealthconsultant.smarthealthconsultant.Admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.Main_Activity.MainActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.ModelClass.Hospitals;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RemoveHospitals extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // database
    private DatabaseReference mDatabase;
    private Query query;
    public Button remove;
    public Button back;
    public String ctg;
    public List<String> list;
    public  String uid;
    //RecyclerView
    private RecyclerView mDoctorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_manage_hospitals_admin);
        remove =findViewById(R.id.remove);
        back=findViewById(R.id.back);
        list=new ArrayList<>();
        Intent i = getIntent();
//        mProfilePicture imageview init
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
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

       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(RemoveHospitals.this, MainActivity.class);
               intent.putExtra("user","Admin");
               startActivity(intent);
           }
       });
//
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.isEmpty()){
                    Toast.makeText(RemoveHospitals.this,"No items to delete",Toast.LENGTH_SHORT).show();
                }
                else{
                    for(String s:list){

                        final DatabaseReference h=mDatabase.child(s);
                        h.removeValue().addOnCompleteListener(RemoveHospitals.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(getBaseContext(), "Removed Successfully", Toast.LENGTH_SHORT).show();
                                    h.removeValue();
                                }
                            }
                        });
                    }
                }
            }
        });
        //Recycler view set up for doctor list
        mDoctorList =  findViewById(R.id.hos_recycler);
        mDoctorList.setHasFixedSize(false);
        mDoctorList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Hospitals> options=new FirebaseRecyclerOptions.Builder<Hospitals>().setQuery(mDatabase,Hospitals.class).build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Hospitals,RemoveHospitals.HospitalsViewHolder>(options) {
            @NonNull
            @Override
            public RemoveHospitals.HospitalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_hospitals_list,parent,false);
                return new RemoveHospitals.HospitalsViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final RemoveHospitals.HospitalsViewHolder viewHolder, int position, @NonNull final Hospitals hospitals) {
                viewHolder.setHosname(hospitals.getHosname());
                viewHolder.setHoscity(hospitals.getHoscity());
                viewHolder.setHosimage(hospitals.getHosimage(), getApplicationContext());
//                viewHolder.showOnline(doctor.getOnline());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        final String name=getIntent().getStringExtra("name");
//                        final String email=getIntent().getStringExtra("email");
//                        Intent i = new Intent(RemoveHospitals.this, AvailableDoctorsForAppointment.class);
//                        i.putExtra("hos_name",hospitals.getHosname());
//                        i.putExtra("name",name);
//                        i.putExtra("email",email);


                        list.add(hospitals.getHosname());
                        viewHolder.setBack(R.color.green);
//                        i.putExtra("pa_cat",getIntent().getStringExtra("pa_cat"));

//                        i.putExtra("doctor_category",doctor.getHos());
//                        i.putExtra("doctor_image",doctor.getImage());
////                        i.putExtra("doctor_id",doctor.getId());
//                        startActivity(i);
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
             // Picasso.with(con).load(url).placeholder(R.drawable.doctor_icon).into(mDoctorImage);
        }
        public void setBack(int color){
            LinearLayout layout=mItem.findViewById(R.id.layout);
            layout.setBackgroundResource(color);
        }
    }



}
