package com.example.blackbox;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.List;

public class FtpListActivity extends AppCompatActivity {

    ListView listView;
    String[] ftpFileList;
    private ConnectFTP ConnectFTP;
    String ip;
    int port;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftplist);
        ConnectFTP = new ConnectFTP();
        CustomListViewAdapter adapter = new CustomListViewAdapter() ;
        listView = findViewById(R.id.ListView4);
        // id를 찾는 이유는 listView.xxx 이라는 명령어들을 쓰기 위함
        ftpFileList = getIntent().getStringArrayExtra("ftpFileList");
        // sd카드 이름과 이전 액티비티에서 선택한 포지션(폴더명)을 getStringExtra로 가져옴
        ip = getIntent().getStringExtra("ip");
        port = getIntent().getIntExtra("port",21);
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ftpFileList));
        // listView의 Adapter 설정 커스텀뷰가 아닌 기본 리스트뷰를 그냥 쓰며 리스트로 fileList를 사용

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // 리스트뷰의 아이템을 클릭했을때 실행되는 코드 재정의
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pos=ftpFileList[position];

                new Thread(new Runnable() {
                    public void run() {
                        String status = ConnectFTP.ftpConnect(ip,username,password,port);
                        ConnectFTP.ftpDownloadFile(pos,"sdcard/Download/test.mp4");
                    }
                }).start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Uri uri = Uri.parse("sdcard/Download/test.mp4");
                        // uri = storage/sd카드 폴더명/이전 액티비에서 선택한 폴더명/이번 액티비티에서 선택한 파일명
                        Intent intent = new Intent(getApplicationContext(), VideoViewActivity.class);
                        intent.putExtra("uri",uri.toString());
                        startActivity(intent);
                        // uri를 문자열로 만들어 다음 액티비티로 보냄 문자열로 만드는 이유는 다음 액티비티에서 getStringExtra를 쓰기 위함
                    }
                }, 4000);
            }
        });
    }
}
