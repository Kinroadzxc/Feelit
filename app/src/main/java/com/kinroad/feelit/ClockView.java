package com.kinroad.feelit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class ClockView extends View {

    private Bitmap back;
    public static int screenWidth,screenHeight,width,heihgt,setHour,setMinute;
    private RectF hour,minute;
    //即时刷新的时、分、上下午
    public static double minuteRadian,hourRadian;
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
        hour = new RectF(width/2-15, heihgt*5/16,
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
        hourRadian = (setHour+setMinute/60.0) * 30.0;

        //绘制分针
        canvas.save();
        canvas.rotate((float) minuteRadian, heihgt / 2, width / 2);
        canvas.drawRoundRect(minute, 20, 20, paint);
        canvas.restore();

        //绘制时针
        canvas.save();
        canvas.rotate((float) hourRadian, heihgt / 2, width / 2);
        canvas.drawRoundRect(hour, 20, 20, paint);
        canvas.restore();
    }
}
