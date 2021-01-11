package com.example.app2;

import android.Manifest;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView = null;

    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE};
    private GoogleMap mMap;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    final LatLng[] location = new LatLng[1];

    private DatePickerDialog.OnDateSetListener callbackMethod_d;
    private TimePickerDialog.OnTimeSetListener callbackMethod_t;

    private int setYear;
    private int setMonth;
    private int setDay;
    private int setHour;
    private int setMinute;

    private Calendar currentDate;
    Intent searchIntent;

    String arrive_str;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDate = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.getMapAsync(this);

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
                tv_add_d.setText("" + year + "/" + month + "/" + dayOfMonth);
            }
        };
        callbackMethod_t = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setHour = hourOfDay;
                setMinute = minute;
                tv_add_t.setText(hourOfDay + " : " + minute);
            }
        };

        date_picker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog_d = new DatePickerDialog(getContext(), callbackMethod_d, currentDate.YEAR, currentDate.MONTH, currentDate.DAY_OF_MONTH);
                dialog_d.show();
            }

        });

        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog_t = new TimePickerDialog(getContext(), callbackMethod_t, 0, 0, true);
                dialog_t.show();
            }
        });

        ib_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ib_arrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_add_arrive.setText(arrive_str);
            }
        });

        tv_add_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), "AIzaSyD9FkuohxlU_lxplMI5bSFHj8SgiGxxN8c");
        }

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /*@Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }*/

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mapView != null)
        {
            mapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapsCreateInitial();

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Toast.makeText(getActivity().getApplicationContext(), place.getName(), Toast.LENGTH_LONG).show();
                location[0] = place.getLatLng();
                Toast.makeText(getActivity().getApplicationContext(), location[0].toString(), Toast.LENGTH_LONG).show();

                mMap.addMarker(new MarkerOptions().position(location[0]).title("Updated Location Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location[0], 16.0f));

                arrive_str = place.getName();
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }

    public void mapsCreateInitial() {
        GpsTracker tracker = new GpsTracker(getContext());
        double lat = tracker.getLatitude();
        double lng = tracker.getLongitude();
        // Add a marker for current location and move the camera
        LatLng currentLocation = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Current Location Marker"));
        //camera 마커로 이동하고 zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16.0f));
    }
}