package com.example.hamlet.newproj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

public class ConnectActivity extends AppCompatActivity {
    EditText urlEditText;
    public static String url;
    SharedPreferences sheredpreferences;
    SharedPreferences.Editor editor;
    Animation animRotate;
    Animation animTranslate;
    static Thread threads;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);


    }

    @Override
    protected void onStart() {
        super.onStart();
        urlEditText=(EditText) findViewById(R.id.url);


        sheredpreferences = getSharedPreferences("com.example.hamlet.newproj", Context.MODE_PRIVATE);
        editor = sheredpreferences.edit();
        urlEditText.setText(sheredpreferences.getString("Last_URL","http://192.168.4.1/weather"));
        context = getApplicationContext();
        animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        animTranslate = AnimationUtils.loadAnimation(this, R.anim.translate);
        threads = new Thread(startNewActivity, "nieWiemPoCoTenArgument");

    }

    public static String getUrl(){

         return url;

    }

    public void setURL(View view) {
        urlEditText.startAnimation(animTranslate);
        view.startAnimation(animRotate);
        url=urlEditText.getText().toString();
        threads.start();
        editor.putString("Last_URL", url);
        editor.commit();
    }

    private Runnable startNewActivity = new Runnable() {
        @Override
        public void run() {

                try {
                    Intent intent= new Intent(ConnectActivity.context, WepViewActivity.class);
                    Thread.sleep(150);

                    startActivity(intent);
                    finish();


                } catch (Exception e) {
                    Log.d("LOG_TAG", "Error: " + e.getMessage());
                }

            }

    };
}
//shhsh