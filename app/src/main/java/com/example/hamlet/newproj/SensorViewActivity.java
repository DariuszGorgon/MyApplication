package com.example.hamlet.newproj;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SensorViewActivity extends AppCompatActivity {

    TextView textViewTemp;
    TextView textViewPress;
    TextView textViewLight;
    TextView textViewHum;
    TextView message;
    static Context context;
    static int SessionCounter = 0;
    int light = 0;
    int humidity = 0;
    double temperature = 0;
    double pressure = 0;
    Animation animHide;
    String receiveData = "";
    static MyAsyncTask myAsyncTask;
    static boolean CreatedOnce = false;
    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyWeatherApp";
    //==============================================================================================

    public static Context getAppContext() {
        return SensorViewActivity.context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_view_activity);

        context = getApplicationContext();
        textViewTemp = (TextView) findViewById(R.id.textWiewTemperature);
        textViewPress = (TextView) findViewById(R.id.textWiewPressure);
        textViewLight = (TextView) findViewById(R.id.textWiewLight);
        textViewHum = (TextView) findViewById(R.id.textWiewHumidity);
        message = (TextView) findViewById(R.id.message);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.sensor_view_activity_id);
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        rl.setOnTouchListener(new OnSwipeTouchListener(SensorViewActivity.this) {

            @Override
            public void onSwipeLeft() {
                gotoMemoryActivity();
            }
            @Override
            public void onSwipeRight() {
                gotoConnectActivity();
            }
            @Override
            public void onSwipeTop() {
                gotoTimerPopup();
            }
            @Override
            public void onSwipeBottom() {
                moveTaskToBack(true);
              //NIE NISZCZY my ASSYNCTTASK . PRACA W BACKGROUD!!!!!!!!!!!!!!!


            }
        });

        //=================tworzenie/sprawdzanie czy istieje folder do zapisu=======================
        File fl = new File(path);
        fl.mkdir();
        animHide = AnimationUtils.loadAnimation(this, R.anim.hide);

        Animation pulseHideLong,pulseHideDownLong;
        pulseHideLong = AnimationUtils.loadAnimation(this, R.anim.hide_pulse_after_some_sec);
        pulseHideDownLong = AnimationUtils.loadAnimation(this, R.anim.hide_down_pulse_after_some_sec);
        if (!CreatedOnce) {
            ImageView imageView = (ImageView) findViewById(R.id.pulse_image) ;
            imageView.startAnimation(pulseHideDownLong);
            TextView textView = (TextView) findViewById(R.id.pulse_text);
            textView.startAnimation(pulseHideLong);
            startActivity(new Intent(this, TimerPopup.class));
            CreatedOnce = true;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    //============================================actionBar=========================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sensorview, menu);//podmiana xmp na jave
        return super.onCreateOptionsMenu(menu);
    }

    public void onTimerClick(MenuItem item) {
        startActivity(new Intent(this, TimerPopup.class));
    }

    //==============================Buttons=========================================================
    @Override
    public void onBackPressed() {
        gotoConnectActivity();
    }

    public void saveButton(View view) {
        view.startAnimation(animHide);
        saveNewData();

    }

    public void onMemoryCLICK(View view) {
        view.startAnimation(animHide);
        new SlideAtivityAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    //=============================getdatafromSTM32=================================================
    public String getUrlContent(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = null;
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(500);
            con.setReadTimeout(500);
            con.setDoOutput(false);
            con.setDoInput(true);
            con.setRequestMethod("GET");
            InputStreamReader in = new InputStreamReader(con.getInputStream(), Charset.forName("ISO-8859-2"));
            BufferedReader r = new BufferedReader(in);                         //ISO-8859-2 odbierane z esp-01
            String s = "";
            s = r.readLine();
            r.close();
            return s;
        } catch (Exception e) {
            Log.d("getUrlContent", "Error: " + e.toString());
            receiveData = null;
            message.setText("CONNECTION ERROR");
        }
        return receiveData;

    }

    //==============================================================================================
    private class MyAsyncTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {


            while (!isCancelled()) {
                try {
                    int period = TimerPopup.getTimeInMS();
                    receiveData = getUrlContent(ConnectActivity.getUrl());
                    if (receiveData == null) {
                        publishProgress(0);
                    } else {
                        publishProgress(SessionCounter);
                        SessionCounter++;
                    }
                    while (period > 0 && !TimerPopup.timerChangeFlag) {
                        //kalibracja na OKO :) powinno być 100 - czas potrzebny na wykonanie pętli itp.
                        //do poprawy!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        Thread.sleep(92);
                        period -= 100;
                    }
                    if (TimerPopup.timerChangeFlag = true)
                    {
                        TimerPopup.timerChangeFlag = false;
                    }


                } catch (Exception e) {
                    Log.d("polaczenie", "Error: " + e.toString());
                    publishProgress(0);
                }

            }
            return SessionCounter;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            char[] charTable = new char[25];
            String tempS = "";
            String presS = "";
            String humS = "";
            String lightS = "";
            if (values[0] == 0) {
                message.setText("NO CONNECTION");
            } else {
                try {
                    charTable = receiveData.toCharArray();
                    tempS = String.valueOf(charTable, 0, 5);
                    presS = String.valueOf(charTable, 6, 6);
                    humS = String.valueOf(charTable, 13, 3);
                    lightS = String.valueOf(charTable, 17, 5);
                    humidity = Integer.parseInt(humS);
                    textViewHum.setText(humidity + " %");

                    light = Integer.parseInt(lightS);
                    textViewLight.setText(light + " lx");

                    temperature = Double.parseDouble(tempS);
                    textViewTemp.setText(temperature + " °C");

                    pressure = Double.parseDouble(presS);
                    textViewPress.setText(pressure + " hPa");
                    if (TimerPopup.isCheckBoxSelected()) {
                        saveNewData();
                        message.setText("Periodic recording ON\n"+
                                " Number of measurements = " + values[0]+"\nPeriod = " + TimerPopup.getTime());
                    } else {
                        message.setText("Periodic recording OFF\n" +
                                " Number of  measurement = " + values[0]+"\nPeriod = " + TimerPopup.getTime());
                    }

                } catch (Exception e) {
                    Log.d("conversion", "Error: " + e.toString() + "x" + receiveData + "x");
                    message.setText("WRONG DATA");
                }

            }
        }
    }//==============================================================================================

    private class SlideAtivityAsyncTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            int k = 0;
            try {
                Thread.sleep(20);
                publishProgress(k);
            } catch (Exception e) {
                Log.d("gotomemoryactivyty", "Error: " + e.toString());
            }
            return k;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            gotoMemoryActivity();

        }
    }//==============================================================================================

    private void saveNewData() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss     yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        File file = new File(path + "/weather.txt");
        String[] s = new String[1];
        s[0] = "TEMPERATURE = " + temperature + " °C   HUMIDITY = " + humidity + " %   LIGHT = " +
                light + " lx   PRESSURE = " + pressure + " hPa                   " +
                currentDateandTime + "";
        MyFileClass.AddNewDataToLoadedFile2(file, s[0]);
    }

    //============================================================================================
    private void gotoTimerPopup() {
        startActivity(new Intent(SensorViewActivity.context, TimerPopup.class));

    }

    private void gotoConnectActivity() {
        Intent intent = new Intent(SensorViewActivity.this, ConnectActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        myAsyncTask.cancel(true);
        finish();

    }

    private void gotoMemoryActivity() {
        startActivity(new Intent(SensorViewActivity.context, MemoryActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);



    }

}