package com.example.employeemanagement.activity.employee;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.GlideApp;
import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.RegisterSupervisorActivity;
import com.example.employeemanagement.activity.supervisor.EditProfileSupervisorActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    Button btnAddAvailibity,btnSeeHostory;
    ImageView imgBack;
    AppCompatButton btnUpdate;
    CircleImageView imgProfile;
    String uploadedFile;
    String documentURL;
    boolean isDocumentSelected = false;
    TextView txtFirstName,txtPosition;
    TextView txtDocumentName;
    EditText firstName,lastName,email,password,phone,designation,address,employeeNo;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    private DatabaseReference mDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        btnAddAvailibity = (Button)findViewById(R.id.btnAddAvailibity);
        btnSeeHostory = (Button)findViewById(R.id.btnSeeHostory);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgProfile = (CircleImageView)findViewById(R.id.imgProfile);
        btnUpdate = (AppCompatButton)findViewById(R.id.btnUpdate);
        txtFirstName = (TextView)findViewById(R.id.txtFirstName);
        txtPosition = (TextView)findViewById(R.id.txtPosition);
        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        designation = (EditText) findViewById(R.id.designation);
        employeeNo = (EditText) findViewById(R.id.employeeNo);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        LinearLayout lilDocument = (LinearLayout)findViewById(R.id.lilDocument);
        txtDocumentName = (TextView)findViewById(R.id.txtDocumentName);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkALLPermission();
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSeeHostory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, SeeHistoryActivity.class);
                startActivity(intent);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstName.getText().toString().equals(""))
                {
                    Toast.makeText(EditProfileActivity.this,"Please enter first name",Toast.LENGTH_SHORT).show();
                }
                else if(lastName.getText().toString().equals(""))
                {
                    Toast.makeText(EditProfileActivity.this,"Please enter last name",Toast.LENGTH_SHORT).show();
                }
                else if(email.getText().toString().equals(""))
                {
                    Toast.makeText(EditProfileActivity.this,"Please enter email address",Toast.LENGTH_SHORT).show();
                }
                else if(address.getText().toString().equals(""))
                {
                    Toast.makeText(EditProfileActivity.this,"Please select address",Toast.LENGTH_SHORT).show();
                }
                else if(phone.getText().toString().equals(""))
                {
                    Toast.makeText(EditProfileActivity.this,"Please enter mobile number",Toast.LENGTH_SHORT).show();
                }
                else if(designation.getText().toString().equals(""))
                {
                    Toast.makeText(EditProfileActivity.this,"Please enter designation",Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().equals(""))
                {
                    Toast.makeText(EditProfileActivity.this,"Please enter password",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Constants.employee.setFirst_name(firstName.getText().toString());
                    Constants.employee.setLast_name(lastName.getText().toString());
                    Constants.employee.setEmail(email.getText().toString());
                    Constants.employee.setAddress(address.getText().toString());
                    Constants.employee.setContactNumber(phone.getText().toString());
                    Constants.employee.setDesignation(designation.getText().toString());
                    Constants.employee.setPassword(password.getText().toString());
                    mDatabase.child("User").child(Constants.employee.getUserId()).setValue(Constants.employee,new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {
                            Toast.makeText(EditProfileActivity.this,"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }

            }
        });
        lilDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDocumentSelected = true;
                selectImage();
            }
        });
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDocumentSelected = false;
                selectImage();
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(EditProfileActivity.this);
                builderSingle.setTitle("Select State");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.select_dialog_item);
                arrayAdapter.add("Alberta");
                arrayAdapter.add("British Columbia");
                arrayAdapter.add("Manitoba");
                arrayAdapter.add("New Brunswick");
                arrayAdapter.add("Newfoundland and Labrador");
                arrayAdapter.add("Northwest Territories");
                arrayAdapter.add("Nova Scotia");
                arrayAdapter.add("Nunavut");
                arrayAdapter.add("Ontario");
                arrayAdapter.add("Prince Edward Island");
                arrayAdapter.add("Quebec");
                arrayAdapter.add("Saskatchewan");
                arrayAdapter.add("Yukon");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        address.setText(strName);
                    }
                });
                builderSingle.show();
            }
        });

        if(Constants.employee != null)
        {
            GlideApp.with(this)
                    .load(Constants.employee.getProfile_pic())
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(imgProfile);
            txtFirstName.setText(Constants.employee.getFirst_name()+" "+Constants.employee.getLast_name());
            txtPosition.setText(Constants.employee.getDesignation());
            firstName.setText(Constants.employee.getFirst_name());
            lastName.setText(Constants.employee.getLast_name());
            email.setText(Constants.employee.getEmail());
            address.setText(Constants.employee.getAddress());
            phone.setText(Constants.employee.getContactNumber());
            designation.setText(Constants.employee.getDesignation());
            txtDocumentName.setText(Constants.employee.getDocument());
            employeeNo.setText(Constants.employee.getEmployeeNumber());
            password.setText(Constants.employee.getPassword());
        }
        btnAddAvailibity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, AddAvailabilityActivity.class);
                Constants.isFromSupervisor = false;
                startActivity(intent);
            }
        });
    }
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT,FileProvider.getUriForFile(RegisterEmployeeActivity.this, getApplicationContext().getPackageName() + ".provider",f));
//                    startActivityForResult(intent, 1);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                try {
                    // Uri selectedImage = data.getData();


                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                    Log.e("Activity", "Pick from Camera::>>> ");


                    Uri selectedImage = getImageUri(this,bitmap);
                    String uploadedFile = "images/"+selectedImage.getLastPathSegment();
                    //Uri file = Uri.fromFile(new File(uploadedFile));
                    StorageReference riversRef = storageReference.child("images/"+selectedImage.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(selectedImage);

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
                                    //String profileUrl = downloadUrl.toString();
                                    if(isDocumentSelected)
                                    {
                                        documentURL = downloadUrl.toString();
                                        txtDocumentName.setText(selectedImage.getLastPathSegment());
                                        Constants.employee.setDocument(documentURL);
                                    }
                                    else
                                    {
                                        String profileUrl = downloadUrl.toString();
                                        Constants.employee.setProfile_pic(profileUrl);
                                    }
                                }
                            });
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                        }
                    });


                    //  Log.e("Final Path","Path Is"+imgPath);
                    imgProfile.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String uploadedFile = "images/"+selectedImage.getLastPathSegment();
                //Uri file = Uri.fromFile(new File(uploadedFile));
                StorageReference riversRef = storageReference.child("images/"+selectedImage.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(selectedImage);

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
                                if(isDocumentSelected)
                                {
                                    documentURL = downloadUrl.toString();
                                    txtDocumentName.setText(selectedImage.getLastPathSegment());

                                    Constants.employee.setDocument(documentURL);
                                }
                                else
                                {
                                    String profileUrl = downloadUrl.toString();
                                    Constants.employee.setProfile_pic(profileUrl);
                                }

                            }
                        });
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                    }
                });
                Log.w("path of ", "image from gallery......******************........."+selectedImage+"");
                imgProfile.setImageURI(selectedImage);
            }
        }
    }

    protected void checkALLPermission(){
        if(ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(EditProfileActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED
                && ( ContextCompat.checkSelfPermission(EditProfileActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED)){

            // Do something, when permissions not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    EditProfileActivity.this,Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    EditProfileActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    EditProfileActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setMessage("Camera, Read Contacts and Write External" +
                        " Storage permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                EditProfileActivity.this,
                                new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        EditProfileActivity.this,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        }else {
            // Do something, when permissions are already granted
           // Toast.makeText(EditProfileActivity.this,"Permissions already granted",Toast.LENGTH_SHORT).show();
        }
    }

}
