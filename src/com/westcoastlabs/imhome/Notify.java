package com.westcoastlabs.imhome;

import com.westcoastlabs.imhome.R;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class Notify {
	Context context;
	String mac;
	
	Notify(Context context, String mac) {
		this.context = context;
		this.mac = mac;
		pcWakeupNotify();
	}

	private void pcWakeupNotify() {
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(context)
			    .setSmallIcon(R.drawable.not_icon)
			    .setContentTitle("I'm Home")
			    .setContentText("Waking PC: " + mac);
		Intent resultIntent = new Intent(context, MainActivity.class);
		PendingIntent resultPendingIntent =
			    PendingIntent.getActivity(
			    context,
			    0,
			    resultIntent,
			    PendingIntent.FLAG_UPDATE_CURRENT
			);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotifyMgr = 
		        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(1, mBuilder.build());
	}
	
}
