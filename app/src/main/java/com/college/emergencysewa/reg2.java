package com.college.emergencysewa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class reg2 extends Fragment implements OnClickListener{
    private TextView next;
    private TextView back;

    private EditText reg_fnames;
    private EditText reg_mnames;
    private EditText reg_lnames;
    private EditText reg_emails;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg2, container, false);

        next = (TextView) view.findViewById(R.id.txt_next);
        back = (TextView) view.findViewById(R.id.txt_back);

        next.setOnClickListener(this);
        back.setOnClickListener(this);

        reg_fnames = (EditText) view.findViewById(R.id.reg_fname);
        reg_mnames = (EditText) view.findViewById(R.id.reg_mname);
        reg_lnames = (EditText) view.findViewById(R.id.reg_lname);
        reg_emails = (EditText) view.findViewById(R.id.reg_email);
        // Inflate the layout for this fragment
        return view;
    }

    public void onClick(View v)
    {

        SharedPreferences sp1 = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        SharedPreferences.Editor Ed1 =sp1.edit();
        FragmentManager fragmentManager = getFragmentManager();
        //android.app.Fragment currentFragment = getActivity().getFragmentManager().findFragmentById(R.id.fragment_container);
        if(v == next) {

            Ed1.putString("reg_fname",reg_fnames.getText().toString());
            Ed1.putString("reg_mname",reg_mnames.getText().toString());
            Ed1.putString("reg_lname",reg_lnames.getText().toString());
            Ed1.putString("reg_email",reg_emails.getText().toString());
            Ed1.commit();


            if (reg_fnames.getText().length() == 0) {
                Toast.makeText(getActivity(), "First Name is required", Toast.LENGTH_LONG).show();
            } else if (reg_lnames.getText().length() == 0) {
                Toast.makeText(getActivity(), "Last Name is required", Toast.LENGTH_LONG).show();
            } else if (reg_emails.getText().length() == 0) {
                Toast.makeText(getActivity(), "Email is Required", Toast.LENGTH_LONG).show();
            } else {
                reg3 frag2 = new reg3();
                fragmentManager.beginTransaction().replace(R.id.reg_list, frag2).commit();
            }
        }
        else if(v == back) {
            reg1 frag2 = new reg1();
            fragmentManager.beginTransaction().replace(R.id.reg_list, frag2).commit();
        }
    }




}
