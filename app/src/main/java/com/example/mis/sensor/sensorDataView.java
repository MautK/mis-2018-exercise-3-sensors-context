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

public class sensorDataView extends View {

    public int width;
    public int height;
    private Bitmap mbitmap;
    private Canvas mcanvas;
    private Path mpath;
    private Paint mpaint;
    private float mX, mY;
    private float startX, startY, stopX, stopY;
    private static final float TOLERANCE = 5;
    Context context;

    public sensorDataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        mpath = new Path();
        mpaint = new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setStyle(Paint.Style.STROKE);
        mpaint.setStrokeJoin(Paint.Join.ROUND);
        mpaint.setStrokeWidth(8f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        mpaint.setColor(Color.DKGRAY);
        canvas.drawLine(0,0, width, height/4, mpaint);
        Log.d(TAG, "onDraw: sensorDataview is active");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mbitmap = Bitmap.createBitmap(w,h/2, Bitmap.Config.ARGB_8888);
    }

}

