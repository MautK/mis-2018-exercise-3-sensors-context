package com.example.mis.sensor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ffftDataView extends sensorDataView {

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

    public ffftDataView(Context context, @Nullable AttributeSet attrs) {
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
        mpaint.setColor(Color.DKGRAY);
        canvas.drawLine(0,height/2, width, height, mpaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mbitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
    }
}
