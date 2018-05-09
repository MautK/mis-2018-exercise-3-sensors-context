package com.example.mis.sensor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //example variables
    private double[] rndAccExamplevalues;
    private double[] freqCounts;
    Button music_button;
    MediaPlayer m;
    private Object view;
    private static final String TAG = "oncreate";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private FFTAsynctask mFFT = new FFTAsynctask(1);
    private ffftDataView FftDataview;
    private sensorDataView SensordataView;
    private Canvas sensorCanvas = new Canvas();
    private Canvas fftCanvas = new Canvas();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        SensordataView = (sensorDataView) findViewById(R.id.imageView);
        SensordataView.draw(sensorCanvas);

        FftDataview = (ffftDataView) findViewById(R.id.fftView);
        FftDataview.draw(fftCanvas);

        //followed this explaination
        // https://developer.android.com/guide/topics/sensors/sensors_overview
        //identifying that the sensors are on your device
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //check if there is an accelerometer available
        //might be a bit double because we are checking for the same thing in onSensorChanged
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            Log.d(TAG, "onCreate: There is an accelerometer!!");
        } else {
            Log.d(TAG, "onCreate: ERROR, no accelerometer!!");
        }
    }




    /**
     * Implements the fft functionality as an async task
     * FFT(int n): constructor with fft length
     * fft(double[] x, double[] y)
     */

    private class FFTAsynctask extends AsyncTask<double[], Void, double[]> {

        private int wsize; //window size must be power of 2

        // constructor to set window size
        FFTAsynctask(int wsize) {
            this.wsize = wsize;
        }

        @Override
        protected double[] doInBackground(double[]... values) {


            double[] realPart = values[0].clone(); // actual acceleration values
            double[] imagPart = new double[wsize]; // init empty

            /**
             * Init the FFT class with given window size and run it with your input.
             * The fft() function overrides the realPart and imagPart arrays!
             */
            FFT fft = new FFT(wsize);
            fft.fft(realPart, imagPart);
            //init new double array for magnitude (e.g. frequency count)
            double[] magnitude = new double[wsize];


            //fill array with magnitude values of the distribution
            for (int i = 0; wsize > i ; i++) {
                magnitude[i] = Math.sqrt(Math.pow(realPart[i], 2) + Math.pow(imagPart[i], 2));
            }

            return magnitude;

        }

        @Override
        protected void onPostExecute(double[] values) {
            //hand over values to global variable after background task is finished
            freqCounts = values;
        }
    }

    /**
     * little helper function to fill example with random double values
     */
    public void randomFill(double[] array){
        Random rand = new Random();
        for(int i = 0; array.length > i; i++){
            array[i] = rand.nextDouble();
        }
    }

    //followed this example
    // https://developer.android.com/reference/android/hardware/SensorManager
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    //followed this example
    //  https://developer.android.com/reference/android/hardware/SensorManager
    @Override
    public void onSensorChanged(SensorEvent event) {
        //here is where you read the current sensor values
        // and update views that are displaying the sensor information
        // do as little as possible here to not block it!!
//        Log.d(TAG, "onSensorChanged: changed");

        double[] foobar = new double[event.values.length];
        //create floating numbers to log the various values
        for(int i=0; i < event.values.length; i++){
            foobar[i] = ((double) event.values[i]);
        }

        double tmpX = ((double) event.values[0]);
        double tmpY = ((double) event.values[1]);
        double tmpZ = ((double) event.values[2]);

        double[] x = new double[1];
        double[] y = new double[1];
        double[] z = new double[1];
        x[0] = tmpX;
        y[0] = tmpY;
        z[0] = tmpZ;
        //here the values should be added to a draw function
        //SensordataView.addDataX(dataX);
        //SensordataView.addDataY(dataY);

        Log.d(TAG, "onSensorChanged: value x" + x);
        Log.d(TAG, "onSensorChanged: value y" + y);
        Log.d(TAG, "onSensorChanged: value z" + z);
        Double foobar2 = mFFT.doInBackground(x)[0];
//        Log.d(TAG, "mFFT x: " + foobar2.toString());
//        Log.d(TAG, "mFFT y: " + mFFT.doInBackground(y)[0]);
//        Log.d(TAG, "mFFT z: " + mFFT.doInBackground(z)[0]);
//        Log.d(TAG, "onSensorChanged: " + y);
//        Log.d(TAG, "onSensorChanged: " + z);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}

