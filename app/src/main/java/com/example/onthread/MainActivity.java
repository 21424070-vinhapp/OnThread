package com.example.onthread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int a, b, c;
    private final String TAG = "BBB";
    MyFlag mMyFlag;
    Handler mHandler;
    TextView mTxtA,mTxtB,mTxtC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initItem();

        a = b = c = 0;
        mMyFlag = new MyFlag(0);
        mHandler=new Handler(Looper.myLooper()){
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:
                        mTxtA.setText("A: "+msg.obj);
                        break;
                    case 1:
                        mTxtB.setText("B: "+msg.obj);
                        break;
                    case 2:
                        mTxtC.setText("C: "+msg.obj);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + msg.what);
                }
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
                            message.what=0;
                            message.obj=a;
                            mHandler.handleMessage(message);
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

                            Message message=new Message();
                            message.what=1;
                            message.obj=b;
                            mHandler.handleMessage(message);
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

                            Message message=new Message();
                            message.what=2;
                            message.obj=c;
                            mHandler.handleMessage(message);
                            mMyFlag.count=0;
                            mMyFlag.notifyAll();

                            //i chi tang khi nhay vao truong hop if

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



    private void initItem() {
        mTxtA=findViewById(R.id.txtA);
        mTxtB=findViewById(R.id.txtB);
        mTxtC=findViewById(R.id.txtC);
    }

}