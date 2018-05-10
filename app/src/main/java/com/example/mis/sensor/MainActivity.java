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
import android.widget.SeekBar;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //example variables
    private double[] rndAccExamplevalues;
    private double[] freqCounts;
    Button music_button;
    MediaPlayer m;
    private static final String TAG = "oncreate";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private FFTAsynctask mFFT = new FFTAsynctask(1);
    private int wsize = 64;
    private fftDataView mFFTDataView;
    private sensorDataView mSensorDataView;
    private Canvas sensorCanvas = new Canvas();
    private Canvas fftCanvas = new Canvas();
    private SeekBar windowControl = null;
    private SeekBar sampleControl = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mSensorDataView = (sensorDataView) findViewById(R.id.imageView);
        mFFTDataView = (fftDataView) findViewById(R.id.fftView);
//        mSensorDataView.setWindowSize(64);
//        mSensorDataView.resizeDataArray(64);
        for (int i = 0; i < wsize; i++) {
            mSensorDataView.addSensorData(new sensorData());
            mFFTDataView.addSensorData(new sensorData());
        }
        sensorData foo = new sensorData();
        mSensorDataView.addSensorData(foo);

        mSensorDataView.draw(sensorCanvas);

        mFFTDataView.draw(fftCanvas);

        //followed this explaination
        // https://developer.android.com/guide/topics/sensors/sensors_overview
        //identifying that the sensors are on your device
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        windowControl = (SeekBar) findViewById(R.id.windowSeekbar);
        sampleControl = (SeekBar) findViewById(R.id.sampleSeekbar);
        
        windowControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
                Log.d(TAG, "onStartTrackingTouch: wsize" + wsize);
                Log.d(TAG, "onStartTrackingTouch: progressChanged" + progressChanged);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wsize = (int) Math.pow(2, progressChanged);
                mSensorDataView.resizeDataArray(wsize);
            }
        });

        sampleControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progressChanged = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


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
        sensorData newSensorData = new sensorData();
        newSensorData.setData(event.values[0], event.values[1], event.values[2]);
        mSensorDataView.addSensorData(newSensorData);
        mFFTDataView.addSensorData(newSensorData);
        mSensorDataView.draw(sensorCanvas);
        mFFTDataView.draw(fftCanvas);
        mSensorDataView.invalidate();
        mFFTDataView.invalidate();
//        mFFTDataView.addSensorData(newSensorData);
//        mFFTDataView.draw(fftCanvas);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}

