package com.example.employeemanagement.activity.employee;

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

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.supervisor.ChatActivitySupervisor;
import com.example.employeemanagement.activity.supervisor.adapter.EmployeeChatAdapter;
import com.example.employeemanagement.model.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ChatActivityEmployee extends AppCompatActivity {
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
        setContentView(R.layout.activity_chat_list);
        Intent i = getIntent();
        TextView txtHeading = (TextView)findViewById(R.id.txtHeading);

        Log.e("Logged In","User Id"+ Constants.employee.getUserId());
        mDatabase = FirebaseDatabase.getInstance().getReference();

       listner1 =  mDatabase.child("User").orderByChild("userType").equalTo("Supervisor")
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
                        Collections.sort(myListData, new ChatActivitySupervisor.CustomComparator());
                        Collections.reverse(myListData);
                        //Attaching recycler view to custom adapter and passing arraylist
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                        EmployeeChatAdapter adapter = new EmployeeChatAdapter(myListData, ChatActivityEmployee.this,Tag);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivityEmployee.this));
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

    public static class CustomComparator implements Comparator<Employee> {
        @Override
        public int compare(Employee o1, Employee o2) {
            return String.valueOf(o1.getMessageCount()).compareTo(String.valueOf(o2.getMessageCount()));
        }
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
