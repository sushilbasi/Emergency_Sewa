package com.college.emergencysewa;

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
import android.view.View.OnClickListener;


public class edit_profile extends AppCompatActivity implements OnClickListener {

    TextView username;
    TextView firstname;
    TextView middlename;
    TextView lastname;
    TextView date_of_birth;
    TextView email;
    TextView phone_number;
    TextView assist;
    Button update;
    Button displayImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);



        username = (TextView) findViewById(R.id.edit_username);
        firstname = (TextView) findViewById(R.id.firstname);
        middlename = (TextView) findViewById(R.id.middlename);
        lastname = (TextView) findViewById(R.id.lastname);
        date_of_birth = (TextView) findViewById(R.id.date_of_birth);
        email = (TextView) findViewById(R.id.email);
        phone_number = (TextView) findViewById(R.id.phone_number);
        assist = (TextView) findViewById(R.id.assist);

        update =(Button) findViewById(R.id.btn_update);
        update.setOnClickListener(this);

        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        String uname=sp1.getString("username", null);
        String fname=sp1.getString("fname", null);
        String mname=sp1.getString("mname", null);
        String lname=sp1.getString("lname", null);
        String dob=sp1.getString("date_of_birth", null);
        String uemail=sp1.getString("email", null);
        String phno=sp1.getString("phone_number", null);
        String uassist=sp1.getString("assist", null);

        username.setText(uname);
        firstname.setText(fname);
        middlename.setText(mname);
        lastname.setText(lname);
        date_of_birth.setText(dob);
        email.setText(uemail);
        phone_number.setText(phno);
        assist.setText(uassist);

        //btn_edit.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this,"Updated Successful",Toast.LENGTH_LONG).show();
        finish();
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
