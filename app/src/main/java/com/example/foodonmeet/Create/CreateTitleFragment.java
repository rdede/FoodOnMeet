package com.example.foodonmeet.Create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.foodonmeet.R;

public class CreateTitleFragment extends Fragment {
    private EditText etTitle;

    public CreateTitleFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_title, container, false);

        etTitle = view.findViewById(R.id.etTitle);

        return view;
    }

    public String getTitle() {
        return etTitle.getText().toString();
    }
}
