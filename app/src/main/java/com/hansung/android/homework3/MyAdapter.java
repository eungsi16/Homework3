package com.hansung.android.homework3;


import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

//안드로이드 4주차 강의자료를 활용하였습니다.

class MyItem {
    String menuIcon;
    String menuName;
    String menuPrice;
    String menuGrade;

    MyItem(String amenuIcon, String aName, String aPrice, String aGrade) {
        menuName = aName;
        menuPrice = aPrice;
        menuGrade = aGrade;
        menuIcon = amenuIcon;
    }
}

public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private int mResource;
    private ArrayList<MyItem> mItems = new ArrayList<MyItem>();

    public MyAdapter(Context context, int resource, ArrayList<MyItem> items) {
        mContext = context;
        mItems = items;
        mResource = resource;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    String ex;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }
        // Set Icon
        ImageView icon = (ImageView) convertView.findViewById(R.id.iconItem);
        if (mItems.get(position).menuIcon != null) {
            File mPhotoFile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), mItems.get(position).menuIcon);
            Uri uri = Uri.fromFile(mPhotoFile);
            Uri image_uri = Uri.parse(uri.toString());
            icon.setImageURI(image_uri);
        }
        // Set Text 01
        TextView name = convertView.findViewById(R.id.menuname);
        name.setText(mItems.get(position).menuName);

        // Set Text 02
        TextView price = convertView.findViewById(R.id.menuprice);
        price.setText(mItems.get(position).menuPrice);

        ex = mItems.get(position).menuGrade;

        return convertView;
    }
}
