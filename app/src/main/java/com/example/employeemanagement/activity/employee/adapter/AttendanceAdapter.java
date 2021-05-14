package com.example.employeemanagement.activity.employee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.employee.AnnouncementListActivity;
import com.example.employeemanagement.activity.employee.AttendanceListActivity;
import com.example.employeemanagement.model.Announcement;
import com.example.employeemanagement.model.Attendance;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder>{
    private ArrayList<Attendance> listdata;
    Context attendanceListActivity;

    //Get List data from activity
    public AttendanceAdapter(ArrayList<Attendance> listdata, Context attendanceListActivity) {
        this.listdata = listdata;
        this.attendanceListActivity = attendanceListActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        //set the layout of attendance list item from xml
        View listItem= layoutInflater.inflate(R.layout.row_attendance, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Attendance myListData = listdata.get(position);
        //set date basic pay and click on main row
        holder.txtDate.setText(myListData.getDate());
        holder.txtStatus.setText(myListData.getStatus());
        holder.timeIn.setText(myListData.getCheckIn());
        holder.timeOut.setText(myListData.getCheckOut());
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDate;
        public TextView txtStatus;
        public TextView timeIn;
        public TextView timeOut;
        public ViewHolder(View itemView) {
            super(itemView);
            //Get the id of the row items
            this.txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            this.txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            this.timeIn = (TextView) itemView.findViewById(R.id.timeIn);
            this.timeOut = (TextView) itemView.findViewById(R.id.timeOut);

        }
    }
}
