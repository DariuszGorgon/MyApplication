package com.example.hamlet.newproj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WepViewActivity extends AppCompatActivity {
    static WebView webView;
    static TextView textViewTemp;
    static TextView textViewPress;
    static TextView textViewLight;
    static TextView textViewHum;
    static String url;
    static Thread threads;
    private static Context context;
    private boolean isRunning = true;
    String htmlCode = "";
    static int light = 0;
    static int humidity = 0;
    static double temperature = 0;
    static double pressure = 0;
    Animation animHide;
    Animation animRotate;

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WepApp";

    public static Context getAppContext() {
        return WepViewActivity.context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wep_view_activity);

        WepViewActivity.context = getApplicationContext();
        webView = (WebView) findViewById(R.id.webView);
        textViewTemp = (TextView) findViewById(R.id.textWiewTemperature);
        textViewPress = (TextView) findViewById(R.id.textWiewPressure);
        textViewLight = (TextView) findViewById(R.id.textWiewLight);
        textViewHum = (TextView) findViewById(R.id.textWiewHumidity);
        url = ConnectActivity.getUrl();
        webView.loadUrl(url);
        threads = new Thread(autoRefresh, "nieWiemPoCoTenArgument");
        //----nowy folder
        File fl = new File(path);
        fl.mkdir();
        animHide = AnimationUtils.loadAnimation(this, R.anim.hide);
        animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);

    }

    @Override
    protected void onResume() {
        super.onResume();
        threads = new Thread(autoRefresh, "nieWiemPoCoTenArgument");
        threads.start();
        isRunning = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;

    }@Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,ConnectActivity.class);
        startActivity(intent);
        finish();
    }

    public void saveButton(View view) {




        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss     yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());

        view.startAnimation(animHide);
        File file = new File(path + "/weather.txt");
        String[] s = new String[1];
        s[0] = "TEMPERATURE = " + WepViewActivity.temperature  + " °C   HUMIDITY = " + WepViewActivity.humidity + " %   LIGHT = "+
                WepViewActivity.light+ " lx   PRESSURE = " + WepViewActivity.pressure + " hPa                   "+
        currentDateandTime+"";
        MyFileClass.AddNewDataToLoadedFile2(file, s[0]);
    }
    public void onCheckDataCLICK(View view) {
        view.startAnimation(animHide);
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                try {

                    sleep(150);
                    Intent intent = new Intent(WepViewActivity.context, StorageActivity.class);
                    startActivity(intent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread1.start();
    }

    public void onClearDataClick1(View view) {
        view.startAnimation(animHide);
        File file = new File(path + "/weather.txt");
        MyFileClass.ClearFile(file);
    }
    public void onProgramieClick(MenuItem item) {

        Toast.makeText(this,"Autorzy:Kamil Kolmus,Darek Gorgoń \nWersja: 1.04", Toast.LENGTH_SHORT).show();
    }



    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            char[] charTable = new char[25];
            webView.loadUrl(url);
            try {
                htmlCode = webView.getTitle().toString();

                charTable = htmlCode.toCharArray();
                String tempS;
                String presS;
                String humS;
                String lightS;

                tempS = String.valueOf(charTable, 0, 5);
                presS = String.valueOf(charTable, 6, 6);
                lightS = String.valueOf(charTable, 17, 5);
                humS = String.valueOf(charTable, 13, 3);

                humidity = Integer.parseInt(humS);
                textViewHum.setText("Humidity = " + humidity + " %");

                light = Integer.parseInt(lightS);
                textViewLight.setText("Light = " + light + " lx");

                temperature = Double.parseDouble(tempS);
                textViewTemp.setText("Temp = " + temperature + " °C");

                pressure = Double.parseDouble(presS);
                textViewPress.setText("Pres = " + pressure + " hPa");
            } catch (Exception e) {
                Log.d("LOG_TAG", "Error: " + e.getMessage());
            }


        }
    };
    private Runnable autoRefresh = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {

                try {
                    Message msg = myHandler.obtainMessage();
                    myHandler.sendMessageAtFrontOfQueue(msg);

                    Thread.sleep(300);

                } catch (Exception e) {
                    Log.d("LOG_TAG", "Error: " + e.getMessage());
                }

            }
        }
    };

}

