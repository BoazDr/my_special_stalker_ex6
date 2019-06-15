package com.boaz.dragonski.mystalker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class CallDetectionService extends Service {

    private CallDetector callDetector;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        callDetector = new CallDetector(this);
        int retVal = super.onStartCommand(intent, flags, startId);
        callDetector.start();
        return retVal;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callDetector.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}