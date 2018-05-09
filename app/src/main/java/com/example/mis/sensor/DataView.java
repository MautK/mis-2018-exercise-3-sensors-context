package com.example.mis.sensor;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class DataView extends View {
    ArrayList<sensorData> DataArray;
    int wsize = 128;

    public DataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        DataArray = new ArrayList<>(wsize);
    }
//
//    @Override
//    public OnDraw(Canvas canvas){
//
//    }
    void addSensorData() {

    }

    void removeSensorData() {

    }
}
