package com.boaz.dragonski.mystalker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsManager;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.boaz.dragonski.mystalker.MainActivity.PHONE_NUMBER;
import static com.boaz.dragonski.mystalker.MainActivity.SP_KEY;
import static com.boaz.dragonski.mystalker.MainActivity.TEXT_MESSAGE;

public class OutgoingCallReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "MyStalkerChannel";
    public static final String SENT_ACTION = "sentAction";
    public static final String RECEIVED_ACTION = "receivedAction";
    @Override
    public void onReceive(Context context, Intent intent) {
        createChannel(context);
        String outGoingPhoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Log.w(TAG, "Out Number: " + outGoingPhoneNumber);

        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_KEY, MODE_PRIVATE);
        String numberForTextMessage = sharedPreferences.getString(PHONE_NUMBER, null);
        String textMessage = sharedPreferences.getString(TEXT_MESSAGE, null);
        if (outGoingPhoneNumber == null || textMessage == null || numberForTextMessage == null) {
            Log.w(TAG, "something is null");
            return;
        }
        Log.w(TAG, "sending sms");
        PendingIntent receivedIntent = PendingIntent.getService(context, 500, new Intent(RECEIVED_ACTION), PendingIntent.FLAG_ONE_SHOT);
        PendingIntent sentIntent = PendingIntent.getService(context, 500, new Intent(SENT_ACTION), PendingIntent.FLAG_ONE_SHOT);
        sendNotification(context);
        SmsManager.getDefault().sendTextMessage(numberForTextMessage,null,textMessage + " " + outGoingPhoneNumber,sentIntent, receivedIntent);
    }

    private void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "MyStalker", importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void sendNotification(Context context) {
        Notification myNotification = new NotificationCompat.Builder(context, OutgoingCallReceiver.CHANNEL_ID).setContentTitle("MyStalkerApp").setSmallIcon(R.drawable.ic_launcher_foreground).setContentText("Sending Message...").setPriority(NotificationCompat.PRIORITY_DEFAULT).build();
        NotificationManagerCompat.from(context).notify(1, myNotification);
    }
}
