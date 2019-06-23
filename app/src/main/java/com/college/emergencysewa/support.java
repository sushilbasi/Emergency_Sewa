package com.college.emergencysewa;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;


public class support extends Fragment implements View.OnClickListener {

    public Button  btn_close;
    public Button  btn_call_ambulance;
    public Button  btn_call_firebrigade;
    public Button  btn_call_police;
    public android.app.Fragment frag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        btn_close = (Button) view.findViewById(R.id.btn_close);
        btn_call_ambulance = (Button) view.findViewById(R.id.btn_call_ambulance);
        btn_call_firebrigade = (Button) view.findViewById(R.id.btn_call_firebrigade);
        btn_call_police = (Button) view.findViewById(R.id.btn_call_police);


        btn_call_ambulance.setOnClickListener(this);
        btn_call_firebrigade.setOnClickListener(this);
        btn_call_police.setOnClickListener(this);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            switch (v.getId()) {
                case R.id.btn_call_ambulance:
                    //Creating intents for making a call
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:1234567"));
                    getActivity().startActivity(callIntent);
                    break;

                case R.id.btn_call_firebrigade:
                    //Creating intents for making a call
                    Intent callIntent2 = new Intent(Intent.ACTION_CALL);
                    callIntent2.setData(Uri.parse("tel:1234567"));
                    getActivity().startActivity(callIntent2);
                    break;

                case R.id.btn_call_police:
                    //Creating intents for making a call
                    Intent callIntent3 = new Intent(Intent.ACTION_CALL);
                    callIntent3.setData(Uri.parse("tel:1234567"));
                    getActivity().startActivity(callIntent3);

                    break;

            }
        }else{
                Toast.makeText(getActivity(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }
}
