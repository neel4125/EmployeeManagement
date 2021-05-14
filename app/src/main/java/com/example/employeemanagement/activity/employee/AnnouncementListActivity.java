package com.example.employeemanagement.activity.employee;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.employee.adapter.AnnousmentAdapter;
import com.example.employeemanagement.model.Announcement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class AnnouncementListActivity extends AppCompatActivity {
    ArrayList<Announcement> announcementArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    ValueEventListener listner1,listner3;
    FloatingActionButton fab;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_employee);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        ImageView ic_back = (ImageView)findViewById(R.id.imgBack);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getAnnousmentList();
//        Announcement announcement = new Announcement();
//        announcement.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
//
//        //Add dummy objects to list
//        announcementArrayList.add(announcement);
//        announcementArrayList.add(announcement);
//        announcementArrayList.add(announcement);
//        announcementArrayList.add(announcement);
//
//
//        //Attaching recycler view to custom adapter and passing arraylist
//        AnnousmentAdapter adapter = new AnnousmentAdapter(announcementArrayList,this);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);

    }
    private void getAnnousmentList() {

        listner3 =  mDatabase.child("Announcement")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        announcementArrayList.clear();
                        //ArrayList<Employee> employeeArrayList = new ArrayList<>();
                        for(DataSnapshot uniqueKeySnapshot : snapshot.getChildren()){
                            //Loop 1 to go through all the child nodes of users
                            String key = uniqueKeySnapshot.getKey();
                            Log.e("Total ","Value is"+key);
                            Log.e("Total ","User Id is"+ Constants.employee.getUserId());
                            listner1 = mDatabase.child("Announcement").child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    //ArrayList<Employee> employeeArrayList = new ArrayList<>();

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
                                    AnnousmentAdapter adapter = new AnnousmentAdapter(announcementArrayList,AnnouncementListActivity.this);
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(AnnouncementListActivity.this));
                                    recyclerView.setAdapter(adapter);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        AnnousmentAdapter adapter = new AnnousmentAdapter(announcementArrayList,AnnouncementListActivity.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AnnouncementListActivity.this));
                        recyclerView.setAdapter(adapter);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
