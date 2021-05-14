package com.example.employeemanagement.activity;

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
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.MainActivity;
import com.example.employeemanagement.R;
import com.example.employeemanagement.model.Employee;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterSupervisorActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    EditText firstName, lastName, email, password,phone,designation,address,employeeNo,confPassword;
    Button register;
    TextView login;
    String uploadedFile;
    String profileUrl;
    String documentURL;
    TextView txtDocumentName;
    boolean isFirstNameValid,isLastNameValid, isEmailValid, isAddressValid,isPhoneValid, isPasswordValid,isDesignationValid,isEmployeeNoValid,isConfPassValid;
    TextInputLayout firstNameError,lastNameError, emailError,addressError, phoneError,designationError, employeeNoError,passError,confPassError;
    CircleImageView profile;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    private DatabaseReference mDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    boolean isDocumentSelected = false;
    ValueEventListener listner1,listner3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_register);

        //Get Ids of all the required fields
        profile = (CircleImageView)findViewById(R.id.profile);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        designation = (EditText) findViewById(R.id.designation);
        employeeNo = (EditText) findViewById(R.id.employeeNo);
        password = (EditText) findViewById(R.id.password);
        confPassword = (EditText) findViewById(R.id.confPassword);
        register = (Button) findViewById(R.id.register);

        firstNameError = (TextInputLayout)findViewById(R.id.firstNameError);
        lastNameError = (TextInputLayout)findViewById(R.id.lastNameError);
        emailError = (TextInputLayout)findViewById(R.id.emailError);
        addressError = (TextInputLayout)findViewById(R.id.addressError);
        phoneError = (TextInputLayout)findViewById(R.id.phoneError);
        designationError = (TextInputLayout)findViewById(R.id.designationError);
        employeeNoError = (TextInputLayout)findViewById(R.id.employeeNoError);
        passError = (TextInputLayout)findViewById(R.id.passError);
        confPassError = (TextInputLayout)findViewById(R.id.confPassError);
        LinearLayout lilDocument = (LinearLayout)findViewById(R.id.lilDocument);
        txtDocumentName = (TextView)findViewById(R.id.txtDocumentName);

        login = (TextView) findViewById(R.id.login);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkALLPermission();
        }
        getCounterValueFromFirebase();

        //Handle on click of login button click
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterSupervisorActivity.this,LoginActivity.class);
                startActivity(i);
                finishAffinity();
            }
        });

        //Handle on click of Register button click
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check for the validation of all the required fields
                SetValidation();
            }
        });

        lilDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDocumentSelected = true;
                selectImage();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDocumentSelected = false;
                selectImage();
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(RegisterSupervisorActivity.this);
                builderSingle.setTitle("Select State");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RegisterSupervisorActivity.this, android.R.layout.select_dialog_item);
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
    }
    private void getCounterValueFromFirebase() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // getValue() returns Long
                if(dataSnapshot.child("count").getValue() != null)
                {

                    int  count = (int)(long) dataSnapshot.child("count").getValue();
                    Log.e("Final Value","Is Coming"+count);
//                    System.out.println("count before setValue()=" + map.get("count"));
//
                    Constants.autoIncrementId = count;
                }
                else
                {
                    mDatabase.child("count").setValue(++Constants.autoIncrementId, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                System.out.println("Error: " + databaseError.getMessage());
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // throw an error if setValue() is rejected
                throw databaseError.toException();
            }
        });
    }

    protected void checkALLPermission(){
        if(ContextCompat.checkSelfPermission(RegisterSupervisorActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(RegisterSupervisorActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED
                && ( ContextCompat.checkSelfPermission(RegisterSupervisorActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED)){

            // Do something, when permissions not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    RegisterSupervisorActivity.this,Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    RegisterSupervisorActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    RegisterSupervisorActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterSupervisorActivity.this);
                builder.setMessage("Camera, Read Contacts and Write External" +
                        " Storage permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                RegisterSupervisorActivity.this,
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
                        RegisterSupervisorActivity.this,
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
           // Toast.makeText(RegisterSupervisorActivity.this,"Permissions already granted",Toast.LENGTH_SHORT).show();
        }
    }
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterSupervisorActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT,FileProvider.getUriForFile(RegisterSupervisorActivity.this, getApplicationContext().getPackageName() + ".provider",f));
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
                    uploadedFile = "images/"+selectedImage.getLastPathSegment();
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
                                    }
                                    else
                                    {
                                        profileUrl = downloadUrl.toString();
                                    }

                                }
                            });
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                        }
                    });


                    //  Log.e("Final Path","Path Is"+imgPath);
                    profile.setImageBitmap(bitmap);
                    // profile.setImageBitmap(bitmap);
//                    Bitmap bitmap;
//                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
//                            bitmapOptions);
//                    profile.setImageBitmap(bitmap);
//                    String path = android.os.Environment
//                            .getExternalStorageDirectory()
//                            + File.separator
//                            + "Phoenix" + File.separator + "default";
//                    f.delete();
//                    OutputStream outFile = null;
//                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
//                    try {
//                        outFile = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
//                        outFile.flush();
//                        outFile.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                uploadedFile = "images/"+selectedImage.getLastPathSegment();
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
                                }
                                else
                                {
                                    profileUrl = downloadUrl.toString();
                                }

                            }
                        });
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                    }
                });

//                final StorageReference ref = storageReference.child("images/mountains.jpg");
//                uploadTask = ref.putFile(file);
//
//                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                        if (!task.isSuccessful()) {
//                            throw task.getException();
//                        }
//
//                        // Continue with the task to get the download URL
//                        return ref.getDownloadUrl();
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if (task.isSuccessful()) {
//                            Uri downloadUri = task.getResult();
//                        } else {
//                            // Handle failures
//                            // ...
//                        }
//                    }
//                });
//                String[] filePath = { MediaStore.Images.Media.DATA };
//                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                String picturePath = c.getString(columnIndex);
//                c.close();
//                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of ", "image from gallery......******************........."+selectedImage+"");
                profile.setImageURI(selectedImage);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CODE:{
                // When request is cancelled, the results array are empty
                if(
                        (grantResults.length >0) &&
                                (grantResults[0] == PackageManager.PERMISSION_GRANTED
                                        && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                                        grantResults[2] == PackageManager.PERMISSION_GRANTED
                                )
                ){
                    // Permissions are granted
                    Toast.makeText(RegisterSupervisorActivity.this,"Permissions granted.",Toast.LENGTH_SHORT).show();
                }else {
                    // Permissions are denied
                    Toast.makeText(RegisterSupervisorActivity.this,"Permissions denied.",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void SetValidation() {
        // Check for a valid name.
        if (firstName.getText().toString().isEmpty()) {
            firstNameError.setError(getResources().getString(R.string.first_name_error));
            isFirstNameValid = false;
        } else  {
            isFirstNameValid = true;
            firstNameError.setErrorEnabled(false);
        }

        if (lastName.getText().toString().isEmpty()) {
            lastNameError.setError(getResources().getString(R.string.last_name_error));
            isLastNameValid = false;
        } else  {
            isLastNameValid = true;
            lastNameError.setErrorEnabled(false);
        }



        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else  {
            isEmailValid = true;
            emailError.setErrorEnabled(false);
        }

        // Check for a valid phone number.
        if (address.getText().toString().isEmpty()) {
            addressError.setError(getResources().getString(R.string.address_error));
            isAddressValid = false;
        } else  {
            isAddressValid = true;
            addressError.setErrorEnabled(false);
        }

        if (phone.getText().toString().isEmpty()) {
            phoneError.setError(getResources().getString(R.string.phone_error));
            isPhoneValid = false;
        } else  {
            isPhoneValid = true;
            phoneError.setErrorEnabled(false);
        }

        if (designation.getText().toString().isEmpty()) {
            designationError.setError(getResources().getString(R.string.designation_error));
            isDesignationValid = false;
        } else  {
            isDesignationValid = true;
            designationError.setErrorEnabled(false);
        }

        if (employeeNo.getText().toString().isEmpty()) {
            employeeNoError.setError(getResources().getString(R.string.employee_no_error));
            isEmployeeNoValid = false;
        } else  {
            isEmployeeNoValid = true;
            employeeNoError.setErrorEnabled(false);
        }

        // Check for a valid password.
        if (password.getText().toString().isEmpty()) {
            passError.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (password.getText().length() < 6) {
            passError.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
            passError.setErrorEnabled(false);
        }

        // Check for a valid password.
        if (confPassword.getText().toString().isEmpty()) {
            confPassError.setError(getResources().getString(R.string.conf_password_error));
            isConfPassValid = false;
        } else if (confPassword.getText().length() < 6) {
            confPassError.setError(getResources().getString(R.string.conf_password_error));
            isConfPassValid = false;
        }
        else if (!confPassword.getText().toString().equals(password.getText().toString())) {
            confPassError.setError(getResources().getString(R.string.pass_conf_equal));
            isConfPassValid = false;
        }
        else  {
            isConfPassValid = true;
            confPassError.setErrorEnabled(false);
        }

//
        // Check for a valid address.
        if (address.getText().toString().isEmpty()) {
            addressError.setError(getResources().getString(R.string.address_error));
            isAddressValid = false;
        } else  {
            isAddressValid = true;
            addressError.setErrorEnabled(false);
        }
        if(documentURL == null)
        {
            Toast.makeText(RegisterSupervisorActivity.this,"Please upload document",Toast.LENGTH_SHORT).show();
            return;
        }
        //Check for all the fields validation if all are checked then make register success
        if (isFirstNameValid && isLastNameValid && isEmailValid && isAddressValid && isPhoneValid && isDesignationValid && isEmployeeNoValid && isPasswordValid && isConfPassValid) {
            Employee employee = new Employee();
            employee.setFirst_name(firstName.getText().toString());
            employee.setLast_name(lastName.getText().toString());
            employee.setEmail(email.getText().toString());
            employee.setAddress(address.getText().toString());
            employee.setContactNumber(phone.getText().toString());
            employee.setDesignation(designation.getText().toString());
            employee.setEmployeeNumber(employeeNo.getText().toString());
            employee.setPassword(password.getText().toString());
            employee.setProfile_pic(profileUrl);
            employee.setDocument(documentURL);
            employee.setUserType("Supervisor");
            if(mDatabase != null)
            {
                writeNewUser(employee);
            }

        }
    }
    ArrayList<Employee> tempList = new ArrayList<Employee>();
    boolean isUserFound = false;
    public void writeNewUser(Employee employee) {
        isUserFound = false;
        tempList.clear();
        listner1 =    mDatabase.child("User").orderByChild("employeeNumber").equalTo(employeeNo.getText().toString())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.e("Snapshot","Value is"+snapshot);
                        if(snapshot != null)
                        {
                            tempList.clear();
                            for(DataSnapshot uniqueKeySnapshot : snapshot.getChildren()){
                                //Loop 1 to go through all the child nodes of users
                                Employee employee = uniqueKeySnapshot.getValue(Employee.class);
                                Log.e("Logged In user","Is Found"+employee.getEmployeeNumber());
                                String passwordValue = String.valueOf(uniqueKeySnapshot.child("password").getValue());
                                Log.e("Logged In user","passwordValue"+passwordValue);
                                isUserFound = true;
                                tempList.add(employee);
                            }
                            if(tempList.size() > 0)
                            {
                                tempList.clear();
                                if(!Constants.isLoggedIn && (!Constants.isRegister))
                                {
                                    Toast.makeText(RegisterSupervisorActivity.this,"This employee number already exist.Please choose different.",Toast.LENGTH_SHORT).show();
                                }
                                if(listner1 != null)
                                {
                                    mDatabase.removeEventListener(listner1);
                                }
                                isUserFound = true;
                                tempList.clear();
                            }
                            else
                            {
                                isUserFound = false;
                                mDatabase.child("User").child(String.valueOf(Constants.autoIncrementId)).setValue(employee, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError,
                                                           DatabaseReference databaseReference) {
                                        Constants.isRegister = true;
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        Toast.makeText(RegisterSupervisorActivity.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(intent);
                                        if(listner1 != null)
                                        {
                                            mDatabase.removeEventListener(listner1);
                                        }
                                        if (databaseError != null) {
                                            System.out.println("Error: " + databaseError.getMessage());
                                        }
                                    }
                                });
                                mDatabase.child("count").setValue(++Constants.autoIncrementId, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError,
                                                           DatabaseReference databaseReference) {

                                        if (databaseError != null) {
                                            System.out.println("Error: " + databaseError.getMessage());
                                        }
                                    }
                                });
                            }
                            Log.e("Snapshot tempList","Value is"+tempList.size());
                        }
                        else
                        {
                            isUserFound = false;
                            mDatabase.child("User").child(String.valueOf(Constants.autoIncrementId)).setValue(employee, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError,
                                                       DatabaseReference databaseReference) {
                                    Constants.isRegister = true;
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    Toast.makeText(RegisterSupervisorActivity.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(intent);
                                    if(listner1 != null)
                                    {
                                        mDatabase.removeEventListener(listner1);
                                    }
                                    if (databaseError != null) {
                                        System.out.println("Error: " + databaseError.getMessage());
                                    }
                                }
                            });
                            mDatabase.child("count").setValue(++Constants.autoIncrementId, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError,
                                                       DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        System.out.println("Error: " + databaseError.getMessage());
                                    }
                                }
                            });
                        }
//                        Log.e("isUserFound","Value:="+isUserFound);
//                        if(isUserFound)
//                        {
//                            Toast.makeText(RegisterSupervisorActivity.this,"This employee number already exist.Please choose different.",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        else
//                        {
//
//                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(listner1 != null)
        {
            mDatabase.removeEventListener(listner1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(listner1 != null)
        {
            mDatabase.removeEventListener(listner1);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(listner1 != null)
        {
            mDatabase.removeEventListener(listner1);
        }

    }
}

