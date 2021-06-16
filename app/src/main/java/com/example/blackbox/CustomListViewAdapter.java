package com.example.blackbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListViewAdapter extends BaseAdapter {
    private ArrayList<CustomListViewItem> listViewItemList = new ArrayList<CustomListViewItem>() ;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList


    public CustomListViewAdapter() { }
    // ListViewAdapter의 생성자

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }
    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현

        final Context context = parent.getContext();
        //Context(현재 상태의 맥락)을 가져와서 context에 담음

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.customlistview, parent, false);
        }
        // "customlistview" Layout을 inflate하여 convertView 참조 획득.

        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        iconImageView.setImageBitmap(listViewItemList.get(position).getIcon());
        titleTextView.setText(listViewItemList.get(position).getFilename());
        // 아이템 내 각 위젯에 데이터 반영

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }
    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }
    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현

    public String getName(int position) {
        return listViewItemList.get(position).getFilename();
    }
    // 사용자가 선택한 위치에 있는 파일명을 가져와서 리턴 시켜줌

    public void remove(int position) {
        listViewItemList.remove(listViewItemList.get(position));
    }
    //listViewItemList 에서 position 에 해당되는 자리의 값을 지우기

    public void addItem(Bitmap icon, String filename) {
        // 아이템 데이터 추가를 위한 함수.
        CustomListViewItem item = new CustomListViewItem();
        item.setIcon(icon);
        item.setFilename(filename);
        listViewItemList.add(item);
        //item을 하나 만들어서 인자로 받은 (Bitmap icon, String filename)값을 넣어 listViewItemList에 추가
    }
}