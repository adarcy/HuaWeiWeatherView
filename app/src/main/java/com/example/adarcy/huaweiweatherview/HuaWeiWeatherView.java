package com.example.adarcy.huaweiweatherview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by likun on 2017/8/29.
 */

public class HuaWeiWeatherView extends View {


    private RectF oval;
    private float startAngle = 120;
    private float sweepAngle = 300;
    private float targetAngle = 200;
    private float radius;
    private Paint paint;
    private boolean isRunning;//判断是否在动
    private int state = 1;//判断前进还是后退状态
    private int red;
    private int green;
    private int score;

    public void changeAngle(final float trueAngle){
        if (isRunning){
            return;
        }
        final Timer timer  = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                switch (state){
                    case 1://后退
                        targetAngle -= 3;
                        if (targetAngle <= 0){
                            targetAngle = 0;
                            state = 2;
                        }
                        break;
                    case 2:
                        targetAngle += 3;
                        if (targetAngle >= trueAngle){
                            targetAngle = trueAngle;
                            state = 1;
                            isRunning = false;
                            timer.cancel();
                        }
                        break;
                }
                score = (int) ((targetAngle/sweepAngle)*100);
                postInvalidate();
            }
        },500,30);
    }

    public HuaWeiWeatherView(Context context) {
        super(context);
    }

    public HuaWeiWeatherView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width  = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int len = Math.min(width, height);
        radius = len/2;
        oval = new RectF(0, 0, len, len);
        setMeasuredDimension(len,len);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(oval,startAngle,sweepAngle,false,paint);
        drawLine(canvas);
        drawScoreText(canvas);
    }

    /**
     * 画分数和小圆圈
     * @param canvas
     */
    private void drawScoreText(Canvas canvas) {
        Paint smallPaint = new Paint();
        smallPaint.setARGB(100,red,green,0);
        float smallRadius = radius - 60;
        canvas.drawCircle(radius,radius,smallRadius,smallPaint);

        Paint textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(smallRadius/2);
        canvas.drawText(""+ score,radius,radius,textPaint);

        textPaint.setTextSize(smallRadius/6);
        canvas.drawText("分",radius+smallRadius/2,radius-smallRadius/4,textPaint);
        canvas.drawText("点击优化",radius,radius + smallRadius/2,textPaint);
    }

    /**
     * 画刻度
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        canvas.save();
        canvas.translate(radius,radius);
        canvas.rotate(30);
        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(2);

        Paint targetPaint = new Paint();
        targetPaint.setAntiAlias(true);
        targetPaint.setColor(Color.GREEN);
        targetPaint.setStrokeWidth(2);

        float rotateAngle = sweepAngle/100;

        float hasDraw = 0;
        for (int i = 0; i <= 100; i++) {
            if (hasDraw <= targetAngle && targetAngle != 0){
                float percent = hasDraw/sweepAngle;
                red = 255 - (int) (255*percent);
                green = (int) (255*percent);
                if (onAngleColorListener != null){
                    onAngleColorListener.setAngleColor(red, green);
                }
                targetPaint.setARGB(255, red, green,0);
                canvas.drawLine(0,radius,0,radius-40,targetPaint);
            }else {
                canvas.drawLine(0,radius,0,radius-40,linePaint);
            }
            hasDraw += rotateAngle;
            canvas.rotate(rotateAngle);
        }
        canvas.restore();
    }


    public OnAngleColorListener onAngleColorListener;

    /**
     * 背景颜色变化的回调接口
     */
    public interface OnAngleColorListener{
        void setAngleColor(int red, int green);
    }

    public void setAngleColorListener(OnAngleColorListener onAngleColorListener){
        this.onAngleColorListener = onAngleColorListener;
    }
}
