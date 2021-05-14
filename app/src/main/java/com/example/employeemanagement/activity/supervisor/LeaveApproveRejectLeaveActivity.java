package com.example.employeemanagement.activity.supervisor;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.GlideApp;
import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.employee.LeaveRequestActivity;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaveApproveRejectLeaveActivity extends AppCompatActivity {
    CircleImageView userImage;
    TextView userName,txtFromDate,txtToDate;
    EditText edtLeaveReason;
    ImageView imgReject,imgApprove,imgBack;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_reject_leave_request);
        userImage = (CircleImageView)findViewById(R.id.userImage);
        userName = (TextView)findViewById(R.id.userName);
        txtFromDate = (TextView)findViewById(R.id.txtFromDate);
        txtToDate = (TextView)findViewById(R.id.txtToDate);
        edtLeaveReason = (EditText)findViewById(R.id.edtLeaveReason);
        imgReject = (ImageView)findViewById(R.id.imgReject);
        imgApprove = (ImageView)findViewById(R.id.imgApprove);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(Constants.selectedEmployee != null)
        {
            GlideApp.with(this)
                    .load(Constants.selectedEmployee.getProfile_pic())
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(userImage);
            userName.setText(Constants.selectedEmployee.getFirst_name()+" "+Constants.selectedEmployee.getLast_name());
            txtFromDate.setText(Constants.selectedEmployee.getFrom_date());
            txtToDate.setText(Constants.selectedEmployee.getTo_date());
            edtLeaveReason.setText(Constants.selectedEmployee.getReason());
            imgReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.selectedEmployee.setStatus("Rejected");
                    mDatabase.child("User").child(Constants.selectedEmployee.getUserId()).setValue(Constants.selectedEmployee,new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {
                            Toast.makeText(LeaveApproveRejectLeaveActivity.this,"Leave Rejected successfully",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            });
            imgApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.selectedEmployee.setStatus("Approved");
                    mDatabase.child("User").child(Constants.selectedEmployee.getUserId()).setValue(Constants.selectedEmployee,new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {
                            Toast.makeText(LeaveApproveRejectLeaveActivity.this,"Leave Approved successfully",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            });
        }
    }

}
