package com.example.employeemanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.MainActivity;
import com.example.employeemanagement.R;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

  LinearLayout lilSupervisor,lilEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Get Ids of all the required fields
        lilSupervisor = (LinearLayout) findViewById(R.id.lilSupervisor);
        lilEmployee = (LinearLayout) findViewById(R.id.lilEmployee);

        //Handle on click of login button click
        lilSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterSupervisorActivity.class);
                Constants.isRegister = false;
                startActivity(intent);
            }
        });

        //Handle click of register button click
        lilEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to RegisterActivity
                Intent intent = new Intent(getApplicationContext(), RegisterEmployeeActivity.class);
                Constants.isRegister = false;
                startActivity(intent);
            }
        });
    }



}
