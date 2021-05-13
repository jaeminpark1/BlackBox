package com.example.blackbox;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class listActivity extends AppCompatActivity {

    ListView listView;
    List<String> fileList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filelist);
        listView = (ListView) findViewById(R.id.ListView);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileList));
        listRaw(fileList);

    }

    public void listRaw(List<String> fileList) {
        Field[] fields = R.raw.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            fileList.add(fields[i].getName());
        }
    }



}



