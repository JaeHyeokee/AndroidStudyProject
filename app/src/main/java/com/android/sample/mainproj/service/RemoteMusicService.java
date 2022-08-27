package com.android.sample.mainproj.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;

import com.android.sample.mainproj.R;

public class RemoteMusicService extends Service
{
	public static final String ACTION_NAME = "com.android.service.REMOTE_MUSIC";

	private MediaPlayer mediaPlayer;

	public class RemoteServiceImpl extends IRemoteMusicService.Stub
	{
		@Override
		public String getPosition() throws RemoteException
		{
			if(mediaPlayer != null && mediaPlayer.isPlaying())
			{
				return mediaPlayer.getCurrentPosition() / 1000 + "초 / " + mediaPlayer.getDuration() / 1000 + "초";
			}
			else
			{
				return "";
			}
		}

		@Override
		public void play() throws RemoteException
		{
			if(mediaPlayer != null && !mediaPlayer.isPlaying())
			{
				mediaPlayer.start();
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return new RemoteServiceImpl();
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

		mediaPlayer = MediaPlayer.create(this, R.raw.tension);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		if(mediaPlayer != null)
		{
			if(mediaPlayer.isPlaying())
			{
				mediaPlayer.stop();
			}

			mediaPlayer.release();
		}
	}
}