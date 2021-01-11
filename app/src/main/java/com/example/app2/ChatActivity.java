package com.example.app2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView chatRecycler;
    private chatListAdapter chatAdapter;
    private ArrayList<Message> chat_list;
    private String userId;
    private String groupId;
    Socket socket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        this.userId = intent.getStringExtra("userId");
        this.groupId = intent.getStringExtra("groupId");
        //this.groupId = "2021016103874802501vwZuQ7TRTuKpqNIwfL5J6bCGDCQRwZEHoeDywzK";
        String loadChat = "http://192.249.18.169:8080/chat/load";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        HashMap data = new HashMap();
        data.put("roomId", this.groupId);
        JSONObject jsonreq = new JSONObject(data);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, loadChat, jsonreq, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                Logger.log(response.toString(), "ddddddddddd");
                try {
                    JSONArray content = response.getJSONArray("data");
                    for(int i = 0 ; i < content.length() ; i ++) {
                        JSONObject chat = content.getJSONObject(i);
                        Message msg = new Message();
                        msg.Message(chat.getString("message"), chat.getString("sender"), Date.from(Instant.parse(chat.getString("timestamp"))));
                        chat_list.add(msg);
                    }

                    chatAdapter.notifyDataSetChanged();
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

        chat_list = new ArrayList<Message>();

        try {
            socket = IO.socket("http://192.249.18.169:8081");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Logger.log("Chat socket connection failed", "failed");
        }
        socket.connect();

        socket.emit("join", this.groupId);



        chatRecycler = (RecyclerView) findViewById(R.id.recylerview_message_list);
        chatAdapter = new chatListAdapter(this, this.chat_list, this.userId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        chatRecycler.setLayoutManager(linearLayoutManager);
        chatRecycler.setAdapter(chatAdapter);

        EditText chatbox = (EditText) findViewById(R.id.edittext_chatbox);

        Button sendBtn = (Button) findViewById(R.id.button_chatbox_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject message = new JSONObject();
                Message msg = new Message();
                msg.Message(chatbox.getText().toString(), userId, new Date());
                chat_list.add(msg);
                try {
                    message.put("message", msg.message);
                    message.put("userId", msg.sender);
                    message.put("createdAt", msg.createdAt);
                    message.put("roomId", groupId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("send", message);
                chatAdapter.notifyDataSetChanged();
                chatRecycler.scrollToPosition(chatAdapter.getItemCount() - 1);
                chatbox.setText("");
            }
        });

        socket.on("updateChat", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                Message message = new Message();
                try {
                    Logger.log("got message", data.getString("message"));
                    message.Message(data.getString("message"), data.getString("userId"), new Date(data.getString("createdAt")));
                    Logger.log("making message obj success", "very good!!!!!!!!!!!");
                    chat_list.add(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Logger.log("before changing ui", "Gggd");
                        chatAdapter.notifyDataSetChanged();
                        chatRecycler.scrollToPosition(chatAdapter.getItemCount() - 1);
                    }
                });
            }
        });


    }

}