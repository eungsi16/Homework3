package com.hansung.android.homework3;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        DBHelper dbHelper = new DBHelper(this);
        TextView textView = (TextView) findViewById(R.id.testview);

        Cursor cursor = dbHelper.getAllLocationsBySQL();

        if (cursor.moveToFirst()) {

            double rLatitude;
            double rLongitude;
            String rName;
            StringBuffer buffer = new StringBuffer();

            while (cursor.moveToNext()) {

                rName = cursor.getString(1);
//                rLatitude = Double.parseDouble(cursor.getString(2));
//                rLongitude = Double.parseDouble(cursor.getString(3));
                rLatitude = cursor.getDouble(2);
                rLongitude = cursor.getDouble(3);


                buffer.append(rName +"\t");
                buffer.append(rLatitude +"\t");
                buffer.append(rLongitude +"\n");
            }

            textView.setText(buffer);
        }
    }
}
