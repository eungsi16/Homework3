package com.hansung.android.homework3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MenuRegistrationActivity extends AppCompatActivity {

    private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_registration);

        mDbHelper = new DBHelper(this);

        ImageButton menuPictureBtn = (ImageButton) findViewById(R.id.menuPictureBtn);
        menuPictureBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });


        Button menuRegisterBtn = (Button) findViewById(R.id.menuRegisterBtn);
        menuRegisterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RestaurantDetailActivity.class);
                insertmenuRecord();
                setMenuInformation();
                startActivity(intent);
                //finish();
            }
        });
    }

    public void setMenuInformation() {

        Cursor cursor = mDbHelper.getAllMenusBySQL();

        String menuName;
        String menuPrice;
        String menuEx;
        String image;

        ArrayList<MyItem> data = new ArrayList<MyItem>();

        while (cursor.moveToNext()) {
            menuName = cursor.getString(1);
            menuPrice = cursor.getString(2);
            menuEx = cursor.getString(3);
            image = cursor.getString(4);

            data.add(new MyItem(image, menuName, menuPrice, menuEx));
        }
        RestaurantDetailActivity.adapter = new MyAdapter(this, R.layout.item, data);
    }

    private void insertmenuRecord() {
        EditText name = (EditText) findViewById(R.id.menuName);
        EditText price = (EditText) findViewById(R.id.menuPrice);
        EditText explanation = (EditText) findViewById(R.id.menuExplanation);

        mDbHelper.insertMenuByMethod(name.getText().toString(), price.getText().toString(), explanation.getText().toString(), mPhotoFileName);
    }

    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    //안드로이드 10주차 강의자료를 활용하였습니다.

    String mPhotoFileName;
    File mPhotoFile;

    final int REQUEST_IMAGE_CAPTURE = 100;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "IMG" + currentDateFormat() + ".jpg";
            mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

            if (mPhotoFile != null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                Uri imageUri = FileProvider.getUriForFile(this, "com.hansung.android.homework3", mPhotoFile);

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
                ImageButton imageButton = (ImageButton) findViewById(R.id.menuPictureBtn);
                imageButton.setImageURI(uri);
                imageButton.setScaleType(ImageButton.ScaleType.FIT_XY);
            } else
                Toast.makeText(getApplicationContext(), "mPhotoFile is null", Toast.LENGTH_SHORT).show();
        }
    }
}