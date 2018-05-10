package com.example.mis.sensor;

public class sensorData {
    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;

    void setData(float newX, float newY, float newZ){
        x = newX;
        y = newY;
        z = newZ;
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
}
