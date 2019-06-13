package com.college.emergencysewa;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
public class activity_registration extends AppCompatActivity implements OnClickListener{

    ConstraintLayout constraintLayout;

    SharedPreferences sp1;
    SharedPreferences.Editor Ed1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        sp1=getSharedPreferences("Register", MODE_PRIVATE);
        Ed1=sp1.edit();

        if(savedInstanceState == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            reg1 frag = new reg1();
            fragmentTransaction.add(R.id.reg_list, frag);
            fragmentTransaction.commit();




        }

    }




    public  void onClick(View v)
    {

        Intent intent = new Intent (activity_registration.this,LoginUser.class);
        this.startActivity(intent);
    }


}
