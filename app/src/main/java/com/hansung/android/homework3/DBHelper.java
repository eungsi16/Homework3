package com.hansung.android.homework3;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


//안드로이드 9주차 강의자료를 활용하였습니다.

public class DBHelper extends SQLiteOpenHelper {
    final static String TAG = "SQLiteDBTest";

    public DBHelper(Context context) {
        super(context, RestaurantInformation.DB_NAME, null, RestaurantInformation.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, getClass().getName() + ".onCreate()");
        db.execSQL(RestaurantInformation.Restaurants.CREATE_TABLE);
        db.execSQL(RestaurantInformation.Menu.CREATE_TABLE);
        db.execSQL(RestaurantInformation.Locations.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG, getClass().getName() + ".onUpgrade()");
        db.execSQL(RestaurantInformation.Restaurants.DELETE_TABLE);
        db.execSQL(RestaurantInformation.Menu.DELETE_TABLE);
        db.execSQL(RestaurantInformation.Locations.DELETE_TABLE);
        onCreate(db);
    }

    public long insertUserByMethod(String name, String address, String phone, String image) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RestaurantInformation.Restaurants.KEY_NAME, name);
        values.put(RestaurantInformation.Restaurants.KEY_ADDRESS, address);
        values.put(RestaurantInformation.Restaurants.KEY_PHONE, phone);
        values.put(RestaurantInformation.Restaurants.KEY_IMAGE, image);

        return db.insert(RestaurantInformation.Restaurants.TABLE_NAME, null, values);
    }

    public long insertMenuByMethod(String name, String price, String explanation, String image, String restaurant) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RestaurantInformation.Menu.KEY_NAME, name);
        values.put(RestaurantInformation.Menu.KEY_Price, price);
        values.put(RestaurantInformation.Menu.KEY_Explanation, explanation);
        values.put(RestaurantInformation.Restaurants.KEY_IMAGE, image);
        values.put(RestaurantInformation.Menu.KEY_RESTAURANTNAME, restaurant);

        return db.insert(RestaurantInformation.Menu.TABLE_NAME, null, values);
    }

    public long insertLocationByMethod(String name, double latitude, double longitude) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RestaurantInformation.Locations.KEY_NAME, name);
        values.put(RestaurantInformation.Locations.KEY_LATITUDE, latitude);
        values.put(RestaurantInformation.Locations.KEY_LONGITUDE, longitude);

        return db.insert(RestaurantInformation.Locations.TABLE_NAME, null, values);
    }

    public Cursor getAllUsersBySQL() {
        String sql = "Select * FROM " + RestaurantInformation.Restaurants.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql, null);
    }

    public Cursor getAllMenusBySQL() {
        String sql = "Select * FROM " + RestaurantInformation.Menu.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql, null);
    }

    public Cursor getAllLocationsBySQL() {
        String sql = "Select * FROM " + RestaurantInformation.Locations.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql, null);
    }
}
