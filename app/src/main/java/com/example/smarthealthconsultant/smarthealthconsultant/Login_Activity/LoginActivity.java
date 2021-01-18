package com.example.smarthealthconsultant.smarthealthconsultant.Login_Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.Main_Activity.MainActivity;
import com.example.smarthealthconsultant.smarthealthconsultant.SignUp.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public Button button;
    public Button button2;
    private FirebaseAuth mAuth;
    public EditText editText1;
    public  EditText editText2;
    private TextView text;
    public ProgressBar progressBar;
    public ProgressDialog progressDialog;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        changeStatusBarColor();
        text=findViewById(R.id.txt);
        button=findViewById(R.id.btn_login);
        button2=findViewById(R.id.btn_signup);
        user=getIntent().getStringExtra("user");
        text.setText(user);
        editText1=findViewById(R.id.email);
        progressDialog=new ProgressDialog(this);
        progressBar=findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        editText1=findViewById(R.id.password);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if(user.equals("Patient")||user.equals("Doctor")) {
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            });
        }
        else{
            button2.setEnabled(false);
            button2.setVisibility(View.INVISIBLE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email=editText1.getText().toString();
                final String password=editText2.getText().toString();
                if(email.isEmpty()||password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please fill all the requirements...",Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.setTitle("Signing In...");
                    progressDialog.setMessage("Please Wait for a while");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser u = mAuth.getCurrentUser();
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(user);

                                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                final String uid = mAuth.getCurrentUser().getUid();
                                                if (snapshot.hasChild(uid)) {
                                                    progressDialog.dismiss();
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    intent.putExtra("user", user);
                                                    Toast.makeText(LoginActivity.this, "Logged in Succesfully",
                                                            Toast.LENGTH_SHORT).show();
                                                    startActivity(intent);
                                                } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(LoginActivity.this, "This " + user + " Does not Exist", Toast.LENGTH_SHORT).show();
                                                    mAuth.signOut();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        // Sign in success, update UI with the signed-in user's information

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Invalid Id or Password.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
            }
        });


    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorSecondaryAccent));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        editText1=findViewById(R.id.email);
        editText2=findViewById(R.id.password);
//        FirebaseUser current=mAuth.getCurrentUser();
//        if(current!=null){
//            Intent intent=new Intent(LoginActivityC.this, MainActivity.class);
//            intent.putExtra("Patient","Patient");
//            startActivity(intent);
//        }
//        else{
//
//            Toast.makeText(LoginActivityC.this,"Please Sign in",Toast.LENGTH_SHORT).show();
//
//        }
   }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(LoginActivity.this, LoginPage.class);
        startActivity(intent);
    }

    public void register(View view){
        Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }
    public void reset(View view){
        Intent intent=new Intent(LoginActivity.this,ResetPasswordActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }
}