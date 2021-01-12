package com.example.app2;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SearchFragment extends Fragment {
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private Calendar currentDate;
    private RecyclerView.Adapter searchAdapter;
    ArrayList<Group> group_list;
    private String userId;
    private String query = "";
    private Double endpoint_lat;
    private Double endpoint_lon;
    private LatLng location;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        this.userId = bundle.getString("userId");
        currentDate = Calendar.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        //set date picker text to current date
        ImageButton datepickbtn = (ImageButton) view.findViewById(R.id.date_pickerbtn);
        TextView datepicker = view.findViewById(R.id.date_picker);
        Date date = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        String current = transFormat.format(date);
        datepicker.setText(current);

        RecyclerView recyclerView = view.findViewById(R.id.searchRecycleview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        group_list = new ArrayList<Group>();
        Logger.log("userId at search fragment is ", this.userId);
        searchAdapter = new searchAdapter(group_list, getContext(), this.userId);
        recyclerView.setAdapter(searchAdapter);

        callbackMethod =  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                currentDate.set(year, month, dayOfMonth);
                datepicker.setText(transFormat.format(currentDate.getTime()));
                searchGroup(query,endpoint_lat, endpoint_lon, recyclerView);
            }
        };

        datepickbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext());
                dialog.setOnDateSetListener(callbackMethod);
                dialog.show();
            }
        });

        //searching landing place
        PlacesClient placesClient;

        String apiKey = getString(R.string.API_KEY);
        if(!Places.isInitialized()) {
            Places.initialize(getContext(), apiKey);
        }
        placesClient = Places.createClient(getContext());


        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_search);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteSupportFragment.setHint("도착지를 입력하시오");
        autocompleteSupportFragment.setCountry("KR");
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                query = place.getName();
                location = place.getLatLng();
                endpoint_lat = location.latitude;
                endpoint_lon = location.longitude;
                searchGroup(query,endpoint_lat,endpoint_lon , recyclerView);
            }

            @Override
            public void onError(@NonNull Status status) {
                Logger.log("error finding place", "Failed");
            }
        });


//        SearchView searchview =  view.findViewById(R.id.search);
//        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                //use geocoding api to find location
//                searchGroup(query, recyclerView);
//                return true;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

        return view;
    }

    private void searchGroup(String query, Double lat, Double lon,RecyclerView recyclerView) {
        this.query = query;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String geourl = "http://192.249.18.169:8080/group/search";
        HashMap data = new HashMap();
        group_list.clear();

        //make date as simple date format
        Calendar matchdate = currentDate;
        data.put("endpointaddress", query);
        data.put("matchdate", currentDate.getTime().toString());
        data.put("endpoint_lat", lat);
        data.put("endpoint_lon", lon);
        JSONObject jreq = new JSONObject(data);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, geourl, jreq, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                //get array of Group(DB) object
                try {
                    Logger.log("got reeeeeeqqqqqqqq", "got req");
                    JSONArray searchData = response.getJSONArray("data");
                    for(int i=0 ; i < searchData.length() ; i ++) {
                        JSONObject jsondata = searchData.getJSONObject(i);
                        Group group = new Group();
                        Date matchdate = Date.from(Instant.parse(jsondata.getString("time")));
                        group.Group(jsondata.getString("groupId"), jsondata.getJSONObject("startPoint").getString("formatAddress"), jsondata.getJSONObject("endPoint").getString("formatAddress"), matchdate,jsondata.getJSONArray("members").length(), jsondata.getString("creator"));
                        group_list.add(group);
                    }
                    searchAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}