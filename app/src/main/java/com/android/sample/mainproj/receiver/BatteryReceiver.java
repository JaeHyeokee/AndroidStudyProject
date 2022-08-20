package com.android.sample.mainproj.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

import com.android.sample.mainproj.log.LogService;

public class BatteryReceiver extends BroadcastReceiver
{
	private Intent intent;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		this.intent = intent;

		int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

		Log.i("TEST", level + "%");
	}

	public String getPlugged()
	{
		String pluggedStr = "";

		if(intent != null)
		{
			int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);

			switch(plugged)
			{
				case BatteryManager.BATTERY_PLUGGED_AC:
					pluggedStr = "BATTERY_PLUGGED_AC";
				break;
				case BatteryManager.BATTERY_PLUGGED_USB:
					pluggedStr = "BATTERY_PLUGGED_USB";
					break;
				case BatteryManager.BATTERY_PLUGGED_WIRELESS:
					pluggedStr = "BATTERY_PLUGGED_WIRELESS";
					break;
			}
		}

		return pluggedStr;
	}

	public String getStatus()
	{
		String statusStr = "";

		if(intent != null)
		{
			int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);

			switch(status)
			{
				case BatteryManager.BATTERY_STATUS_UNKNOWN:
					statusStr = "BATTERY_STATUS_UNKNOWN";
					break;
				case BatteryManager.BATTERY_STATUS_CHARGING:
					statusStr = "BATTERY_STATUS_CHARGING";
					break;
				case BatteryManager.BATTERY_STATUS_DISCHARGING:
					statusStr = "BATTERY_STATUS_DISCHARGING";
					break;
				case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
					statusStr = "BATTERY_STATUS_NOT_CHARGING";
					break;
				case BatteryManager.BATTERY_STATUS_FULL:
					statusStr = "BATTERY_STATUS_FULL";
					break;
			}
		}

		return statusStr;
	}

	public String getLevel()
	{
		String levelStr = "";

		if(intent != null)
		{
			int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

			levelStr = level + "%";
		}

		return levelStr;
	}
}