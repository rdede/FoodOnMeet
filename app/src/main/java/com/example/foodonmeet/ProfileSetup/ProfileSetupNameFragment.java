package com.example.foodonmeet.ProfileSetup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.foodonmeet.R;

public class ProfileSetupNameFragment extends Fragment {

    EditText etName;
    Button btnNext;

    public ProfileSetupNameFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_setup_name, container, false);

        etName = view.findViewById(R.id.etName);
        btnNext = view.findViewById(R.id.btnNext);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundColor(getResources().getColor(R.color.grey_500));
                } else {
                    btnNext.setEnabled(true);
                    btnNext.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        return view;
    }

    public String getName() {
        return etName.getText().toString();
    }
}
