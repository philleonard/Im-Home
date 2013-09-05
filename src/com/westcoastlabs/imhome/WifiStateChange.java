package com.westcoastlabs.imhome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.SystemClock;

public class WifiStateChange extends BroadcastReceiver {

	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		SharedPreferences pref = context.getSharedPreferences("setup", Context.MODE_PRIVATE);
		 
		if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
		    	if(networkInfo.isConnected()) {
		    		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			       	 WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			   	        	 
			      	 String ssid = wifiInfo.getSSID();
			      	 System.out.println("SSID: " + ssid);  	 
			   	        
			      	 String ip = pref.getString("ip", "");
			   	     String mac = pref.getString("mac", "");
			   	     int port = pref.getInt("port", 9);
			   	     String prefssid = pref.getString("ssid", "");
			   	     	
			   	     Editor editor = pref.edit();
			   	     editor.putString("currentSSID", ssid);
			   	     editor.commit();
			   	        	
			   	     boolean state = pref.getBoolean("state", false);
			   	  
			   	     long startTime = pref.getLong("counter", 0);
			   	     long current = System.nanoTime();
			   	     long time = getTime(pref);
			   	     
			   	     if (pref.getBoolean("startup", true)) {
			   	    	 System.out.println(SystemClock.elapsedRealtime());
			   	    	 if (SystemClock.elapsedRealtime() > 60000) {
					   	     if ((startTime + time) < current) {
							      	if (ssid.equals(prefssid) && state) {
							       		AsyncTask<Object, Object, Object> send = new MagicPacket(ip, mac, port).execute();
							       		if (pref.getBoolean("notifications", true)) {
							       			Notify not = new Notify(context, mac);
							       		}
							       	} 
					   	     }
					   	     else {
					   	      		System.out.println("Reconnect too early, not sending packets");
					   	     }
			   	    	 }
			   	    	 else
			   	    		System.out.println("Just booted, not sending packets");
			   	     }
			   	     else {
				   	    if ((startTime + time) < current) {
						   	if (ssid.equals(prefssid) && state) {
						   		AsyncTask<Object, Object, Object> send = new MagicPacket(ip, mac, port).execute();
						   	} 
				   	     }
				   	     else {
				   	      		System.out.println("Reconnect too early, not sending packets");
				   	     }
			   	     }
		    	}
		}
		else if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
		    NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		    if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI && !networkInfo.isConnected()) {
		    	String lastSSID = pref.getString("currentSSID", "");
	        	 String prefSSID = pref.getString("ssid", "");
	        	   	
	        	 if (lastSSID.equals(prefSSID)) {
	        		 Editor editor = pref.edit();
			   	     editor.putLong("counter", System.nanoTime());
			   	     editor.commit();
	        	 }
	        	    		
	        	 System.out.println("Disconnect from " + lastSSID);
		    }
		}
		
		
	}

	  private long getTime(SharedPreferences pref) {
			int progress = pref.getInt("timeSlide", 0);
	       	
	        int mins;
	       	int sec;
	       	if (progress % 2 == 0) {
	       		mins = progress/2;
	       		sec = 0;
	       	}
	       	else {
	       		mins = (int) progress/2;
	       		sec = 30;
	       	}
	       	
	       	long milli = (mins * 60000000000L) + (sec * 1000000000L);
			return milli;
		}
}