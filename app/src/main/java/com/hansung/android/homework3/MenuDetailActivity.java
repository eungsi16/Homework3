package com.hansung.android.homework3;


import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

//안드로이드 6주차 강의자료를 활용하였습니다.

public class MenuDetailActivity extends AppCompatActivity {
   // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    static String mImage;
    static String mName;
    static String mPrice;
    static String mEx;

   // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        Intent intent = getIntent();

        mImage = intent.getStringExtra("MenuIcon");
        mName = intent.getStringExtra("MenuName");
        mPrice = intent.getStringExtra("MenuPrice");
        mEx = intent.getStringExtra("MenuEx");

        MenuDetailFragment details = new MenuDetailFragment();
        details.getMenu(mImage, mName, mPrice, mEx);
        getSupportFragmentManager().beginTransaction().replace(R.id.details, details).commit();
    }
}

