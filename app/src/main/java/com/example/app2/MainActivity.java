package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton add_btn = findViewById(R.id.add_btn);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show();
            }
        });
    }
//    void show () {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View view = inflater.inflate(R.layout.add, null);
//        builder.setView(view);
//
//        final Button add = (Button) view.findViewById(R.id.btn_add);
//        final Button cancel = (Button) view.findViewById(R.id.btn_cancel);
//        final EditText time = (EditText) view.findViewById(R.id.et_add_time);
//        final EditText start = (EditText) view.findViewById(R.id.et_add_start);
//        final EditText arrive = (EditText) view.findViewById(R.id.et_add_arrive);
//
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String sub_time = time.getText().toString();
//                String sub_start = start.getText().toString();
//                String sub_arrive = arrive.getText().toString();
//            }
//        });
//
//    }
}


