package com.example.foodonmeet.ProfileSetup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.foodonmeet.R;

import java.util.Calendar;
import java.util.Date;

public class ProfileSetupBirthdateFragment extends Fragment {

    DatePicker dpBirthdate;

    public ProfileSetupBirthdateFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_setup_birthdate, container, false);

        dpBirthdate = view.findViewById(R.id.dpBirthdate);

        return view;
    }

    public Date getBirthdate() {
        int day = dpBirthdate.getDayOfMonth();
        int month = dpBirthdate.getMonth();
        int year =  dpBirthdate.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}
