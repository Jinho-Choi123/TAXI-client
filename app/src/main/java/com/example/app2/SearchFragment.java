package com.example.app2;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;

import java.util.Calendar;
import java.util.Date;

public class SearchFragment extends Fragment {
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private int setYear;
    private int setMonth;
    private int setDay;
    private Calendar currentDate;



    public SearchFragment() {
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDate = Calendar.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ImageButton datepicker = view.findViewById(R.id.date_picker);
        callbackMethod =  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setYear = year;
                setMonth = month;
                setDay = dayOfMonth;
            }
        };

        datepicker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), callbackMethod, currentDate.YEAR, currentDate.MONTH, currentDate.DAY_OF_MONTH);
                dialog.show();
            }
        });
        return view;
    }
}