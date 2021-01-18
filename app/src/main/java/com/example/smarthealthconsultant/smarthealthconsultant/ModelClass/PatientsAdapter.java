package com.example.smarthealthconsultant.smarthealthconsultant.ModelClass;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthealthconsultant.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.PatViewHolder> {


    private ArrayList<Patients> list;
    private Context context;

    public PatientsAdapter(Context context, ArrayList<Patients> list){
        this.context = context;
        this.list = list;
    }

    @NotNull
    @Override
    public PatientsAdapter.PatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_doctor_online_list, parent, false);
        return new PatientsAdapter.PatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PatientsAdapter.PatViewHolder holder, int position) {
        holder.setName(list.get(position).getName());
        holder.setEmail(list.get(position).getEmail());
        holder.setImage(list.get(position).getImage(),context);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"This",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class PatViewHolder extends RecyclerView.ViewHolder{

        View mItem;
        public PatViewHolder(View itemView) {
            super(itemView);
            mItem = itemView;
        }

        public void setName(String name){
            TextView mDoctorName = (TextView) mItem.findViewById(R.id.doctor_name);
            mDoctorName.setText(name);
        }
        public void setEmail(String ctg){
            TextView mDoctorName = (TextView) mItem.findViewById(R.id.doctor_category);
            mDoctorName.setText(ctg);
        }
        public void setImage(String url, Context con){
            CircleImageView mDoctorImage = (CircleImageView) mItem.findViewById(R.id.doctor_image);
            Picasso.with(con).load(url).placeholder(R.drawable.doctor_icon).into(mDoctorImage);
        }


    }

}
