package com.example.imhome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 
public class BootReceiver extends BroadcastReceiver {
 
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, ImHomeService.class);
        context.startService(service);
    }
}