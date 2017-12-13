package com.hansung.android.homework3;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

//안드로이드 10주차 강의자료를 활용하였습니다.

public class RestaurantRegistrationActivity extends AppCompatActivity {

    private DBHelper mDbHelper;
    EditText address;
    EditText name;
    Intent mapIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_registration);

        mDbHelper = new DBHelper(this);
        address = (EditText) findViewById(R.id.retaurantAddress);
        name = (EditText) findViewById(R.id.restaurantName);

        checkDangerousPermissions();

        mapIntent = getIntent();
        address.setText(mapIntent.getStringExtra("address"));
        ImageButton restaurantPictureBtn = (ImageButton) findViewById(R.id.restaurantPictureBtn);
        restaurantPictureBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });


        Button restaurantRegisterBtn = (Button) findViewById(R.id.restaurantRegisterBtn);
        restaurantRegisterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RestaurantDetailActivity.class);
                insertRecord();
                insertLocationRecord();
                startActivity(intent);
            }
        });
    }

    final int REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA = 1;

    //Permissons 권한 검사 및 요청
    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA);
        }
    }

    //식당의 이름, 주소, 전화번호를 데이터베이스에 추가
    private void insertRecord() {
        EditText phone = (EditText) findViewById(R.id.resraurantContact);

        mDbHelper.insertUserByMethod(name.getText().toString(), address.getText().toString(), phone.getText().toString(), mPhotoFileName);
    }

    //식당의 이름과 위치정보를 데이터베이스에 추가
    private void insertLocationRecord() {
        String longitude = mapIntent.getStringExtra("longitude");
        String latitude = mapIntent.getStringExtra("latitude");

        mDbHelper.insertLocationByMethod(name.getText().toString(), longitude, latitude);
    }


    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    String mPhotoFileName;
    File mPhotoFile;
    Uri imageUri;

    final int REQUEST_IMAGE_CAPTURE = 100;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "IMG" + currentDateFormat() + ".jpg";
            mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

            if (mPhotoFile != null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                imageUri = FileProvider.getUriForFile(this, "com.hansung.android.homework3", mPhotoFile);


                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else
                Toast.makeText(getApplicationContext(), "file null", Toast.LENGTH_SHORT).show();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (mPhotoFileName != null) {
                mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);
                Uri uri = Uri.fromFile(mPhotoFile);
                ImageButton imageButton = (ImageButton) findViewById(R.id.restaurantPictureBtn);
                imageButton.setImageURI(uri);
                imageButton.setScaleType(ImageButton.ScaleType.FIT_XY);
            } else
                Toast.makeText(getApplicationContext(), "mPhotoFile is null", Toast.LENGTH_SHORT).show();
        }
    }
}
