package com.example.smarthealthconsultant.smarthealthconsultant.Appointment.apporequests;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.ModelClass.Patients;
import com.example.smarthealthconsultant.smarthealthconsultant.ModelClass.PatientsAdapter;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppoReqAdapter extends RecyclerView.Adapter<AppoReqAdapter.ApViewHolder> {


    private ArrayList<AppoReqClass> list;
    private Context context;

    public AppoReqAdapter(Context context, ArrayList<AppoReqClass> list){
        this.context = context;
        this.list = list;
    }

    @NotNull
    @Override
    public AppoReqAdapter.ApViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_doctor_online_list, parent, false);
        return new AppoReqAdapter.ApViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AppoReqAdapter.ApViewHolder holder, int position) {
        holder.setEmail(list.get(position).getEmail());
        holder.setName(list.get(position).getDoc_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext()).setTitle("Do you want to Accept?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context,"Accepted",Toast.LENGTH_SHORT).show();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context,"Denied",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ApViewHolder extends RecyclerView.ViewHolder{

        View mItem;
        public ApViewHolder(View itemView) {
            super(itemView);
            mItem = itemView;
        }
        public void setName(String name){
            TextView dname=mItem.findViewById(R.id.doctor_name);
            dname.setText(name);
        }
        public void setEmail(String ctg){
            TextView mDoctor = (TextView) mItem.findViewById(R.id.doctor_category);
            mDoctor.setText(ctg);
        }
        public void setImage(String url, Context con){
            CircleImageView mDoctorImage = (CircleImageView) mItem.findViewById(R.id.doctor_image);
         //   Picasso.with(con).load(url).placeholder(R.drawable.doctor_icon).into(mDoctorImage);
        }


    }
}
