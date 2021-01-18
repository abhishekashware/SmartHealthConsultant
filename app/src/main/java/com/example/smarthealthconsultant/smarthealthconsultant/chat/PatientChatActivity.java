package com.example.smarthealthconsultant.smarthealthconsultant.chat;

import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.Login_Activity.LoginActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.Prescription.PrescriptionList;
import com.example.smarthealthconsultant.smarthealthconsultant.Profile.DoctorProfile;
import com.example.smarthealthconsultant.smarthealthconsultant.Doctor.DoctorSetting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

public class PatientChatActivity extends AppCompatActivity implements IPickResult {

    public static final String TAG="ChatActivity";
    private Toolbar mToolbar;

    //RecyclerView
    RecyclerView mMessageView;
    List<Message1> mMessageList = new ArrayList<>();
    public LinearLayoutManager mLinearLayout;
    public MessageAdapter mAdapter;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //Firbase storage
    private StorageReference mImageStorage;
    // database
    private DatabaseReference mUserRef,mDatabase;
    private Query query;

    public String doctorName,userId;
    public String currentUserId,senderId;

    //Widget
    private ImageView sendImageButton,sendMessageButton;
    private EditText messageTextBox;

    //For loading message

    private static final int TOTAL_MSG_TO_LOAD = 10;
    private int mCurrentPage = 1;
    private int itemPos = 0;
    private String mLastKey="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_chat);

        Intent i = getIntent();
        senderId = i.getStringExtra("sender_id");
        //    Toast.makeText(getApplicationContext(),senderId,Toast.LENGTH_SHORT).show();

        //Widget initialization
        sendImageButton = (ImageView) findViewById(R.id.send_image);
        sendMessageButton = (ImageView) findViewById(R.id.message_send_button);
        messageTextBox = (EditText) findViewById(R.id.send_text_message);


        //Firebase storage
        mImageStorage = FirebaseStorage.getInstance().getReference();

        //RecyclerView

        mAdapter = new MessageAdapter(this,mMessageList);
        mMessageView = (RecyclerView) findViewById(R.id.message_list);

        mLinearLayout = new LinearLayoutManager(this);
        mMessageView.setHasFixedSize(true);
        mMessageView.setLayoutManager(mLinearLayout);
        loadMessage();
        mMessageView.setAdapter(mAdapter);

        //Firebase init
        //Firebase Auth init
        mAuth = FirebaseAuth.getInstance();

        //Firebase init
        //Firebase Auth init
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        //Toolbar initialization
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);


        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        DatabaseReference doctorInfo = FirebaseDatabase.getInstance().getReference().child("Doctor");
        doctorInfo.child(senderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getSupportActionBar().setTitle(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        //Chating Activity
        mDatabase.child("Chat").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(senderId)){
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen",false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/"+userId+"/"+senderId,chatAddMap);
                    chatUserMap.put("Chat/"+senderId+"/"+userId,chatAddMap);


                    mDatabase.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError != null){
                                Log.d(TAG,"Message sending failed for, database failure.");
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Onclick for send button
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }


    //For sending message patient to doctor
    public void sendMessage(){

        String message = messageTextBox.getText().toString();

        if(!TextUtils.isEmpty(message)){

            String current_user_ref = "Messages/"+userId+"/"+senderId;
            String doctor_ref = "Messages/"+senderId+"/"+userId;

            DatabaseReference user_message_push = mDatabase.child("Messages")
                    .child(userId).child(senderId).push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("msg",message);
            messageMap.put("seen",false);
            messageMap.put("type","text");
            messageMap.put("time",ServerValue.TIMESTAMP);
            messageMap.put("from",userId);
            messageMap.put("to",senderId);

            Map messageUserMap = new HashMap();
            messageUserMap.put(doctor_ref+"/"+push_id,messageMap);
            messageUserMap.put(current_user_ref+"/"+push_id,messageMap);


            messageTextBox.setText("");

            mDatabase.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null){
                        Log.d(TAG,"Message sending failed for, database failure.");
                    }
                }
            });
        }
    }

    //For getting message from database
    public void loadMessage(){

        mAuth = FirebaseAuth.getInstance();

        String dId = mAuth.getCurrentUser().getUid();

        //  Toast.makeText(getApplicationContext(),patientId,Toast.LENGTH_SHORT).show();
        DatabaseReference retrieveMessage = FirebaseDatabase.getInstance().getReference().child("Messages").child(dId)
                .child(senderId);
        Query msgQuery = retrieveMessage;


        msgQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message1 message = dataSnapshot.getValue(Message1.class);

                itemPos++;
                if(itemPos == 1){
                    mLastKey = dataSnapshot.getKey();
                }

                mMessageList.add(message);
                mAdapter.notifyDataSetChanged();
                mMessageView.scrollToPosition(mMessageList.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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

    //Send Image code
    public void sendImageP(View view){
        PickImageDialog.build(new PickSetup()).show(this);
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {

            Uri image = pickResult.getUri();

            final String current_user_ref = "Messages/"+userId+"/"+senderId;
            final String doctor_ref = "Messages/"+senderId+"/"+userId;

            DatabaseReference user_message_push = mDatabase.child("Messages")
                    .child(userId).child(senderId).push();

            final String push_id = user_message_push.getKey();

            StorageReference imageRef = mImageStorage.child("message_image").child(push_id+".jpg");
            imageRef.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()){
                        @SuppressWarnings("VisibleForTests") final String imageUrl = mImageStorage.getDownloadUrl().toString();
                        mDatabase.child("image").setValue(imageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Map messageMap = new HashMap();
                                    messageMap.put("msg",imageUrl);
                                    messageMap.put("seen",false);
                                    messageMap.put("type","image");
                                    messageMap.put("time",ServerValue.TIMESTAMP);
                                    messageMap.put("from",userId);
                                    messageMap.put("to",senderId);

                                    Map messageUserMap = new HashMap();
                                    messageUserMap.put(current_user_ref+"/"+push_id,messageMap);
                                    messageUserMap.put(doctor_ref+"/"+push_id,messageMap);

                                    messageTextBox.setText("");

                                    mDatabase.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if(databaseError != null){
                                                Log.d(TAG,"Message sending failed for, database failure.");
                                            }
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }

                }
            });


        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, pickResult.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
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

            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_profile){
            startActivity(new Intent(PatientChatActivity.this, DoctorProfile.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_setting){
            startActivity(new Intent(PatientChatActivity.this, DoctorSetting.class));
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.main_menu_logout){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            mAuth.signOut();
            startActivity(new Intent(PatientChatActivity.this, LoginActivity.class));
            finish();
            return true;
        }

        else if(item.getItemId()==R.id.main_menu_pres){
            startActivity(new Intent(PatientChatActivity.this, PrescriptionList.class));
            finish();
            return true;
        }
        return true;
    }
}
