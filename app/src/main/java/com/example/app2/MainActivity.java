package com.example.app2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.userId = intent.getStringExtra("userId");
        Logger.log("userid is gggggggggggggg", this.userId);
        setContentView(R.layout.activity_main);

        //set event handler for floating button
        FloatingActionButton add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //event when + button is clicked
            }
        });

        //change fragment when bottom nav bar is clicked
        BottomNavigationView bottomNavBar = (BottomNavigationView) findViewById(R.id.bottom_navbar);

        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.searchpage:
                        loadFragment(new SearchFragment());
                        break;
                    case R.id.chatpage:
                        loadFragment(new ChatFragment());
                        break;
                    case R.id.mypage:
                        loadFragment(new MyPageFragment());
                        break;
                    case R.id.logout:
                        //logout. clear all chached token, and finish the app
                        SharedPreferences sf = getSharedPreferences("AuthToken", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sf.edit();
                        editor.putString("jwt", "");
                        editor.apply();
                        finish();
                        break;
                }
                return true;
            }
        });

//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        Fragment initialFragment = new SearchFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("userId", this.userId);
//        initialFragment.setArguments(bundle);
//        ft.add(R.id.fragment, initialFragment);
//        ft.commit();

        loadFragment(new SearchFragment());

    }
    //fragment loader
    public void loadFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("userId", this.userId);
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.commit();
    }

}


