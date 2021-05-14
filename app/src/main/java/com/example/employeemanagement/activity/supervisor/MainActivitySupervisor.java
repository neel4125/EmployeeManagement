package com.example.employeemanagement.activity.supervisor;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.GlideApp;
import com.example.employeemanagement.MainActivity;
import com.example.employeemanagement.R;
import com.example.employeemanagement.aboutUs.AboutUsFragment;
import com.example.employeemanagement.activity.LoginActivity;
import com.example.employeemanagement.activity.employee.ChatActivityEmployee;
import com.example.employeemanagement.activity.employee.EditProfileActivity;
import com.example.employeemanagement.activity.supervisor.adapter.EmployeeChatAdapter;
import com.example.employeemanagement.activity.supervisor.fragment.HomeFragmentSupervisor;
import com.example.employeemanagement.contactUs.ContactUsFragment;

import com.example.employeemanagement.helper.BackButtonHandlerInterface;
import com.example.employeemanagement.helper.OnBackClickListener;
import com.example.employeemanagement.home.HomeFragment;
import com.example.employeemanagement.model.Employee;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivitySupervisor extends AppCompatActivity  implements BackButtonHandlerInterface {
    ImageView navDrawer,clock;
    DrawerLayout drawer;
    TextView txtTitle,text_count;
    FloatingActionButton fab;
    NavigationView navigationView;
    int totalCount = 0;
    ValueEventListener listner1,listner3;
    private DatabaseReference mDatabase;
    private ArrayList<WeakReference<OnBackClickListener>> backClickListenersList = new ArrayList<>();
    @Override
    public void addBackClickListener(OnBackClickListener onBackClickListener) {
        backClickListenersList.add(new WeakReference<>(onBackClickListener));
    }

    @Override
    public void removeBackClickListener(OnBackClickListener onBackClickListener) {
        for (Iterator<WeakReference<OnBackClickListener>> iterator = backClickListenersList.iterator();
             iterator.hasNext();){
            WeakReference<OnBackClickListener> weakRef = iterator.next();
            if (weakRef.get() == onBackClickListener){
                iterator.remove();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get the id of the all the required fields
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        clock = (ImageView) findViewById(R.id.clock);
        Log.e("Logged In","Supervisor Name Is"+ Constants.employee.getFirst_name());
        Log.e("Logged In","Supervisor Is"+ Constants.employee.getUserId());
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        text_count = (TextView)findViewById(R.id.text_count);
        fab = (FloatingActionButton)findViewById(R.id.fab);
         navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
        clock.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivitySupervisor.this, ChatActivitySupervisor.class);
                startActivity(intent);
            }
        });
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivitySupervisor.this, ClockInOutActivitySupervisor.class);
                startActivity(intent);
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference();

        listner1 =  mDatabase.child("User").orderByChild("userType").equalTo("Employee")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        totalCount = 0;
                        ArrayList<Employee> employeeArrayList = new ArrayList<>();
                        for(DataSnapshot uniqueKeySnapshot : snapshot.getChildren()) {
                            //Loop 1 to go through all the child nodes of users
                            Employee employee = uniqueKeySnapshot.getValue(Employee.class);
                            Log.e("Employee Found","Name is"+employee.getFirst_name());
                            employee.setUserId(uniqueKeySnapshot.getKey());
                            totalCount = totalCount+employee.getMessageCount();
                            employeeArrayList.add(employee);
                        }
                        Log.e("totalCount","totalCount"+totalCount);
                        if(totalCount > 0)
                        {
                            text_count.setVisibility(View.VISIBLE);
                            text_count.setText(String.valueOf(totalCount));
                        }
                        else
                        {
                            text_count.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


       View view =  navigationView.getHeaderView(0);
        CircleImageView profileView = view.findViewById(R.id.profileView);
        TextView textView = view.findViewById(R.id.textView);
        GlideApp.with(this)
                .load(Constants.employee.getProfile_pic())
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(profileView);

        ((DrawerLayout) findViewById(R.id.drawer_layout)).addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                // Whatever you want
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                // Whatever you want
                GlideApp.with(MainActivitySupervisor.this)
                        .load(Constants.employee.getProfile_pic())
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile)
                        .into(profileView);
                textView.setText(Constants.employee.getFirst_name() + " "+Constants.employee.getLast_name());
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                // Whatever you want
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Whatever you want
            }
        });

//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivitySupervisor.this, EditProfileSupervisorActivity.class);
//                startActivity(intent);
//            }
//        });
        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivitySupervisor.this, EditProfileSupervisorActivity.class);
                startActivity(intent);
            }
        });
//        Glide.with(this)
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.imagenotfound)
//                .load(Constants.employee.getProfile_pic()).into(profileView);
        textView.setText(Constants.employee.getFirst_name() + " "+Constants.employee.getLast_name());
        navDrawer = (ImageView)findViewById(R.id.navDrawer);
        navDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //For open the navigation drawer on click
                drawer.openDrawer(GravityCompat.START);
            }
        });

        //Default set home fragment menu selection and home screen view
        navigationView.getMenu().getItem(0).setChecked(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new HomeFragmentSupervisor()).commit();
        }

    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        //handle menu change selection and select item
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
    private boolean fragmentsBackKeyIntercept() {
        boolean isIntercept = false;
        for (WeakReference<OnBackClickListener> weakRef : backClickListenersList) {
            OnBackClickListener onBackClickListener = weakRef.get();
            if (onBackClickListener != null) {
                boolean isFragmIntercept = onBackClickListener.onBackClick();
                if (!isIntercept) isIntercept = isFragmIntercept;
            }
        }
        return isIntercept;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(listner1 != null)
        {
            mDatabase.removeEventListener(listner1);
        }

    }
    @Override
    public void onBackPressed() {
        if(!fragmentsBackKeyIntercept()){
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivitySupervisor.this.finish();
                            Constants.isLoggedIn = false;
                            Constants.isRegister = false;
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else
        {
            txtTitle.setText("Employee Management");
            fab.setVisibility(View.VISIBLE);
            navigationView.getMenu().getItem(0).setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new HomeFragmentSupervisor()).commit();
            Constants.isBackFrom = "Home";
        }

//        new AlertDialog.Builder(this)
//                .setMessage("Are you sure you want to exit?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        MainActivitySupervisor.this.finish();
//                    }
//                })
//                .setNegativeButton("No", null)
//                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mDatabase == null || fab == null)
        {
            return;
        }
        listner1 =  mDatabase.child("User").orderByChild("userType").equalTo("Employee")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        totalCount = 0;
                        ArrayList<Employee> employeeArrayList = new ArrayList<>();
                        for(DataSnapshot uniqueKeySnapshot : snapshot.getChildren()) {
                            //Loop 1 to go through all the child nodes of users
                            Employee employee = uniqueKeySnapshot.getValue(Employee.class);
                            Log.e("Employee Found","Name is"+employee.getFirst_name());
                            employee.setUserId(uniqueKeySnapshot.getKey());
                            totalCount = totalCount+employee.getMessageCount();
                            employeeArrayList.add(employee);
                        }
                        Log.e("totalCount","totalCount"+totalCount);
                        if(totalCount > 0)
                        {
                            text_count.setVisibility(View.VISIBLE);
                            text_count.setText(String.valueOf(totalCount));
                        }
                        else
                        {
                            text_count.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                //Set home screen title and load home fragment
                txtTitle.setText("Employee Management");
                fab.setVisibility(View.VISIBLE);
                fragmentClass = HomeFragmentSupervisor.class;
                break;

            case R.id.nav_about_us:
                //Set about us screen title and load about us fragment
                txtTitle.setText("About Us");
                fab.setVisibility(View.GONE);
                fragmentClass = AboutUsFragment.class;
                break;
            case R.id.nav_contact_us:
                //Set contact us screen title and load contact us fragment
                txtTitle.setText("Contact Us");
                fab.setVisibility(View.GONE);
                fragmentClass = ContactUsFragment.class;
                break;
            case R.id.nav_logout:
                //Open logout dialog
                openLogoutDialog();
                break;
            default:
                fragmentClass = HomeFragmentSupervisor.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        if(fragment != null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawer.closeDrawers();
    }


    private void openLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivitySupervisor.this);
        builder.setTitle("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivitySupervisor.this, LoginActivity.class);
                startActivity(intent);
                //remove all the intermediate activity
                finishAffinity();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }


}