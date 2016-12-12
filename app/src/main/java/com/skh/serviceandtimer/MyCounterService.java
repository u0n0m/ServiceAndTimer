package com.skh.serviceandtimer;
import android.app.Service;

import android.content.Intent;

import android.os.Handler;

import android.os.IBinder;

import android.os.RemoteException;

import android.util.Log;

import android.widget.Toast;


public class MyCounterService extends Service {

    public MyCounterService() {
    }

    private int count;

    /**
     * Service와 Activity가 통신하기 위한 바인더
     * Activity에게 getCount() 메소드를 제공해 Service의 Count 값을 전달
     */
    iMyCounterService.Stub binder = new iMyCounterService.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int getCount() throws RemoteException {
            return count;
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();

        Thread counter = new Thread(new Counter());
        counter.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isStop = true;
        return super.onUnbind(intent);
    }

    private boolean isStop;

    /**
     * Stop Service가 실행될 때 호출
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        isStop = true;
    }

    private class Counter implements Runnable {


        private Handler handler = new Handler();

        @Override
        public void run() {

            for ( count = 0; count < 50; count++ ) {

                if ( isStop ) {
                    break;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), count + "", Toast.LENGTH_SHORT).show();
                        Log.d("COUNT", count + "");
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "서비스 종료", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
