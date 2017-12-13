package com.hansung.android.homework3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//안드로이드 6주차 강의자료를 활용하였습니다.

public class RestaurantDetailFragment extends Fragment {

    public interface OnTitleSelectedListener {
        public void onTitleSelected(int i);
    }

    public RestaurantDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = (View) inflater.inflate(R.layout.activity_restaurant_detail, container, false);

        return rootView;

    }
}