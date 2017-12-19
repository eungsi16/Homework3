package com.hansung.android.homework3;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.hansung.android.homework3.R.layout.item;

public class RestaurantMapActivity extends AppCompatActivity implements OnMapReadyCallback {


    private FusedLocationProviderClient mFusedLocationClient;
    final private int REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION = 100;
    final private int REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES = 101;
    private Location mLastLocation;
    private GoogleMap mgoogleMap;
    private DBHelper mDBHelper;
    static String resname;
    Address address;
    EditText editText;
    String ed;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mDBHelper = new DBHelper(this);

        if (!checkLocationPermissions()) {
            requestLocationPermissions(REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION);
        } else {
            getLastLocation();
        }

        Button btn = (Button) findViewById(R.id.b1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText = (EditText) findViewById(R.id.edit);
                ed = editText.getText().toString();
                cursor = mDBHelper.getAllLocationsBySQL();
                String rName;
                double rLatitude;
                double rLongitude;
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                //맛집이름 검색시 해당 맛집 지도표시
                    rName = cursor.getString(1);
                    if (rName.equals(ed)) {
                                rLatitude = cursor.getDouble(2);
                                rLongitude = cursor.getDouble(3);

                                LatLng location = new LatLng(rLatitude, rLongitude);

                        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                        break;
                    }
                    else if(mgoogleMap != null)
                    {

                        getAddress();
                        LatLng location = new LatLng(address.getLatitude(), address.getLongitude());

                        mgoogleMap.addMarker(
                                new MarkerOptions().
                                        position(location)
                        );
                        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

                    }
                }

            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //메뉴아이템 클릭 시, 현재위치 불러옴

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nlocation:
                getLastLocation();
                return true;
            case R.id.nlocation1:
                getkm(1000);
                return true;
            case R.id.nlocation2:
                getkm(2000);
                return true;
            case R.id.nlocation3:
                getkm(3000);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getkm(int km){
        mgoogleMap.clear();

        getLastLocation();
        double distance;

        Location locationA = new Location("point A");

        locationA.setLatitude(klatitude);
        locationA.setLongitude(klongitude);

        cursor = mDBHelper.getAllLocationsBySQL();
        String rName;
        double rLatitude;
        double rLongitude;
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
                rName = cursor.getString(1);
                rLatitude = cursor.getDouble(2);
                rLongitude = cursor.getDouble(3);

            Location locationB = new Location("point B");
            locationB.setLatitude(rLatitude);
            locationB.setLongitude(rLongitude);
            distance = locationA.distanceTo(locationB);
            if(distance<km){
                LatLng location = new LatLng(rLatitude, rLongitude);
                mgoogleMap.addMarker(
                        new MarkerOptions().
                                position(location).
                                title(rName).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.map_mark_iloveimg_resized))
                );
                mgoogleMap.setOnMarkerClickListener(new MyMarkerClickListener());

            }

            }

//http://sunmo.blogspot.kr/2010/12/
   }

    private boolean checkLocationPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissions(int requestCode) {
        ActivityCompat.requestPermissions(
                RestaurantMapActivity.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},        // 요청할 권한 목록을 설정한 String 배열
                requestCode    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // getLastLocation();
                } else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT);
                }
                break;
            }
            case REQUEST_PERMISSIONS_FOR_LOCATION_UPDATES: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //startLocationUpdates();
                } else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT);
                }
            }
        }
    }

    double klatitude;
    double klongitude;
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        Task task = mFusedLocationClient.getLastLocation();
        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    mLastLocation = location;
                    LatLng Location = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    klatitude = mLastLocation.getLatitude();
                    klongitude=mLastLocation.getLongitude();

                    mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Location, 15));

                    //updateUI();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.no_location_detected), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;

        cursor = mDBHelper.getAllLocationsBySQL();
        //맛집이 등록되어 있으면 맛집 커서로 표시
        if (cursor.moveToFirst()) {

            double rLatitude;
            double rLongitude;
            String rName;

            while (cursor.moveToNext()) {

                rName = cursor.getString(1);
                rLatitude = cursor.getDouble(2);
                rLongitude = cursor.getDouble(3);

                LatLng location = new LatLng(rLatitude, rLongitude);

                mgoogleMap.addMarker(
                        new MarkerOptions().
                                position(location).
                                title(rName).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.map_mark_iloveimg_resized))
                );
            }
        }

        mgoogleMap.setOnMarkerClickListener(new MyMarkerClickListener());
    }

    private void getAddress() {
        TextView TextView = (TextView) findViewById(R.id.result);
        try {
            Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            List<Address> addresses = geocoder.getFromLocationName(ed, 1);
            if (addresses.size() > 0) {
                address = (Address) addresses.get(0);

                TextView.setText(String.format("[ %s , %s ]",
                        address.getLatitude(),
                        address.getLongitude()));
            }
        } catch (IOException e) {
            Log.e(getClass().toString(), "Failed in using Geocoder.", e);
            return;
        }
    }

    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // 제목셋팅
        alertDialogBuilder.setTitle("맛집 등록");

        // AlertDialog 셋팅
        alertDialogBuilder
                .setMessage("새로운 맛집을 등록하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                Intent intent = new Intent(getApplicationContext(), RestaurantRegistrationActivity.class);
                                intent.putExtra("address", ed);
                                intent.putExtra("latitude", address.getLatitude());
                                intent.putExtra("longitude", address.getLongitude());
                                startActivity(intent);
                            }
                        })
                .setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 다이얼로그를 취소한다
                                dialog.cancel();
                            }
                        });

        //다이얼로그 보여주기
        alertDialogBuilder.show();

        // 출처: http://mainia.tistory.com/2017 [녹두장군 - 상상을 현실로]
    }

    class MyMarkerClickListener implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            cursor = mDBHelper.getAllLocationsBySQL();
            //맛집마커 클릭시
            if (marker.getTitle() != null) {

                if (cursor.moveToFirst()) {

                    double rlatitude;
                    double rIongitude;

                    while (cursor.moveToNext()) {

                        rlatitude = cursor.getDouble(2);
                        rIongitude = cursor.getDouble(3);

                        if (marker.getPosition().latitude == rlatitude && marker.getPosition().longitude == rIongitude) {
                            String rname = marker.getTitle();
                            Intent intent = new Intent(getApplicationContext(), RestaurantDetailActivity.class);
                            intent.putExtra("resName", rname);
                            startActivity(intent);
                            break;
                        }
                    }
                }
            }
            //기본 마커 클릭시
            else {
                showDialog();
            }
            return false;
        }
    }


}
