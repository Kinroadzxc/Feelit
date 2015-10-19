package com.kinroad.feelit.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.kinroad.feelit.Activity.AlarmActivity;
import com.kinroad.feelit.Activity.WakeupActivity;

import java.util.Calendar;

public class MainService extends Service {

    public static boolean savedAlarmOn,serviceOn;
    public ControlBinder cBinder = new ControlBinder();
    public static long triggerAtTime;

    //控制通道
    public class ControlBinder extends Binder {
        //关闭服务
        public void closeService() {
            stopSelf();
            serviceOn = false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return cBinder;
    }

    //启动此服务
    public static void start(Context context) {
        Intent intent = new Intent(context, MainService.class);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serviceOn = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //初始化闹钟
        if (savedAlarmOn) {
            AlarmManager alarmmanager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(this, WakeupActivity.class);
            PendingIntent alarmPi = PendingIntent.getActivity(this, 0, alarmIntent, 0);

            //指定下次闹钟时间
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MINUTE, AlarmActivity.savedMinute);
            if (AlarmActivity.savedPA.equals("P.M")) AlarmActivity.savedHour +=12;
            c.set(Calendar.HOUR_OF_DAY, AlarmActivity.savedHour);

            triggerAtTime = c.getTimeInMillis();
            final long LATERTIME = 1000*60*60*24;
            if (triggerAtTime <= System.currentTimeMillis()) {
                triggerAtTime += LATERTIME;
            }

            //AlarmManager在指定时间启动Activity
            alarmmanager.set(AlarmManager.RTC_WAKEUP, triggerAtTime, alarmPi);
//            Log.d("现在系统时间", String.valueOf(System.currentTimeMillis()));
//            Log.d("闹钟设定时间", String.valueOf(triggerAtTime));
//            Log.d("闹钟设置", "已重新设置");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceOn = false;
    }

}
