package com.nanziq.messenger.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import com.nanziq.messenger.Firebase.DialogFB;
import com.nanziq.messenger.Model.Dialog;
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
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "ServiceOnBind");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "ServiceOnStartCommand");
        NotificationRunnable runnable = new NotificationRunnable(context);
        new Thread(runnable).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "ServiceDestroy");
        sendBroadcast(new Intent(this, NotificationBroadcastReceiver.class));
    }

    private class NotificationRunnable implements Runnable {

        private static final int NOTIFY_ID = 101;
        private PendingIntent pendingIntent;
        private NotificationManager notificationManager;
        private Context context;


        private DialogFB dialogFB;
        private String currentUserUid;
        private DatabaseReference databaseReference;
        private List<Dialog> dialogList;

        public NotificationRunnable(Context context) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            this.context = context;
            FirebaseApp.initializeApp(context);
            currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dialogFB = DialogFB.getInstance();

            databaseReference = FirebaseDatabase.getInstance().getReference();

        }


        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                    sendNotification();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            databaseReference.child("dialogs").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Log.d(LOG_TAG, "NotificationDataChange");
//                    for (; ; ) {
//                        List<Dialog> dialogListFromBD = dialogFB.getContactDialogList(currentUserUid);
//                        if (dialogListFromBD == null){
//                            try {
//                                Thread.sleep(500);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }else if (!dialogList.equals(dialogListFromBD)) {
//                            dialogList = dialogListFromBD;
//                            Log.d(LOG_TAG, "SendMessage");
//                            sendNotification();
//                            break;
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
        }

        void sendNotification() {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            builder.setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_account_black_48dp)
//                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.hungrycat))
                    .setTicker("Мессенджер")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle("Имя")
                    .setContentText("Новое сообщение"); // Текст уведомления

            Notification notification = builder.build();
            notificationManager.notify(NOTIFY_ID, notification);
        }
    }
}
