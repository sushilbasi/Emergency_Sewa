package com.college.emergencysewa;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class reg3 extends Fragment implements OnClickListener{

    private TextView next;
    private TextView back;

    private EditText reg_dob;
    private CheckBox reg_yes;
    private CheckBox reg_no;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reg3, container, false);

        next = (TextView) view.findViewById(R.id.txt_next);
        back = (TextView) view.findViewById(R.id.txt_back);

        reg_dob = (EditText) view.findViewById(R.id.reg_dob);
        reg_yes = (CheckBox) view.findViewById(R.id.reg_yes);
        reg_no = (CheckBox) view.findViewById(R.id.reg_no);

        next.setOnClickListener(this);
        back.setOnClickListener(this);

        return view;
    }

    public void onClick(View v)
    {
        FragmentManager fragmentManager = getFragmentManager();
        SharedPreferences sp1 = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        SharedPreferences.Editor Ed1 =sp1.edit();

        if(v == next)
        {
            Ed1.putString("reg_dob",reg_dob.getText().toString());
            if(reg_yes.isChecked()){
                Ed1.putString("reg_assist","Medical");
            }else {
                Ed1.putString("reg_assist", null);
            }
                Ed1.commit();

                if (reg_dob.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Date of Birth is required", Toast.LENGTH_LONG).show();
                } else if (reg_yes.isChecked() && reg_no.isChecked()) {
                    Toast.makeText(getActivity(), "Both yes and no can't be checked", Toast.LENGTH_LONG).show();
                } else if (!(reg_yes.isChecked()) && !(reg_no.isChecked())) {
                    Toast.makeText(getActivity(), "Check either 'yes' or 'no' ", Toast.LENGTH_LONG).show();
                } else {
                    reg5 frag = new reg5();
                    fragmentManager.beginTransaction().replace(R.id.reg_list, frag).commit();
                }

        } else if (v == back)
        {
            reg2 frag2 = new reg2();
            fragmentManager.beginTransaction().replace(R.id.reg_list,frag2).commit();
        }


    }

}
