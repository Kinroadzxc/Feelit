package com.kinroad.feelit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class AlarmActivity extends Activity implements OnGestureListener {

    private GestureDetector detector;
    float xd, yd, xm, ym, xu, yu;
    double gama, gamab, hdegree, hdegreeb;
    int gestureFlag = 0;
    //存储的时、分、上下午
    public static int savedHour, savedMinute;
    public static String savedPA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化手势侦听器
        detector = new GestureDetector(this);

        //读取记录的时间
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        savedHour = pref.getInt("hour", 0);
        savedMinute = pref.getInt("minute", 3);
        savedPA = pref.getString("p_a", "A.M");
        ClockView.setHour = savedHour + savedMinute / 60.0;
        ClockView.setMinute = savedMinute;
        ClockView.setPA = savedPA;

        //配置xml显示
        setContentView(R.layout.activity_alarm);
    }

    @Override//侦测物理按键
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //进入首页
            IndexActivity.start(AlarmActivity.this);

            //切换动画
            overridePendingTransition(R.anim.activity_in_up, R.anim.activity_out_up);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override//侦测触摸事件
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            //按下手指
            case MotionEvent.ACTION_DOWN:

                //记录手指离开位置与表盘中心差值
                xd = event.getX() - ClockView.screenWidth / 2;
                yd = event.getY() - ClockView.screenHeight / 2;

                //判断手指是否在表盘内按下
                double r = Math.sqrt(Math.pow(xd, 2.0) + Math.pow(yd, 2.0));
                if (r < 260) {
                    gestureFlag = 1;
                    initGama();
                } else if (r >= 260 && r <= 480) {
                    gestureFlag = 2;
                    initGama();
                } else gestureFlag = 0;
                break;

            //移动手指
            case MotionEvent.ACTION_MOVE:
                gamab = gama;
                hdegreeb = hdegree;
                xm = event.getX() - ClockView.screenWidth / 2;
                ym = event.getY() - ClockView.screenHeight / 2;

                //如果在表盘内按下
                if (gestureFlag == 1 || gestureFlag == 2) {

                    //计算角度
                    gama = calDegree(xm, ym);

                    switch (gestureFlag) {

                        case 0:
                            break;

                        //时针主动，分针从动
                        case 1:

                            //判断上下午变化
                            if ((gama - gamab) < -330) {
                                ClockView.setPA = apChange(ClockView.setPA);
                            } else if ((gama - gamab) > 330) {
                                ClockView.setPA = apChange(ClockView.setPA);
                            }

                            //设置显示时、分
                            ClockView.setHour = (int) gama / 30;
                            ClockView.setMinute = (int) (2 * gama % 60);
                            Log.d("时间", (int) ClockView.setHour + ":" + (int) ClockView.setMinute + " " + ClockView.setPA);
                            break;

                        //分针主动，时针从动
                        case 2:

                            //计算旋转角
                            if ((gama - gamab) < -330) {
                                hdegree += 360 + gama - gamab;
                            } else if ((gama - gamab) > 330) {
                                hdegree += gama - gamab - 360;
                            } else {
                                hdegree += gama - gamab;
                            }

                            //设置显示时、分
                            ClockView.setMinute = (int) (gama / 6);
                            if (((savedHour + (int) hdegree / 360) / 12) % 2 == 1) {
                                ClockView.setPA = apChange(savedPA);
                            } else ClockView.setPA = savedPA;
                            ClockView.setHour = (savedHour + (int) hdegree / 360) % 12;
                            Log.d("时间", (int) ClockView.setHour + ":" + (int) ClockView.setMinute + " " + ClockView.setPA);
                            break;
                    }
                    break;
                }
                //移开手指
            case MotionEvent.ACTION_UP:

                //记录手指离开位置与表盘中心差值
                xu = event.getX() - ClockView.screenWidth / 2;
                yu = event.getY() - ClockView.screenHeight / 2;

                //存储设置的时间
                savedPA = ClockView.setPA;
                savedHour = (int) ClockView.setHour;
                savedMinute = (int) ClockView.setMinute;
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putInt("hour", savedHour);
                editor.putInt("minute", savedMinute);
                editor.putString("p_a", savedPA);
                editor.apply();
                break;
        }

        return this.detector.onTouchEvent(event);
    }

    //启动此活动
    public static void start(Context context) {
        Intent intent = new Intent(context, AlarmActivity.class);
        context.startActivity(intent);
    }

    @Override//按下
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override//停留一段时间
    public void onShowPress(MotionEvent e) {

    }

    @Override//离开屏幕瞬间
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override//滑动
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override//长按
    public void onLongPress(MotionEvent e) {

    }

    @Override//划过
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (Math.sqrt(Math.pow(xd, 2.0) + Math.pow(yd, 2.0)) > 480) {
            //向上滑
            if (e1.getY() > e2.getY()) {
                //进入首页
                IndexActivity.start(AlarmActivity.this);
                //切换动画
                overridePendingTransition(R.anim.activity_in_up, R.anim.activity_out_up);
                return true;
            }

            //向下滑
            if (e1.getY() < e2.getY()) {
                return true;
            }
        }
        return true;
    }

    //已知与中心距离x,y的角度计算
    private double calDegree(float x, float y) {
        double degree = 0.0;
        if (y < 0) {
            if (x > 0) {
                degree = -Math.atan(x / y) / Math.PI * 180;
            }
            if (x < 0) {
                degree = -Math.atan(x / y) / Math.PI * 180 + 360;
            }
        } else if (y > 0) {
            degree = 180 - Math.atan(x / y) / Math.PI * 180;
        } else {
            if (x > 0) degree = 90;
            if (x < 0) degree = -90;
        }
        return degree;
    }

    //AM&PM变换
    private String apChange(String s) {
        if (s.equals("A.M")) {
            return "P.M";
        } else {
            return "A.M";
        }
    }

    //初始化角度缓存
    private void initGama() {
        hdegree = 0.0;
        hdegreeb = 0.0;
        gama = 0.0;
        gamab = 0.0;
    }
}