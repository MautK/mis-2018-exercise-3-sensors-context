package com.example.mis.sensor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class DataView extends View {
    ArrayList<sensorData> DataArray;
    int wsize = 64;

    public DataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        DataArray = new ArrayList<>(wsize);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }
    void addSensorData(sensorData newSensorData) {
        DataArray.add(newSensorData);
        if (DataArray.size() > wsize) {
            popDataArray();
        }
    }
    void popDataArray() {
        DataArray.remove(0);
    }

    void setWindowSize(int newWindowSize) {
        wsize = newWindowSize;
    }
    ArrayList<sensorData> sliceDataArray(ArrayList<sensorData> oldArr, int start, int end) {
        ArrayList<sensorData> tempArr = new ArrayList<>(wsize);
        if (end > wsize) {
            for (int i = 0; i < end - wsize; i++) {
                tempArr.add(oldArr.get(start + i));
            }
        } else {
            for (int i = 0; i < wsize; i++) {
                if (i < wsize - end) {
                    tempArr.add(new sensorData());
                } else {
                    tempArr.add(oldArr.get(i-(wsize - end)));
                }
            }
        }
        return tempArr;
    }
    void resizeDataArray(int newWindowSize) {
        setWindowSize(newWindowSize);
        ArrayList<sensorData> tempArr = sliceDataArray(DataArray, wsize, DataArray.size());
        removeSensorData();
        DataArray = tempArr;
    }
    void removeSensorData() {
        DataArray.clear();
    }
    float calcMagnitude (sensorData data) {
        double magnitude = Math.sqrt((
                Math.pow(data.getX(), 2) +
                Math.pow(data.getY(), 2) +
                Math.pow(data.getZ(), 2))
        );
        return (float) magnitude;
    }
    void drawLine(int firstPointX, int secondPointX,
                  float firstPointY, float secondPointY,
                  Canvas canvas, Paint mPaint) {
        float firstX = firstPointX * canvas.getWidth() / wsize;
        float secondX = secondPointX * canvas.getWidth() / wsize;
        float firstY = canvas.getHeight() / 2 - canvas.getHeight() / 2 / 100 * firstPointY;
        float secondY = canvas.getHeight() / 2 - canvas.getHeight() / 2 / 100 * secondPointY;
        canvas.drawLine(firstX, firstY, secondX, secondY, mPaint);
    }
}
