package com.example.hamlet.newproj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class ConnectActivity extends AppCompatActivity {
    static String url;
    SharedPreferences sheredpreferences;
    SharedPreferences.Editor editor;
    Animation animhide;
    static Context context;

    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.connect_activity_id);
        rl.setOnTouchListener(new OnSwipeTouchListener(ConnectActivity.this) {
            @Override
            public void onSwipeLeft() {
                gotoSensorViewActivity();

            }
            @Override
            public void onSwipeTop() {
                gotoSetURLPopup();
            }
            @Override
            public void onSwipeBottom() {
                moveTaskToBack(true);
            }
            @Override
            public void onSwipeRight() {
                gotoMemoryActivity();
            }


        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        sheredpreferences = getSharedPreferences("com.example.hamlet.newproj", Context.MODE_PRIVATE);
        editor = sheredpreferences.edit();
        context = getApplicationContext();
        animhide = AnimationUtils.loadAnimation(this, R.anim.hide);

    }
    @Override
    public void onBackPressed() {
        System.exit(0);
    }
    //=================================gotoSensorView===============================================
    public void setURL(View view) {
        view.startAnimation(animhide);
        url=sheredpreferences.getString("Last_URL","http://192.168.4.1/weather");
        new SlideAtivityAsyncTask().execute();
    }
    //==============================================================================================
    //============================================actionBar=========================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_connect, menu);//podmiana xmp na jave
        return super.onCreateOptionsMenu(menu);
    }

    public void onPalaczenieClick(MenuItem item) {
        startActivity(new Intent(this,SetUrlPopup.class));

    }

    public void onProgramieClick(MenuItem item) {
        Snackbar.make(this.findViewById(android.R.id.content).getRootView(), "Created by: Kamil Kolmus,Darek Gorgoń \nVersion: 1.5", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }public void onNavigationClick(MenuItem item) {
        gotoNavigationPopup();
    }
    //==============================================================================================
    public static String getUrl(){
        return url;
    }

    //==============================================================================================
    private class SlideAtivityAsyncTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            int i=0;
            try {
                Thread.sleep(20);
                publishProgress(i);
            } catch (Exception e) {
                Log.d("connectActivyty", "Error: " + e.toString());
            }
            return i;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            Intent intent= new Intent(ConnectActivity.context, SensorViewActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

        }
    }
    private void gotoSetURLPopup() {
        startActivity(new Intent(ConnectActivity.context, SetUrlPopup.class));

    }

    private void gotoSensorViewActivity() {
        Intent intent= new Intent(ConnectActivity.context, SensorViewActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();

    }

    private void gotoMemoryActivity() {
        startActivity(new Intent(SensorViewActivity.context, MemoryActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();

    }
    private void gotoNavigationPopup() {
        startActivity(new Intent(ConnectActivity.context, NavigationPopup.class));
    }
}
