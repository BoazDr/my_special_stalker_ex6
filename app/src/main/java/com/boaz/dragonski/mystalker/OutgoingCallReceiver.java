package com.boaz.dragonski.mystalker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.boaz.dragonski.mystalker.MainActivity.PHONE_NUMBER;
import static com.boaz.dragonski.mystalker.MainActivity.SP_KEY;
import static com.boaz.dragonski.mystalker.MainActivity.TEXT_MESSAGE;

public class OutgoingCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String outGoingPhoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Log.i(TAG, "Out Number: " + outGoingPhoneNumber);

        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_KEY, MODE_PRIVATE);
        String numberForTextMessage = sharedPreferences.getString(PHONE_NUMBER, null);
        String textMessage = sharedPreferences.getString(TEXT_MESSAGE, null);
        if (outGoingPhoneNumber == null || textMessage == null || numberForTextMessage == null) {
            Log.i(TAG, "something is null");
            return;
        }
        Log.i(TAG, "sending sms");
        SmsManager.getDefault().sendTextMessage(numberForTextMessage,null,textMessage + " " + outGoingPhoneNumber,null, null);
    }
}
