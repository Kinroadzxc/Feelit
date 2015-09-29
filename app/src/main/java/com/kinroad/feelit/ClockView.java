package com.kinroad.feelit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class ClockView extends View {

    private Bitmap back;
    public static int screenWidth,screenHeight,width,heihgt;
    private RectF hour,minute;
    //即时刷新的时、分、上下午
    public static double minuteRadian,hourRadian,setHour,setMinute;
    public static String setPA;

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取绘图区域宽高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        width = (int) (displayMetrics.density * 300);
        heihgt = width;
        hour = new RectF(width/2-15, heihgt*6/16,
                width/2+15, heihgt/2);
        minute = new RectF(width/2-15, heihgt*3/16,
                width/2+15, heihgt/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        //获取时针分针角度
        minuteRadian = setMinute * 6.0;
        hourRadian = setHour * 30.0;

        canvas.drawRoundRect(hour, 20, 20, paint);
        canvas.drawRoundRect(minute, 20, 20, paint);

        Log.d("saved time", AlarmActivity.savedHour + ":" + AlarmActivity.savedMinute + " "+ AlarmActivity.savedPA);
        Log.d("show angle", String.valueOf(hourRadian)+"+"+String.valueOf(minuteRadian));
    }
}
