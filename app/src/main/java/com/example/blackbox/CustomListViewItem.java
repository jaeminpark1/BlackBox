package com.example.blackbox;

import android.graphics.Bitmap;

public class CustomListViewItem {
    private String filename;
    private Bitmap iconBitmap;
    // 커스텀뷰에 쓰일 멤버들 선언

    public void setIcon(Bitmap icon) {
        iconBitmap = icon ;
    }
    public void setFilename(String name) {
        filename = name;
    }
    // 멤버들의 값을 세팅하기 위해 사용

    public Bitmap getIcon() {
        return this.iconBitmap ;
    }
    public String getFilename() {
        return this.filename ;
    }
    // 멤버들의 값을 가져오기 위해 사용
}