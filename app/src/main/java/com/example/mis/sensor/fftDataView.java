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
import android.view.View;

import static android.content.ContentValues.TAG;

public class fftDataView extends DataView {

    public int width;
    public int height;
    private Bitmap fbitmap;
    private Canvas mcanvas;
    private Path mpath;
    private Paint mpaint;
    Context context;

    public fftDataView(Context context, @Nullable AttributeSet attrs) {
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
//        mpaint.setColor(Color.DKGRAY);
//        width = canvas.getWidth();
//        height = canvas.getHeight();
//        float startX;
//        float stopX;
//        float startY;
//        float stopY;
//        Paint mPaint;
//
//        for (int i = 0; i < wsize - 1; i++) {
//            sensorData dataStart = DataArray.get(i);
//            sensorData dataStop = DataArray.get(i + 1);
////            drawLine(startX, stopX, startY, stopY, mPaint);
//
//        }
//        canvas.drawLine(0,height/2, width, height, mpaint);
//        Log.d(TAG, "onDraw: fftDataView is active");

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        fbitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
    }
}
