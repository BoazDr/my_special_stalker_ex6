package com.boaz.dragonski.mystalker;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Objects;

import static com.boaz.dragonski.mystalker.OutgoingCallReceiver.RECEIVED_ACTION;
import static com.boaz.dragonski.mystalker.OutgoingCallReceiver.SENT_ACTION;

public class SmsService extends IntentService {

    private static final int SENT_ID = 10;
    private static final int RECEIVED_ID = 11;

    public SmsService() {super ("SmsService");}

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Objects.equals(action, RECEIVED_ACTION)){
                receivedSms();
            }
            if (Objects.equals(action, SENT_ACTION)){
                sentSms();
            }
        }
    }

    private void sentSms(){
        Notification myNotification = new NotificationCompat.Builder(this, OutgoingCallReceiver.CHANNEL_ID).setContentTitle("MyStalkerApp")
                .setSmallIcon(R.drawable.ic_launcher_background).setContentText("Message sent successfully!").setPriority(NotificationCompat.PRIORITY_DEFAULT).build();
        NotificationManagerCompat.from(this).notify(SENT_ID, myNotification);
    }

    private void receivedSms(){
        Notification myNotification = new NotificationCompat.Builder(this, OutgoingCallReceiver.CHANNEL_ID).setContentTitle("MyStalkerApp")
                .setSmallIcon(R.drawable.ic_launcher_background).setContentText("Message received successfully!").setPriority(NotificationCompat.PRIORITY_DEFAULT).build();
        NotificationManagerCompat.from(this).notify(RECEIVED_ID, myNotification);
    }
}
