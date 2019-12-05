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
        //clear_preferences();

        load_alarms();

        setAlarmFrag = new AlarmFragment();
        alarmListFrag = new AlarmsListFragment();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Starts App with ListView.
        start_alarms_list_fragment();
    }

    /**
     * Clears saved preferences for alarms for debugging purposes.
     */
    private void clear_preferences() {
        SharedPreferences sharedPref = this.getSharedPreferences("alarms", Context.MODE_PRIVATE);
        sharedPref.edit().clear().commit(); // CLEARS SHAREDPREF FOR TESTING PURPOSES.
    }

    private void challenge_in_progress() {
        // Is this necessary?
        SharedPreferences sharedPref = this.getSharedPreferences("alarms", Context.MODE_PRIVATE);
        boolean challenge = sharedPref.getBoolean("challenge_in_progress", false);
        if(challenge) {
            // Challenge in progress.
            boolean exercise = sharedPref.getBoolean("exercise_challenge", true);
            if(exercise) {
                // Exercise challenge in progress.
                Intent intent = new Intent(MainActivity.this, ShakeActivity.class);
                startActivity(intent);
            } else {
                // Flashcard challenge in progress.
                Intent intent = new Intent(MainActivity.this, ShakeActivity.class);
                startActivity(intent);
            }
        }
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

    public void start_customize_shake_activity() {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, new CustomizeShakesFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void start_manage_cardset_fragment() {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, new ManageFlashcardSets());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /*
    If alarm has no repeats:
    set it to ring in 7 days or today
    once it rings turn alarm off.

    if an alarm has repeats
    if the alarm is in the past, do not set alarm for today, do it for the next repeat

    Must have at least one repeat.
     */
    public static void update_calender_for_next_alarm(boolean[] repeats, Calendar calendar, boolean not_today) {
        Calendar temp_calendar = Calendar.getInstance();
        int day = temp_calendar.get(Calendar.DAY_OF_WEEK);
        day = translate_monday_to_index_zero(day);

        int num_days = 0;
        int i = 0;
        if(not_today) {
            // Do not set alarm for today
            i = 1;
            ++num_days;
            ++day;
        }
        // Using 8 in case it takes one week before alarm rings.
        for(; i < 8; i ++) {
            if(day > 6)
                day = 0;
            if(repeats[day]) {
                calendar.add(Calendar.DAY_OF_YEAR, num_days);
                if(calendar.getTimeInMillis() > System.currentTimeMillis())
                    // Alarm is in the future.
                    break;
                // Restore original time of calender
                calendar.add(Calendar.DAY_OF_YEAR, -1*num_days);
            }
            num_days++;
            day++;
        }
    }

    public static boolean no_repeat(boolean[] repeats) {
        for(boolean b : repeats) {
            if (b)
                return false;
        }
        return true;
    }

    private static int translate_monday_to_index_zero(int day) {
        // From Calender, the day of the week for Sunday is 1.
        // M T W T F S S
        // 2 3 4 5 6 7 1 <- Calender
        // 0 1 2 3 4 5 6 <- Desired
        day = day - 2;
        if (day == -1)
            return 6;
        return day;

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

        if(no_repeat(a.repeats)) {
            // Alarm is not repeated
            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                // Alarm is set for the past so ring it in 7 days.
                calendar.add(Calendar.DAY_OF_YEAR, 7);
                // Note, setting day of the week can put time in the past.
                // Note that incrementing calender day will also increment year when day exceeds 365.
            }
        } else {
            update_calender_for_next_alarm(a.repeats, calendar, false);
        }

        // To update existing alarm, must recreate exact pendingIntent, which is the identifier for an alarm.
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra("alarmRequestCode", a.requestCode);
        // Allows different application to execute intent with all of premissions from this application.
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, a.requestCode, myIntent, 0);
        // Once updated, alarm is automatically turned on.
        System.out.println("Setting Alarm:");
        System.out.println(alarms.get(position).toString());
        System.out.println("Calender time: " + calendar.getTime());
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent); // Exact alarm not working
        // No Need to manually toggle switch for associated alarm since this method is activated once switch is toggled.
        save_alarms(); // Update shared preferences for AlarmReceiver. Ideally, should only save whats necessary.
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
        for(int i = 0; i < size; i ++) {
            int rc = sharedPref.getInt(i + "", -1);
            if(rc == -1)
                continue;
            int h = sharedPref.getInt("hours_" + rc, 6);
            int m = sharedPref.getInt("minutes_" + rc, 0);
            String n = sharedPref.getString("name_" + rc, "");
            String song = sharedPref.getString("song_" + rc, "boat");
            boolean[] repeats = decompress_repeats(sharedPref.getString("repeats_" + rc, "0000000"));
            boolean c = sharedPref.getBoolean("challenge_" + rc, false);
            boolean e = sharedPref.getBoolean("exercise_" + rc, false);
            boolean on = sharedPref.getBoolean("on_" + rc, true);
            Alarm a = new Alarm(h, m, n, song, repeats, c, e);
            a.requestCode = rc;
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
            int rc = a.requestCode;
            editor.putInt(i + "", rc);
            editor.putInt("hours_" + rc, a.hour);
            editor.putInt("minutes_" + rc, a.minute);
            editor.putString("name_" + rc, a.name);
            editor.putString("song_" + rc, a.song);
            editor.putString("repeats_" + rc, compress_repeats(a.repeats));
            editor.putBoolean("challenge_" + rc, a.challenge);
            editor.putBoolean("exercise_" + rc, a.exercise_challenge);
            editor.putBoolean("on_" + rc, a.on);
        }
        editor.apply();
    }

    private static String compress_repeats(boolean[] repeats) {
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

    public static boolean[] decompress_repeats(String repeats_str) {
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
