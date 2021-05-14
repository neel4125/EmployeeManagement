package com.example.employeemanagement.activity.supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.supervisor.adapter.EmployeeAdapter;
import com.example.employeemanagement.model.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LeaveRequestActivitySupervisor extends AppCompatActivity {
    ImageView ic_back;
    ValueEventListener listner1,listner3;
    LinearLayout dateView;
    TextView txtDay,txtDate;

    //String Tag;
    private DatabaseReference mDatabase;

    ArrayList<Employee> myListData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request_supervisor);

        //Get Ids of all the required fields
        ic_back = (ImageView) findViewById(R.id.imgBack);

        //Handle click on back button arrow
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        listner1 = mDatabase.child("User").orderByChild("userType").equalTo("Employee")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        myListData.clear();
                        ArrayList<Employee> employeeArrayList = new ArrayList<>();
                        for(DataSnapshot uniqueKeySnapshot : snapshot.getChildren()) {
                            //Loop 1 to go through all the child nodes of users
                            Employee employee = uniqueKeySnapshot.getValue(Employee.class);
                            Log.e("Employee Found","Name is"+employee.getFirst_name());
                            employee.setUserId(uniqueKeySnapshot.getKey());
                            if(employee.getStatus() != null && ((employee.getStatus().equalsIgnoreCase("Pending"))))
                            {
                                myListData.add(employee);
                            }
                        }
                        //Attaching recycler view to custom adapter and passing arraylist
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                        EmployeeAdapter adapter = new EmployeeAdapter(myListData,LeaveRequestActivitySupervisor.this,"LeaveRequest");
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(LeaveRequestActivitySupervisor.this));
                        recyclerView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        //check as per the tag and visible and hide the date view
//        if(Tag.equalsIgnoreCase("MarkAttendance"))
//        {
//            dateView.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            dateView.setVisibility(View.GONE);
//        }

        //Attaching recycler view to custom adapter and passing arraylist
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        EmployeeAdapter adapter = new EmployeeAdapter(myListData,this,"Tag");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(listner1 != null)
        {
            mDatabase.removeEventListener(listner1);
        }

    }
}
