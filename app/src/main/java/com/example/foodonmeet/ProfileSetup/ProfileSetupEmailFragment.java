package com.example.foodonmeet.ProfileSetup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.foodonmeet.R;

public class ProfileSetupEmailFragment extends Fragment {

    EditText etEmail;
    Button btnNext;

    public ProfileSetupEmailFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_setup_email, container, false);

        etEmail = view.findViewById(R.id.etEmail);
        btnNext = view.findViewById(R.id.btnNext);

        etEmail.addTextChangedListener(new TextWatcher() {
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

    public String getEmail() {
        return etEmail.getText().toString();
    }
}
