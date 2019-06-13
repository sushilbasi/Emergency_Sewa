package com.college.emergencysewa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class LoginCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_check);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        boolean logged=sp1.getBoolean("logged_in",false);

        if (logged){
            Intent int2 = new Intent(LoginCheck.this, activity_container.class);
            startActivity(int2);
        }else{
            Intent int2 = new Intent(LoginCheck.this, LoginUser.class);
            startActivity(int2);
        }
    }

}
