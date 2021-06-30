package com.example.blackbox;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GpsActivity extends AppCompatActivity {

    TextView txtResult;
    double longitude = 0;
    double latitude = 0;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        Button button1 = findViewById(R.id.button1);
        txtResult = findViewById(R.id.txtResult);
        Button button2 = findViewById(R.id.button2);
        layout = findViewById(R.id.gogleMapBtn);
        layout.setVisibility(View.INVISIBLE);
        // 레이아웃을 가려놓는 이유는 gps값을 가져오기 전에는 위도, 경도가 0이기 때문에 그 값으로 지도를 불러오는 버튼을 누르지 못하게 하기 위함

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // LocationManager를 사용하기 위해 선언

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23 &&ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(GpsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);
                    // ACCESS_FINE_LOCATION 권한이 있는지 확인해서 없으면 권한 요청 창을 띄움
                } else {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            //GPS_PROVIDER=GPS 센서로 위치정보 가져옴
                            //실제폰에서 테스트한 결과 GPS센서는 실내에서 인식률 낮음 그래서 아래의 NETWORK_PROVIDER가 필요
                            1000,
                            // 위치 업데이트 간의 최소 시간 간격 (밀리 초)
                            1,
                            // 위치 업데이트 간의 최소 거리 (미터)
                            gpsLocationListener);
                    // gpsLocationListener는 LocationListener를 가져와서 설정해서 사용

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, 1000);
                    // 1초 딜레이를 주기위해 사용.

                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if ( location == null ) {
                        Toast.makeText(getApplicationContext(),"GPS 신호가 약하기 때문에 기지국,WI-FI에서 위치정보를 가져옵니다.",Toast.LENGTH_SHORT).show();
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                //NETWORK_PROVIDER=휴대폰 기지국, 와이파이로 위치정보 가져옴
                                //실제폰에서 테스트한 결과 실내에서도 위치정보를 잘 가져옴 다만 안드로이드 에뮬에서는 이 신호를 못찾고 설정해놓은 gps값을 찾음
                                1000,
                                // 위치 업데이트 간의 최소 시간 간격 (밀리 초)
                                1,
                                // 위치 업데이트 간의 최소 거리 (미터)
                                gpsLocationListener);
                        // gpsLocationListener는 LocationListener를 가져와서 설정해서 사용
                    }

                    layout.setVisibility(View.VISIBLE);
                    // 위에서 가려놓았던 [구글 지도에서 현재 위치 보기] 버튼을 표시함
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            // [구글 지도에서 현재 위치 보기] 버튼을 눌렀을때 작동함 안눌러도 지도는 나오지만 다시한번 지도를 보려고 눌렀을때 작동
            @Override
            public void onClick(View v) {
                googleMap();
            }
        });

    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            // provider=현재 위치를 gps로 가져오는지 network(기지국, wi-fi)로 가져오는지 확인
            longitude = location.getLongitude();
            // longitude=위도
            latitude = location.getLatitude();
            // latitude=경도

            txtResult.setText("위치정보 : " + provider + "\n" +
                    "위도 : " + longitude + "\n" +
                    "경도 : " + latitude);
            // 레이아웃에서 만들어 놓았던 텍스트뷰에 위치정보, 위도, 경도를 표시함
        }
    };

    void googleMap()
    // 버튼1과 버튼2의 온클릭리스너들 2군데서 공동으로 쓰일 코드라 함수로 빼내었음
    {
        Toast.makeText(getApplicationContext(), "구글맵으로 이동합니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.kr/maps/@" + latitude + "," + longitude + ",14z"));
        startActivity(intent);
        // 경도와 위도를 가져와서 URI 주소로 만든후 ACTION_VIEW를 통해 구글 지도를 띄움
    }
}

