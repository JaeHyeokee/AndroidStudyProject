package com.android.sample.mainproj.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.receiver.MusicAlarmReceiver;

import java.util.Calendar;

public class LocalMusicService extends Service
{
	public static Intent intent;

	public static final String ACTION_NAME = "com.android.service.MUSIC";

	public static final String FLAG_MUSIC_STOP = "MUSIC_EXIT_FLAG";

	private static MediaPlayer mediaPlayer = null;

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

		if(mediaPlayer == null)
		{
			mediaPlayer = MediaPlayer.create(this, R.raw.tension);
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		if(intent.getBooleanExtra(FLAG_MUSIC_STOP, false))
		{
			if(mediaPlayer != null)
			{
				if(mediaPlayer.isPlaying())
				{
					mediaPlayer.stop();
				}
				mediaPlayer.release();
				mediaPlayer = null;
			}
		}
		else
		{
			setAlarmTimer();
		}
	}

	protected void setAlarmTimer()
	{
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.add(Calendar.SECOND, 1);
		Intent intent = new Intent(this, MusicAlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		LocalMusicService.intent = intent;

		if(mediaPlayer != null && mediaPlayer.isPlaying() == false)
		{
			mediaPlayer.start();
		}

		return START_STICKY;
	}
}