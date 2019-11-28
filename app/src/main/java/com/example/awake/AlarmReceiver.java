package com.example.awake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.widget.Toast;

/**
 * What happens once the alarm goes off
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static MediaPlayer mediaPlayer;
    private static PowerManager.WakeLock wakeLock;

    @Override
    public void onReceive(Context context, Intent intent) {
        java.util.Date date = new java.util.Date();
        System.out.println("Alarm ring time: " + date);
        int requestCode = intent.getIntExtra("alarmRequestCode", -1);
        System.out.println("Ringing alarm request code: " + requestCode);

        wake_up(context);

        mediaPlayer = MediaPlayer.create(context, R.raw.boat);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        intent = new Intent();
        intent.setClass(context, ShakeActivity.class); //Test is a dummy class name where to redirect
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        release();

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