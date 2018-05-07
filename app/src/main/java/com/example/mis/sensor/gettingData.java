package com.example.mis.sensor;

import android.util.Log;

import static android.content.ContentValues.TAG;

public class gettingData {

    private float x;
    private float y;


    public gettingData(float value) {
        this.x = 0;
    }

    public float setX() {
        Log.d(TAG, "setX: new X" + x);
        return this.x;

    }

    public float setY() {
        return this.y;
    }

}