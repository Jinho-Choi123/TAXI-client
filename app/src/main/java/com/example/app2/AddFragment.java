package com.example.app2;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app2.GpsTracker;
import com.example.app2.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AddFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnPoiClickListener, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener {

    private String userId;
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

    LatLng locations_start;
    LatLng locations_arrive;

    private DatePickerDialog.OnDateSetListener callbackMethod_d;
    private TimePickerDialog.OnTimeSetListener callbackMethod_t;

    private int setYear;
    private int setMonth;
    private int setDay;
    private int setHour;
    private int setMinute;

    private Calendar currentDate;

    String start_address;
    String arrive_address;

    ArrayList<Double> start_loc;
    ArrayList<Double> end_loc;


    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        this.userId = bundle.getString("userId");
        currentDate = Calendar.getInstance();
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.HOUR, 0);
        start_loc = new ArrayList<Double>();
        end_loc = new ArrayList<Double>();
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

        Button btn_add = view.findViewById(R.id.btn_add);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        callbackMethod_d = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setYear = year;
                setMonth = month;
                setDay = dayOfMonth;
                currentDate.set(Calendar.YEAR, year);
                currentDate.set(Calendar.MONTH, month);
                currentDate.set(Calendar.DATE, dayOfMonth);

                tv_add_d.setText("Date: " + transFormat.format(currentDate.getTime()));
            }
        };
        callbackMethod_t = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setHour = hourOfDay;
                setMinute = minute;
                currentDate.set(Calendar.HOUR, hourOfDay);
                currentDate.set(Calendar.MINUTE, minute);

                tv_add_d.setText("Date: " + transFormat.format(currentDate.getTime()));
            }
        };

        date_picker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar today = Calendar.getInstance();

                DatePickerDialog dialog_d = new DatePickerDialog(getContext(), callbackMethod_d, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
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



        /*ib_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ib_arrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_add_arrive.setText(arrive_str);
            }
        });*/
        AutocompleteSupportFragment autocompleteFragment_start = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_start);

        autocompleteFragment_start.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment_start.setCountry("KR");
        autocompleteFragment_start.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                locations_start = place.getLatLng();

                start_address = place.getName();
                //drawMarker((ArrayList<LatLng>) location);
                start_loc.clear();
                start_loc.add(locations_start.longitude);
                start_loc.add(1, locations_start.latitude);

                mMap.addMarker(new MarkerOptions().position(locations_start).title("Updated Location Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations_start, 16.0f));

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

        AutocompleteSupportFragment autocompleteFragment_arrive = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_arrive);

        autocompleteFragment_arrive.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment_arrive.setCountry("KR");
        autocompleteFragment_arrive.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                PolylineOptions options = new PolylineOptions();
                options.color(Color.RED);

                locations_arrive = place.getLatLng();
                end_loc.clear();
                end_loc.add(locations_arrive.longitude);
                end_loc.add(1, locations_arrive.latitude);;
                arrive_address = place.getName();

                mMap.addMarker(new MarkerOptions().position(locations_arrive).title("Updated Location Marker"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location.get(1), 16.0f));

                mMap.addPolyline(options);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(locations_arrive).zoom(16).build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                Polyline polyline = mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                locations_start,
                                locations_arrive
                        ));
                polyline.setColor(Color.RED);
                polyline.setJointType(JointType.ROUND);

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsp = new JSONObject();
                try {
                    jsp.put("formatAddress", start_address);
                    jsp.put("type", "Point");
                    jsp.put("lat",  start_loc.get(1));
                    jsp.put("lon", start_loc.get(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject jap = new JSONObject();
                try {

                    jap.put("formatAddress", arrive_address);
                    jap.put("type", "Point");
                    jap.put("lat",  end_loc.get(1));
                    jap.put("lon", end_loc.get(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                String mypageURL = "http://192.249.18.169:8080/group/create";

                HashMap data = new HashMap();
                data.put("userId", userId);
                data.put("time", currentDate.getTime().toString());
                data.put("startPoint", jsp);
                data.put("endPoint", jap);

                JSONObject jreq = new JSONObject(data);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, mypageURL, jreq, new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        //get array of Group(DB) object
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                requestQueue.add(jsonObjectRequest);
                FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
                fragmentManager1.beginTransaction().remove(AddFragment.this).commit();
                loadFragment(new SearchFragment());
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager2 = getActivity().getSupportFragmentManager();
                fragmentManager2.beginTransaction().remove(AddFragment.this).commit();
                loadFragment(new SearchFragment());
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
    }

    public void mapsCreateInitial() {
        GpsTracker tracker = new GpsTracker(getContext());
        double lat = tracker.getLatitude();
        double lng = tracker.getLongitude();
        // Add a marker for current location and move the camera
        //LatLng currentLocation = new LatLng(lat, lng);
        start_loc.add(lat);
        start_loc.add(0, lng);

       //mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Current Location Marker"));
        //camera 마커로 이동하고 zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16.0f));
    }
    private void drawMarker(ArrayList<LatLng> l) {
        // Creating an instance of MarkerOptions

        PolylineOptions options = new PolylineOptions();
        options.color(Color.RED);

        for (int i = 0; i < l.size(); i++) {
            options.add(l.get(i));
            MarkerOptions marker = new MarkerOptions().position(l.get(i)).title("Bus")
                    ;
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

            // Adding marker on the Google Map
            mMap.addMarker(marker);
        }

        mMap.addPolyline(options);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(l.get(0).latitude, l.get(0).longitude)).zoom(18).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {

    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    /*public void removeFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }*/
    //fragment loader
    public void loadFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("userId", this.userId);
        fragment.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.commit();
    }
}