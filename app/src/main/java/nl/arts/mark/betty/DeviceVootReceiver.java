package nl.arts.mark.betty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceVootReceiver extends BroadcastReceiver {
    public DeviceVootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            /* Setting the alarm here */
            HomeActivity.setService(context);
        }
    }
}
