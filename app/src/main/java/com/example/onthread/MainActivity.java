package com.example.onthread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    int a, b, c;
    private final String TAG = "BBB";
    MyFlag mMyFlag;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        a = b = c = 0;
        mMyFlag = new MyFlag(0);
        mHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
            }
        };

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mMyFlag) {
                    for (int i = 0; i < 10;) {
                        if (mMyFlag.count == 0) {
                            a = i;
                            Message message=new Message();
                            mHandler.handleMessage(message);
                            Log.d(TAG, "A = " + a);
                            mMyFlag.count=1;
                            //danh thuc tat ca cac thread dang wait cai flag nay
                            mMyFlag.notifyAll();
                            //i chi tang khi nhay vao truong hop if
                            i++;
                        }
                        else
                        {
                            try {
                                //khong dung trang thai roi vao trang thai wait
                                mMyFlag.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mMyFlag) {
                    for (int i = 0; i < 10; ) {
                        if (mMyFlag.count == 1) {
                            b = i;
                            Log.d(TAG, "B = " + b);
                            mMyFlag.count=2;
                            mMyFlag.notifyAll();
                            //i chi tang khi nhay vao truong hop if
                            i++;
                        }
                        else
                        {
                            try {
                                mMyFlag.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mMyFlag) {
                    for (int i = 0; i < 10;) {
                        if (mMyFlag.count == 2) {
                            c = a + b;
                            Log.d(TAG, "C = " + c);
                            mMyFlag.count=0;
                            mMyFlag.notifyAll();
                            //i chi tang khi nhay vao truong hop if
                            i++;
                        }
                        else
                        {
                            try {
                                mMyFlag.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

    }

}