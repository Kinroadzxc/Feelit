package com.kinroad.feelit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class IndexActivity extends Activity implements OnGestureListener {

    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

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

        return true;
    }
}
