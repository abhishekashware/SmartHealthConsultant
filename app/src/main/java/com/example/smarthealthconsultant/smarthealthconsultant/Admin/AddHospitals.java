package com.example.smarthealthconsultant.smarthealthconsultant.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.ModelClass.Hospitals;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddHospitals extends AppCompatActivity {
     public EditText name,city;
     public Button button,button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospitals);
        name=findViewById(R.id.name);
        city=findViewById(R.id.city);
        button=findViewById(R.id.add);
        button2=findViewById(R.id.remove);
        final String hos_name=name.getText().toString();
        final String hos_city=city.getText().toString();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Hospitals");
                final String hos_name=name.getText().toString();
                final String hos_city=city.getText().toString();
                Hospitals hospitals=new Hospitals(hos_name,hos_city,"Default");
                if(TextUtils.isEmpty(hos_name)||TextUtils.isEmpty(hos_city)){
                    Toast.makeText(AddHospitals.this,"Please Enter Details",Toast.LENGTH_SHORT).show();
                }
                else {
                    reference.child(hos_name).setValue(hospitals).addOnCompleteListener(AddHospitals.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddHospitals.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddHospitals.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddHospitals.this,RemoveHospitals.class);
                startActivity(intent);
            }
        });

    }
}