package com.android.sample.mainproj.service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.sample.mainproj.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationService extends Service
{
	public static Intent intent;

	private static LocationRequest locationRequest = null;

	public static final String ACTION_NAME = "com.android.service.LOCATION";

	private final int LOCATION_SERVICE_ID = 101;

	private NotificationManager notificationManager;

	private NotificationCompat.Builder builder;

	@Override
	public void onCreate()
	{
		super.onCreate();

		if(locationRequest == null)
		{
			locationRequest = LocationRequest.create();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		LocationService.intent = intent;

		if(locationRequest != null)
		{
			startLocationBackService();
		}

		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		stopLocationBackService();
	}

	private void stopLocationBackService()
	{
		LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
		stopForeground(true);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@SuppressLint("MissingPermission")
	private void startLocationBackService()
	{
		locationRequest.setInterval(2000);

		locationRequest.setFastestInterval(2000);

		locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);

		LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

		builder = getDefaultBuilder();

		// 백그라운드 서비스를 사용할 경우 사용자에게 알림을 통해 알리도록 되어있다.
		// startForground를 사용하여 Notification을 통해 사용자에게 앱이 상용중임을 알려준다.
		startForeground(LOCATION_SERVICE_ID, builder.build());
	}

	private NotificationCompat.Builder getDefaultBuilder()
	{
		String channelId = "loc_notification_channel";

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			if(notificationManager != null && notificationManager.getNotificationChannel(channelId) == null)
			{
				NotificationChannel notificationChannel = new NotificationChannel
				(
					channelId,
					"location Notification Channel",
					NotificationManager.IMPORTANCE_NONE
				);

				notificationChannel.setDescription("지도 알림 채널");
				notificationChannel.setSound(null, null);
				notificationChannel.setShowBadge(false);
				notificationChannel.setVibrationPattern(new long[]{0});
				notificationChannel.enableVibration(true);

				notificationManager.createNotificationChannel(notificationChannel);
			}
		}
		
		// 여기서 지도 알림을 클릭했을때 동작을 정의 -- 현재 아무것도 작성되지않음
		Intent resultIntent = new Intent();

		// FLAG_UPDATE_CURRENT : 이미 존재할 경우 해당 인텐트로 대체
		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);

		builder.setSmallIcon(R.mipmap.ic_launcher);

		builder.setContentTitle("지도");

		builder.setDefaults(NotificationCompat.DEFAULT_ALL);

		builder.setOnlyAlertOnce(true);

		builder.setContentText("맵 정보 호출중입니다.");

		builder.setContentIntent(pendingIntent);

		builder.setAutoCancel(false);

		builder.setPriority(NotificationCompat.PRIORITY_HIGH);

		return builder;
	}

	private LocationCallback locationCallback = new LocationCallback()
	{
		@Override
		public void onLocationResult(@NonNull LocationResult locationResult)
		{
			super.onLocationResult(locationResult);

			if(locationResult != null && locationResult.getLastLocation() != null)
			{
				double latitude = locationResult.getLastLocation().getLatitude();
				double longitude = locationResult.getLastLocation().getLongitude();

				if(notificationManager != null && builder != null)
				{
					builder.setContentText("위도 : " + latitude + " 경도 : " + longitude);
					notificationManager.notify(LOCATION_SERVICE_ID, builder.build());
				}

				Log.i(this.getClass().getName(), "위도 : " + latitude + " 경도 : " + longitude);

			}
		}
	};

}