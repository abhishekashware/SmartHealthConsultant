package com.example.smarthealthconsultant.smarthealthconsultant.PatientDirectory;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.chat.Doctors;
import com.example.smarthealthconsultant.smarthealthconsultant.chat.Helper;
import com.example.smarthealthconsultant.smarthealthconsultant.chat.Message1;
import com.example.smarthealthconsultant.smarthealthconsultant.chat.PatientChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by arif on 11-Nov-17.
 */

public class PatientInboxListAdapter extends RecyclerView.Adapter<PatientInboxListAdapter.PatientInboxListViewHolder> {

    private List<Message1> mMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference userInfo;
    private Context context;

    public PatientInboxListAdapter(PatientInbox context, List<Message1> mMessageList){
        this.context = context;
        this.mMessageList = mMessageList;
    }


    @Override
    public PatientInboxListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_doctor_home_message_list,parent,false);
        return new PatientInboxListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull final PatientInboxListViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance();
        final List<Patient> pt = new ArrayList<>();
        final Message1 msg = mMessageList.get(position);

        final DatabaseReference senderInfo = FirebaseDatabase.getInstance().getReference("Doctor");
        final String pId = mAuth.getCurrentUser().getUid();

        if(!pId.equals(msg.getFrom())) {
            senderInfo.child(msg.getFrom()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Doctors pp = dataSnapshot.getValue(Doctors.class);
                    //  Toast.makeText(DoctorHome.this, message.getMsg(), Toast.LENGTH_SHORT).show();

                    holder.mMessageSender.setText(Helper.subStringH(pp.getName(), 14));
                    holder.mMessageText.setText(Helper.subStringH(msg.getMsg(), 19));
                    holder.mTime.setText(Helper.getTimeAgo(msg.getTime(), context));
                    Picasso.with(context).load(pp.getImage()).placeholder(R.drawable.doctor_icon).into(holder.patientProfileImage);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(context, PatientChatActivity.class);
                            i.putExtra("sender_id", msg.getFrom());
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                            mMessageList.clear();
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            senderInfo.child(msg.getTo()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Doctors pp = dataSnapshot.getValue(Doctors.class);

                    holder.mMessageSender.setText(Helper.subStringH(pp.getName(), 14));
                    holder.mMessageText.setText(Helper.subStringH(msg.getMsg(), 19));
                    holder.mTime.setText(Helper.getTimeAgo(msg.getTime(), context));
                    Picasso.with(context).load(pp.getImage()).placeholder(R.drawable.doctor_icon).into(holder.patientProfileImage);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                    Intent i = new Intent(context, PatientChatActivity.class);
                    i.putExtra("sender_id", msg.getTo());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    mMessageList.clear();
                        }
                    });
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class PatientInboxListViewHolder extends RecyclerView.ViewHolder{

        CircleImageView patientProfileImage;
        TextView mMessageSender,mMessageText,mTime;

        public PatientInboxListViewHolder(View itemView) {
            super(itemView);

            patientProfileImage =  itemView.findViewById(R.id.msg_list_profile_img);
            mMessageSender = (TextView) itemView.findViewById(R.id.message_sender);
            mMessageText = (TextView) itemView.findViewById(R.id.msg_from_patient);
            mTime = (TextView) itemView.findViewById(R.id.msg_send_time);
        }
    }

}
