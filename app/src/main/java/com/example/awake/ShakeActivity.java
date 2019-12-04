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
    private int shakingTimes = 20; // times of shakes happened
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
            mAccel = mAccel * 0.9f + delta * 0.1f;

            if (mAccel > 12) {
                if (shakingTimes > 0) {
                    shakingTimes = shakingTimes - 1;
                    shaking_times.setText(String.valueOf(shakingTimes));
                }
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
        done.setBackgroundColor(Color.GREEN);

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
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}


//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class ShakeActivity extends AppCompatActivity {
//    public int remaining_count = 30;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.shake_challenge);
//        // CHANGE TOOLBAR TITLE.
//        getSupportActionBar().setTitle("Details");
//
//        final TextView remaining_shakes = (TextView) findViewById(R.id.remaining_shakes);
//        final Button press = (Button) findViewById(R.id.press);
//        final Button done = (Button) findViewById(R.id.done);
//        done.setEnabled(false);
//
//        press.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if(remaining_count == 1) {
//                    remaining_shakes.setText("0");
//                    done.setEnabled(true);
//                    done.setBackgroundColor(Color.GREEN);
//                    return;
//                }
//                remaining_count--;
//                remaining_shakes.setText(remaining_count + "");
//            }
//        });
//
//        done.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                AlarmReceiver.mediaPlayer.stop();
//                startActivity(new Intent(ShakeActivity.this, MainActivity.class));
//
//            }
//        });
//    }
//
//
//}
