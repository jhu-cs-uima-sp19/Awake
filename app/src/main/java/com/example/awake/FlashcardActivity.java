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

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FlashcardActivity extends AppCompatActivity {

    private TextView description;
    private EditText term;
    private Button next;
    private Button done;
    private TextView result;
    private List<Flashcard> flashcards;
    public List<FlashcardSet> cardsets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard_challenge);
        // CHANGE TOOLBAR TITLE.
        getSupportActionBar().setTitle("Flashcards");
        //get flashcard
        readCards();
        flashcards = cardsets.get(0).getCardlist();
        //how to iterate between cards...
        description = (TextView) findViewById(R.id.flashcardtext);
        term = (EditText) findViewById(R.id.entertext);
        result = (TextView) findViewById(R.id.result);
        next = (Button) findViewById(R.id.next);
        done = (Button) findViewById(R.id.done);
        done.setBackgroundColor(Color.RED);
        done.setEnabled(false);
        for (int i = 0; i < flashcards.size(); i++) {
            final Flashcard flashcard = flashcards.get(i);
            description.setText(flashcard.getContent());
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
                        next.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                result.setText("");
                                //set the card to be next description tag of card
                            }
                        });
                    }
                    return false;
                }
            });
        }
        done.setEnabled(true);
        done.setBackgroundColor(Color.GREEN);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlarmReceiver.mediaPlayer.stop();
                startActivity(new Intent(FlashcardActivity.this, MainActivity.class));

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

    /*
     * Load in flashcards: make the method
     */
    private void readCards() {
        int i = 0;

        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, FlashcardSet.class);
        JsonAdapter<List> adapter = moshi.adapter(type);

        String filename = "cards.json";
        /*
         * If this is called within a fragment, remember to change to getActivity()
         */
        Context context = this;

        try {
            /*
             * If the file doesn't exist yet, this will go to catch, where a default list
             * of flashcard sets would be created
             */
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (Exception e) {
                // Error occurred when opening raw file for reading.
            } finally {
                String contents = stringBuilder.toString();
                System.out.println("reading success");
                System.out.println(contents);

                List<FlashcardSet> tests = adapter.fromJson(contents);
                cardsets =  tests;
                System.out.println("cards loaded");
            }
        } catch (Exception e) {
            e.printStackTrace();

            /*
             * Creating a new list of flashcard sets
             */
            i++;
            createNewFile();
            if (i<2) {
                readCards();
            } else {
                return;
            }

        }
    }

    private void createNewFile() {
        List<Flashcard> list = new ArrayList<Flashcard>();
        List<Flashcard> list1 = new ArrayList<Flashcard>();

        list.add(new Flashcard("title1", "content1"));
        list.add(new Flashcard("title2", "content2"));
        list1.add(new Flashcard("title3", "content3"));
        list1.add(new Flashcard("title4", "content4"));

        FlashcardSet set1 = new FlashcardSet("test1", list);
        FlashcardSet set2 = new FlashcardSet("test2", list1);

        List<FlashcardSet> list_set = new ArrayList<FlashcardSet>();

        list_set.add(set1);
        list_set.add(set2);

        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, FlashcardSet.class);
        JsonAdapter<List> adapter = moshi.adapter(type);

        String json = adapter.toJson(list_set);

        String filename = "cards.json";
        Context context = this;
        try {
            FileOutputStream fileout = context.openFileOutput(filename, MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(json);
            outputWriter.close();
            System.out.println("writing success");
        } catch (Exception e) {
            System.out.println("writing failed");
            e.printStackTrace();
        }
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