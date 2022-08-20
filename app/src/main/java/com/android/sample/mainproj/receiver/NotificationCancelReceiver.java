package com.android.sample.mainproj.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationCancelReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		try
		{
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

			notificationManager.cancel(intent.getIntExtra("NOTIFICATION_ID", 0));
		}
		catch(Exception ex)
		{
			Log.e(ex.getClass().getName(), ex.getMessage(), ex);
		}
	}
}
