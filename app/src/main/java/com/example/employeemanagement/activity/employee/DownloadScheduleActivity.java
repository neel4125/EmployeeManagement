package com.example.employeemanagement.activity.employee;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.GlideApp;
import com.example.employeemanagement.R;
import com.example.employeemanagement.model.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DownloadScheduleActivity extends AppCompatActivity {
    RelativeLayout downloadSchedule;
    CircleImageView imgProfile;
    TextView txtName;
    private FirebaseStorage firebaseStorage;
    String DownloadUrl = null;
    String extension = null;
    private DatabaseReference mDatabase;
    ImageView imgBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_schedule);
        downloadSchedule = (RelativeLayout)findViewById(R.id.downloadSchedule);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        txtName = (TextView) findViewById(R.id.txtName);
        if(Constants.employee != null)
        {
            GlideApp.with(this)
                    .load(Constants.employee.getProfile_pic())
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(imgProfile);
            txtName.setText(Constants.employee.getFirst_name()+" "+Constants.employee.getLast_name());
            getRefreshUser();
        }

        firebaseStorage = FirebaseStorage.getInstance();
        DownloadUrl = Constants.employee.getSchedule();
        if(DownloadUrl != null)
        {
            try {
                URL fileIneed = new URL(DownloadUrl);
                String filename = fileIneed.getPath();
                Log.e("File Name is","Here"+filename);
                extension =  filename.substring(filename.lastIndexOf("."));
                Log.e("File Extention is","Here"+extension);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        downloadSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.employee.getSchedule() == null)
                {
                    Toast.makeText(DownloadScheduleActivity.this,"No Schedule found",Toast.LENGTH_SHORT).show();
                    return;
                }
//                StorageReference reference = firebaseStorage.getReference(Constants.employee.getPaystub());
//
//                String name = reference.getName().split(".")[0];
//                String extension = reference.getName().split(".")[1];
//                Log.e("File Name","Is "+name+" Extension="+extension);
                downloadFile();
            }
        });

    }

    private void getRefreshUser() {
        mDatabase.child("User").child(Constants.employee.getUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        Employee employee = snapshot.getValue(Employee.class);
                        Constants.employee = employee;

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void downloadFile() {

        if(DownloadUrl != null) {
            String fileName = Constants.employee.getFirst_name()+"_Schedule_"+Constants.employee.getUserId()+extension;
            DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(DownloadUrl));
            request1.setDescription("Downloading...");   //appears the same in Notification bar while downloading
            request1.setVisibleInDownloadsUi(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request1.allowScanningByMediaScanner();
                request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            request1.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            //request1.setDestinationInExternalFilesDir(getApplicationContext(), "/File", "abc.pdf");

            DownloadManager manager1 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Objects.requireNonNull(manager1).enqueue(request1);
            if (DownloadManager.STATUS_SUCCESSFUL == 8) {
                Log.e("Download", "Success");
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                if (file.exists()) {//File Exists};
                    //DownloadSuccess();
                    Log.e("Download", "Success File Exist");
                }
            }
        }

    }
}
