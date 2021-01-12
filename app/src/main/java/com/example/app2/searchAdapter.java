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
        public TextView grouptimeView;

        public ViewHolder(View view) {
            super(view);
            startpointView = (TextView) view.findViewById(R.id.startPoint);
            endpointView = (TextView) view.findViewById(R.id.endPoint);
            joinBtn = (Button) view.findViewById(R.id.joinbtn);
            grouptimeView = (TextView) view.findViewById(R.id.grouptime);
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
        holder.startpointView.setText(group.startPoint);
        holder.endpointView.setText(group.endPoint);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        holder.grouptimeView.setText(dateFormat.format(group.time));
        //join text
        int num_members = group.member_num;
        holder.joinBtn.setText(Integer.toString(num_members) + "/4");
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
                                holder.joinBtn.setText(Integer.toString(num_members + 1) + "/4");
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
