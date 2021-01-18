package com.example.smarthealthconsultant.smarthealthconsultant.Profile;

import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smarthealthconsultant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfile extends AppCompatActivity {
        public static final String TAG = "PatientProfile";
        private Toolbar mToolbar;
        String userName;
        String imageUrl;
        String mobile;
        String email;
        String category;
         String fees;
        //Firebase Auth
        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;
        //Firebase Database
        // Write a message to the database
        private DatabaseReference mDatabase;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_doctor_profile);

            //Toolbar initialization
            mToolbar =  findViewById(R.id.toolbar);
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
                    } else {
                        // User is signed out
                    }
                }
            };

            //mProfilePicture imageview init
            final FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
            String uid = currentUser.getUid();

            mDatabase = FirebaseDatabase.getInstance().getReference("Doctor").child(uid);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    userName = dataSnapshot.child("name").getValue().toString();
                  //  fees = dataSnapshot.child("fees").getValue().toString();
                    imageUrl = dataSnapshot.child("image").getValue().toString();
                 //   mobile = dataSnapshot.child("mobile").getValue().toString();
                    email = dataSnapshot.child("email").getValue().toString();
                    category=dataSnapshot.child("category").getValue().toString();
                    final EditText mUserName = findViewById(R.id.patientName);
                    TextView mEmail = findViewById(R.id.email);
                    final EditText cat=findViewById(R.id.cat);
//                    final EditText mPhone = findViewById(R.id.patientNo);
                    mUserName.setText(userName);
                    mEmail.setText(email);
                    cat.setText(category);
                    //mPhone.setText(mobile);
                    CircleImageView mProfileImage = (CircleImageView) findViewById(R.id.profile_image);

                    if(imageUrl.equals("Default")){
                        mProfileImage.setImageResource(R.drawable.doctor_icon);
                    }
                    else {
                       Picasso.with(getApplicationContext()).load(imageUrl).into(mProfileImage);
                    }
                }

             

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);
//            getMenuInflater().inflate(R.menu.,menu);
            return true;
        }

//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            super.onOptionsItemSelected(item);
//            if (item.getItemId() == android.R.id.home) {
//                startActivity(new Intent(PatientProfile.this, PatientHome.class));
//                finish(); // close this activity and return to preview activity (if there is any)
//                return true;
//            }
//            else if(item.getItemId()==R.id.main_menu_profile){
//                startActivity(new Intent(PatientProfile.this, PatientProfile.class));
//                finish();
//                return true;
//            }
//            else if(item.getItemId()==R.id.main_menu_setting){
//                startActivity(new Intent(PatientProfile.this, PatientProfileSetting.class));
//                finish();
//                return true;
//            }
//            else if(item.getItemId()==R.id.main_menu_inbox){
//                startActivity(new Intent(PatientProfile.this, PatientInbox.class));
//                finish();
//                return true;
//            }
//            else if(item.getItemId()==R.id.main_menu_logout){
//                mAuth.signOut();
//                startActivity(new Intent(PatientProfile.this, PrimaryLogin.class));
//                finish();
//                return true;
//            }
//
//            return true;
//        }
}