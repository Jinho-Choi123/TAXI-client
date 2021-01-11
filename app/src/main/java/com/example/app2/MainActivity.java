package com.example.app2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
                loadFragment(new AddFragment());
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


