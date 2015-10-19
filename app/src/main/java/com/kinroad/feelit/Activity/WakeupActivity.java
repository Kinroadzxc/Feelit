package com.kinroad.feelit.Activity;

import android.app.Activity;
import android.app.Service;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.kinroad.feelit.R;
import com.kinroad.feelit.Service.MainService;

public class WakeupActivity extends Activity implements OnGestureListener{

    private Vibrator vibrator;
    private GestureDetector detector;
    MediaPlayer alarmRing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置下一次闹钟
        MainService.start(WakeupActivity.this);

        //激活屏幕
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_wakeup);

        //初始化手势侦听器
        detector = new GestureDetector(this);

        //播放铃声
        alarmRing = MediaPlayer.create(this, R.raw.alarm_ring);
        alarmRing.setLooping(true);
        alarmRing.start();

        //设置震动
        vibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
        long[] way = {1000,700,1000,700};
        vibrator.vibrate(way, 2);
    }

    @Override//侦测物理按键
    public boolean onKeyDown(int keyCode, KeyEvent event) {


//        switch (keyCode){
//            case KeyEvent.KEYCODE_BACK:
//                break;
//            case KeyEvent.KEYCODE_HOME:
//                break;
//            case KeyEvent.KEYCODE_MENU:
//                break;
//        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                onDestroy();
        }
        return this.detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alarmRing.stop();
        vibrator.cancel();
    }
}
