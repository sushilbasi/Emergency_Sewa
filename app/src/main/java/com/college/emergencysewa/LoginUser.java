package com.college.emergencysewa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class LoginUser extends AppCompatActivity implements OnClickListener {

    SharedPreferences sp;
    SharedPreferences.Editor Ed;


    private TextView txt_termsandconditons;
    private TextView txt_forgotpassword;
    private TextView txt_registration;
    private Button btn_signin;

    private EditText Username;
    private EditText Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp=getSharedPreferences("Login", MODE_PRIVATE);
        Ed=sp.edit();
        Ed.putBoolean("logged_in",false);
        Ed.commit();

        setContentView(R.layout.activity_login_user);
        txt_termsandconditons = (TextView) findViewById(R.id.txt_termsandconditions);
        txt_termsandconditons.setOnClickListener(this);

        txt_forgotpassword = (TextView) findViewById(R.id.txt_forgotpassword);
        txt_forgotpassword.setOnClickListener(this);

        txt_registration = (TextView) findViewById(R.id.txt_signup);
        txt_registration.setOnClickListener(this);

        btn_signin = (Button) findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(this);

        Username = (EditText) findViewById(R.id.txt_username);
        Password = (EditText) findViewById(R.id.txt_password);




    }
    int backButtonCount=0;
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    final int[] reponse_code = {0};
    String response = "";
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
                        Response responses2 = null;
                        try {
                            responses = client.newCall(request).execute();
                            reponse_code[0] = responses.code();
                            responses2 = client.newCall(request).execute();
                            response = new String(responses2.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }).start();

        }

        String userJSON(String username, String password) {
            return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_termsandconditions:

                Intent int1 = new Intent(LoginUser.this, activity_termsandconditions.class);
                this.startActivity(int1);
                break;


            case R.id.txt_forgotpassword:
                Intent int2 = new Intent(LoginUser.this, activity_main.class);
                this.startActivity(int2);
                break;

            case R.id.txt_signup:
                Intent int3 = new Intent(LoginUser.this, activity_registration.class);
                this.startActivity(int3);
                break;

            //for login or sign in
            case R.id.btn_signin:


                String entered_username = Username.getText().toString();
                String entered_password = Password.getText().toString();

                postRequest example = new postRequest();
                String json = example.userJSON(entered_username, entered_password);

                example.post("http://wagle04.pythonanywhere.com/userlogin", json);

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (reponse_code[0]==200 ){


                    //converting string to json
                    JSONObject jsonresponse = null;
                    try {
                        jsonresponse = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //getting string user from json file
                    String user = null;
                    try { user = new String(String.valueOf(jsonresponse.get("user")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //converting user string to json
                    try {
                        jsonresponse = new JSONObject(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //assigning details of user to sharedpreferance variables
                    try {
                        Ed.putString("username",String.valueOf(jsonresponse.get("username")) );
                        Ed.putString("password",String.valueOf(jsonresponse.get("password")) );
                        Ed.putString("fname",String.valueOf(jsonresponse.get("fname")) );
                        Ed.putString("mname",String.valueOf(jsonresponse.get("mname")) );
                        Ed.putString("lname",String.valueOf(jsonresponse.get("lname")) );
                        Ed.putString("phone_number",String.valueOf(jsonresponse.get("phone_number")) );
                        Ed.putString("date_of_birth",String.valueOf(jsonresponse.get("date_of_birth")) );
                        Ed.putString("email",String.valueOf(jsonresponse.get("email")) );
                        Ed.putString("assist",String.valueOf(jsonresponse.get("assist")) );
                        Ed.putString("personal_id",String.valueOf(jsonresponse.get("personal_id")) );
                        Ed.putString("profile_picture",String.valueOf(jsonresponse.get("profile_picture")) );
                        Ed.putBoolean("logged_in",true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Ed.commit();

                    //printing
                    SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
                    Boolean unm=sp1.getBoolean("logged_in", false);
                    System.out.println(unm);


                    Toast.makeText(LoginUser.this,"Success!!!",Toast.LENGTH_LONG).show();
                    Intent int4 = new Intent(LoginUser.this, activity_container.class);
                    startActivity(int4);
                }else{
                    Username.getText().clear();
                    Password.getText().clear();
                    Username.requestFocus();
                    Toast.makeText(LoginUser.this,"Error!!!!",Toast.LENGTH_LONG).show();

                }

                break;

        }
    }
}