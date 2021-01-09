package com.example.app2;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class AddFragment extends Fragment {
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private int setYear;
    private int setMonth;
    private int setDay;
    private Calendar currentDate;
    TextView tv_add_td;

    public AddFragment() {
        // Required empty public constructor
    }

    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add, container, false);
        ImageButton date_picker = view.findViewById(R.id.add_date_picker);
        ImageButton time_picker = view.findViewById(R.id.add_time_picker);
        tv_add_td = view.findViewById(R.id.tv_add_td);

        callbackMethod =  new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setYear = year;
                setMonth = month;
                setDay = dayOfMonth;
                tv_add_td.setText("" + year + "/" + month + "." + dayOfMonth);
                //System.out.println(year);
            }
        };

        date_picker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), callbackMethod, currentDate.YEAR, currentDate.MONTH, currentDate.DAY_OF_MONTH);
                dialog.show();
               /* dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                            tv_add_td.setText("왜 안돼 짜증나게");
                    }
                });*/
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dialog.getDatePicker().setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            tv_add_td.setText(year + "/" + monthOfYear + "." + dayOfMonth);
                        }
                    });
                }*/

            }

        });

        return view;
    }

    /*public void SelectTime(View v) {
        String date = tv_add_td.getText().toString();
        TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tv_add_td.setText(hourOfDay + " : " + minute);
            }
        }, 0, 0, false);
        dialog.show();
        dialog.getButton(BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        dialog.getButton(BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                tv_add_td.setText(date);
            }
        });
    }*/
}