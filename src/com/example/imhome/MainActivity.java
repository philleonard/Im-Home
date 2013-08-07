package com.example.imhome;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	EditText ip, mac, port, ssid;
	Button send, save, cancel; 
	TextView time;
	SeekBar timeSlide;
	ToggleButton state;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		        
		ip = (EditText) findViewById(R.id.editText1);
		mac = (EditText) findViewById(R.id.editText2);
		port = (EditText) findViewById(R.id.editText3);
		ssid = (EditText) findViewById(R.id.editText4);	
		save = (Button) findViewById(R.id.button1);
		send = (Button) findViewById(R.id.button3);
		cancel = (Button) findViewById(R.id.button2);
		state = (ToggleButton) findViewById(R.id.toggleButton1);
		time = (TextView) findViewById(R.id.textView8);
		timeSlide = (SeekBar) findViewById(R.id.seekBar1);
		
		loadPrefs();
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!checkFilled()) {
					//Notify empty
					Toast.makeText(getApplicationContext(), "Error: Empty fields", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!checkIp()) {
					//Notify bad ip
					Toast.makeText(getApplicationContext(), "Error: Invalid IP address", Toast.LENGTH_SHORT).show();
					return;
				}
							
				if (!checkMac()) {
					//Notify invalid mac
					Toast.makeText(getApplicationContext(), "Error: Invalid MAC address", Toast.LENGTH_SHORT).show();
					return;
				}
				
				String ipStr = ip.getText().toString();
				String macStr = mac.getText().toString();
				String portStr = port.getText().toString();
	
				int portInt = Integer.parseInt(portStr);

				AsyncTask<Object, Object, Object> send = new MagicPacket(ipStr, macStr, portInt).execute();
				Toast.makeText(getApplicationContext(), "Sending magic packets", Toast.LENGTH_SHORT).show();
			}

			
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!checkFilled()) {
					//Notify empty
					Toast.makeText(getApplicationContext(), "Error: Empty fields", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!checkIp()) {
					//Notify bad ip
					Toast.makeText(getApplicationContext(), "Error: Invalid IP address", Toast.LENGTH_SHORT).show();
					return;
				}
							
				if (!checkMac()) {
					//Notify invalid mac
					Toast.makeText(getApplicationContext(), "Error: Invalid MAC address", Toast.LENGTH_SHORT).show();
					return;
				}
				
				savePrefs();
				Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
			}
		});
		timeSlide.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (progress % 2 == 0) {
					if (progress/2 > 9)
						time.setText(progress/2 + ":00 Min");
					else
						time.setText("0" + progress/2 + ":00 Min");
				}
				else
					if (((int)progress/2) > 9)
						time.setText((int)progress/2 + ":30 Min");
					else
						time.setText("0" + (int)progress/2 + ":30 Min");
				
			}
		});
	}

	private void loadPrefs() {
		SharedPreferences pref = getSharedPreferences("setup", MODE_PRIVATE);
		ip.setText(pref.getString("ip", ""));
		mac.setText(pref.getString("mac", ""));
		port.setText(Integer.toString(pref.getInt("port", 9)));
		ssid.setText(pref.getString("ssid", ""));
		state.setChecked(pref.getBoolean("state", true));
		timeSlide.setProgress(pref.getInt("timeSlide", 0));
		time.setText(pref.getString("time", "00:00 Min"));
	}

	private void savePrefs() {
		SharedPreferences pref = getSharedPreferences("setup", MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putBoolean("state", state.isChecked());
		editor.putString("ip", ip.getText().toString());
		editor.putString("mac", mac.getText().toString());
		editor.putInt("port", Integer.parseInt(port.getText().toString()));
		editor.putString("ssid", ssid.getText().toString());
		editor.putInt("timeSlide", timeSlide.getProgress());
		editor.putString("time", time.getText().toString());
		editor.commit();
	}
	
	private boolean checkMac() {
		byte[] bytes = new byte[6];
        String[] hex = mac.getText().toString().split("(\\:|\\-)");
        if (hex.length != 6) {
            return false;
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            return false;
        }
		return true;
	}

	private boolean checkIp() {
		final Pattern IP_ADDRESS = Pattern.compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
		        + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
		        + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
		        + "|[1-9][0-9]|[0-9]))");
		
		Matcher matcher = IP_ADDRESS.matcher(ip.getText().toString());
		
		if (matcher.matches()) {
		    return true;
		}
		
		return false;
	}

	private boolean checkFilled() {
		
		if(ip.getText().toString().equals("") || 
				mac.getText().toString().equals("") || 
				port.getText().toString().equals("") ||
				ssid.getText().toString().equals(""))
			return false;
		
		return true;
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
