package com.example.awake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Fragment information.
    private AlarmFragment setAlarmFrag;
    private AlarmsListFragment alarmListFrag;
    private FragmentTransaction transaction;

    public boolean edit;
    public int position;

    /**
     * Allows you to send intents at some time in the future.
     * Registered alarms are retained while the device is asleep and can wake device up if off.
     */
    public AlarmManager alarmManager;
    public List<Alarm> alarms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //for debugging.
        //SharedPreferences sharedPref = this.getSharedPreferences("alarms", Context.MODE_PRIVATE);
        //sharedPref.edit().clear().commit(); // CLEARS SHAREDPREF FOR TESTING PURPOSES.


        load_alarms();

        setAlarmFrag = new AlarmFragment();
        alarmListFrag = new AlarmsListFragment();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Starts App with ListView.
        start_alarms_list_fragment();
    }

    public void start_set_alarm_fragment() {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, setAlarmFrag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void start_alarms_list_fragment() {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, alarmListFrag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void cancelAlarm(int position) {
        // To cancel existing alarm, must recreate exact pendingIntent, which is the identifier for an alarm.
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        /*
         * A PendingIntent is a token that you give to a foreign application
         * (e.g. NotificationManager, AlarmManager, Home Screen AppWidgetManager, or other 3rd party applications),
         * which allows the foreign application to use your application's permissions
         * to execute a predefined piece of code.
         */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarms.get(position).requestCode, myIntent, 0);
        System.out.println("Canceling Alarm:");
        System.out.println(alarms.get(position).toString());
        alarmManager.cancel(pendingIntent);
    }


    public void create_alarm(int position) {
        Alarm a = alarms.get(position);
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, a.hour);
        calendar.set(Calendar.MINUTE, a.minute);
        calendar.set(Calendar.SECOND, 0);

        // Check if the Calendar time is in the past b/c alarms set in the past will ring immediately.
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 4); // it will tell to run to next day
            // setting day of the week can put time in the past. Sets date to within THIS WEEK.
        }


        // To update existing alarm, must recreate exact pendingIntent, which is the identifier for an alarm.
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra("alarmRequestCode",a.requestCode);
        // Allows different application to execute intent with all of premissions from this application.
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, a.requestCode, myIntent, 0);
        // Once updated, alarm is automatically turned on.
        System.out.println("Setting Alarm:");
        System.out.println(alarms.get(position).toString());
        System.out.println("Calender time: " + calendar.getTime());
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent); // Exact alarm not working
        // No Need to manually toggle switch for associated alarm since this method is activated once switch is toggled.
    }

    public void deleteAlarm(int position) {
        cancelAlarm(position);
        alarms.remove(position);
        alarmListFrag.update_list_view();
    }

    private void load_alarms() {
        System.out.println("Loading Alarms");
        SharedPreferences sharedPref = this.getSharedPreferences("alarms", Context.MODE_PRIVATE);
        int size = sharedPref.getInt("size", 0);
        String default_song = "C:/Users/Shuha/AndroidStudioProjects/Awake/app/src/main/res/raw/boat.mp3";
        for(int i = 0; i < size; i ++) {
            String repeats_str = sharedPref.getString("repeat_str" + i, "M");
            int h = sharedPref.getInt("hours_" + i, 6);
            int m = sharedPref.getInt("minutes_" + i, 0);
            String n = sharedPref.getString("name_" + i, "");
            String song_file_path = sharedPref.getString("sound_file_path" + i, default_song);
            boolean[] repeats = decompress_repeats(sharedPref.getString("repeats" + i, "0000000"));
            boolean c = sharedPref.getBoolean("challenge" + i, false);
            boolean e = sharedPref.getBoolean("exercise" + i, false);
            boolean on = sharedPref.getBoolean("on_" + i, true);
            int requestCode = sharedPref.getInt("requestCode_" + i, 0);
            Alarm a = new Alarm(h, m, n, song_file_path, repeats, c, e);
            a.requestCode = requestCode;
            a.on = on;
            alarms.add(a);
            System.out.println("Loading Alarm:");
            System.out.println(a.toString());
        }
    }

    private void save_alarms() {
        // Need to save alarms to shared preference for when app is closed.
        SharedPreferences sharedPref = getSharedPreferences("alarms", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        System.out.println("Saving alarms:");

        int size = alarms.size();
        editor.putInt("size", size);
        for(int i = 0; i < size; i ++) {
            Alarm a = alarms.get(i);
            System.out.println("Saving Alarm:");
            System.out.println(a.toString());
            editor.putInt("hours_" + i, a.hour);
            editor.putInt("minutes_" + i, a.minute);
            editor.putString("name_" + i, a.name);
            editor.putString("sound_file_path" + i, a.name);
            editor.putString("repeats" + i, compress_repeats(a.repeats));
            editor.putBoolean("challenge" + i, a.challenge);
            editor.putBoolean("exercise" + i, a.exercise_challenge);
            editor.putInt("requestCode_" + i, a.requestCode);
            editor.putBoolean("on_" + i, a.on);
        }
        editor.apply();
    }

    private String compress_repeats(boolean[] repeats) {
        String repeats_compress = "";
        for(boolean repeat : repeats) {
            if(repeat) {
                repeats_compress += "1";
            } else {
                repeats_compress += "0";
            }
        }
        return repeats_compress;
    }

    private boolean[] decompress_repeats(String repeats_str) {
        boolean[] repeats = new boolean[7];
        for(int i = 0; i < 7; i++) {
            if(repeats_str.charAt(i) == '1') {
                repeats[i] = true;
            } else {
                repeats[i] = false;
            }
        }
        return repeats;
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("On Pause");
        save_alarms();
    }
    /*
    Existing problem so far:

    Setting alarms in the past causes unexpected alarm fires. This includes turning on past alarms.

    What I know:
    -The ringing alarm is 100% caused by the past alarm we set.
    -The time for the past alarm does not matter.
    -Turning on and off the AM alarm will make it ring in some nearby time.

    Solution:
    -Calender sets alarm for a specific time and DATE. Alarms set in the past will ring immediately!
    -So dumb
     */
}
