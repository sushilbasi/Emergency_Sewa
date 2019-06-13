package com.college.emergencysewa;

import android.app.ActionBar;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class activity_profile extends AppCompatActivity{

    TextView username;
    TextView fullname;
    TextView date_of_birth;
    TextView email;
    TextView phone_number;
    TextView assist;

    TextView btn_edit;
    ActionBar actionBar;
    Button displayImage;
    Button logout;

    public boolean onCreateOptionsMenu(Menu menu) {
        // This menu let us add Profile icons on the ActionBar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFE8E8")));
        actionBar.setTitle(Html.fromHtml("<font color='#FF1515' size='15dp'><b>&nbsp &nbsp &nbsp  Profile</b></font>"));

        setContentView(R.layout.activity_profile);
        username = (TextView) findViewById(R.id.username);
        fullname = (TextView) findViewById(R.id.fullname);
        date_of_birth = (TextView) findViewById(R.id.date_of_birth);
        email = (TextView) findViewById(R.id.email);
        phone_number = (TextView) findViewById(R.id.phone_number);
        assist = (TextView) findViewById(R.id.assist);
        btn_edit = (TextView) findViewById(R.id.btn_edit);
        displayImage = (Button) findViewById(R.id.btn_displayImage);
        logout = (Button) findViewById(R.id.btn_logout);

        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        String uname=sp1.getString("username", null);
        String fname=sp1.getString("fname", null)+" "+sp1.getString("mname", null)+" "+sp1.getString("lname", null);
        String dob=sp1.getString("date_of_birth", null);
        String uemail=sp1.getString("email", null);
        String phno=sp1.getString("phone_number", null);
        String uassist=sp1.getString("assist", null);
        String peronalid=sp1.getString("personal_id", null);

        username.setText(uname);
        fullname.setText(fname);
        date_of_birth.setText(dob);
        email.setText(uemail);
        phone_number.setText(phno);
        assist.setText(uassist);

        btn_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_profile.this,edit_profile.class));
            }
        });

        displayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_profile.this,display_image.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.exit_profile)
        {
            Toast.makeText(this,"Profile closed",Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}