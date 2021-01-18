package com.example.smarthealthconsultant.smarthealthconsultant.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.Main_Activity.MainActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.ModelClass.Hospitals;
import com.example.smarthealthconsultant.smarthealthconsultant.ModelClass.Patients;
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

public class RemovePatients extends AppCompatActivity {

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
    private RecyclerView mPatientList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_patients);
        changeStatusBarColor();
        remove =findViewById(R.id.remove);
        back=findViewById(R.id.back);
        list=new ArrayList<>();
        Intent i = getIntent();
//        mProfilePicture imageview init
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Retrive from database
        mDatabase =  FirebaseDatabase.getInstance().getReference("Patient");
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
                Intent intent=new Intent(RemovePatients.this, MainActivity.class);
                intent.putExtra("user","Admin");
                startActivity(intent);
            }
        });
//
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.isEmpty()){
                    Toast.makeText(RemovePatients.this,"No items to delete",Toast.LENGTH_SHORT).show();
                }
                else{
                    for(String s:list){

                        final DatabaseReference h=mDatabase.child(s);
                        h.removeValue().addOnCompleteListener(RemovePatients.this, new OnCompleteListener<Void>() {
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
        mPatientList =  findViewById(R.id.pat_recycler);
        mPatientList.setHasFixedSize(false);
        mPatientList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Patients> options=new FirebaseRecyclerOptions.Builder<Patients>().setQuery(mDatabase,Patients.class).build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Patients,RemovePatients.PatientsViewHolder>(options) {
            @NonNull
            @Override
            public RemovePatients.PatientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_doctor_online_list,parent,false);
                return new RemovePatients.PatientsViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull final RemovePatients.PatientsViewHolder viewHolder, int position, @NonNull final Patients patients) {
               viewHolder.setPatname(patients.getName());
               viewHolder.setPatmail(patients.getEmail());
                viewHolder.setPatimage(patients.getImage(), getApplicationContext());
                viewHolder.setId(patients.getUserid());
//                viewHolder.showOnline(doctor.getOnline());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list.add(patients.getUserid());
                        viewHolder.setBack(R.color.green);
                    }
                });
            }

        };
        mPatientList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }


    public class PatientsViewHolder extends RecyclerView.ViewHolder{

        View mItem;
        public PatientsViewHolder(View itemView) {
            super(itemView);
            this.mItem = itemView;
        }

        public void setPatname(String name){
            TextView mPName =  mItem.findViewById(R.id.doctor_name);
            mPName.setText(name);
        }
        public void setPatmail(String ctg){
            TextView mPName = (TextView) mItem.findViewById(R.id.doctor_category);
            mPName.setText(ctg);
        }
        public void setPatimage(String url, Context con){
            CircleImageView mPImage = (CircleImageView) mItem.findViewById(R.id.doctor_image);
            if(!url.equals("Default")) {
                Picasso.with(con).load(url).placeholder(R.drawable.patient_icon).into(mPImage);
            }
        }
        public void setBack(int color){
            LinearLayout layout=mItem.findViewById(R.id.layout);
            layout.setBackgroundResource(color);
        }
        public void setId(String id){
            TextView textView=mItem.findViewById(R.id.doc_id);
            textView.setText(id);
        }
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorSecondaryAccent));
        }
    }
}