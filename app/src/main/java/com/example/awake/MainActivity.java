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

//         for debugging.
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
        alarmManager.cancel(pendingIntent);
    }


    public void create_alarm(int position) {
        Alarm a = alarms.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, a.hour);
        calendar.set(Calendar.MINUTE, a.minute);
        calendar.set(Calendar.SECOND, 0);

        // To update existing alarm, must recreate exact pendingIntent, which is the identifier for an alarm.
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        // Allows different application to execute intent with all of premissions from this application.
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, a.requestCode, myIntent, 0);
        // Once updated, alarm is automatically turned on.
        System.out.println("Set Alarm: " + a.alarm_time + "  " + a.hour + " " + a.minute);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent); // Exact alarm not working
        // No Need to manually toggle switch for associated alarm since this method is activated once switch is toggled.
    }

    public void deleteAlarm(int position) {
        cancelAlarm(position);
        alarms.remove(position);
        alarmListFrag.update_list_view();
    }

    private void load_alarms() {
        SharedPreferences sharedPref = this.getSharedPreferences("alarms", Context.MODE_PRIVATE);
        int size = sharedPref.getInt("size", 0);
        for(int i = 0; i < size; i ++) {
            String repeats_str = sharedPref.getString("repeat_str" + i, "M");
            int hours = sharedPref.getInt("hours_" + i, 6);
            int minutes = sharedPref.getInt("minutes_" + i, 0);
            boolean on = sharedPref.getBoolean("on_" + i, true);
            int requestCode = sharedPref.getInt("requestCode_" + i, 0);
            Alarm a = new Alarm(hours, minutes, repeats_str);
            a.requestCode = requestCode;
            a.on = on;
            alarms.add(a);
        }
    }

    private void save_alarms() {
        // Need to save alarms to shared preference for when app is closed.
        SharedPreferences sharedPref = getSharedPreferences("alarms", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        int size = alarms.size();
        editor.putInt("size", size);
        for(int i = 0; i < size; i ++) {
            Alarm a = alarms.get(i);
            editor.putString("repeat_str_" + i, a.repeat_str);
            editor.putInt("hours_" + i, a.hour);
            editor.putInt("minutes_" + i, a.minute);
            editor.putBoolean("on_" + i, a.on);
            editor.putInt("requestCode_" + i, a.requestCode);
        }
        editor.apply();
    }

    @Override
    public void onPause() {
        super.onPause();
        save_alarms();
        System.out.println("On Pause");
    }
}
