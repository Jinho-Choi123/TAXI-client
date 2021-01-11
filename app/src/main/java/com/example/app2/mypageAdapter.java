package com.example.app2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class mypageAdapter extends RecyclerView.Adapter<mypageAdapter.ViewHolder> {
    private ArrayList<Group> mDataset;
    private RequestQueue requestQueue;
    private String userId;
    private String mypageURL;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView startpointView;
        public TextView endpointView;
        public TextView membernumView;
        public Button leaveBtn;
        public Button chatBtn;


        public ViewHolder(View view) {
            super(view);
            startpointView = (TextView) view.findViewById(R.id.startpoint);
            endpointView = (TextView) view.findViewById(R.id.endpoint);
            membernumView = (TextView) view.findViewById(R.id.members_num);
            leaveBtn = (Button) view.findViewById(R.id.leavebtn);
            chatBtn = (Button) view.findViewById(R.id.chatbtn);
        }
    }

    public mypageAdapter(ArrayList<Group> dataset, Context context, String userId) {
        this.mDataset = dataset;
        this.requestQueue = Volley.newRequestQueue(context);
        this.userId = userId;
        this.context = context;
        this.mypageURL = "http://192.249.18.169:8080/group/delete";
    }

    @Override
    public mypageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mypage_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Group group = mDataset.get(position);
        holder.startpointView.setText(group.startPoint);
        holder.membernumView.setText(Integer.toString(group.member_num) + " / 4");
        holder.endpointView.setText(group.endPoint);

        //leave btn
        holder.leaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap data = new HashMap();
                data.put("userId", userId);
                data.put("groupId", group.groupId);

                JSONObject jsonreq = new JSONObject(data);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, mypageURL, jsonreq, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast deleteStatus = Toast.makeText(context, response.getString("msg"), Toast.LENGTH_LONG);
                            deleteStatus.show();
                            mDataset.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mDataset.size());
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

        //chat btn
        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupid = group.groupId;
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("groupId", groupid);
                intent.putExtra("userId", userId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
