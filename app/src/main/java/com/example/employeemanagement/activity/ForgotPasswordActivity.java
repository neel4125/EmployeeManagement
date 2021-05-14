package com.example.employeemanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.MainActivity;
import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.employee.AddAvailabilityActivity;
import com.example.employeemanagement.activity.supervisor.MainActivitySupervisor;
import com.example.employeemanagement.model.Employee;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText password, confPassword,employeeID;
    Button submit;
    boolean isPassword, isConfPasswordValid,isEmloyeeIdValid,isUserFound;
    TextInputLayout passworErr, confPassError,employeeIDError;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    ValueEventListener listner1,listner3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        auth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Get Ids of all the required fields
        password = (EditText) findViewById(R.id.password);
        confPassword = (EditText) findViewById(R.id.confPassword);
        employeeID = (EditText) findViewById(R.id.employeeID);
        submit = (Button) findViewById(R.id.submit);

        passworErr = (TextInputLayout) findViewById(R.id.passworErr);
        confPassError = (TextInputLayout) findViewById(R.id.confPassError);
        employeeIDError = (TextInputLayout) findViewById(R.id.employeeIDError);

        //Handle on click of login button click
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();
            }
        });
    }


    public void SetValidation() {


        if (employeeID.getText().toString().isEmpty()) {
            employeeIDError.setError("Enter employee id");
            isEmloyeeIdValid = false;
        }  else  {
            isEmloyeeIdValid = true;
            employeeIDError.setErrorEnabled(false);
        }

        // Check for a valid password.
        if (password.getText().toString().isEmpty()) {
            passworErr.setError(getResources().getString(R.string.password_error));
            isPassword = false;
        } else if (password.getText().length() < 6) {
            passworErr.setError(getResources().getString(R.string.error_invalid_password));
            isPassword = false;
        } else  {
            isPassword = true;
            passworErr.setErrorEnabled(false);
        }

        // Check for a valid password.
        if (confPassword.getText().toString().isEmpty()) {
            confPassError.setError(getResources().getString(R.string.password_error));
            isConfPasswordValid = false;
        } else if (confPassword.getText().length() < 6) {
            confPassError.setError(getResources().getString(R.string.error_invalid_password));
            isConfPasswordValid = false;
        } else  {
            isConfPasswordValid = true;
            confPassError.setErrorEnabled(false);
        }




        //If email and password is valid then go next
        if (isPassword && isConfPasswordValid && isEmloyeeIdValid) {
            isUserFound = false;
            if(!password.getText().toString().equals(confPassword.getText().toString()))
            {
                Toast.makeText(ForgotPasswordActivity.this,"Password and confirm password must be same",Toast.LENGTH_SHORT).show();
                return;
            }

            listner1 =  mDatabase.child("User").orderByChild("employeeNumber").equalTo(employeeID.getText().toString())
                  .addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot snapshot) {
                  ArrayList<Employee> employeeArrayList = new ArrayList<>();
                  for(DataSnapshot uniqueKeySnapshot : snapshot.getChildren()){
                      //Loop 1 to go through all the child nodes of users
                      Employee employee = uniqueKeySnapshot.getValue(Employee.class);
                      Log.e("Logged In user","Is Found"+employee.getEmployeeNumber());
                      String passwordValue = String.valueOf(uniqueKeySnapshot.child("password").getValue());
                      Log.e("Logged In user","passwordValue"+passwordValue);
                      isUserFound = true;
                      employee.setUserId(uniqueKeySnapshot.getKey());
                      Constants.employee = employee;
                  }
                  if(isUserFound)
                  {
                      Constants.employee.setPassword(password.getText().toString());

                      mDatabase.child("User").child(Constants.employee.getUserId()).setValue(Constants.employee,new DatabaseReference.CompletionListener() {
                          @Override
                          public void onComplete(DatabaseError databaseError,
                                                 DatabaseReference databaseReference) {
                              Toast.makeText(ForgotPasswordActivity.this,"Password changed successfully",Toast.LENGTH_SHORT).show();
                              mDatabase.removeEventListener(listner1);
                              finish();
                          }
                      });
                  }
                  else{
                      Toast.makeText(getApplicationContext(), "Employee Id is not found", Toast.LENGTH_SHORT).show();
                  }
              }
              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });


            //Add static object to constants
//            Constants.admin = new Admin("",""
//                    ,"",""
//                    ,"",email.getText().toString()
//                    ,"",password.getText().toString());

     //       startActivity(intent);

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
