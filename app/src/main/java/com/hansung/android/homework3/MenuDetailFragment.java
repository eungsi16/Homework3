package com.hansung.android.homework3;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

//안드로이드 6주차 강의자료를 활용하였습니다.

public class MenuDetailFragment extends Fragment {

    static String Image;
    static String Name;
    static String Price;
    static String Ex;

    public void getMenu(String image, String name, String price, String ex) {
        Image = image;
        Name = name;
        Price = price;
        Ex = ex;
    }

    public MenuDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = (View) inflater.inflate(R.layout.activity_menu_detail, container, false);

        ImageView img = (ImageView) rootView.findViewById(R.id.micon);
        if (Image != null) {
            File mPhotoFile = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), Image);
            Uri uri = Uri.fromFile(mPhotoFile);
            img.setImageURI(uri);
        }

        TextView name = (TextView) rootView.findViewById(R.id.mname);
        name.setText(Name);

        TextView price = (TextView) rootView.findViewById(R.id.mprice);
        price.setText(Price + " 원");

        TextView info = (TextView) rootView.findViewById(R.id.mgrade);
        info.setText("설명: " + Ex);

        return rootView;
    }
}