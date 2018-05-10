package com.example.mis.sensor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static android.content.ContentValues.TAG;

public class sensorDataView extends DataView {

    public int width;
    public int height;
    private Bitmap mbitmap;
    private Canvas mcanvas;
    private Path mpath;
    private Paint mPaint;
    private Paint xPaint = new Paint();
    private Paint yPaint = new Paint();
    private Paint zPaint = new Paint();
    private Paint magPaint = new Paint();
    Context context;
    private DataView dataView;

    public sensorDataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        mpath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        xPaint.setColor(Color.RED);
        yPaint.setColor(Color.GREEN);
        zPaint.setColor(Color.BLUE);
        magPaint.setColor(Color.WHITE);
        Log.d(TAG, "onDraw: point1X" + wsize);

        for (int i = 0; i < DataArray.size() - 1 ; i++) {
            sensorData dataPoint1 = DataArray.get(i);
            sensorData dataPoint2 = DataArray.get(i + 1);
            float point1X = (float) dataPoint1.getX();
            float point2X = (float) dataPoint2.getX();
            float point1Y = (float) dataPoint1.getY();
            float point2Y = (float) dataPoint2.getY();
            float point1Z = (float) dataPoint1.getZ();
            float point2Z = (float) dataPoint2.getZ();

            //draw line using the function
            drawLine(i, i + 1, point1X, point2X, canvas, xPaint);
            drawLine(i, i + 1, point1Y, point2Y, canvas, yPaint);
            drawLine(i, i + 1, point1Z, point2Z, canvas, zPaint);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mbitmap = Bitmap.createBitmap(w,h/2, Bitmap.Config.ARGB_8888);
    }

}

