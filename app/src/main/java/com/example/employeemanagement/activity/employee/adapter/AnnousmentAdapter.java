package com.example.employeemanagement.activity.employee.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.employee.AnnouncementListActivity;
import com.example.employeemanagement.model.Announcement;

import java.util.ArrayList;

public class AnnousmentAdapter extends RecyclerView.Adapter<AnnousmentAdapter.ViewHolder>{
    private ArrayList<Announcement> listdata;
    AnnouncementListActivity attendanceListActivity;

    //Get List data from activity
    public AnnousmentAdapter(ArrayList<Announcement> listdata, AnnouncementListActivity attendanceListActivity) {
        this.listdata = listdata;
        this.attendanceListActivity = attendanceListActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        //set the layout of attendance list item from xml
        View listItem= layoutInflater.inflate(R.layout.row_announcement_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Announcement myListData = listdata.get(position);
        //set date basic pay and click on main row
        holder.txtContent.setText(myListData.getText());

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtContent;
        public ViewHolder(View itemView) {
            super(itemView);
            //Get the id of the row items
            this.txtContent = (TextView) itemView.findViewById(R.id.txtAnnouncement);

        }
    }
}
