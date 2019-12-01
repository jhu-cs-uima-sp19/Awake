package com.example.awake;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * What happens once the alarm goes off
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static MediaPlayer mediaPlayer;
    private static PowerManager.WakeLock wakeLock;

    @Override
    public void onReceive(Context context, Intent intent) {
        int rc = intent.getIntExtra("alarmRequestCode", -1);

        java.util.Date date = new java.util.Date();
        System.out.println("Alarm ring time: " + date);
        System.out.println("Ringing alarm request code: " + rc);

        wake_up(context);

        SharedPreferences sharedPref = context.getSharedPreferences("alarms", Context.MODE_PRIVATE);
        boolean challenge = sharedPref.getBoolean("challenge_" + rc, false);
        boolean exercise = sharedPref.getBoolean("exercise_" + rc, false);

        play_song(context, sharedPref.getString("song_" + rc, "boat"));

        if(challenge) {
            // This alarm has a challenge
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("challenge_in_progress", true);
            if(exercise) {
                // This alarm has the exercise/shake challenge.
                System.out.println("Exercise activity triggered.");
                editor.putBoolean("exercise_challenge", true);
                intent = new Intent(context, ShakeActivity.class);
                context.startActivity(intent);

            } else {
                // This alarm has the flashcard challenge.
                System.out.println("Flashcard activity triggered.");
                editor.putBoolean("exercise_challenge", false);
                intent = new Intent(context, ShakeActivity.class);
                context.startActivity(intent);
            }
            editor.apply();
        }
        release();

    }

    private void play_song(Context context, String song) {
        if(song.equals("boat")) {
            mediaPlayer = MediaPlayer.create(context, R.raw.boat);
        } else if (song.equals("bell")) {
            mediaPlayer = MediaPlayer.create(context, R.raw.bell);
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void set_next_alarm(Context context, int rc, SharedPreferences sharedPref) {
        int h = sharedPref.getInt("hours_" + rc, 6);
        int m = sharedPref.getInt("minutes_" + rc, 0);
        boolean[] repeats = MainActivity.decompress_repeats(
                sharedPref.getString("repeats_" + rc, "0000000"));

        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, 0);

        if(MainActivity.no_repeat(repeats)) {
            System.out.println(rc + ": is not repeating, turning off.");
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("on_" + rc, false);
            editor.apply();
            return;
        } else {
            // Alarm should not include todays repeat since that would make the alarm in the past.
            MainActivity.update_calender_for_next_alarm(repeats, calendar, true);
        }

        // To update existing alarm, must recreate exact pendingIntent, which is the identifier for an alarm.
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra("alarmRequestCode", rc);
        // Allows different application to execute intent with all of premissions from this application.
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, rc, myIntent, 0);
        // Once updated, alarm is automatically turned on.
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent); // Exact alarm not working
        System.out.println("Set repeating alarm for next date\nNext alarm on: " + calendar.getTime());
    }

    public static void wake_up(Context ctx) {
        if(wakeLock!=null)
            wakeLock.release();

        PowerManager pm =(PowerManager)ctx.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock( PowerManager.FULL_WAKE_LOCK, "awake:my_wake_lock");
        wakeLock.acquire();
    }

    public static void release(){
        if(wakeLock!=null)
            wakeLock.release();
        wakeLock=null;
    }
}