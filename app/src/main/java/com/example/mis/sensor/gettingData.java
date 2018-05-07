package com.example.mis.sensor;

import android.util.Log;

import static android.content.ContentValues.TAG;

public class gettingData {

    private double x;
    private double y;

    public gettingData(double x, double y, double z) {
        this.x = 0;
        this.y = 0;
    }

    public gettingData(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double setX() {
        Log.d(TAG, "setX: new X" + x);
        return this.x;

    }

    public double setY() {
        return this.y;
    }

}