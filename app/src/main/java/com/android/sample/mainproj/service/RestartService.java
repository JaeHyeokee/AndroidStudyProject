package com.android.sample.mainproj.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.activity.MainActivity;

public class RestartService extends Service
{
	public RestartService()
	{
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
		builder.setSmallIcon((R.mipmap.ic_launcher));
		builder.setContentTitle(null);
		builder.setContentText(null);
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		builder.setContentIntent(pendingIntent);

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_NONE));
		}

		Notification notification = builder.build();
		startForeground(9, notification);

		Intent in = new Intent(this, LocalMusicService.class);
		startService(in);

		stopForeground(true);
		stopSelf();

		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}