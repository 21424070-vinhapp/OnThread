package com.example.onthread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    }

    private void printLog(String b) {
        for(int i=0;i<10;i++)
        {
            Log.d("BBB", b+": "+i);
        }
    }
}