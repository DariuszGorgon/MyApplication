package com.example.hamlet.newproj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class ConnectActivity extends AppCompatActivity {
    EditText urlEditText;
    public static String url;
    SharedPreferences sheredpreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);
        urlEditText=(EditText) findViewById(R.id.url);
        sheredpreferences = getSharedPreferences("com.example.hamlet.newproj", Context.MODE_PRIVATE);
        editor = sheredpreferences.edit();
        urlEditText.setText(sheredpreferences.getString("Last_URL","http://192.168.4.1/weather"));


    }
    public static String getUrl(){
        return url;

    }

    public void setURL(View view) {
        url=urlEditText.getText().toString();

        editor.putString("Last_URL", url);
        editor.commit();

        Intent intent= new Intent(this, WepViewActivity.class);
        startActivity(intent);
        finish();
    }
}
