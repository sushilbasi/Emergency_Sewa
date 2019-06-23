package com.college.emergencysewa;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class edit_profile extends AppCompatActivity implements OnClickListener {

    TextView username;
    TextView firstname;
    TextView middlename;
    TextView lastname;
    TextView date_of_birth;
    TextView email;
    TextView phone_number;
    TextView assist;
    TextView password;
    TextView cpassword;
    Button update;
    ImageView profile_picture;



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
        password = (TextView) findViewById(R.id.password);
        cpassword = (TextView) findViewById(R.id.confirm_password);
        profile_picture = (ImageView) findViewById(R.id.img_round);

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
        String upassword=sp1.getString("password", null);
        String profile_pictures=sp1.getString("profile_picture", null);

        byte[] decodedString = Base64.decode(profile_pictures, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        profile_picture.setImageBitmap(decodedByte);

        username.setTextColor(Color.BLUE);
        username.setText(uname);
        firstname.setText(fname);
        middlename.setText(mname);
        lastname.setText(lname);
        date_of_birth.setText(dob);
        email.setText(uemail);
        phone_number.setText(phno);
        assist.setText(uassist);
        password.setText(upassword);
        cpassword.setText(upassword);


    }

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    final int[] response_code = {0};
    public class u_postRequest {

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
                        response_code[0] = responses.code();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

        String userJSON(String username, String password, String fname, String mname, String lname, String phone_number,String dob,String email, String assist,String profile_picture, String personal_id) {
            return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\",\"fname\":\""+fname+"\",\"mname\":" +
                    "\""+mname+"\",\"lname\":\""+lname+"\",\"phone_number\":\""+phone_number+"\",\"date_of_birth\":\""+dob+"\"," +
                    "\"email\":\""+email+"\",\"assist\":\""+assist+"\",\"profile_picture\":\""+profile_picture+"\",\"personal_id\":\""+personal_id+"\"}";
        }
    }

    @Override
    public void onClick(View v) {



        SharedPreferences sp1 = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor Ed1 =sp1.edit();



        String u_username=sp1.getString("username", null);
        String u_password=password.getText().toString();
        String u_fname=firstname.getText().toString();
        String u_mname=middlename.getText().toString();
        String u_lname=lastname.getText().toString();
        String u_phone_number=phone_number.getText().toString();
        String u_date_of_birth=date_of_birth.getText().toString();
        String u_email=email.getText().toString();
        String u_assist=assist.getText().toString();
        String u_profile_picture=sp1.getString("profile_picture", null);
        String u_personal_id=sp1.getString("personal_id", null);

        u_postRequest example = new u_postRequest();

        String json = example.userJSON(u_username,u_password,u_fname,u_mname,u_lname,u_phone_number,u_date_of_birth,u_email,u_assist,u_profile_picture,u_personal_id);

        example.post("http://wagle04.pythonanywhere.com/userupdate", json);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(response_code[0]);
        if (response_code[0]==201 ){

                Ed1.putString("username",u_username);
                Ed1.putString("password",u_password );
                Ed1.putString("fname",u_fname );
                Ed1.putString("mname",u_mname);
                Ed1.putString("lname",u_lname );
                Ed1.putString("phone_number",u_phone_number );
                Ed1.putString("date_of_birth",u_date_of_birth );
                Ed1.putString("email",u_email );
                Ed1.putString("assist",u_assist );
                Ed1.putString("profile_picture", u_profile_picture);
                Ed1.putString("personal_id", u_personal_id);

            Ed1.commit();

            Toast.makeText(this,"Updated Successful",Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(edit_profile.this,activity_container.class));
        }else{
            Toast.makeText(this,"Error while updating profile",Toast.LENGTH_LONG).show();
            finish();
        }


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
