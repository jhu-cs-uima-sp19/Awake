package com.example.awake;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Alarm implements Comparable<Alarm> {
    /**
     * String representation of alarm in 12-hour format.
     */
    public String alarm_time;
    /**
     * String of repeats.
     */
    public String repeat_str;
    /**
     * Hours in 24
     */
    public int hour;
    /**
     * Minutes of alarm.
     */
    public int minute;
    /**
     * States whether the switch should be on or off.
     */
    public boolean on;
    /**
     * Unique integer that represents the alarm.
     */
    public int requestCode;

    public Alarm(int h, int m, String repeats_str) {
        this.hour = h;
        this.minute = m;
        this.alarm_time = "6:00 AM";

        try {
            String _24Hour = h + ":" + m;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24Date = _24HourSDF.parse(_24Hour);
            alarm_time = _12HourSDF.format(_24Date);
        } catch (Exception e) {
            System.err.println("Could not convert hours and min to 12 hours format.");
        }
        this.repeat_str = repeats_str;
        this.on = true;
        int random_num = (int)(Math.random() * 10000);
        this.requestCode = Integer.parseInt("" + h + m + random_num);
    }

    @Override
    public int compareTo(Alarm alarm) {
        if(hour != alarm.hour) {
            return hour - alarm.hour;
        } else {
            return minute - alarm.minute;
        }
    }
}
