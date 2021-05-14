package com.example.employeemanagement.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.employee.AnnouncementListActivity;
import com.example.employeemanagement.activity.employee.AttendanceListActivity;
import com.example.employeemanagement.activity.employee.ClockInOutActivity;
import com.example.employeemanagement.activity.employee.DownloadPayStubActivity;
import com.example.employeemanagement.activity.employee.DownloadScheduleActivity;
import com.example.employeemanagement.activity.employee.LeaveRequestActivity;

public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //Get Ids of all the required fields
        final CardView cardAnnousment = root.findViewById(R.id.cardAnnousment);
        final CardView cardAttendence = root.findViewById(R.id.cardAttendence);
        final CardView cardPayStub = root.findViewById(R.id.cardPayStub);
        final CardView cardLeaveRequest = root.findViewById(R.id.cardLeaveRequest);
        final CardView cardViewSchedule = root.findViewById(R.id.cardViewSchedule);
        final CardView clockInOut = root.findViewById(R.id.cardInOut);

        //Click on to See Announcement
        cardAnnousment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(getActivity(), AnnouncementListActivity.class);
            startActivity(intent);
            }
        });

        //Click on the Card Attendance
        cardAttendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AttendanceListActivity.class);
                startActivity(intent);
            }
        });

        //Click on the Mark Attendance
        cardPayStub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), EmployeeListActivity.class);
//                Bundle b = new Bundle();
//                b.putString("TAG","MarkAttendance");
//                intent.putExtras(b);
//                startActivity(intent);
            }
        });

        //Click on the Leave Request
        cardLeaveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LeaveRequestActivity.class);
                startActivity(intent);
            }
        });

        //Click on the Clock in out
        clockInOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ClockInOutActivity.class);
                startActivity(intent);
            }
        });

        //Click on the Pay Stub
        cardPayStub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DownloadPayStubActivity.class);
                startActivity(intent);
            }
        });

        //Click on the View Schedule
        cardViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DownloadScheduleActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}