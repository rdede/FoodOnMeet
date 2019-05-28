package com.example.foodonmeet.ProfileSetup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodonmeet.R;
import com.hbb20.CountryCodePicker;

public class ProfileSetupNationalityFragment extends Fragment {

    CountryCodePicker ccp;

    public ProfileSetupNationalityFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_setup_nationality, container, false);

        ccp = view.findViewById(R.id.ccp);

        return view;
    }

    public String getNationality() {
        return ccp.getSelectedCountryNameCode();
    }
}
