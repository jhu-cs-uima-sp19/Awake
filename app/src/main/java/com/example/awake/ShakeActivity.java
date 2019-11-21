package com.example.awake;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShakeActivity extends AppCompatActivity {
    public int remaining_count = 30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_challenge);
        // CHANGE TOOLBAR TITLE.
        getSupportActionBar().setTitle("Details");

        final TextView remaining_shakes = (TextView) findViewById(R.id.remaining_shakes);
        final Button press = (Button) findViewById(R.id.press);
        final Button done = (Button) findViewById(R.id.done);
        done.setEnabled(false);

        press.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(remaining_count == 1) {
                    remaining_shakes.setText("0");
                    done.setEnabled(true);
                    done.setBackgroundColor(Color.GREEN);
                    return;
                }
                remaining_count--;
                remaining_shakes.setText(remaining_count + "");
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlarmReceiver.mediaPlayer.stop();
                startActivity(new Intent(ShakeActivity.this, MainActivity.class));

            }
        });
    }


}
