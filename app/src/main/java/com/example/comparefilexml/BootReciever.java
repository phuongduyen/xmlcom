package com.example.comparefilexml;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.util.Objects;

public class BootReciever extends BroadcastReceiver {
    String TAG = "BootReciever ntp_";
    @Override
    public void onReceive(Context context, Intent intent) {
        String a = intent.getAction();
        Log.d(TAG, "onReceive ");
        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "onReceive ACTION_BOOT_COMPLETED");
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(i);
        }
        if (Objects.equals(intent.getAction(), Intent.ACTION_REBOOT)) {
            Log.d(TAG, "onReceive ACTION_REBOOT");
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(i);
        }
        if (Objects.equals(intent.getAction(), Intent.ACTION_USER_PRESENT)) {
            Log.d(TAG, "onReceive ACTION_USER_PRESENT");
            Intent i = new Intent(context, LaunchWord.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        if (Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_ON)) {
            Log.d(TAG, "onReceive ACTION_SCREEN_ON");
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(i);
        }
        if (Objects.equals(intent.getAction(), Intent.ACTION_USER_FOREGROUND)) {
            Log.d(TAG, "onReceive ACTION_USER_FOREGROUND");
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(i);
        }
        if (Objects.equals(intent.getAction(), Intent.ACTION_USER_BACKGROUND)) {
            Log.d(TAG, "onReceive ACTION_USER_BACKGROUND");
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(i);
        }
        if (Objects.equals(intent.getAction(), Intent.ACTION_POWER_CONNECTED)) {
            Log.d(TAG, "onReceive ACTION_POWER_CONNECTED");
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(i);
        }
        if (Objects.equals(intent.getAction(), Intent.ACTION_USER_INITIALIZE)) {
            Log.d(TAG, "onReceive ACTION_USER_INITIALIZE");
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(i);
        }
        if (Objects.equals(intent.getAction(), Intent.ACTION_USER_UNLOCKED)) {
            Log.d(TAG, "onReceive ACTION_USER_UNLOCKED");
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(i);
        }
        if (Objects.equals(intent.getAction(), Intent.CATEGORY_LAUNCHER)) {
            Log.d(TAG, "onReceive CATEGORY_LAUNCHER");
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(i);
        }
    }
}