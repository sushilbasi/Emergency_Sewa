package com.college.emergencysewa;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class reg5 extends Fragment implements OnClickListener {

    public static final int PICK_IMAGE = 20;
    private ImageView imageView;
    private TextView finish;
    private TextView back;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg5, container, false);

        imageView = (ImageView) view.findViewById(R.id.reg_image);
        finish =(TextView) view.findViewById(R.id.txt_finish);
        back =(TextView) view.findViewById(R.id.txt_back);
        imageView.setOnClickListener(this);
        finish.setOnClickListener(this);
        back.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    final int[] reponse_code = {0};
    public class reg_postRequest {

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
    public void onClick(View v)
    {
        FragmentManager fragmentManager = getFragmentManager();
        SharedPreferences sp1 = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        SharedPreferences.Editor Ed1 =sp1.edit();

        switch (v.getId()){

            case R.id.txt_back:
                reg4 frag1 = new reg4();
                fragmentManager.beginTransaction().replace(R.id.reg_list,frag1).commit();
                break;

            case R.id.reg_image:
                Intent intent = new Intent(Intent.ACTION_PICK);
                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);;
                String pictureDirectoryPath = pictureDirectory.getPath();
                Uri data = Uri.parse(pictureDirectoryPath);
                intent.setDataAndType(data,"image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, ""),
                        PICK_IMAGE);

                break;

            case R.id.txt_finish:


                String e_username=sp1.getString("reg_username", null);
                String e_password=sp1.getString("reg_password", null);
                String e_phone_number=sp1.getString("reg_phone_number", null);
                String e_fname=sp1.getString("reg_fname", null);
                String e_mname=sp1.getString("reg_mname", null);
                String e_lname=sp1.getString("reg_lname", null);
                String e_email=sp1.getString("reg_email", null);
                String e_dob=sp1.getString("reg_dob", null);
                String e_assist=sp1.getString("reg_assist", null);
                String e_profile_picture=sp1.getString("reg_profile_picture", null);
                String e_personal_id=sp1.getString("reg_personal_id", null);


                reg_postRequest example = new reg_postRequest();


                String json = example.userJSON(e_username,e_password,e_fname,e_mname,e_lname,e_phone_number,e_dob,e_email,e_assist,e_profile_picture,e_personal_id);
                System.out.println(json);
                example.post("http://wagle04.pythonanywhere.com/userregister", json);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (reponse_code[0]==201 ){
                    Toast.makeText(getActivity(),"Registration Successful! Login Now!!",Toast.LENGTH_LONG).show();
                    Intent int4 = new Intent(getActivity(), LoginUser.class);
                    startActivity(int4);
                }else{
                    Toast.makeText(getActivity(),"Registration Unsuccessful! Try Again!!",Toast.LENGTH_LONG).show();
                    Intent int2 = new Intent(getActivity(), activity_registration.class);
                    this.startActivity(int2);
                }

                break;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences sp1 = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        SharedPreferences.Editor Ed1 =sp1.edit();
        if(resultCode == RESULT_OK)
        {
            if(requestCode == PICK_IMAGE)
            {
                Uri imageUri = data.getData();

                InputStream inputStream;

                try{
                    inputStream = getActivity().getContentResolver().openInputStream(imageUri);

                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    boolean x=image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    System.out.println(x);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    String personal_id = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    Ed1.putString("reg_personal_id",personal_id);
                    Ed1.commit();
                    imageView.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Unable to Open image",Toast.LENGTH_LONG).show();
//                            Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }
    }





}
