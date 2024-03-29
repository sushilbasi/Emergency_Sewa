package com.college.emergencysewa;


import android.content.Context;
import android.content.Intent;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class reg4 extends Fragment implements OnClickListener{

    private TextView next;
    private TextView back;
    private  ImageView imageView;
    public static final int PICK_IMAGE = 20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_reg4, container, false);
         imageView = (ImageView) view.findViewById(R.id.img_view);
        imageView.setOnClickListener(this);


        next= (TextView) view.findViewById(R.id.txt_next);
        back = (TextView) view.findViewById(R.id.txt_back);
        next.setOnClickListener(this);
        back.setOnClickListener(this);


        return view;
    }
    public void onClick(View v)
    {
        FragmentManager fragmentManager = getFragmentManager();

        switch (v.getId()){
            case R.id.txt_next:
                reg5 frag = new reg5();
                fragmentManager.beginTransaction().replace(R.id.reg_list,frag).commit();
                break;
            case R.id.txt_back:
                reg3 frag1 = new reg3();
                fragmentManager.beginTransaction().replace(R.id.reg_list,frag1).commit();
                break;
            case R.id.img_view:
                Intent intent = new Intent(Intent.ACTION_PICK);
                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String pictureDirectoryPath = pictureDirectory.getPath();
                Uri data = Uri.parse(pictureDirectoryPath);
                intent.setDataAndType(data,"image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, ""),
                        PICK_IMAGE);
                break;


            }

       if(v == next)
       {

           reg5 frag = new reg5();
           fragmentManager.beginTransaction().replace(R.id.reg_list,frag).commit();
       }
       else if (v == back)
       {
           reg3 frag1 = new reg3();
           fragmentManager.beginTransaction().replace(R.id.reg_list,frag1).commit();
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
                    Ed1.putString("reg_profile_picture",personal_id);
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
