package com.example.awake;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FlashcardActivity extends AppCompatActivity {

    private TextView description;
    private EditText term;
    private Button next;
    private TextView result;
    private List<Flashcard> flashcards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard_challenge);
        // CHANGE TOOLBAR TITLE.
        getSupportActionBar().setTitle("Flashcards");
        //get flashcard
        flashcards = new ArrayList<>(); //replace this with some way to import the cards
        final Flashcard flashcard = flashcards.get(0);
        //how to iterate between cards...
        description = (TextView) findViewById(R.id.flashcardtext);
        description.setText(flashcard.getContent());
        term = (EditText) findViewById(R.id.entertext);
        result = (TextView) findViewById(R.id.result);
        term.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (term.getText().equals(flashcard.getName())) {
                        result.setText("Correct!");
                        result.setTextColor(Color.GREEN);
                    } else {
                        result.setText("Wrong!");
                        result.setTextColor(Color.RED);
                        new AlertDialog.Builder(getParent())
                                .setMessage("The correct answer is \\\" " + flashcard.getName() + " \\\"")
                                .setNeutralButton(android.R.string.ok, null).show();
                        return true;

                    }
                }
                return false;
            }
        });

        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                result.setText("");
                //set the card to be next description tag of card
            }
        });

        //set condition for done: when last card is picked
        /*done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlarmReceiver.mediaPlayer.stop();
                startActivity(new Intent(FlashcardActivity.this, MainActivity.class));

            }
        }); */
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}