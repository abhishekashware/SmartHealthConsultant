package com.example.smarthealthconsultant.smarthealthconsultant.Profile;



import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.Login_Activity.LoginActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.PatientDirectory.PatientInbox;
import com.example.smarthealthconsultant.smarthealthconsultant.PatientDirectory.PatientHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientProfile extends AppCompatActivity {
    public static final String TAG = "PatientProfile";
    private Toolbar mToolbar;
    String userName;
    String imageUrl;
    String mobile;
    String email;
    public Button submit;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //Firebase Database
    // Write a message to the database
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        //Toolbar initialization
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile");
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Firebase init
        //Firebase Auth init
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
               //     Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                 //   Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        //mProfilePicture imageview init
        final FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
       submit=findViewById(R.id.submit);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Patient").child(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userName = dataSnapshot.child("name").getValue().toString();
                imageUrl = dataSnapshot.child("image").getValue().toString();
                //mobile = dataSnapshot.child("mobile").getValue().toString();
                 email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                EditText mUserName = findViewById(R.id.patientName);
                TextView mEmail = (TextView)findViewById(R.id.email);
                //TextView mPhone = (TextView)findViewById(R.id.patientNo);
                mUserName.setText(userName);
                mEmail.setText(email);
                //mPhone.setText(mobile);

                CircleImageView mProfileImage = (CircleImageView) findViewById(R.id.profile_image);

                if(imageUrl.equals("Default")){
                    mProfileImage.setImageResource(R.drawable.patient_icon);
                }
                else {
                    Picasso.with(getApplicationContext()).load(imageUrl).into(mProfileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mUserName = findViewById(R.id.patientName);
                final String un = mUserName.getText().toString();
                if (!un.isEmpty()){
                    mDatabase.child("name").setValue(un).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(PatientProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
              else{
                    Toast.makeText(PatientProfile.this,"Name cannot be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            startActivity(new Intent(PatientProfile.this, PatientHome.class));
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_profile){
            startActivity(new Intent(PatientProfile.this, PatientProfile.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_setting){
           // startActivity(new Intent(PatientProfile.this, PatientProfileSetting.class));
            // finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_inbox){
            startActivity(new Intent(PatientProfile.this, PatientInbox.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_logout){
            mAuth.signOut();
            startActivity(new Intent(PatientProfile.this, LoginActivity.class));
            finish();
            return true;
        }

        return true;
    }

}
