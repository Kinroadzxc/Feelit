package com.kinroad.feelit;

import android.app.Activity;
import android.app.Service;

import java.util.ArrayList;
import java.util.List;

public class AppController {

    public static List<Activity> activities = new ArrayList<Activity>();

    //添加活动至app控制器
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    //从app控制器移除活动
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    //关闭所有正在运行的活动
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    //关闭服务
    public static void stopService(Service service){
        service.stopSelf();
    }
}
