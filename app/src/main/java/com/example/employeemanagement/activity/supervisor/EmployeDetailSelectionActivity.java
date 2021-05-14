package com.example.employeemanagement.activity.supervisor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.GlideApp;
import com.example.employeemanagement.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeDetailSelectionActivity extends AppCompatActivity {

    ImageView imgBack;
    CircleImageView userProfile;
    TextView userName;
    LinearLayout lilViewAvailability,lilViewSchedule,lilViewAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail_selection);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        userProfile = (CircleImageView)findViewById(R.id.userProfile);
        userName = (TextView)findViewById(R.id.userName);
        lilViewAvailability = (LinearLayout)findViewById(R.id.lilViewAvailability);
        lilViewSchedule = (LinearLayout)findViewById(R.id.lilViewSchedule);
        lilViewAttendance = (LinearLayout)findViewById(R.id.lilViewAttendance);

        if(Constants.selectedEmployee != null)
        {
            GlideApp.with(this)
                    .load(Constants.selectedEmployee.getProfile_pic())
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(userProfile);
            userName.setText(Constants.selectedEmployee.getFirst_name()+" "+Constants.selectedEmployee.getLast_name());
        }

        lilViewAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeDetailSelectionActivity.this, ViewAvailabilityActivity.class);
                startActivity(intent);
            }
        });

        lilViewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeDetailSelectionActivity.this, ViewAttendanceActivity.class);
                startActivity(intent);
            }
        });

        lilViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(Constants.selectedEmployee.getSchedule() != null)
            {
                Intent intent = new Intent(EmployeDetailSelectionActivity.this, ViewScheduleActivity.class);
                startActivity(intent);
//                String url= "" ;
//                try {
//                    url= URLEncoder.encode(Constants.selectedEmployee.getSchedule(),"UTF-8"); //Url Convert to UTF-8 It important.
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//
//                //webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+url);
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://drive.google.com/viewerng/viewer?embedded=true&url="+url));
//                startActivity(browserIntent);
            }
            else
            {
                Toast.makeText(EmployeDetailSelectionActivity.this,"No schedule available",Toast.LENGTH_SHORT).show();
            }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
