package com.example.hamlet.newproj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import java.io.File;

/**
 * Created by Kamil on 2016-05-15.
 */
public class Clear extends Activity {

    Animation animHide;
    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WepApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clear_activity);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.22));
        animHide = AnimationUtils.loadAnimation(this, R.anim.hide);
    }
    public void onTakClick(View view) {
        view.startAnimation(animHide);
        File file = new File(path + "/weather.txt");
        MyFileClass.ClearFile(file);
        StorageActivity.s= MyFileClass.LoadFile(file);
        StorageActivity.numberOfPosition = StorageActivity.s.length;
        StorageActivity.MyAdapter Adapter = new StorageActivity.MyAdapter();
        StorageActivity.recyclerview.setAdapter(Adapter);
        finish();
    }
    public void onNieClick(View view) {
        finish();

    }
    ///hovbh
}
