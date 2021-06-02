package com.example.blackbox;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoViewActivity extends AppCompatActivity {

    VideoView mVideoView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //상단바를 제거하기 위한 코드

        Uri uri = Uri.parse(getIntent().getStringExtra("uri"));
        //이전 액티비티 listActivity2에서 보낸 uri를 가져옴

        mVideoView = findViewById(R.id.videoView);
        //id를 찾는 이유는 mVideoView.xxx 이라는 명령어들을 쓰기 위함
        mVideoView.setMediaController(new MediaController(this));
        //미디어 컨트롤러(재생,멈춤,앞으로,뒤로)를 사용하기 위해서 세팅
        mVideoView.setVideoURI(uri);
        //비디오뷰에서 쓸 URI를 아까 이전 액티비티에서 가져왔던 uri로 설정
        mVideoView.start();
        //비디오뷰 실행

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        //영상이 끝까지 재생되서 종료 되었을 경우 감지해서 실행하는 리스너
            @Override
            public void onCompletion(MediaPlayer mp) {
                onBackPressed();
                //영상이 종료되면 뒤로가기 버튼을 눌러서 동영상 파일 리스트를 보여주던 이전 액티비티로 돌아감
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    //하단바(소프트키)를 제거하기 위한 코드
}

