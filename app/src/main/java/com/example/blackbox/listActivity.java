package com.example.blackbox;


import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class listActivity extends AppCompatActivity {

    ListView listView;
    List<String> fileList = new ArrayList<>();
    VideoView mVideoView = null;
    String root_sd = Environment.getExternalStorageDirectory().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filelist);
        listView = (ListView) findViewById(R.id.ListView);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileList));
        listRaw(fileList);

        mVideoView = (VideoView) findViewById(R.id.videoView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String st=(String)fileList.get(position);
                Uri uri = Uri.parse(root_sd + "/Movies/"+st);
                Intent intent = new Intent(getApplicationContext(), VideoViewActivity.class);
                intent.putExtra("uri",uri.toString());
                startActivity(intent);
            }
        });
    }

    public void listRaw(List<String> fileList) {
        File file = new File( root_sd + "/Movies/" ) ;
        File list[] = file.listFiles();

        for( int i=0; i< list.length; i++)
        {
            fileList.add(list[i].getName());
        }

    }


}



