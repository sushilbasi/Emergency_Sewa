package com.college.emergencysewa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


public class activity_container extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final int STATE_SCORE = 40;
    private ActionBar actionBar;
    private Button profile;
    FrameLayout frameLayout;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // This menu let us add Profile icons on the ActionBar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_container, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        // code for action bar
        actionBar =getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_iconfinder_alarm__alert__light__emergency_2542103);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFE8E8")));
        actionBar.setTitle(Html.fromHtml("<font color='#FF1515' size='15dp'><b>EMERGENCY</b></font>"));
//        getActionBar().setIcon(R.drawable.ic_iconfinder_alarm__alert__light__emergency_2542103);

        //action bar profile button
//        profile = (Button) findViewById(R.id.action_profile);
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity_container.this, activity_profile.class);
//                startActivity(intent);
//            }
//        });

        //bottom navigation bar
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        frameLayout = (FrameLayout) findViewById(R.id.fragment_container);
        activity_home activityHome = new activity_home();
        loadFragment(activityHome);  // this code will open loadFragment function

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        view_container okkk = new view_container();
//        fragmentTransaction.add(R.id.fragment_container, okkk);
//        fragmentTransaction.commit();


    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return true; //it can be kept false it you want to show HOME as default open.
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId())
        {
            case R.id.navigation_home:
                fragment = new activity_home();
                break;
            case R.id.navigation_map:
                fragment = new activity_map();
                break;
            case R.id.navigation_message:
                fragment = new activity_message();
                break;
            case R.id.navigation_info:
                fragment = new activity_info();
                break;

        }

        return loadFragment(fragment) ;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_profile)
        {
            Toast.makeText(this, " Profile Opened",Toast.LENGTH_SHORT).show();
//            onBackPressed();
            startActivity(new Intent(this,activity_profile.class));
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    int backButtonCount=0;
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }



}
