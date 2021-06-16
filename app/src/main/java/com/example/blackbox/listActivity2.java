package com.example.blackbox;


import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class listActivity2 extends AppCompatActivity {

    ListView listView;
    String sdcardName2;
    String pos2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filelist2);
        CustomListViewAdapter adapter = new CustomListViewAdapter() ;
        listView = findViewById(R.id.ListView2);
        // id를 찾는 이유는 listView.xxx 이라는 명령어들을 쓰기 위함
        listView.setAdapter(adapter);
        // listView의 Adapter를 커스텀뷰의 adapter로 설정함

        sdcardName2 = getIntent().getStringExtra("sdcardName");
        pos2 = getIntent().getStringExtra("pos");
        // sd카드 이름과 이전 액티비티에서 선택한 포지션(폴더명)을 getStringExtra로 가져옴
        listRaw(adapter);
        // 리스트뷰에 들어갈 내용을 쓰기 위해 listRaw 함수 호출

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // 리스트뷰의 아이템을 클릭했을때 실행되는 코드 재정의
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String st=adapter.getName(position);
                // 사용자가 선택한 포지션에 해당되는 파일 이름을 adapter에서 가져와서 st에 담음
                Uri uri = Uri.parse("storage/"+sdcardName2+"/"+pos2+"/"+st);
                // uri = storage/sd카드 폴더명/이전 액티비에서 선택한 폴더명/이번 액티비티에서 선택한 파일명
                Intent intent = new Intent(getApplicationContext(), VideoViewActivity.class);
                intent.putExtra("uri",uri.toString());
                startActivity(intent);
                // uri를 문자열로 만들어 다음 액티비티로 보냄 문자열로 만드는 이유는 다음 액티비티에서 getStringExtra를 쓰기 위함
            }
        });
    }

    public void listRaw(CustomListViewAdapter adapter) {
        File file = new File("storage/"+sdcardName2+"/"+pos2);
        // storage/sd카드폴더명/이전 액티비에서 선택한 폴더명 경로를 file에 담음
        File list[] = file.listFiles();
        // file의 경로(storage/sd카드폴더/이전 액티비에서 선택한 폴더명) 밑에 있는 모든 폴더를 list File 배열에 담음

        for( int i=0; i< list.length; i++)
        {
            Bitmap bitmap  = ThumbnailUtils.createVideoThumbnail(list[i].getPath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            // list에 있는 파일을 하나씩 풀스크린으로 썸네일 추출
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 240, 240);
            // 추출한 썸네일을 240x240 사이즈로 리사이즈
            adapter.addItem(thumbnail,list[i].getName());
            // 썸네일과 파일명을 adapter를 통해 리스트에 추가시킴
        }

    }
}

