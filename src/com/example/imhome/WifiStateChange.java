package com.example.imhome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

public class WifiStateChange extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
	    if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
	    	
	    			NetworkInfo nwInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
	    			
	        	    if(NetworkInfo.State.CONNECTED.equals(nwInfo.getState())) {
		        	     WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		   	        	 WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		   	        	 
		   	        	 String ssid = wifiInfo.getSSID();
		   	        	 System.out.println("SSID: " + ssid);
		   	        	 
		   	        	 
		   	        	SharedPreferences pref = context.getSharedPreferences("setup", Context.MODE_PRIVATE);
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
		   	        	
		   	        	/*System.out.println("Threshold: " + time);
		   	        	System.out.println("Start Time: " + startTime);
		   	        	System.out.println("Current Time: " + current);
		   	        	System.out.println("Time when can send: " + (time + startTime));*/
		   	        	
		   	        	if ((startTime + time) < current) {
				        	if (ssid.equals(prefssid) && state) {
				        		AsyncTask<Object, Object, Object> send = new MagicPacket(ip, mac, port).execute();
				        	} 
		   	        	}
		   	        	else {
		   	        		System.out.println("Reconnect too early, not sending packets");
		   	        	}
	        	    }
	        	         
	        	    else if (NetworkInfo.State.DISCONNECTED.equals(nwInfo.getState())) {
	        	    	SharedPreferences pref = context.getSharedPreferences("setup", Context.MODE_PRIVATE);
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