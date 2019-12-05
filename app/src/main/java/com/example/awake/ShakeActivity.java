package com.example.awake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.hardware.Sensor;
import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.graphics.Color;

public class ShakeActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private int shakingTimes; // times of shakes happened

    private TextView shake_hint;
    private TextView shake_text;
    private TextView count;
    private TextView shaking_times;
    private Button done;

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double)(x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            // perform low-cut filter
            // using the difference equation: y(t) = 0.9y(t-1) + x(t)
            mAccel = mAccel * 0.9f + delta;

            if (mAccel > 12) {
                if (shakingTimes > 0) {
                    shakingTimes = shakingTimes - 1;
                    shaking_times.setText(String.valueOf(shakingTimes));
                }
            }
            if(shakingTimes == 0) {
                done.setEnabled(true);
                done.setBackgroundColor(Color.GREEN);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_challenge);
        // CHANGE TOOLBAR TITLE.
        getSupportActionBar().setTitle("Exercise");

        SharedPreferences sharedPref = getSharedPreferences("alarms", Context.MODE_PRIVATE);
        shakingTimes = sharedPref.getInt("shakes", 25);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        shake_text = (TextView) findViewById(R.id.shaketext);
        shake_hint = (TextView) findViewById(R.id.shakehint);
        count = (TextView) findViewById(R.id.count);
        shaking_times = (TextView) findViewById(R.id.shakingTimes);
        done = (Button) findViewById(R.id.done);
        done.setBackgroundColor(Color.RED);
        done.setEnabled(false);

        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlarmReceiver.mediaPlayer.stop();
                startActivity(new Intent(ShakeActivity.this, MainActivity.class));

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        shaking_times.setText(String.valueOf(shakingTimes));
        shake_hint.setText(String.valueOf(shakingTimes) + " Times");
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}