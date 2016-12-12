package com.skh.serviceandtimer;


import android.content.ComponentName;

import android.content.Intent;

import android.content.ServiceConnection;

import android.os.Bundle;

import android.os.Handler;

import android.os.IBinder;

import android.os.RemoteException;

import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;

import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView tvCounter;
    private Button btnPlay;
    private Button btnStop;

    private iMyCounterService binder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 서비스가 가진 binder를 리턴 받음
            binder = iMyCounterService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Intent intent;
    private boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCounter = (TextView) findViewById(R.id.tvCounter);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyCounterService.class);
                //startService(intent);
                bindService(intent, connection, BIND_AUTO_CREATE);
                running = true;
                new  Thread(new GetCountThread()).start();
            }
        });
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(connection);
                running = false;
            }
        });
    }

    private class GetCountThread implements Runnable {

        private Handler handler = new Handler();

        @Override
        public void run() {

            while(running) {
                if ( binder == null ) {
                    continue;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tvCounter.setText(binder.getCount() + "");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
