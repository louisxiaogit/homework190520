package com.example.Exercise.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomAnalogClock extends View {

    private Paint mPaint;
    private Paint mPaintNum;
    private Paint mPaintHour;
    private Paint mPaintMinute;
    private Paint mPaintSecond;
    private float mX, mY;
    private int mR;

    public CustomAnalogClock(Context context) {
        super(context);
        initPaint();
    }

    public CustomAnalogClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CustomAnalogClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }


    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);

        mPaintNum = new Paint();
        mPaintNum.setColor(Color.BLACK);
        mPaintNum.setAntiAlias(true);
        mPaintNum.setTextSize(35);
        mPaintNum.setStyle(Paint.Style.STROKE);
        mPaintNum.setTextAlign(Paint.Align.CENTER);

        mPaintSecond = new Paint();
        mPaintSecond.setColor(Color.RED);
        mPaintSecond.setAntiAlias(true);
        mPaintSecond.setStrokeWidth(5);
        mPaintSecond.setStyle(Paint.Style.FILL);

        mPaintMinute = new Paint();
        mPaintMinute.setColor(Color.BLACK);
        mPaintMinute.setAntiAlias(true);
        mPaintMinute.setStrokeWidth(8);
        mPaintMinute.setStyle(Paint.Style.FILL);

        mPaintHour = new Paint();
        mPaintHour.setColor(Color.BLACK);
        mPaintHour.setAntiAlias(true);
        mPaintHour.setStrokeWidth(13);
        mPaintHour.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        mX = width / 2;
        mY = height / 2;
        mR = (int) mX - 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mX, mY, mR, mPaint);
        canvas.drawCircle(mX, mY, 15, mPaintMinute);
        drawLines(canvas);
        drawText(canvas);

        try {
            initCurrentTime(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        postInvalidateDelayed(1000);
    }

    private void drawLines(Canvas canvas) {
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                mPaint.setStrokeWidth(8);
                canvas.drawLine(mX, mY - mR, mX, mY - mR + 40, mPaint);
            } else {
                mPaint.setStrokeWidth(3);
                canvas.drawLine(mX, mY - mR, mX, mY - mR + 30, mPaint);
            }
            canvas.rotate(6, mX, mY);
        }
    }

    private void drawText(Canvas canvas) {
        float textSize = (mPaintNum.getFontMetrics().bottom - mPaintNum.getFontMetrics().top);
        int distance = mR - 40 - 20;
        float a, b;
        for (int i = 0; i < 12; i++) {
            a = (float) (distance * Math.sin(i * 30 * Math.PI / 180) + mX);
            b = (float) (mY - distance * Math.cos(i * 30 * Math.PI / 180));
            if (i == 0) {
                canvas.drawText("12", a, b + textSize / 3, mPaintNum);
            } else {
                canvas.drawText(String.valueOf(i), a, b + textSize / 3, mPaintNum);
            }
        }
    }

    private void initCurrentTime(Canvas canvas) {
        SimpleDateFormat format = new SimpleDateFormat("HH-mm-ss");
        String time = format.format(new Date(System.currentTimeMillis()));
        String[] split = time.split("-");
        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);
        int second = Integer.parseInt(split[2]);
        int hourAngle = hour * 30 + minute / 2;
        int minuteAngle = minute * 6 + second / 10;
        int secondAngle = second * 6;

        canvas.rotate(hourAngle, mX, mY);
        canvas.drawLine(mX, mY, mX, mY - mR + 150, mPaintHour);
        canvas.save();
        canvas.restore();
        canvas.rotate(-hourAngle, mX, mY);

        canvas.rotate(minuteAngle, mX, mY);
        canvas.drawLine(mX, mY, mX, mY - mR + 60, mPaintMinute);
        canvas.save();
        canvas.restore();

        canvas.rotate(-minuteAngle, mX, mY);

        canvas.rotate(secondAngle, mX, mY);
        canvas.drawLine(mX, mY, mX, mY - mR + 20, mPaintSecond);
    }

}
