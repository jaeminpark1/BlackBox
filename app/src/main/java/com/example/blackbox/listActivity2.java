package com.example.blackbox;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    List<String> fileList2 = new ArrayList<>();
    String sdcardName2;
    String pos2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filelist2);
        listView = (ListView) findViewById(R.id.ListView2);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileList2));
        sdcardName2 = getIntent().getStringExtra("sdcardName");
        pos2 = getIntent().getStringExtra("pos");
        listRaw(fileList2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String st=(String)fileList2.get(position);
                Uri uri = Uri.parse("storage/"+sdcardName2+"/"+pos2+"/"+st);
                Intent intent = new Intent(getApplicationContext(), VideoViewActivity.class);
                intent.putExtra("uri",uri.toString());
                startActivity(intent);
            }
        });
    }

    public void listRaw(List<String> fileList) {
        File file3 = new File("storage/"+sdcardName2+"/"+pos2);
        File list[] = file3.listFiles();

        for( int i=0; i< list.length; i++)
        {
            fileList2.add(list[i].getName());
        }

    }
}



