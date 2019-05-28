package com.example.foodonmeet.Create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.foodonmeet.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreateDateFragment extends Fragment {

    private CalendarView cvDate;

    public CreateDateFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_date, container, false);

        cvDate = view.findViewById(R.id.cvDate);
        cvDate.setMinDate(Calendar.getInstance().getTimeInMillis());
        cvDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int day) {
                Date date = new GregorianCalendar(year, month, day).getTime();
                ((CreateActivity) getActivity()).setDate(date);
            }
        });

        return view;
    }
}
