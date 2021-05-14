package com.example.employeemanagement.activity.supervisor;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.employee.ClockInOutActivity;
import com.example.employeemanagement.model.Attendance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockInOutActivitySupervisor extends AppCompatActivity {
    ImageView imgBack;
    LinearLayout lilCheckIn,lilCheckOut;
    private DatabaseReference mDatabase;
    ValueEventListener listner1,listner3;
    EditText edtPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in_out);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgBack = (ImageView)findViewById(R.id.imgBack);
        lilCheckIn = (LinearLayout)findViewById(R.id.lilCheckIn);
        lilCheckOut = (LinearLayout)findViewById(R.id.lilCheckOut);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lilCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPassword.getText().toString() == null || (edtPassword.getText().toString().equals("")))
                {
                    Toast.makeText(ClockInOutActivitySupervisor.this,"Please enter password",Toast.LENGTH_SHORT).show();
                }
                else if(!edtPassword.getText().toString().equals(Constants.employee.getPassword()))
                {
                    Toast.makeText(ClockInOutActivitySupervisor.this,"Please enter correct password",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addAttendance("In");
                }

            }
        });
        lilCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPassword.getText().toString() == null || (edtPassword.getText().toString().equals("")))
                {
                    Toast.makeText(ClockInOutActivitySupervisor.this,"Please enter password",Toast.LENGTH_SHORT).show();
                }
                else if(!edtPassword.getText().toString().equals(Constants.employee.getPassword()))
                {
                    Toast.makeText(ClockInOutActivitySupervisor.this,"Please enter correct password",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addAttendance("Out");
                }

            }
        });
    }
    Attendance attendance = null;
    private void addAttendance(String type) {
        attendance = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(System.currentTimeMillis()));
        Log.e("UserId","Is"+Constants.employee.getUserId());
        if(Constants.employee.getUserId() == null)
        {
            return;
        }
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
                            if(tempattendance != null)
                            {
                                if(tempattendance.getDate().equals(dateString))
                                {
                                    Log.e("Value ","One"+attendance);
                                    attendance = new Attendance();
                                    attendance = tempattendance;
                                    attendance.setId(key);
                                }
                            }

                        }
                        Log.e("Value ","two"+attendance);
                        if(attendance != null)
                        {
                            if(type.equals("In"))
                            {
                                if(attendance.getCheckIn() != null)
                                {
                                    Toast.makeText(ClockInOutActivitySupervisor.this,"You have already added time in for this date",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");
                                String dateString = formatter.format(new Date(System.currentTimeMillis()));
                                String timeString = formatterTime.format(new Date(System.currentTimeMillis()));
                                attendance.setStatus("Present");
                                attendance.setDate(dateString);
                                attendance.setCheckIn(timeString);
                            }
                            else if(type.equals("Out"))
                            {
                                if(attendance.getCheckOut() != null)
                                {
                                    Toast.makeText(ClockInOutActivitySupervisor.this,"You have already added time out for this date",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");
                                String dateString = formatter.format(new Date(System.currentTimeMillis()));
                                String timeString = formatterTime.format(new Date(System.currentTimeMillis()));
                                attendance.setStatus("Present");
                                attendance.setDate(dateString);
                                attendance.setCheckOut(timeString);
                            }
                            mDatabase.child("Attendance").child(Constants.employee.getUserId()).child(attendance.getId()).setValue(attendance,new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError,
                                                       DatabaseReference databaseReference) {
                                    if(type.equals("In"))
                                    {
                                        Toast.makeText(ClockInOutActivitySupervisor.this,"Time In added successfully",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(ClockInOutActivitySupervisor.this,"Time Out added successfully",Toast.LENGTH_SHORT).show();

                                    }
                                    finish();
                                }
                            });

                        }
                        else
                        {
                            if(type.equals("In"))
                            {
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");
                                String dateString = formatter.format(new Date(System.currentTimeMillis()));
                                String timeString = formatterTime.format(new Date(System.currentTimeMillis()));
                                attendance = new Attendance();
                                attendance.setStatus("Present");
                                attendance.setDate(dateString);
                                attendance.setCheckIn(timeString);
                            }
                            else if(type.equals("Out"))
                            {
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");
                                String dateString = formatter.format(new Date(System.currentTimeMillis()));
                                String timeString = formatterTime.format(new Date(System.currentTimeMillis()));
                                attendance = new Attendance();
                                attendance.setStatus("Present");
                                attendance.setDate(dateString);
                                attendance.setCheckOut(timeString);
                            }
                            mDatabase.child("Attendance").child(Constants.employee.getUserId()).push().setValue(attendance,new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError,
                                                       DatabaseReference databaseReference) {
                                    if(type.equals("In"))
                                    {
                                        Toast.makeText(ClockInOutActivitySupervisor.this,"Time In added successfully",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(ClockInOutActivitySupervisor.this,"Time Out added successfully",Toast.LENGTH_SHORT).show();
                                    }
                                    finish();
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Value ","three"+attendance);
                    }
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(listner3 != null)
        {
            mDatabase.removeEventListener(listner3);
        }
    }
}
