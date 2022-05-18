package com.example.onthread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                printLog("A");
            }
        });

        Thread thread2=new Thread(new Runnable() {
            @Override
            public void run() {
                printLog("B");
            }
        });

        thread1.start();
        thread2.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", thread1.getState().toString());
                Log.d("TAG", thread2.getState().toString());
            }
        },1000);
    }

    private synchronized void printLog(String b) {
        for(int i=0;i<100;i++)
        {
            Log.d("BBB", b+": "+i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}