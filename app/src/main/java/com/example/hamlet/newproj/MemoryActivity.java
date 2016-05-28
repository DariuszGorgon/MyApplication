package com.example.hamlet.newproj;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemoryActivity extends AppCompatActivity {
    static RecyclerView recyclerview;
    static int numberOfPosition = 0;
    static String[] s ;
    static Animation animHide;
    static Animation animHideDeep;
    static Context context;
    //==============================================================================================
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory_recycle_view_activity);

        File file = new File(SensorViewActivity.path + "/weather.txt");
        s= MyFileClass.LoadFile(file);
        numberOfPosition = s.length;

        context=getApplicationContext();

        recyclerview = (RecyclerView) findViewById(R.id.recylerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL,false)); // zmieni!1
        MyAdapter Adapter = new MyAdapter();
        recyclerview.setAdapter(Adapter);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.memory_activity_id);
        rl.setOnTouchListener(new OnSwipeTouchListener(MemoryActivity.this) {
            @Override
            public void onSwipeRight() {
                gotoSensorViewActivity();
            }
            @Override
            public void onSwipeBottom() {
                moveTaskToBack(true);
            }

            @Override
            public void onSwipeLeft(){
                gotoConnectActivity();
            }

        });


        animHide = AnimationUtils.loadAnimation(this, R.anim.hide);
        animHideDeep = AnimationUtils.loadAnimation(this, R.anim.hidedeep);


    }
    @Override
    protected void onResume() {
        super.onResume();
        MyAdapter Adapter = new MyAdapter();
        recyclerview.setAdapter(Adapter);

    }


    @Override
    protected void onPause() {
        super.onPause();
        MyAdapter Adapter = new MyAdapter();
        recyclerview.setAdapter(Adapter);


    }
    //==============================================================================================
    public void onReturnStorageClick(View view) {
        view.startAnimation(animHide);
        new SlideAtivityAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public void onClearDataClick(View view) {

        startActivity(new Intent(this,ClearPopup.class));

    }@Override
    public void onBackPressed()
    {
        new SlideAtivityAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    public  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView SavedData;

        public MyViewHolder(View itemView) {
            super(itemView);

            SavedData = (TextView) itemView.findViewById(R.id.seved_data);
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.memory_card_view,parent,false);
            MyViewHolder myViewHolder = new MyViewHolder(view);


            return myViewHolder;
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder,final int position) {
           try {
               holder.SavedData.setText((numberOfPosition-position)+". "+s[numberOfPosition-1-position]);
           }catch (Exception e) {
               e.printStackTrace();
           }
            //====================usuwanieElementówZPamieci==============================
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    try {
                        view.startAnimation(animHideDeep);
                        //========mielenie danych radze nie wnikać============
                        List<String> stringList = new ArrayList<String>(Arrays.asList(s));
                        stringList.remove(numberOfPosition - 1 - position);
                        String[] stringArray = stringList.toArray(new String[numberOfPosition - 1]);
                        File file = new File(SensorViewActivity.path + "/weather.txt");
                        MyFileClass.ClearFile(file);
                        MyFileClass.SaveFile(file, stringArray);
                        s = MyFileClass.LoadFile(file);
                        numberOfPosition = stringArray.length;
                        //=====================================================
                        Thread thread1 = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    /////////opuznienie odswieżania dla celu Animacji/////////////////
                                    sleep(400);
                                    Message msg = myHandler.obtainMessage();
                                    myHandler.sendMessageAtFrontOfQueue(msg);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread1.start();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        }
        Handler myHandler = new Handler() {
              @Override
              public void handleMessage(Message msg) {
                 super.handleMessage(msg);
                  MyAdapter Adapter = new MyAdapter();
                  MemoryActivity.recyclerview.setAdapter(Adapter);
                 }
       };
    //==============================================================================================
        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemCount() {
            return numberOfPosition;
        }
    }//==============================================================================================
    private class SlideAtivityAsyncTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            int k=0;
            try {
                Thread.sleep(20);
                publishProgress(k);
            } catch (Exception e) {
                Log.d("gotosensoractiviy", "Error: " + e.toString());
            }
            return k;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Intent intent= new Intent(MemoryActivity.this, SensorViewActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }
    } private void gotoConnectActivity() {
        Intent intent = new Intent(MemoryActivity.this, ConnectActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();

    }
    private void gotoSensorViewActivity() {
        Intent intent= new Intent(MemoryActivity.context, SensorViewActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();

    }
}
