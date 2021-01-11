package com.example.app2;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MyPageFragment extends Fragment {
    private String userId;
    private RecyclerView.Adapter mypageAdapter;
    private ArrayList<Group> group_list;

    public MyPageFragment() {
        // Required empty public constructor
    }

    public static MyPageFragment newInstance(String param1, String param2) {
        MyPageFragment fragment = new MyPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        this.userId = bundle.getString("userId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_page, container, false);
        group_list = new ArrayList<Group>();

        mypageAdapter = new mypageAdapter(group_list, getContext(), this.userId);
        RecyclerView recyclerView = view.findViewById(R.id.mypageRecycleview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mypageAdapter);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String mypageURL = "http://192.249.18.169:8080/group/mypage";
        HashMap data = new HashMap();
        data.put("userId", userId);
        JSONObject jreq = new JSONObject(data);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, mypageURL, jreq, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                Logger.log("gggggggggggggg", "get mypage info");
                try {
                    JSONArray mypageData = response.getJSONArray("data");
                    for(int i = 0 ; i < mypageData.length() ; i ++ ) {
                        JSONObject jsondata = mypageData.getJSONObject(i);
                        Group group = new Group();
                        Date matchDate = Date.from(Instant.parse(jsondata.getString("time")));
                        group.Group(jsondata.getString("groupId"), jsondata.getJSONObject("startPoint").getString("formatAddress"), jsondata.getJSONObject("endPoint").getString("formatAddress"), matchDate,jsondata.getJSONArray("members").length(), jsondata.getString("creator"));
                        group_list.add(group);
                    }
                    mypageAdapter.notifyDataSetChanged();

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
        return view;
    }
}