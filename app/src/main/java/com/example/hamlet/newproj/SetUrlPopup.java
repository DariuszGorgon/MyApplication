package com.example.hamlet.newproj;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

/**
 * Created by Kamil on 2016-05-15.
 */
public class SetUrlPopup extends Activity {
    public static String newURL;
    SharedPreferences sheredpreferences;
    SharedPreferences.Editor editor;
    EditText urlEditText;
    Animation animHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_url_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.22));
        urlEditText=(EditText) findViewById(R.id.newurl);
        sheredpreferences = getSharedPreferences("com.example.hamlet.newproj",Context.MODE_PRIVATE);
        editor = sheredpreferences.edit();
        urlEditText.setText(sheredpreferences.getString("Last_URL","http://192.168.4.1/weather"));
        animHide = AnimationUtils.loadAnimation(this, R.anim.hide);
    }
    public void setnewURL(View view) {

        newURL=urlEditText.getText().toString();
        editor.putString("Last_URL", newURL);
        editor.commit();
        view.startAnimation(animHide);
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(150);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread1.start();

    }
}
