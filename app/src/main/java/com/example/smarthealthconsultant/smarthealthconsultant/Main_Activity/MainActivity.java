package com.example.smarthealthconsultant.smarthealthconsultant.Main_Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.smarthealthconsultant.R;
import com.example.smarthealthconsultant.smarthealthconsultant.Appointment.AllAppointments;
import com.example.smarthealthconsultant.smarthealthconsultant.Appointment.AvailableDoctorsForAppointment;
import com.example.smarthealthconsultant.smarthealthconsultant.Login_Activity.LoginPage;
import com.example.smarthealthconsultant.smarthealthconsultant.Appointment.AppointmentForm;
import com.example.smarthealthconsultant.smarthealthconsultant.Doctor.DoctorFragment;
import com.example.smarthealthconsultant.smarthealthconsultant.FindDoctor.FindDoctorFragment;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainFragmentClient.OnFragmentInteractionListener,MainFragmentDoctor.OnFragmentInteractionListener,MainFragmentAdmin.OnFragmentInteractionListener,
        DoctorFragment.OnFragmentInteractionListener, FindDoctorFragment.OnFragmentInteractionListener
     , AppointmentForm.OnFragmentInteractionListener {
    public NavigationView navigationView = null;
    public Toolbar toolbar = null;
    public String Doctor;
    public String Admin;
    public String Patient;
    public FirebaseAuth auth;
    public String user;
    public int layout;

    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user=getIntent().getStringExtra("user");
        auth=FirebaseAuth.getInstance();
        Toast.makeText(MainActivity.this, user, Toast.LENGTH_SHORT).show();
        changeStatusBarColor();

        //Patient
        if(user.equals("Patient")) {
            setContentView(R.layout.activity_main);
            MainFragmentClient fragmentclient = new MainFragmentClient();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragmentclient);
            fragmentTransaction.commit();
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);

            NavigationView navigationView =  findViewById(R.id.nav_view1);
            navigationView.setNavigationItemSelectedListener(this);
           // View hview=navigationView.getHeaderView(0);

            View hview=navigationView.getHeaderView(0);
            CircleImageView circleImageView=hview.findViewById(R.id.profile_image);
            final TextView nav_user=hview.findViewById(R.id.username);
            TextView nav_email=hview.findViewById(R.id.email);
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Patient").child(auth.getCurrentUser().getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name=(String) snapshot.child("name").getValue();
                    nav_user.setText(name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            circleImageView.setImageResource(R.drawable.patient_icon);
            nav_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());


        }


        //Doctor
        else if(user.equals("Doctor")){
            setContentView(R.layout.activity_main_doctor);
            MainFragmentDoctor fragmentdoctor = new MainFragmentDoctor();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragmentdoctor);
            fragmentTransaction.commit();
            drawer = findViewById(R.id.drawer_layout2);
            NavigationView navigationView =  findViewById(R.id.nav_view2);
            navigationView.setNavigationItemSelectedListener(this);

            View hview=navigationView.getHeaderView(0);
            CircleImageView circleImageView=hview.findViewById(R.id.profile_image);
            final TextView nav_user=hview.findViewById(R.id.username);
            TextView nav_email=hview.findViewById(R.id.email);
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Doctor").child(auth.getCurrentUser().getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name=(String) snapshot.child("name").getValue();
                    nav_user.setText(name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            circleImageView.setImageResource(R.drawable.doctor_icon);
            nav_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());


        }
        //Admin
        if(user.equals("Admin")){
            setContentView(R.layout.activity_main_admin);

            MainFragmentAdmin fragmentadmin = new MainFragmentAdmin();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragmentadmin);
            fragmentTransaction.commit();
             drawer = (DrawerLayout) findViewById(R.id.drawer_layout3);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view3);
            navigationView.setNavigationItemSelectedListener(this);
            View hview=navigationView.getHeaderView(0);
        //    CircleImageView circleImageView=hview.findViewById(R.id.profile_image);
            final TextView nav_user=hview.findViewById(R.id.username);
            TextView nav_email=hview.findViewById(R.id.email);
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Admin").child(auth.getCurrentUser().getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name=(String) snapshot.child("name").getValue();
                    nav_user.setText(name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            nav_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

//            TextView textView2=findViewById(R.id.email);
//            textView2.setText(Objects.requireNonNull(auth.getCurrentUser()).getEmail());
        }





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                      //  .setAction("Action", null).show();
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "108", null)));
                Toast.makeText(getApplicationContext(), "Call Emergency Helpline !!", Toast.LENGTH_SHORT).show();
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else{

                    Toast.makeText(getApplicationContext(),"Signing Out....",Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(MainActivity.this, LoginPage.class);
                    startActivity(intent);

        }
//        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public  void onFragmentInteraction(Uri uri){

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //client
              if(id==R.id.nav_homec){

                MainFragmentClient fragmentclient = new MainFragmentClient();
                FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragmentclient);
                fragmentTransaction.commit();

        } else if (id == R.id.nav_book) {

            AppointmentForm fragment = new AppointmentForm();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_my) {
                  Intent intent=new Intent(MainActivity.this, AllAppointments.class);
                  startActivity(intent);
        }
//              else if(id==R.id.prescription){
//                Intent intent=new Intent(MainActivity.this, PrescriptionList.class);
//                startActivity(intent);
//              }
        else if(id == R.id.nav_find_doctor){
            FindDoctorFragment fragment = new  FindDoctorFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }


        //doctor
        else if(id == R.id.nav_chatd){
//                  FindDoctorFragment fragment = new  FindDoctorFragment();
//                  FragmentTransaction fragmentTransaction =
//                          getSupportFragmentManager().beginTransaction();
//                  fragmentTransaction.replace(R.id.fragment_container, fragment);
//                  fragmentTransaction.commit();
              }

        else if(id == R.id.nav_req){
//                   fragment = new  FindDoctorFragment();
//                  FragmentTransaction fragmentTransaction =
//                          getSupportFragmentManager().beginTransaction();
//                  fragmentTransaction.replace(R.id.fragment_container, fragment);
//                  fragmentTransaction.commit();
              }
        else if(id == R.id.nav_homed){
                  MainFragmentDoctor fragment = new MainFragmentDoctor();
                  FragmentTransaction fragmentTransaction =
                          getSupportFragmentManager().beginTransaction();
                  fragmentTransaction.replace(R.id.fragment_container, fragment);
                  fragmentTransaction.commit();
              }

        //common
        else if (id == R.id.nav_logout) {
                auth= FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent=new Intent(MainActivity.this, LoginPage.class);
                startActivity(intent);
              }

        else if (id == R.id.nav_share) {
                Intent share=new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                String sharebody="Here is the sharing content";
                share.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
                share.putExtra(Intent.EXTRA_TEXT,sharebody);
                startActivity(Intent.createChooser(share,"Share Via"));
           // Toast.makeText(MainActivity.this,"Share feature will be available soon",Toast.LENGTH_SHORT).show();
              }

        //admin
              else if (id == R.id.nav_homea) {
                  MainFragmentAdmin fragment = new MainFragmentAdmin();
                  FragmentTransaction fragmentTransaction =
                          getSupportFragmentManager().beginTransaction();
                  fragmentTransaction.replace(R.id.fragment_container, fragment);
                  fragmentTransaction.commit();
              }
              else if (id == R.id.nav_manage_doctor) {
//                   fragment = new MainFragmentAdmin();
//                  FragmentTransaction fragmentTransaction =
//                          getSupportFragmentManager().beginTransaction();
//                  fragmentTransaction.replace(R.id.fragment_container, fragment);
//                  fragmentTransaction.commit();
              }
              else if (id == R.id.nav_manage_patient) {
                  MainFragmentAdmin fragment = new MainFragmentAdmin();
                  FragmentTransaction fragmentTransaction =
                          getSupportFragmentManager().beginTransaction();
                  fragmentTransaction.replace(R.id.fragment_container, fragment);
                  fragmentTransaction.commit();
              }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
