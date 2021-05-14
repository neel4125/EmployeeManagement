package com.example.employeemanagement.activity.supervisor.fragment;

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
import com.example.employeemanagement.activity.supervisor.AnnouncementListActivitySupervisor;
import com.example.employeemanagement.activity.supervisor.ClockInOutActivitySupervisor;
import com.example.employeemanagement.activity.supervisor.EmployeeListActivitySupervisor;
import com.example.employeemanagement.activity.supervisor.LeaveRequestActivitySupervisor;
import com.example.employeemanagement.activity.supervisor.UploadPayStubActivitySupervisor;

public class HomeFragmentSupervisor extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home_supervisor, container, false);

        //Get Ids of all the required fields
        final CardView cardAnnousment = root.findViewById(R.id.cardAnnousment);
        final CardView cardAttendence = root.findViewById(R.id.cardAttendence);
        final CardView cardPayStub = root.findViewById(R.id.cardPayStub);
        final CardView cardLeaveRequest = root.findViewById(R.id.cardLeaveRequest);
        final CardView cardViewSchedule = root.findViewById(R.id.cardViewSchedule);
        final CardView cardEmployeeList = root.findViewById(R.id.cardEmployeeList);
        final CardView clockInOut = root.findViewById(R.id.cardInOut);

        //Click on to See Announcement
        cardAnnousment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(getActivity(), AnnouncementListActivitySupervisor.class);
            startActivity(intent);
            }
        });

        //Click on to See Announcement
        cardEmployeeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EmployeeListActivitySupervisor.class);
                intent.putExtra("TYPE","NORMAL_LIST");
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
        cardViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EmployeeListActivitySupervisor.class);
                intent.putExtra("TYPE","UPLOAD_SCHEDULE");
                startActivity(intent);
            }
        });
        //Click on the Mark Attendance
        cardPayStub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(getActivity(), UploadPayStubActivitySupervisor.class);
                Intent intent = new Intent(getActivity(), EmployeeListActivitySupervisor.class);
                intent.putExtra("TYPE","UPLOAD_PAYSTUB");
                startActivity(intent);
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
                Intent intent = new Intent(getActivity(), LeaveRequestActivitySupervisor.class);
                startActivity(intent);
            }
        });

        //Click on the Clock in out
        clockInOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ClockInOutActivitySupervisor.class);
                startActivity(intent);
            }
        });




        return root;
    }
}