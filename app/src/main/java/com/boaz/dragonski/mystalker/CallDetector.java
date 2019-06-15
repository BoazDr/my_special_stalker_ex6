package com.boaz.dragonski.mystalker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.widget.Toast;

public class CallDetector {

    public static final String MY_PREF = "MY_PREF";
    public static final String NUMBER_KEY = "NUMBER_KEY";

    private SharedPreferences sharedPreferences;

    public class OutgoingCallDetector  extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Toast.makeText(context,"Outgoing: "+phoneNumber, Toast.LENGTH_LONG).show();
            sharedPreferences = ctx.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(NUMBER_KEY, phoneNumber);
            editor.commit();

        }

    }

    private Context ctx;
    private OutgoingCallDetector outgoingDetector;

    public CallDetector(Context ctx) {
        this.ctx = ctx;
        outgoingDetector = new OutgoingCallDetector();
    }

    public void start() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        ctx.registerReceiver(outgoingDetector, intentFilter);
    }

    public void stop(){
        ctx.unregisterReceiver(outgoingDetector);
    }
}
