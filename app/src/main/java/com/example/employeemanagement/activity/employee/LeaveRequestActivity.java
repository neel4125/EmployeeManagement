package com.example.employeemanagement.activity.employee;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.R;
import com.example.employeemanagement.model.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LeaveRequestActivity extends AppCompatActivity {
    ImageView imgFromDate,imgToDate,imgBack;
    TextView txtFromDate,txtToDate,txtStatus;

    AppCompatButton btnSendRequest;
    EditText edtLeaveReason;
    private DatabaseReference mDatabase;
    ValueEventListener listner1,listner3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request);
        imgFromDate = (ImageView)findViewById(R.id.imgFromDate);
        imgToDate = (ImageView)findViewById(R.id.imgToDate);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        txtFromDate = (TextView)findViewById(R.id.txtFromDate);
        txtStatus = (TextView)findViewById(R.id.txtStatus);
        edtLeaveReason = (EditText)findViewById(R.id.edtLeaveReason);
        txtToDate = (TextView)findViewById(R.id.txtToDate);
        btnSendRequest = (AppCompatButton)findViewById(R.id.btnSendRequest);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getLeaveRequest();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtLeaveReason.getText().toString() == null || edtLeaveReason.getText().toString().equals(""))
                {
                    Toast.makeText(LeaveRequestActivity.this,"Please enter leave reason",Toast.LENGTH_SHORT).show();
                }
               else if(txtFromDate.getText().toString() == null || txtFromDate.getText().toString().equals(""))
                {
                    Toast.makeText(LeaveRequestActivity.this,"Please select from date",Toast.LENGTH_SHORT).show();
                }
                else if(txtToDate.getText().toString() == null || txtToDate.getText().toString().equals(""))
                {
                    Toast.makeText(LeaveRequestActivity.this,"Please select to date",Toast.LENGTH_SHORT).show();
                }
                else{
                    Constants.employee.setReason(edtLeaveReason.getText().toString());
                    Constants.employee.setFrom_date(txtFromDate.getText().toString());
                    Constants.employee.setTo_date(txtToDate.getText().toString());
                    Constants.employee.setStatus("Pending");

                    mDatabase.child("User").child(Constants.employee.getUserId()).setValue(Constants.employee,new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {
                            Toast.makeText(LeaveRequestActivity.this,"Leave request added successfully",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });

        imgFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerFromDate();
            }
        });

        imgToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerToDate();
            }
        });
    }

    private void getLeaveRequest() {
        listner1 = mDatabase.child("User").child(Constants.employee.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        Employee employee = snapshot.getValue(Employee.class);

                        if(employee.getStatus() != null)
                        {
                            edtLeaveReason.setText(employee.getReason());
                            txtFromDate.setText(employee.getFrom_date());
                            txtFromDate.setVisibility(View.VISIBLE);
                            txtToDate.setVisibility(View.VISIBLE);
                            txtToDate.setText(employee.getTo_date());
                            if(employee.getStatus().equalsIgnoreCase("Pending"))
                            {
                                txtStatus.setText("Your request is Pending");
                                txtStatus.setTextColor(Color.parseColor("#F0E68C"));
                            }
                            else if(employee.getStatus().equalsIgnoreCase("Approved"))
                            {
                                txtStatus.setText("Your request has been approved");
                                txtStatus.setTextColor(Color.parseColor("#006400"));
                            }
                            else if(employee.getStatus().equalsIgnoreCase("Rejected"))
                            {
                                txtStatus.setText("Your request has been rejected");
                                txtStatus.setTextColor(Color.parseColor("#DC143C"));
                            }

                            Log.e("Leave Request","Is Found"+employee.getReason());
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void openDatePickerFromDate() {
        //Open date picker
        final Calendar c = Calendar.getInstance();
        int  mYear = c.get(Calendar.YEAR);
        int  mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(LeaveRequestActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        txtFromDate.setVisibility(View.VISIBLE);
                        txtFromDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                        Calendar calendar = Calendar.getInstance();
                        calendar.clear(); // Sets hours/minutes/seconds/milliseconds to zero
                        calendar.set(year , (monthOfYear), dayOfMonth);
                        Date result = calendar.getTime();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    private void openDatePickerToDate() {
        //Open date picker
        final Calendar c = Calendar.getInstance();
        int  mYear = c.get(Calendar.YEAR);
        int  mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(LeaveRequestActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtToDate.setVisibility(View.VISIBLE);
                        txtToDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                        Calendar calendar = Calendar.getInstance();
                        calendar.clear(); // Sets hours/minutes/seconds/milliseconds to zero
                        calendar.set(year , (monthOfYear), dayOfMonth);
                        Date result = calendar.getTime();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }
}
