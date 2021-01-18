package com.example.smarthealthconsultant.smarthealthconsultant.Login_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.smarthealthconsultant.R;

import com.example.smarthealthconsultant.smarthealthconsultant.SignUp.SignUpActivity;

public class LoginPage extends AppCompatActivity {
    public Button button1;
    public Button button2;
    public Button button3;
    public Button button4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        button1=findViewById(R.id.patient);
        button2=findViewById(R.id.doctor);
        button3=findViewById(R.id.admin);
        button4=findViewById(R.id.btn_signup);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1=new Intent(LoginPage.this, LoginActivity.class);
                intent1.putExtra("user","Patient");
                startActivity(intent1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1=new Intent(LoginPage.this, LoginActivity.class);
                intent1.putExtra("user","Doctor");
                startActivity(intent1);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1=new Intent(LoginPage.this, LoginActivity.class);
                intent1.putExtra("user", "Admin");
                startActivity(intent1);
            }
        });
        //making status bar transparent
        changeStatusBarColor();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

//    public void onSignUp(View view){
//        Intent intent4=new Intent(LoginPage.this, SignUpActivity.class);
//        startActivity(intent4);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}