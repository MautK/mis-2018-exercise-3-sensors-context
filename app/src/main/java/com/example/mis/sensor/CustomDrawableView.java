package com.example.mis.sensor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CustomDrawableView extends View {
    private ShapeDrawable mDrawable;

    ArrayList<gettingData> receivedData;

    //add the senses data into an array
    public void addData(gettingData data) {
        receivedData.add(data);
    }



    public CustomDrawableView(Context context) {
        super(context);

        Resources res = getResources();
        //make myImage into a drawable
        Drawable my_imageView1 = res.getDrawable(R.drawable.my_imageview);
        Drawable my_imageView2 = res.getDrawable(R.drawable.my_imageview2);

        int x = 10;
        int y = 10;
        int width = 300;
        int height = 50;


        mDrawable = new ShapeDrawable(new OvalShape());
        mDrawable.setBounds(x, y, x + width, y + height);

    }

}

//class for retrieving the sensed data as doubles
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
         return this.x;
        Log.d(TAG, "setX: new X" + x);
    }

    public double setY() {
        return this.y;
    }

}
