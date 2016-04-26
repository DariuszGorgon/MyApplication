package com.example.hamlet.newproj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class StorageActivity extends AppCompatActivity {
    RecyclerView recyclerview;
    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WepApp";

    static int numberOfPosition = 0;
    static String[] s ;
    Animation animHide;
    static Context context;
    Animation animRotate;
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
        animRotate  = AnimationUtils.loadAnimation(this, R.anim.rotate);

    }

    public void onReturnStorageClick(View view) {
        view.startAnimation(animHide);
        view.startAnimation(animRotate);
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
        view.startAnimation(animHide);
        File file = new File(path + "/weather.txt");
        MyFileClass.ClearFile(file);
        s= MyFileClass.LoadFile(file);

        numberOfPosition = s.length;

        MyAdapter Adapter = new MyAdapter();
        recyclerview.setAdapter(Adapter);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView SavedData;

        public MyViewHolder(View itemView) {
            super(itemView);
            SavedData = (TextView) itemView.findViewById(R.id.seved_data);

    }
    }


    public static class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.storage_card_view,parent,false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

           try {
               holder.SavedData.setText((position+1)+". "+s[position]); ////////////////////////////////
           }catch (Exception e){
               e.printStackTrace();
           }
        }

        @Override
        public int getItemCount() {
            return numberOfPosition;
        }
    }
}
