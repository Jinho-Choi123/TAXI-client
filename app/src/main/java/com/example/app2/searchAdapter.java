package com.example.app2;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.ViewHolder> {
    private ArrayList<Group> mDataset;
    private RequestQueue requestQueue;
    private String userId;
    private String joinURL;
    private Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView startpointView;
        public TextView endpointView;
        public Button joinBtn;
        public TextView time_hour_View;
        public TextView time_minute_View;
        public TextView member_num_View;

        public ViewHolder(View view) {
            super(view);
            startpointView = (TextView) view.findViewById(R.id.startPoint);
            endpointView = (TextView) view.findViewById(R.id.endPoint);
            joinBtn = (Button) view.findViewById(R.id.joinbtn);
            time_hour_View = (TextView) view.findViewById(R.id.time_hour);
            time_minute_View = (TextView) view.findViewById(R.id.time_minute);
            member_num_View = (TextView) view.findViewById(R.id.members_num);
        }
    }

    public searchAdapter(ArrayList<Group> dataset, Context context, String userId) {
        this.mDataset = dataset;
        this.requestQueue = Volley.newRequestQueue(context);
        this.userId = userId;
        this.context = context;
        this.joinURL = "http://192.249.18.169:8080/group/join";
    }

    @Override
    public searchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Group group = mDataset.get(position);
        holder.startpointView.setText("출발지: "+group.startPoint);
        holder.endpointView.setText("도착지: "+group.endPoint);
        DateFormat dateFormat_hour = new SimpleDateFormat("hh");
        DateFormat dateFormat_minute = new SimpleDateFormat("mm");
        holder.time_hour_View.setText(dateFormat_hour.format(group.time));
        holder.time_minute_View.setText(" : " + dateFormat_minute.format(group.time));
        //join text
        int num_members = group.member_num;
        holder.member_num_View.setText("현재 인원: " + Integer.toString(num_members) + "/4");
        holder.joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //join the group!!
                HashMap data = new HashMap();
                data.put("groupId", group.groupId);
                data.put("userId", userId);
                JSONObject jsonreq = new JSONObject(data);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, joinURL, jsonreq, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast joinStatus = Toast.makeText(context, response.getString("msg"), Toast.LENGTH_LONG);
                            joinStatus.show();
                            if(response.getString("msg").equals("Joined Group!")) {
                                holder.member_num_View.setText("현재 인원: " + Integer.toString(num_members + 1) + "/4");
                            }
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
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
