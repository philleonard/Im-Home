Im-Home
=======
Play Store page: <https://play.google.com/store/apps/details?id=com.westcoastlabs.imhome>

WOL service for Android which sends magic packets upon WiFi connection. In effect, the app powers on your computer when you come home.

##License
GNU GENERAL PUBLIC LICENSE Version 3 (GPLv3)
Copyright (C) 2014  West Coast Labs

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

##Introduction
I'm Home is a new Android Application from West Coast Labs which automatically starts up your computer when you arrive at home or work, and runs quietly and efficiently in the background of your Android device.

If you are someone who likes their PC to be on constantly so that you don't have to spend time waiting for you PC to turn on. Then this app is a smart alternative which will help you save on energy bills and do a little extra for the environment!

Alternately if you are someone who shuts their computer down when you leave your home/work and turn it back on when you return, then this app will help reduce the time in your day spent staring at loading screens, and you don't even need to take your phone out!

It works by using the Wake On Lan service built into almost all motherboards, and by sending magic packets to your computer when your android phone joins your WiFi network. Wake On Lan is only provided by ethernet adaptors so your PC will have to be connected to the router via ethernet. (This app will work on WiFi only to wake the PCs in sleep mode (i.e. when the WiFi card is still on)).

##Usage
1. Simply configure your Windows PC/Mac/Linux machine using the tutorial in this link. 
..* The most important part is to ensure that Wake On Lan is enabled in your BIOS (usually that's all that's needed).
2. Download the application on the Play Store here or using the QR code above.
3. Configure the application as follows;

###Parameters
* PC IP or Broadcast IP: This is the broadcast IP of your router or the specific IP of the PC. Note that putting the broadcast IP of the router is more reliable than the specific IP address especially if IP addresses are allocated dynamically on your router. As it says in the app, typically your Broadcast IP will be one of the following; 255.255.255.255, 192.168.0.255 or 255.255.255.0.

* MAC: The MAC address of your computer. To find out, in Windows type "ipconfig /all" into a command prompt and look for "physical address" under your Ethernet Adaptor.
In Mac or Linux type "ifconfig" and look for "HWaddr" under your eth device.

* Port: This is important for when the PC is in sleep mode instead of being completely turned off. (Must be filled out anyway, it will either be 9/7)

* SSID: This is the name of the router to which your phone and computer are connected or connect to. You can use the button to auto fill this box with your current WiFi network's SSID.

* The time slider is used to stop the application from sending packets for a certain amount of time after the last disconnect. This is useful if you briefly come back but don't want your computer started. Also I noticed that Android phones have a habit of quickly disconnecting and reconnecting from WiFi even when they are in range, in the middle of the night! So this feature also prevents your computer being started when your asleep.

* The first checkbox stops your PC from waking up when you start your phone and connect to WiFi straight away. The second checkbox explains itself.

* The disable from checkbox allows you to setup a time frame in which the app will be disabled and your PC will not be woken.

* All the forms must be filled out when saving and the IP and MAC must be in the correct syntax.

* The send test button sends magic packets using the configuration on the screen (so it doesn't need to be saved).

* Cancel closes the configuration. The PC wakeup part of the app is started when the device connects to the SSID in the configuration.

* Note: On some machines Wake On Lan only works after a clean shutdown. If you turn the power off and on at the switch or at the PSU then the ethernet adapter might not be turned back on again until you start the computer manually.

![alt text](http://1.bp.blogspot.com/-Pbm0K264DL0/UjL59v1OckI/AAAAAAAAJ5M/Wvq6o62zI1w/s1600/device-2013-09-12-174354.png "Screenshot")
