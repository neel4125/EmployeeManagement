package com.example.employeemanagement.activity.supervisor.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.employee.AnnouncementListActivity;
import com.example.employeemanagement.activity.supervisor.AnnouncementListActivitySupervisor;
import com.example.employeemanagement.model.Announcement;

import java.util.ArrayList;

public class AnnousmentAdapterSupervisor extends RecyclerView.Adapter<AnnousmentAdapterSupervisor.ViewHolder>{
    private ArrayList<Announcement> listdata;
    AnnouncementListActivitySupervisor attendanceListActivity;
   public OnItemClickListener listener;
    //Get List data from activity
    public AnnousmentAdapterSupervisor(ArrayList<Announcement> listdata, AnnouncementListActivitySupervisor attendanceListActivity, OnItemClickListener listener) {
        this.listdata = listdata;
        this.attendanceListActivity = attendanceListActivity;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        //set the layout of attendance list item from xml
        View listItem= layoutInflater.inflate(R.layout.row_announcement_item_superviser, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Announcement myListData = listdata.get(position);
        //set date basic pay and click on main row
        holder.txtContent.setText(myListData.getText());
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(myListData);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtContent;
        public ImageView imgEdit;
        public ViewHolder(View itemView) {
            super(itemView);
            //Get the id of the row items
            this.txtContent = (TextView) itemView.findViewById(R.id.txtAnnouncement);
            this.imgEdit = (ImageView) itemView.findViewById(R.id.imgEdit);

        }
    }
}
