package com.example.awake;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AlarmFragment extends Fragment {


    /**
     * Widget for selecting the time of the day in 24-hour or AM/PM mode.
     */
    private TimePicker alarmTimePicker;
    /**
     * Activity this fragment is apart of.
     */
    private MainActivity mA;
    /**
     * If challenge enabled, then exercise challenge enabled?
     */
    public boolean exercise_challenge = true;

    // View components;

    private EditText name_edittext;
    private Spinner song_spinner;
    private RadioButton mo_radio;
    private RadioButton tu_radio;
    private RadioButton we_radio;
    private RadioButton th_radio;
    private RadioButton fr_radio;
    private RadioButton sa_radio;
    private RadioButton su_radio;
    private Switch challenge_switch;
    private ImageButton flashcards_button;
    private ImageButton exercise_button;

    private Button done_button;
    private Button cancel_button;

    // Color values
    private final int selected = 0x30EE3A;
    private final int unselected = 0xA3A09B;

    private String song_root_path = "C:/Users/Shuha/AndroidStudioProjects/Awake/app/src/main/res/raw/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.set_alarm_fragment, container, false);
        mA.getSupportActionBar().setTitle("Set Alarms");
        initialize_views(view);
        flashcards_button.setEnabled(false);
        exercise_button.setEnabled(false);

        // Check if editing or adding new alarm.
        boolean edit = mA.edit;
        if(edit) {
            // Need to prefill in information about old alarm.
            Alarm a = mA.alarms.get(mA.position);
            // Filling time information;
            fill_time_picker(a.hour, a.minute);
            name_edittext.setText(a.name.trim());
            // Filling in repeat information.
            fill_repeat_info(a.repeats);
            // Filling in challenge info.
            fill_challenge_info(a.challenge, a.exercise_challenge);
        }

        // Creating the listeners.
        done_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Update or create new alarm. THIS NEEDS TO BE UPDATED ONCE REPEATS ARE IMPLEMENTED.
                set_alarm();
                // Will switch in ListView fragment and update the ArrayAdapter.
                mA.start_alarms_list_fragment();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Will switch in ListView fragment and update the ArrayAdapter.
                mA.start_alarms_list_fragment();
            }
        });

        challenge_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if(exercise_challenge) {
                        exercise_button.setBackgroundColor(selected);
                        flashcards_button.setBackgroundColor(unselected);
                    } else {
                        exercise_button.setBackgroundColor(unselected);
                        flashcards_button.setBackgroundColor(selected);
                    }
                } else {
                    flashcards_button.setEnabled(false);
                    exercise_button.setEnabled(false);
                }
            }
        });

        flashcards_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exercise_challenge = true;
                exercise_button.setBackgroundColor(selected);
                flashcards_button.setBackgroundColor(unselected);
            }
        });

        exercise_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exercise_challenge = false;
                exercise_button.setBackgroundColor(unselected);
                flashcards_button.setBackgroundColor(selected);
            }
        });

        return view;
    }

    private void fill_repeat_info(boolean[] repeats) {
        if(repeats[0]) {
            mo_radio.setChecked(true);
        }
        if(repeats[1]) {
            tu_radio.setChecked(true);
        }
        if(repeats[2]) {
            we_radio.setChecked(true);
        }
        if(repeats[3]) {
            th_radio.setChecked(true);
        }
        if(repeats[4]) {
            fr_radio.setChecked(true);
        }
        if(repeats[5]) {
            sa_radio.setChecked(true);
        }
        if(repeats[6]) {
            su_radio.setChecked(true);
        }
    }

    private void fill_time_picker(int hour, int minute) {
        // Set current hour always expects 24-hour format so no need to specify AM/PM.
        alarmTimePicker.setHour(hour);
        alarmTimePicker.setMinute(minute);
    }

    private void fill_challenge_info(boolean c, boolean e) {
        if(c) {
            challenge_switch.setChecked(true);
            flashcards_button.setEnabled(false);
            exercise_button.setEnabled(false);
        }
        if(e) {
            exercise_button.setBackgroundColor(selected);
            flashcards_button.setBackgroundColor(unselected);
            exercise_challenge = true;
        } else {
            exercise_button.setBackgroundColor(unselected);
            flashcards_button.setBackgroundColor(selected);
            exercise_challenge = false;
        }
    }

    private void initialize_views(View view) {
        alarmTimePicker = view.findViewById(R.id.alarmTimePicker);
        name_edittext = view.findViewById(R.id.name_input);
        song_spinner = view.findViewById(R.id.alarm_sounds);
        mo_radio = view.findViewById(R.id.monday);
        tu_radio = view.findViewById(R.id.tuesday);
        we_radio = view.findViewById(R.id.wednesday);
        th_radio = view.findViewById(R.id.thursday);
        fr_radio = view.findViewById(R.id.friday);
        sa_radio = view.findViewById(R.id.saturday);
        su_radio = view.findViewById(R.id.sunday);
        challenge_switch = view.findViewById(R.id.challenge);
        flashcards_button = view.findViewById(R.id.flashcardButton);
        exercise_button = view.findViewById(R.id.exerciseButton);
        done_button = view.findViewById(R.id.done);
        cancel_button = view.findViewById(R.id.cancel);
    }

    private Alarm get_alarm_from_inputs() {
        int h = alarmTimePicker.getHour();
        int m = alarmTimePicker.getMinute();
        String n = name_edittext.getText().toString();
        String song_name = song_spinner.getSelectedItem().toString();
        String song_file_path = song_root_path + song_name + ".mp3";
        boolean[] repeats = {false, false, false, false, false, false, false};
        if(mo_radio.isChecked())
            repeats[0] = true;
        if(tu_radio.isChecked())
            repeats[1] = true;
        if(we_radio.isChecked())
            repeats[2] = true;
        if(th_radio.isChecked())
            repeats[3] = true;
        if(fr_radio.isChecked())
            repeats[4] = true;
        if(sa_radio.isChecked())
            repeats[5] = true;
        if(su_radio.isChecked())
            repeats[6] = true;
        boolean c = challenge_switch.isChecked();

        return new Alarm(h, m, n, song_name, repeats, c, exercise_challenge);
    }

    public void set_alarm() {
        Alarm a = get_alarm_from_inputs();
        if(mA.edit) {
            mA.cancelAlarm(mA.position);
            mA.alarms.set(mA.position, a);
            mA.create_alarm(mA.position);
        } else {
            // Hours should be in 24-hour format.
            mA.alarms.add(a);
            mA.create_alarm(mA.alarms.size()-1);
        }
        mA.edit = false;
    }

    // Used to access activity in fragment.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            mA =(MainActivity) context;
        }
    }
}