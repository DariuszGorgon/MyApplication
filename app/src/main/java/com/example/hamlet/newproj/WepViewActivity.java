package com.example.hamlet.newproj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class WepViewActivity extends AppCompatActivity {
    static WebView webView;
    static TextView textViewTemp;
    static TextView textViewPress;
    static TextView textViewLight;
    static TextView textViewHum;
    static  String url;
    static Thread threads;
    private static Context context;
    private boolean isRunning=true;
    String htmlCode="";
    char[] charTable=new char[50];
    int light=0;
    int humidity=0;
    double temperature=0;
    double pressure=0;


    Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            webView.loadUrl(url);
            try {
            htmlCode=webView.getTitle().toString();

            charTable=htmlCode.toCharArray();

            textViewTemp.setText(charTable, 0, 5);
            textViewPress.setText(charTable, 6, 6);
            textViewLight.setText(charTable, 17, 5);
            textViewHum.setText(charTable, 13, 3);

            humidity = Integer.parseInt(textViewHum.getText().toString());
            textViewHum.setText("Humidity = "+humidity+" %");

            light = Integer.parseInt(textViewLight.getText().toString());
            textViewLight.setText("Light = "+light+" lx");

            temperature= Double.parseDouble(textViewTemp.getText().toString());
            textViewTemp.setText("Temp = "+temperature+" C");

            pressure= Double.parseDouble(textViewPress.getText().toString());
            textViewPress.setText("Pres = "+pressure+" hPa");
            } catch (Exception e) {
                Log.d("LOG_TAG", "Error: " + e.getMessage());
            }




        }
    };


    private Runnable autoRefresh = new Runnable() {
        @Override
        public void run() {
            while(isRunning) {



                try {
                    Message msg = myHandler.obtainMessage();
                    myHandler.sendMessageAtFrontOfQueue(msg);

                    Thread.sleep(250);

                /*  String inputLine;
                    String stringtestowy="";
                   int inttestowy=0;

                    htmlCode="";
                    URL url = new URL(WepViewActivity.url);

                    InputStreamReader inStreamReader = new InputStreamReader(url.openStream());
                    BufferedReader in = new BufferedReader(inStreamReader);

                    while ((inputLine = in.readLine()) != null)
                        htmlCode += inputLine;

                    in.close();
                    Log.d("LOG_TAG", "good" + stringtestowy + inttestowy);*/


                } catch (Exception e) {
                    Log.d("LOG_TAG", "Error: " + e.getMessage());
                }




            }
        }
    };

    public static Context getAppContext() {
        return WepViewActivity.context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wep_view_activity);

        WepViewActivity.context = getApplicationContext();
        webView = (WebView) findViewById(R.id.webView);
        textViewTemp=(TextView)findViewById(R.id.textWiewTemperature);
        textViewPress=(TextView)findViewById(R.id.textWiewPressure);
        textViewLight=(TextView)findViewById(R.id.textWiewLight);
        textViewHum=(TextView)findViewById(R.id.textWiewHumidity);
        url = ConnectActivity.getUrl();
        webView.loadUrl(url);
        threads = new Thread(autoRefresh, "nieWiemPoCoTenArgument");

    }

    @Override
    protected void onResume() {
        super.onResume();
        threads.start();
        isRunning=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning=false;

    }

    public void returnButton(View view) {

        Intent intent = new Intent(this, ConnectActivity.class);
        startActivity(intent);
        finish();

    }


    public void refreshButton(View view) {
       /* byte[] buff = new byte[10];
        buff[0]=0x69;
        buff[1]=0x70;
        buff[2]=0x71;
        buff[3]=0x72;
        buff[4]=0x73;

        webView.postUrl(url,buff);*/



        Toast.makeText(WepViewActivity.getAppContext(), htmlCode, Toast.LENGTH_LONG).show();

    }





}
