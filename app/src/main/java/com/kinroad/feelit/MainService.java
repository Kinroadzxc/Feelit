package com.kinroad.feelit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Calendar;

public class MainService extends Service {

    public static boolean savedAlarmOn,serviceOn;
    private ControlBinder cBinder = new ControlBinder();

    //控制通道
    class ControlBinder extends Binder {
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

            //指定闹钟时间
            Calendar c = Calendar.getInstance();
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MINUTE, AlarmActivity.savedMinute);
            if (AlarmActivity.savedPA.equals("P.M")) AlarmActivity.savedHour +=12;
            c.set(Calendar.HOUR_OF_DAY, AlarmActivity.savedHour);

            //AlarmManager在指定时间启动Activity
            alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1000*60*60*24, alarmPi);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceOn = false;
    }

}
