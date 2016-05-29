package com.example.hamlet.newproj;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.File;

/**
 * Created by Kamil on 2016-05-15.
 */
public class ClearPopup extends Activity {

    Animation animHide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clear_popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.22));
        animHide = AnimationUtils.loadAnimation(this, R.anim.hide);
    }
    public void onTakClick(View view) {
        view.startAnimation(animHide);
        File file = new File(SensorViewActivity.path + "/weather.txt");
        MyFileClass.ClearFile(file);
        com.example.hamlet.newproj.MemoryActivity.s= MyFileClass.LoadFile(file);
        com.example.hamlet.newproj.MemoryActivity.numberOfPosition = com.example.hamlet.newproj.MemoryActivity.s.length;
      //  MemoryActivity.MyAdapter Adapter = new MemoryActivity.MyAdapter();
      //  com.example.hamlet.newproj.MemoryActivity.recyclerview.setAdapter(Adapter);
        finish();
    }
    public void onNieClick(View view) {
        finish();

    }
}
