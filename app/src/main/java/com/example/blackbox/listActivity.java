package com.example.blackbox;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
        setContentView(R.layout.filelist);
        listView = (ListView) findViewById(R.id.ListView);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileList));
        listRaw(fileList);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pos=(String)fileList.get(position);
                Intent intent = new Intent(getApplicationContext(), listActivity2.class);
                intent.putExtra("sdcardName",sdcardName);
                intent.putExtra("pos",pos);
                startActivity(intent);
            }
        });
    }

    public void sdPath() {
        File file = new File("storage/");
        File[] listOfStorages = file.listFiles();

        for (File tmp : listOfStorages) {
            if (tmp.getName().contains("-")) {
                sdcardName = tmp.getName();
                break;
            }
        }
    }

    public void listRaw(List<String> fileList) {
        sdPath();
        File file2 = new File("storage/"+sdcardName);
        File list[] = file2.listFiles();

        for( int i=0; i< list.length; i++)
        {
            fileList.add(list[i].getName());
        }

    }
}



