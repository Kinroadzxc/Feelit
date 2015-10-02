package com.kinroad.feelit;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class IndexActivity extends Activity implements OnGestureListener {

    private GestureDetector detector;

    //设置与service的连接
    private MainService.ControlBinder controlBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            controlBinder = (MainService.ControlBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        //绑定服务
        Intent bindIntent = new Intent(this, MainService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
        Log.d("绑定服务", "成功");

        //加入AppController列表
        AppController.addActivity(this);

        //初始化手势侦听器
        detector = new GestureDetector(this);
    }

    //启动此活动
    public static void start(Context context) {
        Intent intent = new Intent(context, IndexActivity.class);
        context.startActivity(intent);
    }

    @Override//侦测触摸事件
    public boolean onTouchEvent(MotionEvent event) {
        return this.detector.onTouchEvent(event);
    }

    @Override//手指触到触摸屏瞬间
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override//手指按在屏幕上一小段时间
    public void onShowPress(MotionEvent e) {

    }

    @Override//手指离开屏幕瞬间
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override//手指在屏幕滑动
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override//手指持续按在屏幕上
    public void onLongPress(MotionEvent e) {

    }

    @Override//手指在屏幕迅速划过
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        //向下滑
        if (e1.getY() < e2.getY()) {

            //创建SharedPrefference存储以记录时间
            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
            editor.apply();

            //进入闹钟页面
            AlarmActivity.start(IndexActivity.this);

            //切换动画
            overridePendingTransition(R.anim.activity_in_down, R.anim.activity_out_down);
            return true;
        }

        //向上滑
        if (e1.getY() > e2.getY()) {
            //关闭服务
            if (MainService.isRunning) {
                controlBinder.closeService();
            }
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //关闭所有开启的activity
        AppController.finishAll();

        //与服务解除绑定
        unbindService(connection);
        Log.d("解除绑定", "成功");
    }
}
