package com.example.employeemanagement.aboutUs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.employeemanagement.Constant.Constants;
import com.example.employeemanagement.R;
import com.example.employeemanagement.helper.BackButtonHandlerInterface;
import com.example.employeemanagement.helper.OnBackClickListener;
import com.example.employeemanagement.home.HomeFragment;


public class AboutUsFragment extends Fragment implements OnBackClickListener {

    private BackButtonHandlerInterface backButtonHandler;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        backButtonHandler = (BackButtonHandlerInterface) activity;
        backButtonHandler.addBackClickListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        backButtonHandler.removeBackClickListener(this);
        backButtonHandler = null;
    }

    @Override
    public boolean onBackClick() {
        //This method handle onBackPressed()! return true or false
        Log.e("On","Back Fragment");
        Constants.isBackFrom = "AboutUs";
        return true;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Load view from the xml
        View root = inflater.inflate(R.layout.fragment_about_us, container, false);
        return root;
    }
}