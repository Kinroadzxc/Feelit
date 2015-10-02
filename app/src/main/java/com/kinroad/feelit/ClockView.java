package com.kinroad.feelit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ClockView extends View {

    public static int setHour,setMinute;
    //即时刷新的时、分、上下午
    public static float centerX,centerY;
    public static double minuteRadian,hourRadian;
    public static String setPA;

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取中心区域宽高
        centerX = AlarmActivity.screenWidth*409/800;
        centerY = (float) (AlarmActivity.screenHeight*453.5/1280);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //载入时针分针图像
        Bitmap bitmapHour = BitmapFactory.decodeResource(this.getResources(), R.drawable.hour);
        Bitmap bitmapminute = BitmapFactory.decodeResource(this.getResources(), R.drawable.minute);

        //载入画笔
        Paint paint = new Paint();
        canvas.translate(centerX,centerY);

        //获取时针分针角度
        minuteRadian = setMinute * 6.0;
        hourRadian = (setHour+setMinute/60.0) * 30.0;

        //绘制时针
        canvas.save();
        canvas.scale((float) (AlarmActivity.screenHeight / 1280.0 / AlarmActivity.screenScale), (float) (AlarmActivity.screenHeight / 1280.0 / AlarmActivity.screenScale));
        canvas.rotate((float) hourRadian, 0, 0);
        canvas.drawBitmap(bitmapHour, (float) (-40.5*AlarmActivity.screenScale), -112*AlarmActivity.screenScale, paint);
        canvas.restore();

        //绘制分针
        canvas.save();
        canvas.scale((float) (AlarmActivity.screenHeight / 1280.0 / 3), (float) (AlarmActivity.screenHeight / 1280.0 / 3));
        canvas.rotate((float) minuteRadian, 0, 0);
        canvas.drawBitmap(bitmapminute, -60*AlarmActivity.screenScale, -175*AlarmActivity.screenScale, paint);
        canvas.restore();

    }
}
