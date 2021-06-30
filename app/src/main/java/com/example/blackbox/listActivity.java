package com.example.blackbox;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class listActivity extends AppCompatActivity {

    ListView listView;
    List<String> fileList = new ArrayList<>();
    String sdcardName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = findViewById(R.id.ListView);
        // id를 찾는 이유는 listView.xxx 이라는 명령어들을 쓰기 위함
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileList));
        // listView의 Adapter 설정 커스텀뷰가 아닌 기본 리스트뷰를 그냥 쓰며 리스트로 fileList를 사용
        listRaw(fileList);
        // fileList의 리스트를 채우기 위해서 함수 호출

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // 리스트뷰의 아이템을 클릭했을때 실행되는 코드 재정의
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pos=fileList.get(position);
                // 사용자가 선택한 포지션에 해당되는 폴더이름을 fileList에서 가져와서 pos에 담음
                Intent intent = new Intent(getApplicationContext(), listActivity2.class);
                // listActivity2 intent를 생성
                intent.putExtra("sdcardName",sdcardName);
                // sdcardName을 sdcardName라는 키값으로 intent의 Extra로 담음
                intent.putExtra("pos",pos);
                // pos를 pos라는 키값으로 intent의 Extra로 담음
                startActivity(intent);
                // intent(listActivity2)를 실행함 이때 Extra들도 같이 보냄
            }
        });
    }

    public void sdPath() {
        // sd카드 경로를 찾기위한 함수
        File file = new File("storage/");
        // storage/ 라는 경로를 file에 담음
        File[] listOfStorages = file.listFiles();
        // file의 경로(storage/) 밑에 있는 모든 폴더를 listOfStorages라는 File 배열에 담음

        for (File tmp : listOfStorages) {
            if (tmp.getName().contains("-")) {
                //file의 경로(storage/) 밑에 있는 모든 폴더들 중에 하이픈(-)이 포함된 폴더를 찾음
                //하이픈(-)을 찾는 이유는 sd카드 폴더가 XXXX-XXXX의 형태를 띄고 있기 때문
                sdcardName = tmp.getName();
                //하이픈(-)이 포함된 폴더를 찾았다면 그걸 sdcardName으로 지정
                break;
            }
        }
    }

    public void listRaw(List<String> fileList) {
        sdPath();
        // sd카드 경로르 찾기위해 함수 호출
        File file2 = new File("storage/"+sdcardName);
        // storage/sd카드폴더명 경로를 file2에 담음
        File list[] = file2.listFiles();
        // file2의 경로(storage/sd카드폴더) 밑에 있는 모든 폴더를 list File 배열에 담음
        for( int i=0; i< list.length; i++)
        {
            fileList.add(list[i].getName());
            // list에 있는 폴더들의 이름을 하나씩 fileList에 담음
        }

    }
}



