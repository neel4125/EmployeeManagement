package com.example.employeemanagement.activity.supervisor;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.MainActivity;
import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.supervisor.adapter.EmployeeAdapter;
import com.example.employeemanagement.model.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EmployeeListActivitySupervisor extends AppCompatActivity {
    ImageView ic_back;
    private DatabaseReference mDatabase;
    LinearLayout dateView;
    TextView txtDay,txtDate,txtHeading;
    public static String type;
    ValueEventListener listner1,listner3;
    String Tag;
    ArrayList<Employee> myListData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);
        Intent i = getIntent();
        TextView txtHeading = (TextView)findViewById(R.id.txtHeading);

        Bundle bundle = i.getExtras();
         type = bundle.getString("TYPE");
        if(type.equals("NORMAL_LIST"))
        {
            Tag = "NORMAL_LIST";
            txtHeading.setText("Employee List");
        }
        else if(type.equals("UPLOAD_SCHEDULE"))
        {
            Tag = "UPLOAD_SCHEDULE";
            txtHeading.setText("Upload Schedule");
        }
        else if(type.equals("UPLOAD_PAYSTUB"))
        {
            Tag = "UPLOAD_PAYSTUB";
            txtHeading.setText("Upload Paystub");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

       listner1 =  mDatabase.child("User").orderByChild("userType").equalTo("Employee")
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
                            myListData.add(employee);
                        }
                        //Attaching recycler view to custom adapter and passing arraylist
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                        EmployeeAdapter adapter = new EmployeeAdapter(myListData,EmployeeListActivitySupervisor.this,Tag);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(EmployeeListActivitySupervisor.this));
                        recyclerView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        //Get Ids of all the required fields
        ic_back = (ImageView) findViewById(R.id.imgBack);

        //Handle click on back button arrow
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        //check as per the tag and visible and hide the date view
//        if(Tag.equalsIgnoreCase("MarkAttendance"))
//        {
//            dateView.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            dateView.setVisibility(View.GONE);
//        }


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
