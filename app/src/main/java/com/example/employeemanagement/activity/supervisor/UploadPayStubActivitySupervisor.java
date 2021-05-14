package com.example.employeemanagement.activity.supervisor;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

public class UploadPayStubActivitySupervisor extends AppCompatActivity {
    ImageView ic_back;

    LinearLayout dateView;
    TextView txtDay,txtDate;
    String Tag;
    ArrayList<Employee> myListData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pay_stub);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        Employee employee = new Employee();
        employee.setFirst_name("Kishan");
        employee.setDesignation("Developer");
        myListData.add(employee);
        myListData.add(employee);
        myListData.add(employee);
        myListData.add(employee);
        myListData.add(employee);
        myListData.add(employee);
        //Get tag as per the selection from the HomeFragment
        Tag = bundle.getString("TAG");

        //Get Ids of all the required fields
        ic_back = (ImageView) findViewById(R.id.imgBack);

        //Handle click on back button arrow
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //check as per the tag and visible and hide the date view
        if(Tag.equalsIgnoreCase("MarkAttendance"))
        {
            dateView.setVisibility(View.VISIBLE);
        }
        else
        {
            dateView.setVisibility(View.GONE);
        }

        //Attaching recycler view to custom adapter and passing arraylist
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        EmployeeAdapter adapter = new EmployeeAdapter(myListData,this,Tag);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }



}
