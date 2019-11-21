package com.example.awake;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
     * Used to create string of repeated days
     */
    public static final String[] days = {"M", "Tu", "W", "Th", "F", "Sa", "Su"};


    public void set_alarm() {
        // Had to change minimumSDK for this.
        int hour = alarmTimePicker.getHour();
        int minute = alarmTimePicker.getMinute();
        String repeat_str = get_repeat_choices();
        if(mA.edit) {
            mA.cancelAlarm(mA.position);
            Alarm new_alarm = new Alarm(hour, minute, repeat_str);
            mA.alarms.set(mA.position, new_alarm);
            mA.create_alarm(mA.position);
        } else {
            // Hours should be in 24-hour format.
            Alarm a = new Alarm(hour, minute, repeat_str);
            mA.alarms.add(a);
            mA.create_alarm(mA.alarms.size()-1);
        }
        mA.edit = false;
    }

    /**
     * Creates boolean of repeats to fill drop down.
     * @param repeats_arr
     * @return
     */
    private static boolean[] fill_repeats_arr(String[] repeats_arr) {
        boolean[] repeats = new boolean[repeats_arr.length];
        for(String day : repeats_arr) {
            if(day.equals(days[0])) {
                repeats[0] = true;
            } else if (day.equals(days[1])) {
                repeats[1] = true;
            } else if (day.equals(days[2])) {
                repeats[2] = true;
            } else if (day.equals(days[3])) {
                repeats[3] = true;
            } else if (day.equals(days[4])) {
                repeats[4] = true;
            } else if (day.equals(days[5])) {
                repeats[5] = true;
            } else if (day.equals(days[6])) {
                repeats[6] = true;
            }
        }
        return repeats;
    }

    /**
     * Gets String of repeats to pass to Alarm.
     * @return String of repeat days
     */
    private String get_repeat_choices() {
        // Creates array of booleans.
        boolean[] repeats = {true, false, false, false, false, false, false};
        return create_repeat_str(repeats);
    }

    /**
     * Creates the String of repeats.
     * @param repeats
     * @return
     */
    private static String create_repeat_str(boolean[] repeats) {
        String repeats_str = "";
        for(int i = 0; i < repeats.length; i++) {
            if (repeats[i])
                repeats_str += (", " + days[i]);
        }
        if(repeats_str.isEmpty())
            return repeats_str;
        return repeats_str.substring(2);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.set_alarm_fragment, container, false);
        mA.getSupportActionBar().setTitle("Set Alarms");

        // Check if editing or adding new alarm.
        boolean edit = mA.edit;

        alarmTimePicker = (TimePicker) view.findViewById(R.id.alarmTimePicker);
        Button done = (Button) view.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Update or create new alarm. THIS NEEDS TO BE UPDATED ONCE REPEATS ARE IMPLEMENTED.
                set_alarm();
                // Will switch in ListView fragment and update the ArrayAdapter.
                mA.start_alarms_list_fragment();
            }
        });

        if(edit) {
            // Need to prefill in information about old alarm.
            Alarm a = mA.alarms.get(mA.position);
            String time = a.alarm_time;
            String repeat_str = a.repeat_str;

            // filling in repeat information.
            String[] repeats_arr = repeat_str.trim().split(",\\s");
            boolean[] repeats = fill_repeats_arr(repeats_arr); // Use in second sprint.

            // filling in time information.
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Date date = null;
            try {
                // default date.
                date = sdf.parse("6:00 AM");
                // Read in date.
                Date temp_date = sdf.parse(time);
                date = temp_date;
            } catch (ParseException e) {
                System.out.println("Unable to parse time");
            }
            Calendar calender = Calendar.getInstance();
            calender.setTime(date);
            // Set current hour always expects 24-hour format so no need to specify AM/PM.
            alarmTimePicker.setHour(calender.get(Calendar.HOUR_OF_DAY));
            alarmTimePicker.setMinute(calender.get(Calendar.MINUTE));
        }
        return view;
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