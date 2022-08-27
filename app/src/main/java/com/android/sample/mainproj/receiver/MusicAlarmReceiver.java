package com.android.sample.mainproj.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.android.sample.mainproj.service.LocalMusicService;
import com.android.sample.mainproj.service.RestartService;

public class MusicAlarmReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			Intent in = new Intent(context, RestartService.class);
			context.startForegroundService(in);
		}
		else
		{
			Intent in = new Intent(context, LocalMusicService.class);
			context.startService(in);
		}
	}
}
