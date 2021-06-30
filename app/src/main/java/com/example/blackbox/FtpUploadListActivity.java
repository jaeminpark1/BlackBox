package com.example.blackbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class FtpUploadListActivity extends AppCompatActivity {

    ListView listView;
    private ConnectFTP ConnectFTP;
    String status="";
    String ip;
    int port;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftpuploadlist);
        ConnectFTP = new ConnectFTP();
        CustomListViewAdapter adapter = new CustomListViewAdapter() ;
        listView = findViewById(R.id.ListView5);
        // id를 찾는 이유는 listView.xxx 이라는 명령어들을 쓰기 위함
        listView.setAdapter(adapter);
        // listView의 Adapter를 커스텀뷰의 adapter로 설정함
        ip = getIntent().getStringExtra("ip");
        port = getIntent().getIntExtra("port",21);
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        listRaw(adapter);
        // 리스트뷰에 들어갈 내용을 쓰기 위해 listRaw 함수 호출

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // 리스트뷰의 아이템을 클릭했을때 실행되는 코드 재정의
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String st=adapter.getName(position);
                // 사용자가 선택한 포지션에 해당되는 파일 이름을 adapter에서 가져와서 st에 담음
                Uri uri = Uri.parse("sdcard/Movies/"+st);
                // uri = sdcard(내부sd카드)/Movies/이번 액티비티에서 선택한 파일명
                Intent intent = new Intent(getApplicationContext(), VideoViewActivity.class);
                intent.putExtra("uri",uri.toString());
                startActivity(intent);
                // uri를 문자열로 만들어 다음 액티비티로 보냄 문자열로 만드는 이유는 다음 액티비티에서 getStringExtra를 쓰기 위함
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // 리스트뷰의 아이템을 길게 클릭했을때 실행되는 코드 재정의
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder ad = new AlertDialog.Builder(view.getContext());
                //AlertDialog 생성
                ad.setIcon(R.mipmap.ic_launcher);
                //AlertDialog의 아이콘을 ic_launcher로 지정
                ad.setTitle("확인");
                //AlertDialog의 타이틀을 확인으로 설정
                ad.setMessage(adapter.getName(position)+" 파일을 업로드 하시겠습니까?");
                //AlertDialog의 메시지를 adapter에서 현재 선택된 포지션의 파일명으로 가져와 그 뒤에 그 파일을 삭제할지 물음

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    //포지티브 버튼을 확인으로 설정하고 사용자가 그 버튼을 눌렀을때의 작업 재정의
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        CountDownLatch latch = new CountDownLatch(1);

                        new Thread(new Runnable() {
                            public void run() {
                        ConnectFTP.ftpConnect(ip,username,password,port);
                        status=ConnectFTP.ftpUploadFile("sdcard/Movies/"+adapter.getName(position)+"/",adapter.getName(position),"");
                                latch.countDown();
                            }
                        }).start();

                        try {
                            latch.await();
                            Toast.makeText(getApplicationContext(),adapter.getName(position)+" 영상 업로드 완료",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    //포지티브 버튼을 취소로 설정하고 사용자가 그 버튼을 눌렀을때의 작업 재정의
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                    //별다른 작업없이 AlertDialog 해산만 시킴
                });

                ad.show();
                //AlertDialog를 보여줌
                return true;
                //true로 하면 클릭에서 응답을 얻을 때까지 AlertDialog 바깥부분의 UI 스레드를 차단
                //false로 하면 AlertDialog 바깥 부분이 차단되지 않았기 때문에 바깥 UI의 작업이 실행됨 그래서 반드시 차단해야함
            }
        });
    }

    public void listRaw(CustomListViewAdapter adapter) {
        File file = new File("sdcard/Movies/");
        // sdcard/Movies/ 경로를 file에 담음
        File list[] = file.listFiles();
        // file의 경로(sdcard/Movies/) 밑에 있는 모든 폴더를 list File 배열에 담음

        for( int i=0; i< list.length; i++)
        {
            Bitmap bitmap  = ThumbnailUtils.createVideoThumbnail(list[i].getPath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            // list에 있는 파일을 하나씩 풀스크린으로 썸네일 추출
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 240, 240);
            // 추출한 썸네일을 240x240 사이즈로 리사이즈
            adapter.addItem(thumbnail,list[i].getName());
            // 썸네일과 파일명을 adapter를 통해 리스트에 추가시킴
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 300);
            //0.3초 딜레이를 주기위해 사용 코드가 너무 빠르게 실행되면 썸네일을 못읽어오는 경우가 있어서 그걸 방지함
        }

    }

}
