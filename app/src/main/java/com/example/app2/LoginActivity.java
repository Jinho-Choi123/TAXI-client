package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_btn = (Button) findViewById(R.id.loginBtn);
        EditText userId = (EditText) findViewById(R.id.userId);
        EditText userPassword = (EditText) findViewById(R.id.userPassword);

        String url = "http://192.249.18.169:8080/auth/login";

        Context context = getApplicationContext();
        login_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                HashMap data = new HashMap();
                data.put("userId", userId.getText().toString());
                data.put("userPassword", userPassword.getText().toString());
                JSONObject jsonreq = new JSONObject(data);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonreq, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("msg");
                            Boolean success = response.getBoolean("success");
                            if(success) {
                                //login success
                                Toast login = Toast.makeText(getApplicationContext(), "Login Success!", LENGTH_SHORT);
                                login.show();
                                String jwt = response.getString("jwt");
                                Logger.log("jwt token is ", jwt);
                                SharedPreferences sf = getSharedPreferences("AuthToken", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sf.edit();
                                editor.putString("jwt", jwt);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                //failed to login
                                Logger.log("login failed", "ddddddddddddddddddd");
                                Toast loginFail = Toast.makeText(LoginActivity.this, "Login Failed!", LENGTH_SHORT);
                                loginFail.show();
                                userId.setText("");
                                userPassword.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.log("error occurrrrrrrrrrrrrrrr", error.toString());
                    }
                });
                requestQueue.add(jsonObjectRequest);
            }
        });

        TextView register_btn = findViewById(R.id.register);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show register dialog
                showRegisterDialog();
            }
        });

    }
    private void showRegisterDialog (){
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout registerLayout = (LinearLayout) vi.inflate(R.layout.register_dialog, null);

        final EditText register_userId = (EditText) registerLayout.findViewById(R.id.register_userId);
        final EditText register_userPassword = (EditText) registerLayout.findViewById(R.id.register_userPassword);

        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Register")
                .setView(registerLayout)
                .setPositiveButton("Register as Teacher", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //when register button is clicked
                        String userid = register_userId.getText().toString();
                        String userpassword = register_userPassword.getText().toString();
                        Logger.log("user id and user password is ", userid + userpassword);
                        String register_url = "http://192.249.18.169:8080/auth/register";
                        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                        HashMap data = new HashMap();
                        data.put("userId", userid);
                        data.put("userPassword", userpassword);
                        data.put("isTeacher", true);
                        JSONObject jsonreq = new JSONObject(data);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, register_url, jsonreq, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Boolean success = response.getBoolean("success");
                                    String msg = response.getString("msg");
                                    if(success) {
                                        Toast registerSuccess = Toast.makeText(LoginActivity.this, "Register Success", LENGTH_SHORT);
                                        registerSuccess.show();
                                    } else {
                                        Toast registerFail = Toast.makeText(LoginActivity.this, msg, LENGTH_SHORT);
                                        registerFail.show();
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
                })
                .setNegativeButton("Register as Student", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //when register button is clicked
                        String userid = register_userId.getText().toString();
                        String userpassword = register_userPassword.getText().toString();
                        Logger.log("user id and user password is ", userid + userpassword);
                        String register_url = "http://192.249.18.169:8080/auth/register";
                        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                        HashMap data = new HashMap();
                        data.put("userId", userid);
                        data.put("userPassword", userpassword);
                        data.put("isTeacher", false);
                        JSONObject jsonreq = new JSONObject(data);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, register_url, jsonreq, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Boolean success = response.getBoolean("success");
                                    String msg = response.getString("msg");
                                    if(success) {
                                        Toast registerSuccess = Toast.makeText(LoginActivity.this, "Register Success", LENGTH_SHORT);
                                        registerSuccess.show();
                                    } else {
                                        Toast registerFail = Toast.makeText(LoginActivity.this, msg, LENGTH_SHORT);
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
                })
                .show();

    }
}