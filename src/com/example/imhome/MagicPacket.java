package com.example.imhome;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.os.AsyncTask;

public class MagicPacket extends AsyncTask<Object, Object, Object> {

	String ip, mac;
	final int port;
	
	MagicPacket(String ip, String mac, int port) {
		this.ip = ip;
		this.mac = mac;
		this.port = port;
	}
	
	@Override
	protected Object doInBackground(Object... params) {
              
        try {
            byte[] macBytes = getMacBytes(mac);
            byte[] bytes = new byte[6 + 16 * macBytes.length];
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }
            
            InetAddress address = InetAddress.getByName(ip);
            
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
            DatagramSocket socket = new DatagramSocket();
           
            for (int i = 0; i < 5; i++) {
            	socket.send(packet);
            	Thread.sleep(500);
            }
            socket.close();
            
            System.out.println("Wake-on-LAN packets sent.");
        }
        catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Failed to send Wake-on-LAN packet: " + e);
        
        }

        
		return null;
	}
	
	 private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
	        byte[] bytes = new byte[6];
	        String[] hex = macStr.split("(\\:|\\-)");
	        if (hex.length != 6) {
	            throw new IllegalArgumentException("Invalid MAC address.");
	        }
	        try {
	            for (int i = 0; i < 6; i++) {
	                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
	            }
	        }
	        catch (NumberFormatException e) {
	            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
	        }
	        return bytes;
	    }

}
