package com.example.awake;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Alarm implements Comparable<Alarm> {
    /**
     * Hours in 24
     */
    public int hour;
    /**
     * Minutes of alarm.
     */
    public int minute;
    /**
     * The name of the alarm.
     */
    public String name;
    /**
     * The path for the alarm soundtrack.
     */
    public String song;
    /**
     * index 0 = monday, 1 = tuesday, 2 = wednesday, 3 = thursday, etc.
     * 1 if alarm is set to repeat on the weekday.
     */
    public boolean[] repeats;
    /**
     * Challenge enabled?
     */
    public boolean challenge;
    /**
     * If challenge enabled, then exercise challenge enabled?
     */
    public boolean exercise_challenge;

    // Variables beyond this point do not directly rely on constructor.

    /**
     * String of repeats.
     */
    public String repeat_str;
    /**
     * String representation of alarm in 12-hour format.
     */
    public String alarm_time;
    /**
     * Unique integer that represents the alarm.
     */
    public int requestCode;
    /**
     * States whether the switch should be on or off.
     */
    public boolean on;

    public Alarm(int h, int m, String n, String song, boolean[] repeats, boolean c, boolean e) {
        this.hour = h;
        this.minute = m;
        this.name = n;
        this.song = song;
        this.repeats = repeats;
        this.challenge = c;
        this.exercise_challenge = e;
        gen_repeat_str();
        gen_alarm_time();
        gen_request_code();
        this.on = true;
    }

    private void gen_alarm_time() {
        this.alarm_time = "6:00 AM";

        try {
            String _24Hour = hour + ":" + minute;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24Date = _24HourSDF.parse(_24Hour);
            alarm_time = _12HourSDF.format(_24Date);
        } catch (Exception e) {
            System.err.println("Could not convert hours and min to 12 hours format.");
        }
    }

    private void gen_request_code() {
        int random_num = (int) (Math.random() * 10000);
        this.requestCode = Integer.parseInt("" + hour + minute + random_num);
    }


    private void gen_repeat_str() {
        repeat_str = "";

        if(repeats[0])
            repeat_str += "M, ";
        if(repeats[1])
            repeat_str += "Tu, ";
        if(repeats[2])
            repeat_str += "W, ";
        if(repeats[3])
            repeat_str += "Th, ";
        if(repeats[4])
            repeat_str += "F, ";
        if(repeats[5])
            repeat_str += "Sa, ";
        if(repeats[6])
            repeat_str += "Su, ";

        if(!repeat_str.isEmpty())
            repeat_str = repeat_str.substring(0, repeat_str.length()-2);
    }

    @Override
    public int compareTo(Alarm alarm) {
        if(hour != alarm.hour) {
            return hour - alarm.hour;
        } else {
            return minute - alarm.minute;
        }
    }

    @Override
    public String toString() {
        // Used for debugging purposes.
        String message = "\nAlarm string: " + alarm_time + "\n";
        message += String.format("Raw Alarm time: %d:%d\n", hour, minute);
        message += "RequestCode: " + requestCode + "\n";
        message += "Boolean on: " + on + "\n\n\n";
        return message;
    }
}
