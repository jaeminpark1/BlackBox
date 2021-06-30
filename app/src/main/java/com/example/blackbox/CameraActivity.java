package com.example.blackbox;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private Camera camera;
    private MediaRecorder mediaRecorder;
    private SurfaceHolder surfaceHolder;
    private boolean recording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_camera);
        } else {
            setContentView(R.layout.activity_camera_landscape);
        }
        // 화면이 가로/세로로 전환될때 orientation 상태를 체크해서 가로면 camera 레이아웃을 세로면 camera_landscape 레이아웃을 가져옴

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        Button btn_record = (Button) findViewById(R.id.btn_record);

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            // CAMERA 권한이 있는지 확인해서 없으면 권한 요청 창을 띄움
        }else {
            try {
                camera = Camera.open();
            }catch (Exception e) {
                Log.e(getString(R.string.app_name), "failed to open Camera");
                e.printStackTrace();
            }
            // Camera.open()을 호출할 때 이미 다른 애플리케이션에서 카메라를 사용 중이라면 예외가 발생하므로 try 블록으로 호출 부분을 래핑
            camera.setDisplayOrientation(90);
            // 카메라를 보여줄데 90도 방향으로 보이게 실행
            surfaceHolder = surfaceView.getHolder();
            // surfaceView에서 홀더를 가져와서 surfaceHolder에 넣음
            surfaceHolder.addCallback(this);
            // surfaceHolder의 콜백을 this(현재 액티비티)로 지정
        }

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recording) {
                    // recording=true일때 즉 레코딩 중일때 하는 작업
                    mediaRecorder.stop();
                    // 레코더를 멈춤
                    mediaRecorder.release();
                    // 레코더를 방출
                    Toast.makeText(CameraActivity.this, "녹화가 종료되었습니다.", Toast.LENGTH_SHORT).show();
                    // 녹화 종료를 알리는 토스트 메시지
                    camera.lock();
                    // 카메라 잠금
                    recording = false;
                    // recording 변수를 false로 바꿔줌
                    btn_record.setText("녹화 시작");
                    // btn_record 버튼을 녹화 시작으로 바꿔줌
                } else {
                    //recording=false일때 즉 레코딩 중이 아닐때 하는 작업
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mediaRecorder = new MediaRecorder();
                                // MediaRecorder를 mediaRecorder라는 이름으로 생성
                                camera.unlock();
                                // 카메라 잠금 해제
                                mediaRecorder.setCamera(camera);
                                // mediaRecorder에서 쓸 카메라를 지정
                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                                // mediaRecorder의 AudioSource를 CAMCORDER에서 가져올거라고 지정
                                mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                                // mediaRecorder의 VideoSource를 CAMERA에서 가져올거라고 지정
                                mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
                                // mediaRecorder의 녹화 화질을 720P로 지정
                                mediaRecorder.setOrientationHint(90);
                                // 90도 방향으로 보여준다는걸 힌트로 세팅
                                String time = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
                                // 현재 시간을 yyyy년MM월dd일 HH시mm분ss초 형식으로 가져와서 time에 담음
                                mediaRecorder.setOutputFile("sdcard/Movies/"+time+".mp4");
                                // mediaRecorder가 녹화한 파일일 어디다 저장할지 지정
                                mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
                                // mediaRecorder의 프리뷰 디스플레이를 설정 surfaceHolder에서 Surface를 가져옴
                                mediaRecorder.prepare();
                                // mediaRecorder를 준비함
                                mediaRecorder.start();
                                // mediaRecorder를 시작함
                                Toast.makeText(CameraActivity.this, "녹화가 시작되었습니다.", Toast.LENGTH_SHORT).show();
                                // 녹화 시작을 알리는 토스트 메시지
                                recording = true;
                                // recording 변수를 true로 바꿔줌
                                btn_record.setText("녹화 중지");
                                // btn_record 버튼을 녹화 중지로 바꿔줌
                            } catch (Exception e) {
                                e.printStackTrace();
                                mediaRecorder.release();
                                // 위의 try 작업을 하는중에 Exception이 발생하면 mediaRecorder를 방출시킴
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (camera == null) {
            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
            // surface가 생성 되었을대 프리뷰를 시작
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        // 프리뷰가 존재하지 않을때

        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 프리뷰를 멈추고

        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (Exception e) {

        }
        camera.startPreview();
        // 프리뷰를 다시 시작
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

}