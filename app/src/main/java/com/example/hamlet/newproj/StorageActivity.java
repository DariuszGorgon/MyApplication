package com.example.hamlet.newproj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StorageActivity extends AppCompatActivity {
    static RecyclerView recyclerview;
    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WepApp";
    static int numberOfPosition = 0;
    static String[] s ;
    static Animation animHide;
    static Animation animHideDeep;
    static Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_recycle_view_activity);

        File file = new File(path + "/weather.txt");

        s= MyFileClass.LoadFile(file);
        numberOfPosition = s.length;

        context=getApplicationContext();
        recyclerview = (RecyclerView) findViewById(R.id.recylerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL,false)); // zmieni!1

        MyAdapter Adapter = new MyAdapter();
        recyclerview.setAdapter(Adapter);
        animHide = AnimationUtils.loadAnimation(this, R.anim.hide);
        animHideDeep = AnimationUtils.loadAnimation(this, R.anim.hidedeep);
        File fl = new File(path);
        fl.mkdir();

    }

    public void onReturnStorageClick(View view) {
        view.startAnimation(animHide);
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                try {

                    sleep(150);
                    Intent intent = new Intent(StorageActivity.context, WepViewActivity.class);
                    startActivity(intent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread1.start();

    }
    public void onClearDataClick(View view) {

        startActivity(new Intent(this,Clear.class));


    }@Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,WepViewActivity.class);
        startActivity(intent);
        finish();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView SavedData;

        public MyViewHolder(View itemView) {
            super(itemView);
            SavedData = (TextView) itemView.findViewById(R.id.seved_data);
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    public static class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.storage_card_view,parent,false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder,final int position) {

           try {
               holder.SavedData.setText((numberOfPosition-position)+". "+s[numberOfPosition-1-position]); ////////////////////////////////
           }catch (Exception e){
               e.printStackTrace();
           }
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    view.startAnimation(animHideDeep);
                    List<String> stringList = new ArrayList<String>(Arrays.asList(s));
                    stringList.remove(numberOfPosition-1-position);
                    String[] stringArray = stringList.toArray(new String[numberOfPosition-1]);
                    File file = new File(path + "/weather.txt");
                    MyFileClass.ClearFile(file);
                    MyFileClass.SaveFile(file,stringArray);
                    s= MyFileClass.LoadFile(file);
                    numberOfPosition = stringArray.length;
                    Thread thread1 = new Thread() {
                        @Override
                        public void run() {
                            try {

                                sleep(400);
                                Message msg = myHandler.obtainMessage();
                                myHandler.sendMessageAtFrontOfQueue(msg);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread1.start();

                    return true;
                }
            });
        }
        Handler myHandler = new Handler() {
              @Override
              public void handleMessage(Message msg) {
                 super.handleMessage(msg);
                  MyAdapter Adapter = new MyAdapter();
                  recyclerview.setAdapter(Adapter);


                 }
       };

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemCount() {
            return numberOfPosition;
        }
    }
}
