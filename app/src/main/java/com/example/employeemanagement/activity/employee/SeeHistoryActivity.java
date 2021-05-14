package com.example.employeemanagement.activity.employee;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.GlideApp;
import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.employee.adapter.AttendanceAdapter;
import com.example.employeemanagement.model.Attendance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeeHistoryActivity extends AppCompatActivity {
    ArrayList<Attendance> announcementArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    CircleImageView imgProfile;
    TextView userName,userPosition;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_history);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        imgProfile= (CircleImageView)findViewById(R.id.imgProfile);
        userName= (TextView)findViewById(R.id.userName);
        userPosition= (TextView)findViewById(R.id.userPosition);
        ImageView ic_back = (ImageView)findViewById(R.id.imgBack);
        if(Constants.employee != null)
        {
            GlideApp.with(this)
                    .load(Constants.employee.getProfile_pic())
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(imgProfile);
            userName.setText(Constants.employee.getFirst_name()+" "+Constants.employee.getLast_name());
            userPosition.setText(Constants.employee.getDesignation());
        }
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDatabase.child("Attendance").child(Constants.employee.getUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //ArrayList<Employee> employeeArrayList = new ArrayList<>();
                        for(DataSnapshot uniqueKeySnapshot : snapshot.getChildren()){
                            //Loop 1 to go through all the child nodes of users
                            Attendance tempattendance = new Attendance();
                            tempattendance = uniqueKeySnapshot.getValue(Attendance.class);
                            String key = uniqueKeySnapshot.getKey();
                            announcementArrayList.add(tempattendance);

                        }
                        //Attaching recycler view to custom adapter and passing arraylist
                        AttendanceAdapter adapter = new AttendanceAdapter(announcementArrayList, SeeHistoryActivity.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(SeeHistoryActivity.this));
                        recyclerView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        
                    }
                });




    }

}
