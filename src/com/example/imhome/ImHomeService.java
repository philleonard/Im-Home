package com.example.imhome;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;

public class ImHomeService extends Service {

	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
			  
		IntentFilter intentFilter = new IntentFilter();
		WifiStateChange wifiRecv = new WifiStateChange();
		intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		registerReceiver(wifiRecv, intentFilter);
			  
	    return Service.START_NOT_STICKY;
	  }
	  
	  @Override
	  public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	  }

}

