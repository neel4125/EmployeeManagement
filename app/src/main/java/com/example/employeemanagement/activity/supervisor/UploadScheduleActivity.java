package com.example.employeemanagement.activity.supervisor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.GlideApp;
import com.example.employeemanagement.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.net.URISyntaxException;
import de.hdodenhof.circleimageview.CircleImageView;

public class UploadScheduleActivity extends AppCompatActivity {
    CircleImageView imgEmployee;
    TextView txtEmployeeName,txtAnnouncement;
    RelativeLayout uploadSchedule;
    ImageView imgBack;
    private static final int FILE_SELECT_CODE = 0;
    private DatabaseReference mDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_schedule);
        imgEmployee = (CircleImageView)findViewById(R.id.imgEmployee);
        txtEmployeeName = (TextView)findViewById(R.id.txtEmployeeName);
        txtAnnouncement = (TextView)findViewById(R.id.txtAnnouncement);
        uploadSchedule = (RelativeLayout)findViewById(R.id.uploadSchedule);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(Constants.selectedEmployee != null)
        {
            GlideApp.with(this)
                    .load(Constants.selectedEmployee.getProfile_pic())
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(imgEmployee);
            txtEmployeeName.setText(Constants.selectedEmployee.getFirst_name()+" "+Constants.selectedEmployee.getLast_name());
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        uploadSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("TAG", "File Uri: " + uri.toString());

                    String uploadedFile = "Documents/"+uri.getLastPathSegment();
                    //Uri file = Uri.fromFile(new File(uploadedFile));
                    StorageReference riversRef = storageReference.child("Documents/Schedule/"+uri.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(uri);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Log.e("On Failer","Image Stored"+exception);
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.e("On Sucess","Image Stored"+taskSnapshot);

                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    //Do what you want with the url
                                    Log.e("Get Download URL","IS HERE============"+ downloadUrl);
                                    txtAnnouncement.setText(uri.getLastPathSegment());
                                    String documentURL = downloadUrl.toString();
                                    Constants.selectedEmployee.setSchedule(documentURL);
                                    UpdateUserObject();
                                }
                            });
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                        }
                    });

                    // Get the path
                    String path = null;
                    try {
                        path = getPath(this, uri);
                        Log.e("Final Path","Is "+path);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d("TAG", "File Path: " + path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void UpdateUserObject() {
        mDatabase.child("User").child(Constants.selectedEmployee.getUserId()).setValue(Constants.selectedEmployee, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                                   DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Error: " + databaseError.getMessage());
                }
                else
                {
                    Toast.makeText(UploadScheduleActivity.this,"Schedule uploaded",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
