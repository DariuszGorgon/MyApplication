package com.example.hamlet.newproj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class ConnectActivity extends AppCompatActivity {
    static String url;
    SharedPreferences sheredpreferences;
    SharedPreferences.Editor editor;
    Animation animRotate;
    Animation animhide;
    Thread threads;
    static Context context;
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);


    }
    @Override
    protected void onStart() {
        super.onStart();
        sheredpreferences = getSharedPreferences("com.example.hamlet.newproj", Context.MODE_PRIVATE);
        editor = sheredpreferences.edit();
        context = getApplicationContext();
        animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        animhide = AnimationUtils.loadAnimation(this, R.anim.hide);
        threads = new Thread(startNewActivity, "nieWiemPoCoTenArgument");

    }
    //=================================gotoSensorView===============================================
    public void setURL(View view) {
        view.startAnimation(animhide);
        url=sheredpreferences.getString("Last_URL","http://192.168.4.1/weather");
        threads.start();
    }
    private Runnable startNewActivity = new Runnable() {
        @Override
        public void run() {

                try {

                    Thread.sleep(150);
                    Intent intent= new Intent(ConnectActivity.context, SensorViewActivity.class);
                    startActivity(intent);
                    finish();


                } catch (Exception e) {
                    Log.d("LOG_TAG", "Error: " + e.getMessage());
                }

            }

    };
    //==============================================================================================
    //============================================actionBar=========================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);//podmiana xmp na jave
        return super.onCreateOptionsMenu(menu);


    }

    public void onPalaczenieClick(MenuItem item) {
        startActivity(new Intent(this,SetUrlPopup.class));

    }

    public void onProgramieClick(MenuItem item) {

        Toast.makeText(this,"Created by: Kamil Kolmus,Darek Gorgoń \nVersion: 1.2", Toast.LENGTH_SHORT).show();
    }
    //==============================================================================================
    public static String getUrl(){
        return url;
    }
}
