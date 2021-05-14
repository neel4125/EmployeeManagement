package com.example.employeemanagement.activity.supervisor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.MainActivity;
import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.employee.adapter.AnnousmentAdapter;
import com.example.employeemanagement.activity.supervisor.adapter.AnnousmentAdapterSupervisor;
import com.example.employeemanagement.activity.supervisor.adapter.OnItemClickListener;
import com.example.employeemanagement.model.Announcement;
import com.example.employeemanagement.model.Employee;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class AnnouncementListActivitySupervisor extends AppCompatActivity {
    ArrayList<Announcement> announcementArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    FloatingActionButton fab;
    private DatabaseReference mDatabase;
    ValueEventListener listner1,listner3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_employee);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        ImageView ic_back = (ImageView) findViewById(R.id.imgBack);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getAnnousmentList();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnnousment();
            }
        });

//        Announcement announcement = new Announcement();
//        announcement.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
//
//        //Add dummy objects to list
//        announcementArrayList.add(announcement);
//        announcementArrayList.add(announcement);
//        announcementArrayList.add(announcement);
//        announcementArrayList.add(announcement);


//        //Attaching recycler view to custom adapter and passing arraylist
//        AnnousmentAdapterSupervisor adapter = new AnnousmentAdapterSupervisor(announcementArrayList, this);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);

    }

    private void getAnnousmentList() {
        announcementArrayList.clear();
      listner3 =  mDatabase.child("Announcement")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //ArrayList<Employee> employeeArrayList = new ArrayList<>();
                        for(DataSnapshot uniqueKeySnapshot : snapshot.getChildren()){
                            //Loop 1 to go through all the child nodes of users
                            String key = uniqueKeySnapshot.getKey();
                            Log.e("Total ","Value is"+key);
                            Log.e("Total ","User Id is"+Constants.employee.getUserId());
                            if (key.equals(Constants.employee.getUserId()))
                            {
                               listner1 =  mDatabase.child("Announcement").child(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        //ArrayList<Employee> employeeArrayList = new ArrayList<>();
                                        announcementArrayList.clear();
                                        for(DataSnapshot uniqueKeySnapshot : snapshot.getChildren()){
                                            //Loop 1 to go through all the child nodes of users
                                            Announcement announcement = new Announcement();
                                            String key = uniqueKeySnapshot.getKey();

                                            Log.e("Total ","Key is"+key);
                                            String text = String.valueOf(uniqueKeySnapshot.getValue());
                                            Log.e("Total ","text is"+text);
                                            announcement.setId(key);
                                            announcement.setText(text);
                                            announcementArrayList.add(announcement);
                                        }

                                        Collections.reverse(announcementArrayList);
                                        //Attaching recycler view to custom adapter and passing arraylist
                                        AnnousmentAdapterSupervisor adapter = new AnnousmentAdapterSupervisor(announcementArrayList, AnnouncementListActivitySupervisor.this,new OnItemClickListener() {
                                            @Override public void onItemClick(Announcement item) {
                                                //Toast.makeText(AnnouncementListActivitySupervisor.class, "Item Clicked", Toast.LENGTH_LONG).show();
//                                                Toast.makeText(this,"Item Clicked"+item.getId()+item.getText(),Toast.LENGTH_LONG).show();
                                                Log.e("Item Clicked","Value is"+item.getId()+item.getText());
                                                editAnnousment(item);
                                            }
                                        });
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(AnnouncementListActivitySupervisor.this));
                                        recyclerView.setAdapter(adapter);
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
//                            Employee employee = uniqueKeySnapshot.getValue(Employee.class);
//                            Log.e("Logged In user","Is Found"+employee.getEmployeeNumber());
//                            String passwordValue = String.valueOf(uniqueKeySnapshot.child("password").getValue());
//                            Log.e("Logged In user","passwordValue"+passwordValue);
//                            if(passwordValue.equals(password.getText().toString()))
//                            {
//                                isUserFound = true;
//                                employee.setUserId(uniqueKeySnapshot.getKey());
//                                Constants.employee = employee;
//                                Log.e("Logged In user","Is Found"+uniqueKeySnapshot.child("first_name").getValue());
//                            }
                        }


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void addAnnousment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AnnouncementListActivitySupervisor.this);
        builder.setTitle("Announcement");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(AnnouncementListActivitySupervisor.this).inflate(R.layout.text_inpu_password, (ViewGroup) findViewById(android.R.id.content), false);
        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String m_Text = input.getText().toString();
                Log.e("Enter Value", "Is HEre" + m_Text);
                mDatabase.child("Announcement").child(Constants.employee.getUserId()).push().setValue(m_Text);

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void editAnnousment(Announcement announcement) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AnnouncementListActivitySupervisor.this);
        builder.setTitle("Announcement");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(AnnouncementListActivitySupervisor.this).inflate(R.layout.text_inpu_password, (ViewGroup) findViewById(android.R.id.content), false);
        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);
        input.setText(announcement.getText());
        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String m_Text = input.getText().toString();
                Log.e("Enter Value", "Is HEre" + m_Text);
                mDatabase.child("Announcement").child(Constants.employee.getUserId()).child(announcement.getId()).setValue(m_Text);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(listner1 != null)
        {
            mDatabase.removeEventListener(listner1);
        }
        if(listner3 != null)
        {
            mDatabase.removeEventListener(listner3);
        }

    }
}
