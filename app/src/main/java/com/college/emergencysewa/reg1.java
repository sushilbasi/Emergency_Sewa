package com.college.emergencysewa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class reg1 extends Fragment implements OnClickListener {




    private TextView txt_next;
    private EditText reg_usernames;
    private EditText reg_passwords;
    private EditText reg_confirm_passwords;
    private EditText reg_phone_numbers;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_reg1,container,false);
        txt_next = (TextView) view.findViewById(R.id.txt_next1);
        txt_next.setOnClickListener(this);

        reg_usernames = (EditText) view.findViewById(R.id.reg_username);
        reg_passwords = (EditText) view.findViewById(R.id.reg_password);
        reg_confirm_passwords = (EditText) view.findViewById(R.id.reg_confirm_password);
        reg_phone_numbers = (EditText) view.findViewById(R.id.reg_phone_number);


        // Inflate the layout for this fragment
        return view;
    }



    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    final int[] reponse_code = {0};
    public class postRequest {

        OkHttpClient client = new OkHttpClient();

        void post(String url, String json) {
            RequestBody body = RequestBody.create(json, JSON);
            final Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            new Thread(new Runnable(){
                @Override
                public void run() {

                    Response responses = null;
                    try {
                        responses = client.newCall(request).execute();
                        reponse_code[0] = responses.code();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        String userJSON(String username) {
            return "{\"username\":\"" + username+"\"}";
        }

    }
    public void  onClick(View v)
    {

        SharedPreferences sp1 = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        SharedPreferences.Editor Ed1 =sp1.edit();

        Ed1.putString("reg_username",reg_usernames.getText().toString());
        Ed1.putString("reg_password",reg_passwords.getText().toString());
        Ed1.putString("reg_phone_number",reg_phone_numbers.getText().toString());
        Ed1.commit();

        String entered_username = reg_usernames.getText().toString();

        reg1.postRequest example = new reg1.postRequest();
        String json = example.userJSON(entered_username);

        example.post("http://wagle04.pythonanywhere.com/userexist", json);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (reg_usernames.getText().length() < 4) {
            Toast.makeText(getActivity(),"Username's length must be more than 4",Toast.LENGTH_LONG).show();
        }

        else if (reg_passwords.getText().length() < 8) {
            Toast.makeText(getActivity(),"Password must be length of 8",Toast.LENGTH_LONG).show();
        }

        else if (!(reg_passwords.getText().toString().equals(reg_confirm_passwords.getText().toString()))) {
            Toast.makeText(getActivity(),"Passwords must be same",Toast.LENGTH_LONG).show();
        }
        else if (reg_phone_numbers.getText().length() != 10) {
            Toast.makeText(getActivity(),"Phone number must of length 10",Toast.LENGTH_LONG).show();
        }
        else if (reponse_code[0]==401 ){
            Toast.makeText(getActivity(),"Username already exists",Toast.LENGTH_LONG).show();
        }
        else if(reponse_code[0]==200 ){

            reg2 frag = new reg2();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.reg_list,frag).commit();
        }
        else {
            Toast.makeText(getActivity(),"Error while checking for username availibilty",Toast.LENGTH_LONG).show();
        }


    }


}
