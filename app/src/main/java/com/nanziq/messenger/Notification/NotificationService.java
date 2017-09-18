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
    ExecutorService executorService;

    public NotificationService() {
        Log.d(LOG_TAG, "Service");
        context = this;
        executorService = Executors.newFixedThreadPool(1);
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
        executorService.execute(runnable);
        return super.onStartCommand(intent, flags, startId);
    }

    private class NotificationRunnable implements Runnable {

        private static final int NOTIFY_ID = 101;
        private PendingIntent pendingIntent;
        private NotificationManager notificationManager;
        private Context context;

        private DialogFB dialogFB = DialogFB.getInstance();
        private String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        private DatabaseReference databaseReference;
        private List<Dialog> dialogList;

        public NotificationRunnable(Context context) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            this.context = context;

            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("dialogs").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(LOG_TAG, "NotificationDataChange");
                    for (; ; ) {
                        List<Dialog> dialogListFromBD = dialogFB.getContactDialogList(currentUserUid);
                        if (dialogListFromBD == null){
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else if (!dialogList.equals(dialogFB.getContactDialogList(currentUserUid))) {
                            dialogList = dialogFB.getContactDialogList(currentUserUid);
                            sendNotification();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        @Override
        public void run() {
            sendNotification();
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
