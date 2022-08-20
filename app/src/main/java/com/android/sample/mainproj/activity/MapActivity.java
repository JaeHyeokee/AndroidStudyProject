package com.android.sample.mainproj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.dialog.PermissionDialog;
import com.android.sample.mainproj.log.LogService;
import com.android.sample.mainproj.service.LocationService;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity
{
	private final int REQUEST_MAP_ACCESS = 1005;

	private Activity activity;

	private ImageButton ibtn_back_click;

	private TextView tv_latitude, tv_longitude;

	private Button btn_detect_pos, btn_back_detect_pos;

	private LocationRequest locationRequest;

	private PermissionDialog dialog = null;

	private Boolean locDetectStatus = false;

	private Boolean locBackDetectStatus = false;

	private Intent locIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_map);

			init();

			setting();

			addListener();
		}
		catch(Exception ex)
		{
			LogService.error(this, ex.getMessage(), ex);
		}
	}

	private void init()
	{
		activity = this;

		ibtn_back_click = findViewById(R.id.ibtn_back_click);

		tv_latitude = findViewById(R.id.tv_latitude);

		tv_longitude = findViewById(R.id.tv_longitude);

		btn_detect_pos = findViewById(R.id.btn_detect_pos);

		btn_back_detect_pos = findViewById(R.id.btn_back_detect_pos);
	}

	private void setting()
	{
		locationRequest = LocationRequest.create();
	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		btn_detect_pos.setOnClickListener(listener_detect_pos);

		btn_back_detect_pos.setOnClickListener(listener_back_detect_pos);
	}

	private View.OnClickListener listener_back_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};

	private View.OnClickListener listener_detect_pos = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if(checkMapPermission())
			{
				locDetectStatus = !locDetectStatus;

				if(locDetectStatus)
				{
					locationRequest.setInterval(1000);
					locationRequest.setFastestInterval(1000);
					locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);

					LocationServices.getFusedLocationProviderClient(activity).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

					btn_detect_pos.setText("위치 감지 중지");

					Toast.makeText(activity, "위치 감지를 시작합니다.", Toast.LENGTH_SHORT).show();
				}
				else
				{
					LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(locationCallback);

					btn_detect_pos.setText("위치 감지 시작");

					Toast.makeText(activity, "위치 감지를 중지합니다.", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	private View.OnClickListener listener_back_detect_pos = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			// ACCESS_BACKGROUND_LOCATION 권한을 추가하여 주어야 한다.
			if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == false)
			{
				checkLocationIntent();

				locBackDetectStatus = !locBackDetectStatus;
				
				if(locBackDetectStatus)
				{
					startService(locIntent);

					btn_back_detect_pos.setText("백그라운드 위치 감지 중지");
				}
				else
				{
					stopService(locIntent);

					btn_back_detect_pos.setText("백그라운드 위치 감지 시작");
				}
			}
			else
			{
				requestCustomPermission();
			}
		}
	};

	private void checkLocationIntent()
	{
		if(LocationService.intent == null)
		{
			locIntent = new Intent(LocationService.ACTION_NAME);
			locIntent.setPackage(getPackageName());
			LogService.info(activity, getPackageName());
		}
		else
		{
			locIntent = LocationService.intent;
		}
	}

	private Boolean checkMapPermission()
	{
		// ACCESS_FINE_LOCATION : 기기의 위치 추정치 데이터 접근 권한 요청
		// ACCESS_COARSE_LOCATION : 최대한 정확한 기기의 위치 추정치 데이터 접근 권한 요청
		if
		(
			checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
			checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
		)
		{
			String[] permissions =
			{
				Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
			};
			requestPermissions(permissions, REQUEST_MAP_ACCESS);
		}
		else
		{
			return true;
		}

		return false;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if(grantResults.length > 0)
		{
			if(requestCode == REQUEST_MAP_ACCESS)
			{
				if
				(
					(
						grantResults[0] == PackageManager.PERMISSION_GRANTED &&
						grantResults[1] == PackageManager.PERMISSION_GRANTED
					) == false
				)
				{
					requestUserPermission("위치 접근");
				}
			}
		}
	}

	private void requestUserPermission(String perm)
	{
		dialog = new PermissionDialog(activity, perm);
		dialog.setOnDialogClickListener(new PermissionDialog.OnDialogClickListener()
		{
			@Override
			public void onYesClick()
			{
				Intent appDetail = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

				appDetail.addCategory(Intent.CATEGORY_DEFAULT);

				appDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(appDetail);
			}

			@Override
			public void onNoClick()
			{
				finish();
			}
		});
		dialog.setCanceledOnTouchOutside(false);

		dialog.show();
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

				tv_latitude.setText(String.valueOf(latitude));
				tv_longitude.setText(String.valueOf(longitude));

				SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gv_map);

				supportMapFragment.getMapAsync(new OnMapReadyCallback()
				{
					@Override
					public void onMapReady(@NonNull GoogleMap googleMap)
					{
						LatLng myPostion = new LatLng(latitude, longitude);

						googleMap.clear();

						googleMap.addMarker(new MarkerOptions().position(myPostion).title("나의 위치"));

						googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPostion, 16));
					}
				});
			}
		}
	};

	private void requestCustomPermission()
	{
		dialog = new PermissionDialog(activity, "");
		dialog.setOnDialogClickListener(new PermissionDialog.OnDialogClickListener()
		{
			@Override
			public void onYesClick()
			{
				Intent appDetail = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

				appDetail.addCategory(Intent.CATEGORY_DEFAULT);

				appDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(appDetail);
			}

			@Override
			public void onNoClick()
			{

			}
		});
		dialog.setDialogText("백그라운드 위치 확인을 위해\n사용자가 직접 위치에 대한 권한을\n항상 허용으로 설정할 필요가 있습니다.\n변경화면으로 이동하시겠습니까?");
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
}