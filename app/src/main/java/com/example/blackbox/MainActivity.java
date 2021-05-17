package com.example.blackbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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