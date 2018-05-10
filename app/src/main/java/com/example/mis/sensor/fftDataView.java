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

import java.util.Arrays;

import static android.content.ContentValues.TAG;
import static android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE;

public class fftDataView extends DataView {
    private Bitmap fbitmap;
    Context context;

    FFT mFFT;
    double[] x = new double[wsize];
    double[] y = new double[wsize];

    public fftDataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mFFT = new FFT(wsize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        x = new double[wsize];
        y = new double[wsize];
        Arrays.fill(y, 0.0d);

        for (int i = 0; i < DataArray.size() - 1; i++) {
            x[i] = calcMagnitude(DataArray.get(i));
        }

        mFFT.fft(x, y);
        for (int i = 0; i < wsize / 2; i++) {
            y[i] = Math.sqrt(Math.pow(x[i], 2) - Math.pow(y[i], 2));
        }
        Paint newPaint = new Paint();
        newPaint.setColor(Color.YELLOW);
        newPaint.setStrokeWidth(3.0f);
        for (int i = 0; i < wsize / 2 - 1; i++) {
            drawLine(i, i + 1,
                    (float) y[i], (float) y[i + 1],
                    canvas, newPaint);
        }
    }

    double calcAvg() {
        double sum = 0;
        for (int i = 0; i < wsize / 2 - 1; i++) {
            sum += x[i];
        }
        return Math.round((sum/(wsize/2))*1000)/1000;
    }

    @Override
    void drawLine(int firstPointX, int secondPointX,
                  float firstPointY, float secondPointY,
                  Canvas canvas, Paint mPaint) {
        float firstX = firstPointX * canvas.getWidth() / (wsize/2);
        float secondX = secondPointX * canvas.getWidth() / (wsize/2);
        float firstY = canvas.getHeight() - (canvas.getHeight() / 100) * firstPointY;
        float secondY = canvas.getHeight() - (canvas.getHeight() / 100) * secondPointY;
        canvas.drawLine(firstX, firstY, Math.abs(secondX), Math.abs(secondY), mPaint);
    }
    @Override
    public void removeSensorData(){
        super.removeSensorData();
        x = new double[wsize];
        y = new double[wsize];
        mFFT = new FFT(wsize);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        fbitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
    }
}
