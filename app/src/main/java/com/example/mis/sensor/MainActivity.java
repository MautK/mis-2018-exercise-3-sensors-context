package com.example.mis.sensor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener, SeekBar.OnSeekBarChangeListener {

    //example variables
    private double[] rndAccExamplevalues;
    private double[] freqCounts;
    Button music_button;
    MediaPlayer m = new MediaPlayer();
    private static final String TAG = "oncreate";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private FFTAsynctask mFFT = new FFTAsynctask(1);
    private int wsize = (int) Math.pow(2, 7);
    private fftDataView mFFTDataView;
    private sensorDataView mSensorDataView;
    private Canvas sensorCanvas = new Canvas();
    private Canvas fftCanvas = new Canvas();
    private SeekBar windowControl;
    private SeekBar sampleControl;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity thisActivity = this;
        checkPermission(thisActivity);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                decideToPlay(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                100000);

        mSensorDataView = (sensorDataView) findViewById(R.id.imageView);
        mFFTDataView = (fftDataView) findViewById(R.id.fftView);

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
        sampleControl.setOnSeekBarChangeListener(this);

        windowControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                wsize = (int) Math.pow(2, progress);
                mSensorDataView.resizeDataArray(wsize);
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
    public void decideToPlay (Location mLocation){
        if (mLocation.getSpeed() / 3.6 < 6) {
            playMusic("Walk.mp3");
        } else if (mLocation.getSpeed() / 3.6 >= 6 && mLocation.getSpeed() / 3.6 < 14) {
            playMusic("Run.mp3");
        } else if (mLocation.getSpeed() / 3.6 >= 14) {
            playMusic("Cycle.mp3");
        }
    }
    public void checkPermission(Activity thisActivity) {
        // followed this guide https://developer.android.com/training/permissions/requesting.html
        //permission added to manifest
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(thisActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            );
//            }
        } else {
//            mMap.setMyLocationEnabled(true);
        }
    }

    // followed this guide https://developer.android.com/training/permissions/requesting.html
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStopTrackingTouch: DEFUQ");
        int progress = seekBar.getProgress();
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                progress * 1000);
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
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    //source https://stackoverflow.com/questions/3289038/play-audio-file-from-the-assets-directory
    public void playMusic(String file) {
            try {

                if(m.isPlaying()) {

                } else {
//                    m.stop();
                    m.release();
                    m = new MediaPlayer();
                    AssetFileDescriptor descriptor = getAssets().openFd(file);
                    m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                    descriptor.close();

                    m.prepare();
                    m.setVolume(6f, 6f);
                    m.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



