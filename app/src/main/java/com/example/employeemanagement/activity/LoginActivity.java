package com.example.employeemanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.MainActivity;
import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.supervisor.MainActivitySupervisor;
import com.example.employeemanagement.model.Employee;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText employeeNumber, password;
    Button login;
    TextView register,forgotPassword;
    boolean isEmployeeNoValid, isPasswordValid;
    TextInputLayout employeeNumberError, passError;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    ValueEventListener listner1,listner3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Get Ids of all the required fields
        employeeNumber = (EditText) findViewById(R.id.employeeNumber);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        employeeNumberError = (TextInputLayout) findViewById(R.id.employeeNumberError);
        passError = (TextInputLayout) findViewById(R.id.passError);

        //Handle on click of login button click
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        //Handle click of register button click
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to RegisterActivity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
     boolean isUserFound = false;
    public void SetValidation() {
        // Check for a valid employee number.
        if (employeeNumber.getText().toString().isEmpty()) {
            employeeNumberError.setError(getResources().getString(R.string.employee_no_error));
            isEmployeeNoValid = false;
        }
        else  {
            isEmployeeNoValid = true;
            employeeNumberError.setErrorEnabled(false);
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

        //If email and password is valid then go next
        if (isEmployeeNoValid && isPasswordValid) {

            mDatabase.child("User").orderByChild("employeeNumber").equalTo(employeeNumber.getText().toString())
                  .addListenerForSingleValueEvent(new ValueEventListener() {

              @Override
              public void onDataChange(DataSnapshot snapshot) {
                  ArrayList<Employee> employeeArrayList = new ArrayList<>();
                  for(DataSnapshot uniqueKeySnapshot : snapshot.getChildren()){
                      //Loop 1 to go through all the child nodes of users
                      Employee employee = uniqueKeySnapshot.getValue(Employee.class);
                      Log.e("Logged In user","Is Found"+employee.getEmployeeNumber());
                      String passwordValue = String.valueOf(uniqueKeySnapshot.child("password").getValue());
                      Log.e("Logged In user","passwordValue"+passwordValue);
                      if(passwordValue.equals(password.getText().toString()))
                      {
                          isUserFound = true;
                          employee.setUserId(uniqueKeySnapshot.getKey());
                          Constants.employee = employee;
                        Log.e("Logged In user","Is Found"+uniqueKeySnapshot.child("first_name").getValue());
                      }
                  }

                  if(isUserFound)
                  {
                      if(Constants.employee.getUserType().equals("Employee"))
                      {
                          Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                          finishAffinity();
                          startActivity(intent);
                          if(!Constants.isLoggedIn)
                          {
                              Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                          }
                          isUserFound = false;
                          Constants.isLoggedIn = true;
                      }
                      else if(Constants.employee.getUserType().equals("Supervisor"))
                      {
                          Intent intent = new Intent(getApplicationContext(), MainActivitySupervisor.class);
                          finishAffinity();
                          startActivity(intent);
                          if(!Constants.isLoggedIn)
                          {
                              Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                          }
                          isUserFound = false;
                          Constants.isLoggedIn = true;
                      }
                      mDatabase.removeEventListener(this);
                  }
                  else{
                      Toast.makeText(getApplicationContext(), "Employee Id or password is wrong", Toast.LENGTH_SHORT).show();
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
    protected void onPause() {
        super.onPause();
        if(listner1 != null)
        {
            mDatabase.removeEventListener(listner1);
        }
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
    protected void onDestroy() {
        super.onDestroy();
        if(listner1 != null)
        {
            mDatabase.removeEventListener(listner1);
        }

    }
}
