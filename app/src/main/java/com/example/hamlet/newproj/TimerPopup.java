package com.example.hamlet.newproj;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Kamil on 2016-05-15.
 */
public class TimerPopup extends Activity {
    int hour;
    int min;
    int second;
    int milisecond;
    static boolean isCheckBoxSelected = false;
    static boolean wasCheckBoxChangedStatus = false;
    static boolean timerChangeFlag = false;
    static int timerChangeValue = 0;
    static SharedPreferences sheredpreferences;
    SharedPreferences.Editor editor;
    TextView txH, txMin, txSec, txMilSec;
    CheckBox periodSave,clearmomory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout(width, (int) (height * 0.38));

        txH = (TextView) findViewById(R.id.txHout);
        txMin = (TextView) findViewById(R.id.txMin);
        txSec = (TextView) findViewById(R.id.txSecond);
        txMilSec = (TextView) findViewById(R.id.txMiliSecond);
        periodSave = (CheckBox) findViewById(R.id.period_save);
        clearmomory = (CheckBox) findViewById(R.id.clr_memory);

        sheredpreferences = getSharedPreferences("com.example.hamlet.newproj", Context.MODE_PRIVATE);
        editor = sheredpreferences.edit();
        hour = sheredpreferences.getInt("TimerPopupHour", 0);
        min = sheredpreferences.getInt("TimerPopupMinutes", 0);
        second = sheredpreferences.getInt("TimerPopupSecond", 0);
        milisecond = sheredpreferences.getInt("TimerPopupMiliSecond", 500);

        txSec.setText("" + second);
        txMin.setText("" + min);
        txH.setText("" + hour);
        txMilSec.setText("" + milisecond);

        periodSave.setChecked(isCheckBoxSelected);

    }

    //=======================================================
    public void OnSubstractSecondClick(View view) {
        if (second > 0) {
            second--;
        } else {
            second = 59;
        }
        txSec.setText("" + second);
    }

    public void OnSubstractMinClick(View view) {
        if (min > 0) {
            min--;
        } else {
            min = 59;
        }
        txMin.setText("" + min);
    }

    public void OnSubstractHourClick(View view) {
        if (hour > 0) {
            hour--;
        } else {
            hour = 23;
        }
        txH.setText("" + hour);
    }

    public void OnSubstractMiliSecondClick(View view) {

        if (milisecond <=0)
            milisecond = 900;

        else {
            milisecond = milisecond - 100;
        }
        
        txMilSec.setText("" + milisecond);

    }

    //=======================================================
    public void OnAddSecondClick(View view) {
        if (second < 58) {
            second++;
        } else {
            second = 0;
        }
        txSec.setText("" + second);

    }

    public void OnAddMinClick(View view) {
        if (min < 58) {
            min++;
        } else {
            min = 0;
        }
        txMin.setText("" + min);
    }

    public void OnAddHourClick(View view) {
        if (hour < 22) {
            hour++;
        } else {
            hour = 0;
        }
        txH.setText("" + hour);
    }

    public void OnAddMiliSecondClick(View view) {
        if (milisecond < 900) {
            milisecond = milisecond + 100;
        } else {
            milisecond = 0;
        }
        txMilSec.setText("" + milisecond);
    }

    //========================================================

    public void OnSavePeriodClick(View view) {
        int time=milisecond + 1000 * second + 1000 * 60 * min + 1000 * 60 * 60 * hour;
        if ((time) >= 300) {
            editor.putInt("TimerPopupHour", hour);
            editor.putInt("TimerPopupMinutes", min);
            editor.putInt("TimerPopupSecond", second);
            editor.putInt("TimerPopupMiliSecond", milisecond);
            editor.commit();
            isCheckBoxSelected = periodSave.isChecked();
            if(timerChangeValue!=time) {
                timerChangeFlag = true;
            }
            if(clearmomory.isChecked()){
                File file = new File(SensorViewActivity.path + "/weather.txt");
                MyFileClass.ClearFile(file);
                SensorViewActivity.SessionCounter=1;
            }
            finish();
        } else {
            Toast.makeText(this, "Minimalny czas wynosi 300 ms", Toast.LENGTH_LONG).show();
        }
    }


    public static int getHour() {
        try {
            return sheredpreferences.getInt("TimerPopupHour", 0);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getMin() {
        try {
            return sheredpreferences.getInt("TimerPopupMinutes", 0);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getSecond() {
        try {
            return sheredpreferences.getInt("TimerPopupSecond", 0);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getMilisecond() {
        try {
            return sheredpreferences.getInt("TimerPopupMiliSecond", 1000);
        } catch (Exception e) {
            return 1000;
        }
    }

    public static boolean isCheckBoxSelected() {
        try {
            return isCheckBoxSelected;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean wasCheckBoxChangedStatus() {
        if(wasCheckBoxChangedStatus==isCheckBoxSelected){
            return false;
        }else{
            wasCheckBoxChangedStatus=isCheckBoxSelected;
            return true;
        }

    }

    public static int getTimeInMS() {
        int time = getMilisecond() + 1000 * getSecond() + 1000 * 60 * getMin() + 1000 * 60 * 60 * getHour();
        if (time < 300) {
            timerChangeValue=300;
            return 300;
        }
        timerChangeValue=time;
        return time;
    }
    public static String getTime() {
        int h= getHour();
        int min = getMin();
        int sec = getSecond();
        int milisec=getMilisecond();
        String H="",M="",S="",MS="";
        String sep1="",sep2="",sep3="";
        if(h>0){
            H=""+h+"h ";
        }if(min>0){
            M=""+min+"min ";
        }if(sec>0){
            S=""+sec+"s ";
        }if(milisec>0){
            MS=""+milisec+"ms ";
        }
        return H+sep1+M+sep2+S+sep3+MS;
    }
}