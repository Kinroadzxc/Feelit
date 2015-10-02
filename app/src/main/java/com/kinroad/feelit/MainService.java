package com.kinroad.feelit;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MainService extends Service {

    public static boolean isRunning;
    private ControlBinder cBinder = new ControlBinder();
    public MainService() {
    }

    //控制通道
    class ControlBinder extends Binder {
        //关闭服务
        public void closeService() {
            stopSelf();
            Log.d("服务停止", "成功");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return cBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        Log.d("创建服务", "成功");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        Log.d("关闭服务", "成功");
    }


}
