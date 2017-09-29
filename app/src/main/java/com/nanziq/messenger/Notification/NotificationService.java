package com.nanziq.messenger.Notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nanziq.messenger.DialogViewActivity;
import com.nanziq.messenger.Firebase.ContactFB;
import com.nanziq.messenger.Firebase.DialogFB;
import com.nanziq.messenger.Model.Dialog;
import com.nanziq.messenger.Model.DialogView;
import com.nanziq.messenger.Model.Message;
import com.nanziq.messenger.R;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationService extends Service {
    final String LOG_TAG = "myLogs";
    Context context;

    public NotificationService() {
        Log.d(LOG_TAG, "Service");
        context = this;
    }

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "ServiceOnCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "ServiceOnBind");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "ServiceOnStartCommand");
        NotificationRunnable runnable = new NotificationRunnable(context);
        new Thread(runnable).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                cancelAlarm();
            }
        }).start();
        setAlarm();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "ServiceDestroy");
    }

    private void setAlarm() {
        Log.d(LOG_TAG, "setAlarm");
        int timeInterval = 60 * 1000;
        Intent restartIntent = new Intent("notificationBroadcast");
        final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final PendingIntent pi = PendingIntent.getBroadcast(context, timeInterval, restartIntent, 0);
        am.cancel(pi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + timeInterval, pi);
            am.setAlarmClock(alarmClockInfo, pi);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeInterval, pi);
        else
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeInterval, pi);
    }

    private void cancelAlarm() {
        Log.d(LOG_TAG, "cancelAlarm");
        try {
            Thread.sleep(55 * 1000);
            Intent restartIntent = new Intent("notificationBroadcast");
            final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            final PendingIntent pi = PendingIntent.getBroadcast(context, 0, restartIntent, 0);
            am.cancel(pi);
            setAlarm();
            cancelAlarm();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(LOG_TAG, "ServiceRemoved");
    }

    private class NotificationRunnable implements Runnable {

        private static final int NOTIFY_ID = 101;
        private PendingIntent pendingIntent;
        private NotificationManager notificationManager;
        private Context context;


        private DialogFB dialogFB;
        private ContactFB contactFB;
        private String currentUserUid;
        private DatabaseReference databaseReference;
        private List<Dialog> dialogList;

        public NotificationRunnable(Context context) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            this.context = context;
            FirebaseApp.initializeApp(context);
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }
            dialogFB = DialogFB.getInstance();
            contactFB = ContactFB.getInstance();

            databaseReference = FirebaseDatabase.getInstance().getReference();

        }


        @Override
        public void run() {

//                try {
//                    Thread.sleep(5000);
//                    sendNotification();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

            databaseReference.child("dialogs").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(LOG_TAG, "NotificationDataChange");
                    for (; ; ) {
                        List<Dialog> dialogListFromBD = dialogFB.getContactDialogList(currentUserUid);
                        if (dialogListFromBD == null) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else if (dialogList == null || !dialogList.equals(dialogListFromBD)) {
                            dialogList = dialogListFromBD;
                            Message message = dialogFB.getContactNewMessageFromUid(currentUserUid);
                            Intent intent = new Intent(getApplicationContext(), DialogViewActivity.class);
                            intent.putExtra("dialogId", dialogFB.getIdDialogFromNewMessage());
                            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            if (message != null) {
                                sendNotification(message);
                            }
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        void sendNotification(Message message) {
            Log.d(LOG_TAG, "SendNotification");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_account_black_48dp)
//                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.hungrycat))
                    .setTicker("Мессенджер")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(contactFB.getContactNameFromUid(message.getUid()))
                    .setContentText(message.getText())
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{0, 250})
                    .setLights(Color.BLUE, 3000, 3000);

            Notification notification = builder.build();
            notificationManager.notify(NOTIFY_ID, notification);
        }
    }
}
