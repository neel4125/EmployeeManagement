package com.example.employeemanagement.activity.supervisor;

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
import com.example.employeemanagement.activity.employee.EditProfileActivity;
import com.example.employeemanagement.activity.employee.model.Availability;
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

public class ViewAvailabilityActivity extends AppCompatActivity {
    ImageView imgBack;
    ArrayList<Availability> availabilities = new ArrayList<Availability>();
    TextView txtMonday,txtTuesday,txtWednesday,txtThursday,txtFriday,txtSaturday,txtSunday;
    TextView txtMondayTo,txtTuesdayTo,txtWednesdayTo,txtThursdayTo,txtFridayTo,txtSaturdayTo,txtSundayTo;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_availabity);


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

        if(Constants.selectedEmployee.getAvailability() != null && (!Constants.selectedEmployee.getAvailability().equals("")))
        {
            Gson gson = new Gson();
            String jsonText = Constants.selectedEmployee.getAvailability();
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





    }

}
