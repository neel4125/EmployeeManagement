package com.example.employeemanagement.activity.employee;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.R;
import com.example.employeemanagement.activity.employee.model.Availability;
import com.example.employeemanagement.activity.supervisor.EditProfileSupervisorActivity;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddAvailabilityActivity extends AppCompatActivity {
    ImageView imgBack;
    ArrayList<Availability> availabilities = new ArrayList<Availability>();
    TextView txtMonday,txtTuesday,txtWednesday,txtThursday,txtFriday,txtSaturday,txtSunday;
    TextView txtMondayTo,txtTuesdayTo,txtWednesdayTo,txtThursdayTo,txtFridayTo,txtSaturdayTo,txtSundayTo;
    AppCompatButton btnSubmit;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_availabity);

        btnSubmit = (AppCompatButton) findViewById(R.id.btnSubmit);
        txtMonday = (TextView)findViewById(R.id.txtMonday);
        txtTuesday = (TextView)findViewById(R.id.txtTuesday);
        txtWednesday = (TextView)findViewById(R.id.txtWednesday);
        txtThursday = (TextView)findViewById(R.id.txtThursday);
        txtFriday = (TextView)findViewById(R.id.txtFriday);
        txtSaturday = (TextView)findViewById(R.id.txtSaturday);
        txtSunday = (TextView)findViewById(R.id.txtSunday);

        txtMondayTo = (TextView)findViewById(R.id.txtMondayTo);
        txtTuesdayTo = (TextView)findViewById(R.id.txtTuesdayTo);
        txtWednesdayTo = (TextView)findViewById(R.id.txtWednesdayTo);
        txtThursdayTo = (TextView)findViewById(R.id.txtThursdayTo);
        txtFridayTo = (TextView)findViewById(R.id.txtFridayTo);
        txtSaturdayTo = (TextView)findViewById(R.id.txtSaturdayTo);
        txtSundayTo = (TextView)findViewById(R.id.txtSundayTo);

        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        availabilities.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Availability monday = new Availability();
        monday.setDay("Monday");
        Availability tue = new Availability();
        tue.setDay("Tuesday");
        Availability wednesday = new Availability();
        wednesday.setDay("Wednesday");
        Availability thursday = new Availability();
        thursday.setDay("Thursday");
        Availability friday = new Availability();
        friday.setDay("Friday");
        Availability saturday = new Availability();
        saturday.setDay("Saturday");
        Availability sunday = new Availability();
        sunday.setDay("Sunday");

        availabilities.add(monday);
        availabilities.add(tue);
        availabilities.add(wednesday);
        availabilities.add(thursday);
        availabilities.add(friday);
        availabilities.add(saturday);
        availabilities.add(sunday);

        if(Constants.employee.getAvailability() != null && (!Constants.employee.getAvailability().equals("")))
        {
            Gson gson = new Gson();
            String jsonText = Constants.employee.getAvailability();
            Log.e("Json Text","Is Here"+jsonText);
            Type type = new TypeToken<ArrayList<Availability>>() {}.getType();
            ArrayList<Availability> availabilitiesfirebase = gson.fromJson(jsonText, type);
            Log.e("Json Text","Is Here"+availabilitiesfirebase.size());
            txtMonday.setText(availabilitiesfirebase.get(0).getTime());
            txtTuesday.setText(availabilitiesfirebase.get(1).getTime());
            txtWednesday.setText(availabilitiesfirebase.get(2).getTime());
            txtThursday.setText(availabilitiesfirebase.get(3).getTime());
            txtFriday.setText(availabilitiesfirebase.get(4).getTime());
            txtSaturday.setText(availabilitiesfirebase.get(5).getTime());
            txtSunday.setText(availabilitiesfirebase.get(6).getTime());

            txtMondayTo.setText(availabilitiesfirebase.get(0).getToTime());
            txtTuesdayTo.setText(availabilitiesfirebase.get(1).getToTime());
            txtWednesdayTo.setText(availabilitiesfirebase.get(2).getToTime());
            txtThursdayTo.setText(availabilitiesfirebase.get(3).getToTime());
            txtFridayTo.setText(availabilitiesfirebase.get(4).getToTime());
            txtSaturdayTo.setText(availabilitiesfirebase.get(5).getToTime());
            txtSundayTo.setText(availabilitiesfirebase.get(6).getToTime());

            availabilities.get(0).setTime(availabilitiesfirebase.get(0).getTime());
            availabilities.get(1).setTime(availabilitiesfirebase.get(1).getTime());
            availabilities.get(2).setTime(availabilitiesfirebase.get(2).getTime());
            availabilities.get(3).setTime(availabilitiesfirebase.get(3).getTime());
            availabilities.get(4).setTime(availabilitiesfirebase.get(4).getTime());
            availabilities.get(5).setTime(availabilitiesfirebase.get(5).getTime());
            availabilities.get(6).setTime(availabilitiesfirebase.get(6).getTime());

            availabilities.get(0).setToTime(availabilitiesfirebase.get(0).getToTime());
            availabilities.get(1).setToTime(availabilitiesfirebase.get(1).getToTime());
            availabilities.get(2).setToTime(availabilitiesfirebase.get(2).getToTime());
            availabilities.get(3).setToTime(availabilitiesfirebase.get(3).getToTime());
            availabilities.get(4).setToTime(availabilitiesfirebase.get(4).getToTime());
            availabilities.get(5).setToTime(availabilitiesfirebase.get(5).getToTime());
            availabilities.get(6).setToTime(availabilitiesfirebase.get(6).getToTime());

        }



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String availibility =  gson.toJson(availabilities);
                Constants.employee.setAvailability(availibility);
                mDatabase.child("User").child(Constants.employee.getUserId()).setValue(Constants.employee,new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                        Toast.makeText(AddAvailabilityActivity.this,"Availability added successfully",Toast.LENGTH_SHORT).show();
                       if(Constants.isFromSupervisor)
                       {
                           Intent intent = new Intent(AddAvailabilityActivity.this, EditProfileSupervisorActivity.class);
                           Constants.isFromSupervisor = false;
                           finish();
                           startActivity(intent);
                       }
                       else
                       {
                           Intent intent = new Intent(AddAvailabilityActivity.this, EditProfileActivity.class);
                           Constants.isFromSupervisor = false;
                           finish();
                           startActivity(intent);
                       }
                    }
                });
            }
        });

        txtMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(txtMonday);
            }
        });

        txtTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(txtTuesday);
            }
        });

        txtWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(txtWednesday);
            }
        });

        txtThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(txtThursday);
            }
        });

        txtFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(txtFriday);
            }
        });

        txtSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(txtSaturday);
            }
        });

        txtSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(txtSunday);
            }
        });





        txtMondayTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(txtMondayTo);
            }
        });

        txtTuesdayTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(txtTuesdayTo);
            }
        });

        txtWednesdayTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(txtWednesdayTo);
            }
        });

        txtThursdayTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(txtThursdayTo);
            }
        });

        txtFridayTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(txtFridayTo);
            }
        });

        txtSaturdayTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(txtSaturdayTo);
            }
        });

        txtSundayTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(txtSundayTo);
            }
        });


    }
         void openTimePicker(TextView txtView){
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddAvailabilityActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY,selectedHour);
                    cal.set(Calendar.MINUTE,selectedMinute);
                    Format formatter;
                    formatter = new SimpleDateFormat("hh:mm a");

                    txtView.setText( formatter.format(cal.getTime()));
                    if(txtView.getId() == txtMonday.getId())
                    {
                        availabilities.get(0).setTime(txtView.getText().toString());
                    }
                    else if(txtView.getId() == txtTuesday.getId())
                    {
                        availabilities.get(1).setTime(txtView.getText().toString());
                    }
                    else if(txtView.getId() == txtWednesday.getId())
                    {
                        availabilities.get(2).setTime(txtView.getText().toString());
                    }
                    else if(txtView.getId() == txtThursday.getId())
                    {
                        availabilities.get(3).setTime(txtView.getText().toString());
                    }
                    else if(txtView.getId() == txtFriday.getId())
                    {
                        availabilities.get(4).setTime(txtView.getText().toString());
                    }
                    else if(txtView.getId() == txtSaturday.getId())
                    {
                        availabilities.get(5).setTime(txtView.getText().toString());
                    }
                    else if(txtView.getId() == txtSunday.getId())
                    {
                        availabilities.get(6).setTime(txtView.getText().toString());
                    }


                    else if(txtView.getId() == txtMondayTo.getId())
                    {
                        availabilities.get(0).setToTime(txtView.getText().toString());
                    }
                    else if(txtView.getId() == txtTuesdayTo.getId())
                    {
                        availabilities.get(1).setToTime(txtView.getText().toString());
                    }
                    else if(txtView.getId() == txtWednesdayTo.getId())
                    {
                        availabilities.get(2).setToTime(txtView.getText().toString());
                    }
                    else if(txtView.getId() == txtThursdayTo.getId())
                    {
                        availabilities.get(3).setToTime(txtView.getText().toString());
                    }
                    else if(txtView.getId() == txtFridayTo.getId())
                    {
                        availabilities.get(4).setToTime(txtView.getText().toString());
                    }
                    else if(txtView.getId() == txtSaturdayTo.getId())
                    {
                        availabilities.get(5).setToTime(txtView.getText().toString());
                    }
                    else if(txtView.getId() == txtSundayTo.getId())
                    {
                        availabilities.get(6).setToTime(txtView.getText().toString());
                    }
                }
            }, hour, minute, false);//No 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        }
}
