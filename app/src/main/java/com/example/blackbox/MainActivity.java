package com.example.blackbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyAllPermissions(this);
        // sd카드 권한 확인을 위한 함수 호출
    }

    public static void verifyAllPermissions(Activity activity) {
        // 앱을 실행하는데 필요한 권한 확인을 위한 함수
        int permission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        // sd카드 읽기 권한이 있는지 체크해서 permission에 담음
        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET},1
            );
        }
        // 만약 권한이 승인되지 않은 상태라면 권한요청 창을 액티비티에 뛰움
    }

    public void mainMenu1(View view) {
        Intent intent = new Intent(getApplicationContext(),CameraActivity.class);
        startActivity(intent);
    }

    public void mainMenu2(View view) {
        Intent intent = new Intent(getApplicationContext(),RecordFilesListActivity.class);
        startActivity(intent);
    }

    public void mainMenu3(View view) {
        Intent intent = new Intent(getApplicationContext(),listActivity.class);
        startActivity(intent);
    }

    public void mainMenu4(View view) {
        Intent intent = new Intent(getApplicationContext(),GpsActivity.class);
        startActivity(intent);
    }

    public void mainMenu5(View view) {
        Intent intent = new Intent(getApplicationContext(), FtpUploadActivity.class);
        startActivity(intent);
    }

    public void mainMenu6(View view) {
        Intent intent = new Intent(getApplicationContext(), FtpStreamingActivity.class);
        startActivity(intent);
    }
}