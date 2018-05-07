package com.example.mis.sensor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    public int sumPlots = 64;


    //add the senses data from mainActivity into an array
    public void addDataX(gettingData data) {
        receivedData.add(data);
    }

    public void addDataY(gettingData data) {
        receivedData.add(data);
    }

    //connect subsequent points in the array list of gettingData
    public void drawLine(int i, float a, float b, Canvas canvas, int color){
        Paint p = new Paint();
        p.setColor(color);
    }

    //draw the lines
    protected void onDraw(Canvas canvas) {
        receivedData = new ArrayList<>(sumPlots);
        for (int i = 1; i < sumPlots-1; ++i) {
            gettingData data1 = this.receivedData.get(this.receivedData.size() - i);
            gettingData data2 = this.receivedData.get(this.receivedData.size() - 1 -i);
            drawLine(i,  data1.setX(), data2.setX(), canvas, Color.RED);
            drawLine(i, data1.setY(), data2.setY(), canvas, Color.BLUE);
        }

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


