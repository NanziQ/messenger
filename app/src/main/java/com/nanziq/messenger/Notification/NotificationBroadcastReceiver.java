package com.nanziq.messenger.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Konstantin on 18.09.2017.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    final String LOG_TAG = "myLogs";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG,"BroadcastReceiverOnReceive");
        Toast.makeText(context.getApplicationContext(), "Работай сука", Toast.LENGTH_SHORT).show();
        context.startService(new Intent(context, NotificationService.class));
    }
}
