package com.westcoastlabs.imhome;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.westcoastlabs.imhome.R;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	EditText ip, mac, port, ssid;
	Button send, save, cancel, timeframe, currentSSID; 
	ImageButton helpButton;
	TextView time;
	SeekBar timeSlide;
	ToggleButton state;
	CheckBox startupBoot, enableNotifications, disableFrom;
	Spinner from, to;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
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
		startupBoot = (CheckBox) findViewById(R.id.checkBox1);
		enableNotifications = (CheckBox) findViewById(R.id.checkBox2);
		helpButton = (ImageButton) findViewById(R.id.imageButton1);
		disableFrom = (CheckBox) findViewById(R.id.checkBox3);
		from = (Spinner) findViewById(R.id.spinner1);
		to = (Spinner) findViewById(R.id.spinner2);
		currentSSID = (Button) findViewById(R.id.button4);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		R.array.time, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		from.setAdapter(adapter);
		to.setAdapter(adapter);
				
		
		loadPrefs();

		if(!disableFrom.isChecked()) {
			from.setEnabled(false);
			to.setEnabled(false);
		}
		
		currentSSID.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
				NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

				if (mWifi.isConnected()) {
					String SSID = getCurrentSsid(getApplicationContext());
					if (SSID != null) {
						ssid.setText(SSID);
					}
					else {
						Toast.makeText(getApplicationContext(), "Error: Can't get SSID!", Toast.LENGTH_SHORT).show();
					}
				}
				
				else {
					Toast.makeText(getApplicationContext(), "Error: Can't get SSID! WiFi not connected", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		disableFrom.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					from.setEnabled(true);
					to.setEnabled(true);
				}
				else if (!isChecked) {
					from.setEnabled(false);
					to.setEnabled(false);
				}
				
			}
		});
		
		
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
	
		helpButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://westcoastlabs.blogspot.com/2013/09/im-home-v10-android-application.html");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				
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
		startupBoot.setChecked(pref.getBoolean("startup", true));
		enableNotifications.setChecked(pref.getBoolean("notifications", true));
		disableFrom.setChecked(pref.getBoolean("disableFrom", false));
		List<String> time = Arrays.asList(getResources().getStringArray(R.array.time));
		from.setSelection(pref.getInt("fromTimePos", 0));
		to.setSelection(pref.getInt("toTimePos", 0));
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
		editor.putBoolean("startup", startupBoot.isChecked());
		editor.putBoolean("notifications", enableNotifications.isChecked());
		editor.putBoolean("disableFrom", disableFrom.isChecked());
		editor.putString("fromTime", from.getSelectedItem().toString());
		editor.putString("toTime", to.getSelectedItem().toString());
		editor.putInt("fromTimePos", from.getSelectedItemPosition());
		editor.putInt("toTimePos", to.getSelectedItemPosition());
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
	
	public static String getCurrentSsid(Context context) {
		  String ssid = null;
		  ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		  if (networkInfo.isConnected()) {
		    final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		    if (connectionInfo != null && connectionInfo.getSSID() != null) {
		      ssid = connectionInfo.getSSID();
		    }
		  }
		  return ssid;
		}

}
