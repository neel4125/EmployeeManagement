package com.example.employeemanagement.activity.supervisor.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.GlideApp;
import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.supervisor.EmployeDetailSelectionActivity;
import com.example.employeemanagement.activity.supervisor.EmployeeListActivitySupervisor;
import com.example.employeemanagement.activity.supervisor.LeaveApproveRejectLeaveActivity;
import com.example.employeemanagement.activity.supervisor.UploadPayStubActivity;
import com.example.employeemanagement.activity.supervisor.UploadPayStubActivitySupervisor;
import com.example.employeemanagement.activity.supervisor.UploadScheduleActivity;
import com.example.employeemanagement.model.Employee;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder>{
    private ArrayList<Employee> listdata;
    Context employeeListActivity;
    String TAG;
    //Get the list of employee and tag from the activity
    public EmployeeAdapter(ArrayList<Employee> listdata, Context employeeListActivity, String TAG) {
        this.listdata = listdata;
        this.employeeListActivity = employeeListActivity;
        this.TAG = TAG;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        //set the layout of the employee list row item
        View listItem= layoutInflater.inflate(R.layout.employee_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Employee myListData = listdata.get(position);
        //set the data from the list object
        holder.txtName.setText(myListData.getFirst_name()+" "+myListData.getLast_name());

        holder.txtDesignation.setText(myListData.getDesignation());
        GlideApp.with(employeeListActivity)
                .load(listdata.get(position).getProfile_pic())
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(holder.profile);
        holder.lilMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedEmployee = listdata.get(position);
                if(TAG.equals("UPLOAD_PAYSTUB"))
                {
                    Intent intent = new Intent(employeeListActivity, UploadPayStubActivity.class);
                    intent.putExtra("TYPE","NORMAL_LIST");
                    employeeListActivity.startActivity(intent);
                }
                else if(TAG.equals("UPLOAD_SCHEDULE"))
                {
                    Intent intent = new Intent(employeeListActivity, UploadScheduleActivity.class);
                    intent.putExtra("TYPE","NORMAL_LIST");
                    employeeListActivity.startActivity(intent);
                }
                else if(TAG.equals("NORMAL_LIST"))
                {
                    Intent intent = new Intent(employeeListActivity, EmployeDetailSelectionActivity.class);
                    intent.putExtra("TYPE","NORMAL_LIST");
                    employeeListActivity.startActivity(intent);
                }
                else if(TAG.equals("LeaveRequest"))
                {
                    Intent intent = new Intent(employeeListActivity, LeaveApproveRejectLeaveActivity.class);
                    intent.putExtra("TYPE","NORMAL_LIST");
                    employeeListActivity.startActivity(intent);
                }


//                else if(EmployeeListActivitySupervisor.type.equals("UPLOAD_PAYSTUB"))
//                {
//                    Intent intent = new Intent(employeeListActivity, UploadPayStubActivity.class);
//                    intent.putExtra("TYPE","NORMAL_LIST");
//                    employeeListActivity.startActivity(intent);
//                }
             //   Toast.makeText(employeeListActivity,"Employee Id"+ listdata.get(position).getUserId(),Toast.LENGTH_SHORT).show();
                //As per the tag from the previous screen open different activity with passing employee object

//                if(TAG.equalsIgnoreCase("EmployeeList"))
//                {
//                    //Redirect to the employee details screen and pass the employee object using intent bundle
//                    Intent yourIntent = new Intent(employeeListActivity, EmployeeDetailsActivity.class);
//                    Bundle b = new Bundle();
//                    b.putSerializable("employee",myListData );
//                    yourIntent.putExtras(b); //pass bundle to your intent
//                    employeeListActivity.finish();
//                    employeeListActivity.startActivity(yourIntent);
//                }
//                else if(TAG.equalsIgnoreCase("MarkAttendance"))
//                {
//                    //Redirect to the mark attendance screen and pass the employee object using intent bundle
//                    Intent yourIntent = new Intent(employeeListActivity, MarkAttendanceActivity.class);
//                    Bundle b = new Bundle();
//                    b.putSerializable("employee",myListData );
//                    yourIntent.putExtras(b); //pass bundle to your intent
//                    employeeListActivity.finish();
//                    employeeListActivity.startActivity(yourIntent);
//                }
//                else if(TAG.equalsIgnoreCase("SalarySlip"))
//                {
//                    //Redirect to the attendance list screen and pass the employee object using intent bundle
//                    Intent yourIntent = new Intent(employeeListActivity, AttendanceListActivity.class);
//                    Bundle b = new Bundle();
//                    b.putSerializable("employee",myListData );
//                    yourIntent.putExtras(b); //pass bundle to your intent
//                    employeeListActivity.finish();
//                    employeeListActivity.startActivity(yourIntent);
//                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName,txtDesignation,txtNicNumber;
        public LinearLayout lilMain;
        public CircleImageView profile;
        public ViewHolder(View itemView) {
            super(itemView);
            //Get the id of the all the fields
            this.txtName = (TextView) itemView.findViewById(R.id.txtName);
            this.profile = (CircleImageView) itemView.findViewById(R.id.profile);
            this.txtDesignation = (TextView) itemView.findViewById(R.id.txtDesignation);
            this.txtNicNumber = (TextView) itemView.findViewById(R.id.txtNicNumber);
            lilMain = (LinearLayout)itemView.findViewById(R.id.lilMain);
        }
    }
}  