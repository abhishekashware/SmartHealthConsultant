package com.example.smarthealthconsultant.smarthealthconsultant.Appointment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smarthealthconsultant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Appointment_Dialog extends Dialog implements View.OnClickListener {
    public Activity activity;
    public Dialog dialog;
    public String doc_id;
    public String docName;
    public String date;
    public  String docImage;
    public String status;
    public String docCat;
    public TextView doc_name,doc_cat,fees;
    public ImageView imageView;
    public boolean c;
    public Button yes,no;
    public Appointment_Dialog(Activity activity){
        super(activity);
        this.activity=activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_appointment_details);
        imageView=findViewById(R.id.image);
        doc_name=findViewById(R.id.name);
        doc_cat=findViewById(R.id.category);
        fees=findViewById(R.id.fees);
        doc_name.setText(docName);
        doc_cat.setText(docCat);
        fees.setText("1500");
        yes=findViewById(R.id.yes);
        no=findViewById(R.id.no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.yes:
                final String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Appointments").child(uid);
                    Map det=new HashMap<>();
                    det.put("email",FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    det.put("date",date);
                    det.put("status","pending");
                    det.put("doc_name",docName);
                    det.put("image",docImage);
                    reference.child(doc_id).setValue(det).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                               // DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Appointments");
                                AlertDialog dialog = new AlertDialog.Builder(getContext()).setMessage("Done Successfully").setPositiveButton("ok",null).show();
                            }
                            else{
                                Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Appointments").child(doc_id).child(uid);
                    reference1.setValue(det).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(),"Done",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                break;
            case R.id.no:
                dismiss();
                break;
            default:
                break;


        }
             dismiss();
    }

    public void setDoc_name(String name){
        this.docName=name;
    }
    public void setImage(Context context,String image){
        this.docImage=image;
    }
    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }
    public void setCat(String cat){
        this.docCat=cat;
    }
    public void setDate(String date){
        this.date=date;
    }
}
