package com.example.blackbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        // sd카드 권한 확인을 위한 함수 호출
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    // sd카드 읽기 권한, 쓰기 권한을 PERMISSIONS_STORAGE라는 스트링 배열로 만듬

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    // REQUEST_EXTERNAL_STORAGE를 1이라는 상수로 고정함

    public static void verifyStoragePermissions(Activity activity) {
        // 스토리지 권한 확인을 위한 함수
        int permission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // sd카드 쓰기 권한이 있는지 체크해서 permission에 담음
        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        //만약 권한이 승인되지 않은 상태라면 권한요청 창을 액티비티에 뛰움
    }

    public void mainMenu1(View view) {
        Toast.makeText(this,"실시간 영상공유 기능은 구현중 입니다.",Toast.LENGTH_SHORT).show();
    }


    public void mainMenu2(View view) {
        Intent intent = new Intent(getApplicationContext(),listActivity.class);
        startActivity(intent);
    }

    public void mainMenu3(View view) {
        Toast.makeText(this,"위치정보 기능은 구현중 입니다.",Toast.LENGTH_SHORT).show();
    }
}