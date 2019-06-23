package com.college.emergencysewa;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class activity_message extends Fragment {

    TextView user_name;
    TextView registered_time;
    TextView ticketid;
    TextView assigned_agent;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_activity_message, container, false);

        SharedPreferences sp=this.getActivity().getSharedPreferences("Request", MODE_PRIVATE);
        String username=sp.getString("user_name", null);
        String time_created=sp.getString("time_created", null);
        String agent_username=sp.getString("agent_username", null);
        String ticket_id=sp.getString("ticket_id", null);

        user_name = (TextView) view.findViewById(R.id.id_user);
        registered_time = (TextView) view.findViewById(R.id.id_regtime);
        ticketid = (TextView) view.findViewById(R.id.id_ticketid);
        assigned_agent = (TextView) view.findViewById(R.id.id_assignedagent);

        user_name.setText(username);
        registered_time.setText(time_created);
        ticketid.setText(ticket_id);
        assigned_agent.setText(agent_username);

        return view;
    }
}
