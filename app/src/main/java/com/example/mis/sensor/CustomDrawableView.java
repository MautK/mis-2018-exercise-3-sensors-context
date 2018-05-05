package com.example.mis.sensor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;

public class CustomDrawableView extends View {
    private ShapeDrawable mDrawable;
    private double x = 0;
    private double y = 0;

    public CustomDrawableView(Context context) {
        super(context);

        Resources res = getResources();
        //make myImage into a drawable
        Drawable my_imageView1 = res.getDrawable(R.drawable.my_imageview);
        Drawable my_imageView2 = res.getDrawable(R.drawable.my_imageview2);

        int x =10;
        int y = 10;
        int width = 300;
        int height = 50;

        mDrawable = new ShapeDrawable(new OvalShape());
        mDrawable.setBounds(x,y, x + width, y + height);

    }

    public void setX(double newX) {
         x = newX;
    }

    public void setY(double newY) {
        y = newY;
    }

    protected void onDraw(Canvas canvas) {
        mDrawable.draw(canvas);
    }
}
