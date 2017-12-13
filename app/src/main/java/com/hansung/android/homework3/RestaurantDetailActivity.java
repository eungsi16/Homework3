package com.hansung.android.homework3;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RestaurantDetailActivity extends AppCompatActivity implements RestaurantDetailFragment.OnTitleSelectedListener {
    static MyAdapter adapter;
    private DBHelper mDBHelper;
    static String mImage;
    static String mName;
    static String mPrice;
    static String mEx;

    //기기가 가로일 때 프래그먼트 설정과 세로일 때 프레그먼트 설정
    public void onTitleSelected(int i) {
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            MenuDetailFragment detailsFragment = new MenuDetailFragment();
            detailsFragment.getMenu(mImage, mName, mPrice, mEx);
            getSupportFragmentManager().beginTransaction().replace(R.id.details, detailsFragment).commit();
        } else {
            Intent intent = new Intent(getApplicationContext(), MenuDetailActivity.class);

            intent.putExtra("MenuIcon", mImage);
            intent.putExtra("MenuName", mName);
            intent.putExtra("MenuPrice", mPrice);
            intent.putExtra("MenuEx", mEx);

            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DBHelper(this);

        getRestaruntInformation();

        //데이터 베이스에서 식당의 번호를 불러와 설정
        //이미지 클릭 시, 다이얼 화면으로 이동
        ImageButton btn = (ImageButton) findViewById(R.id.iconItem2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = mDBHelper.getAllUsersBySQL();
                String num = "";
                while (cursor.moveToNext()) {
                    num = cursor.getString(3);
                }
                Intent implicit_intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
                startActivity(implicit_intent);
            }
        });

        //어댑터 연결
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setDivider(new ColorDrawable(Color.GRAY));
        listView.setDividerHeight(3);

        //리스트 뷰 클릭 시, MyItem 내용 저장
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View vClicked,
                                    int position, long id) {
                mImage = ((MyItem) adapter.getItem(position)).menuIcon;
                mName = ((MyItem) adapter.getItem(position)).menuName;
                mPrice = ((MyItem) adapter.getItem(position)).menuPrice;
                mEx = ((MyItem) adapter.getItem(position)).menuGrade;
                onTitleSelected(position);
            }
        });
    }

    //안드로이드 5주차 강의자료를 활용하였습니다.
    //옵션 메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //메뉴아이템 클릭 시, MenuRegistrationActivity 불려짐
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.plus:
                startActivity(new Intent(this, MenuRegistrationActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //레스토랑의 정보를 데이터베이스에서 불러와 설정
    private void getRestaruntInformation() {
        TextView name = (TextView) findViewById(R.id.name);
        TextView address = (TextView) findViewById(R.id.address);
        TextView phone = (TextView) findViewById(R.id.phone);
        ImageView imageView = (ImageView) findViewById(R.id.picture);

        Cursor cursor = mDBHelper.getAllUsersBySQL();

        String rName;
        String rAddress;
        String rPhone;
        String rImage;

        while (cursor.moveToNext()) {
            rName = cursor.getString(1);
            rAddress = cursor.getString(2);
            rPhone = cursor.getString(3);
            rImage = cursor.getString(4);

            name.setText(rName);
            address.setText(rAddress);
            phone.setText(rPhone);
            if (rImage != null) {
                File mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), rImage);
                Uri uri = Uri.fromFile(mPhotoFile);
                imageView.setImageURI(uri);
            }
        }
    }
}



