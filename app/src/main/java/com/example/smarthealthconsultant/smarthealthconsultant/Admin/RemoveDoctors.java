package com.example.smarthealthconsultant.smarthealthconsultant.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.Main_Activity.MainActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.chat.Doctors;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RemoveDoctors extends AppCompatActivity {
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // database
    private DatabaseReference mDatabase;
    private Query query;
    public List<String> list;
    public Button remove,back;

    //RecyclerView
    private RecyclerView mDoctorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_doctors);

        remove =findViewById(R.id.remove);
        back=findViewById(R.id.back);
        list=new ArrayList<>();
        //mProfilePicture imageview init
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        //Retrive from database
        mDatabase =  FirebaseDatabase.getInstance().getReference("Doctor");
//        query = mDatabase.orderByChild("hosname").equalTo(hos_name);

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


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RemoveDoctors.this, MainActivity.class);
                intent.putExtra("user","Admin");
                startActivity(intent);
            }
        });
//
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.isEmpty()){
                    Toast.makeText(RemoveDoctors.this,"No items to delete",Toast.LENGTH_SHORT).show();
                }
                else{
                    for(String s:list){

                        final DatabaseReference h=mDatabase.child(s);
                        h.removeValue().addOnCompleteListener(RemoveDoctors.this, new OnCompleteListener<Void>() {
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
        mDoctorList =  findViewById(R.id.pat_recycler);
        mDoctorList.setHasFixedSize(false);
        mDoctorList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Doctors> options=new FirebaseRecyclerOptions.Builder<Doctors>().setQuery(mDatabase,Doctors.class).build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Doctors, RemoveDoctors.DoctorsViewHolder>(options) {
            @NonNull
            @Override
            public RemoveDoctors.DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_doctor_online_list,parent,false);
                return new RemoveDoctors.DoctorsViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final RemoveDoctors.DoctorsViewHolder viewHolder, int position, @NonNull final Doctors doctors) {
                viewHolder.setName(doctors.getName());
                viewHolder.setCategory(doctors.getCategory());
                viewHolder.setImage(doctors.getImage(), getApplicationContext());
                viewHolder.setId(doctors.getDoc_id());
//                viewHolder.showOnline(doctor.getOnline());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list.add(doctors.getDoc_id());
                        viewHolder.setBack(R.color.green);

                    }
                });
            }


        };
        mDoctorList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class DoctorsViewHolder extends RecyclerView.ViewHolder{

        View mItem;
        public DoctorsViewHolder(View itemView) {
            super(itemView);
            mItem = itemView;
        }

        public void setName(String name){
            TextView mDoctorName =  mItem.findViewById(R.id.doctor_name);
            mDoctorName.setText(name);
        }
        public void setCategory(String ctg){
            TextView mDoctorName =  mItem.findViewById(R.id.doctor_category);
            mDoctorName.setText(ctg);
        }
        public void setImage(String url, Context con){
            CircleImageView mDoctorImage = (CircleImageView) mItem.findViewById(R.id.doctor_image);
            Picasso.with(con).load(url).placeholder(R.drawable.doctor_icon).into(mDoctorImage);
        }

        public void setId(String id){
            TextView textView = mItem.findViewById(R.id.doc_id);
            textView.setText(id);
        }

        public void showOnline(Boolean b){
            ImageView onlineImage = mItem.findViewById(R.id.online_image);
            if(b){
                onlineImage.setImageResource(R.drawable.online);
            }

        }
        public void setBack(int color){
            LinearLayout layout=mItem.findViewById(R.id.layout);
            layout.setBackgroundResource(color);
        }
    }
}