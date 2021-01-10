package com.example.app2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.libraries.places.api.model.Place;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddFragment extends Fragment {
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
    private DatePickerDialog.OnDateSetListener callbackMethod_d;
    private TimePickerDialog.OnTimeSetListener callbackMethod_t;

    private int setYear;
    private int setMonth;
    private int setDay;
    private int setHour;
    private int setMinute;

    private Calendar currentDate;

    Intent searchIntent;

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

        TextView tv_add_d = view.findViewById(R.id.tv_add_d);
        TextView tv_add_t = view.findViewById(R.id.tv_add_t);

        EditText et_add_start = view.findViewById(R.id.et_add_start);
        EditText et_add_arrive = view.findViewById(R.id.et_add_arrive);

        ImageButton ib_start = view.findViewById(R.id.ib_start);
        ImageButton ib_arrive = view.findViewById(R.id.ib_arrive);

        TextView tv_add_start = view.findViewById(R.id.tv_add_start);
        TextView tv_add_arrive = view.findViewById(R.id.tv_add_arrive);

        callbackMethod_d = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setYear = year;
                setMonth = month;
                setDay = dayOfMonth;
                tv_add_d.setText("" + year + "년" + month + "월" + dayOfMonth + "일");
            }
        };
        callbackMethod_t = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setHour = hourOfDay;
                setMinute = minute;
                tv_add_t.setText(hourOfDay + "시" + minute + "분");
            }
        };


        date_picker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog_d = new DatePickerDialog(getContext(), callbackMethod_d, currentDate.YEAR, currentDate.MONTH, currentDate.DAY_OF_MONTH);
                dialog_d.show();
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

        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog_t = new TimePickerDialog(getContext(), callbackMethod_t, 0, 0, true);
                dialog_t.show();
            }
        });

        /*et_add_start.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    SelectLocation(v);
                }
            }
        });*/
        //edit을 두번 클릭하면 위치 자동완성 activity 생성

        /*et_add_arrive.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    startAutocompleteActivity(v);
                }
            }
        });*/

        ib_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

       /* ib_arrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).loadFragment(new MapFragment());
            }
        });*/

        return view;
    }

    /*public void SelectLocation(View v) {
        *//*searchIntent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        startActivityForResult(searchIntent, AUTOCOMPLETE_REQUEST_CODE);*//*
        //Fragment 전환
        ((MainActivity)getActivity()).loadFragment(new MapFragment());

    }*/
    /*public void startAutocompleteActivity (View view) {
        Intent intent = new Autocomplete.IntentBuilder (AutocompleteActivityMode.FULLSCREEN, Arrays.asList(Place.Field.ID, Place.Field.NAME)).build(getContext());
    }*/

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
