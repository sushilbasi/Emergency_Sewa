package com.college.emergencysewa;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class activity_home extends Fragment implements OnClickListener{

    private Button btn_ambulance;
    private Button btn_firebrigade;
    private Button btn_police;
    private  Button btn_submit;
    Animation fade_in_up;
    public FrameLayout frameLayout;


    public activity_home() {
        // Required empty public constructor
    }

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    final int[] response_code = {0};
    String response = "";

    public class r_postRequest {

        OkHttpClient client = new OkHttpClient();

        void post(String url, String json) {
            RequestBody body = RequestBody.create(json, JSON);
            final Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();


            new Thread(new Runnable() {
                @Override
                public void run() {

                    Response responses = null;
                    try {
                        responses = client.newCall(request).execute();
                        response_code[0] = responses.code();
                        response = new String(responses.body().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

        String userJSON(String username, String type, String current_time) {
            return "{\"user_name\":\"" + username + "\",\"type\":\"" + type + "\",\"time_created\":\"" + current_time + "\"}";
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        View view = inflater.inflate(R.layout.fragment_activity_home, container, false);

        btn_ambulance = (Button) view.findViewById(R.id.btn_quick_ambulance);
        btn_ambulance.setOnClickListener(this);

        btn_firebrigade = (Button) view.findViewById(R.id.btn_quick_firebrigade);
        btn_firebrigade.setOnClickListener(this);

        btn_police = (Button) view.findViewById(R.id.btn_quick_police);
        btn_police.setOnClickListener(this);

        btn_submit = (Button) view.findViewById(R.id.btn_support);
        frameLayout = (FrameLayout) view.findViewById(R.id.frame_support);
        fade_in_up = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in_up);
        btn_submit.setOnClickListener(this);
        btn_submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                support frag = new support();
                fragmentTransaction.add(R.id.frame_support, frag);
                frameLayout.startAnimation(fade_in_up);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//                frameLayout.setAnimation(transition_up);
            }
        });

        return view;
    }

    public void onClick(View v) {

        SharedPreferences sp = getActivity().getSharedPreferences("Login", MODE_PRIVATE);
        final String username = sp.getString("username", null);

        SharedPreferences sp1 = getActivity().getSharedPreferences("Request", MODE_PRIVATE);
        final SharedPreferences.Editor Ed;
        Ed = sp1.edit();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(c.getTime());
        String type = null;

        if(v.getId() == R.id.btn_quick_ambulance)
        {
            type = "Medical";
        }else if (v.getId() == R.id.btn_quick_firebrigade)
        {
            type = "Fire";
        }else if (v.getId() == R.id.btn_quick_police)
        {
            type = "Police";
        }

        r_postRequest example = new r_postRequest();
        String json = example.userJSON(username, type, currentTime);
        System.out.println(json);
        example.post("http://wagle04.pythonanywhere.com/assignagent", json);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (response_code[0] == 200) {


            Toast.makeText(getActivity(), "Agent Assignment Successful", Toast.LENGTH_LONG).show();
            //converting string to json
            JSONObject jsonresponse = null;
            try {
                jsonresponse = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //assigning details of user to sharedpreferance variables
            try {
                Ed.putString("user_name", String.valueOf(jsonresponse.get("user_name")));
                Ed.putString("time_created", String.valueOf(jsonresponse.get("time_created")));
                Ed.putString("agent_username", String.valueOf(jsonresponse.get("agent_username")));
                Ed.putString("agent_latitude", String.valueOf(jsonresponse.get("agent_latitude")));
                Ed.putString("agent_longitude", String.valueOf(jsonresponse.get("agent_longitude")));
                Ed.putString("ticket_id", String.valueOf(jsonresponse.get("ticket_id")));
                Ed.putBoolean("requested", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Ed.commit();

            activity_message frag = new activity_message();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, frag).commit();


        } else {
            Toast.makeText(getActivity(), "Error while assigning agent", Toast.LENGTH_LONG).show();
        }

    }


}






