package com.example.smarthealthconsultant.smarthealthconsultant.SignUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.Login_Activity.LoginActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.Main_Activity.MainActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.ModelClass.UserHelper;
import com.example.smarthealthconsultant.smarthealthconsultant.ModelClass.UserHelperDoctor;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;
    public Button button;
    public FirebaseDatabase root;
    public EditText email,password,username;
    public AutoCompleteTextView category;
    public String usertype;
    public TextInputLayout textInputLayout;
    public TextView textView;
    public DatabaseReference reference;
    public LinearLayout linearLayout;
    public ProgressDialog dialog;
    public FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usertype=getIntent().getStringExtra("user");
        dialog=new ProgressDialog(this);
        dialog.setTitle("Registering...");
        dialog.setMessage("Please Wait for a while");
        dialog.setCanceledOnTouchOutside(false);
        category=findViewById(R.id.category);
        String[] spel=getResources().getStringArray(R.array.specialists);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,spel);
        category.setAdapter(arrayAdapter);
//        textView=findViewById(R.id.user);
//        textView.setText(usertype);
        email=findViewById(R.id.email);

        floatingActionButton=findViewById(R.id.fab);
        password=findViewById(R.id.password);
        username=findViewById(R.id.username);
        linearLayout=findViewById(R.id.type);
        linearLayout.setEnabled(false);
        if(usertype.equals("Doctor")){
            linearLayout.setEnabled(true);
            linearLayout.setVisibility(View.VISIBLE);
        }
        textInputLayout=findViewById(R.id.category_l);
        final String email1=email.getText().toString();
        final String password1=password.getText().toString();
        final String username1=username.getText().toString();
        mAuth=FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(usertype+" Register Form");
        button=findViewById(R.id.button);
        floatingActionButton.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final String email1=email.getText().toString();
               final String password1=password.getText().toString();
               final String username1=username.getText().toString();
               if(TextUtils.isEmpty(username1)||TextUtils.isEmpty(email1)||TextUtils.isEmpty(password1)){
                   Toast.makeText(SignUpActivity.this,"Please Enter all Fields",Toast.LENGTH_SHORT).show();
               }
               else {
                   dialog.show();
                   mAuth.createUserWithEmailAndPassword(email1, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               FirebaseUser user = mAuth.getCurrentUser();
                               String userid = user.getUid();
                               reference = FirebaseDatabase.getInstance().getReference(usertype).child(userid);
                               final String email1 = email.getText().toString();
                               final String password1 = password.getText().toString();
                               final String username1 = username.getText().toString();
                               final String image = "Default";
                               if (usertype.equals("Doctor")) {
                                    final String cat=category.getText().toString();
                                   UserHelperDoctor userHelperDoctor = new UserHelperDoctor(username1, email1, password1, usertype, userid, image,cat);
                                   reference.setValue(userHelperDoctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()) {
                                               dialog.dismiss();
                                               Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                               intent.putExtra("user", usertype);
                                               Toast.makeText(SignUpActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                               startActivity(intent);
                                               finish();
                                           }
                                       }
                                   });

                               } else{
                                  textInputLayout.setEnabled(false);
                                  textInputLayout.setFocusable(false);
                                   textInputLayout.setVisibility(View.GONE);
                                   UserHelper userHelper = new UserHelper(username1, email1, password1, usertype, userid, image);
                               reference.setValue(userHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()) {
                                           dialog.dismiss();
                                           Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                           intent.putExtra("user", usertype);
                                           Toast.makeText(SignUpActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                           startActivity(intent);
                                           finish();
                                       }
                                   }
                               });

                           }

//                               String uid=mAuth.getCurrentUser().getUid();

//                               DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Messages").child(uid);
//                               reference1.child("Sent").setValue("");
//                               reference1.child("Recieved").setValue("");
                           }
                           else {
                               dialog.dismiss();
                               Toast.makeText(SignUpActivity.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }

           }
       });

    }


}