package com.example.smarthealthconsultant.smarthealthconsultant.Appointment;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthealthconsultant.R;


public class FinalAppointment extends AppCompatActivity {

    public  DatePicker picker;
    public  String date;
    public  String id;
    public  String name;
    public  String image;
    public String  category;
    public Button button;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_final_appointment);

        button=findViewById(R.id.confirm);
        picker=findViewById(R.id.datePicker2);
        final String id= getIntent().getStringExtra("doc_id");
        final String name=getIntent().getStringExtra("doc_name");
        final String category=getIntent().getStringExtra("doc_cat");
        image=getIntent().getStringExtra("doc_image");
        date=getDate();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Appointment_Dialog dialog=new Appointment_Dialog(FinalAppointment.this);
                dialog.setDoc_id(id);
                dialog.setDoc_name(name);
                dialog.setDate(date);
                dialog.setCat(category);
                dialog.setImage(getApplicationContext(),image);
                dialog.show();
    }

        });
    }

    public  String getDate(){
        StringBuilder builder=new StringBuilder();
        builder.append((picker.getMonth()+1)+"/");
        builder.append(picker.getDayOfMonth()+"/");
        builder.append(picker.getYear());
        return builder.toString();
    }
}
