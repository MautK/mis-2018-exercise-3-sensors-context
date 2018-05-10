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
    private Paint mpaint;
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
        mpaint.setColor(Color.RED);

        for (int i = 0; i < wsize - 1 ; i++) {
            sensorData dataPoint1 = this.DataArray.get(wsize-this.DataArray.size() + i);
            sensorData dataPoint2 = this.DataArray.get(wsize + 1 -this.DataArray.size() + i);
            double point1X = (dataPoint1.getX() / 2 );
            double point2X = (dataPoint2.getX() / 2 );
            double point1Y = (dataPoint1.getY() / 2 );
            double point2Y = (dataPoint2.getY() / 2 );

             float xAxes1 = ( width / wsize ) + i;
             float xAxes2 = ( width / wsize ) + i;


            //draw line using the function
            drawLine();
            //draw line for y values
            canvas.drawPath(xAxes1, point1Y, xAxes2, point2Y, mpaint);
            //draw line for x values
            canvas.drawPath(i, point1X, i + 1, point2X, mpaint);

        }


        canvas.drawLine(0,0, width, height/4, mpaint);
        Log.d(TAG, "onDraw: sensorDataview is active");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mbitmap = Bitmap.createBitmap(w,h/2, Bitmap.Config.ARGB_8888);
    }

}

