package com.example.mis.sensor;

import java.lang.Math;

public class sensorData {
    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;
    private double magnitude = 0.0;

    void setData(float newX, float newY, float newZ){
        x = newX;
        y = newY;
        z = newZ;
        calculateMagnitude();
    }

    double getX(){
        return x;
    }
    double getY(){
        return y;
    }
    double getZ(){
        return z;
    }
    double getMagnitude(){
        return magnitude;
    }

    void calculateMagnitude() {
        magnitude = Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z,2)));
    }
}
