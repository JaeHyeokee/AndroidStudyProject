package com.android.sample.mainproj.activity;

import static com.android.sample.mainproj.service.LocalMusicService.FLAG_MUSIC_STOP;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.log.LogService;
import com.android.sample.mainproj.service.IRemoteMusicService;
import com.android.sample.mainproj.service.LocalMusicService;
import com.android.sample.mainproj.service.RemoteMusicService;

/*
서비스는 특정 액티비티와 관계없이 백그라운드에 동작 할 수 있는 컴포넌트이다.
액티비티를 종료해도 지속적으로 동작해야 하는 작업의 경우 서비스 등록을 한다.
서비스는 두가지 서비스로 나뉘는데 로컬 서비스와 원격서비스이다.
로컬 서비스는 onCreate -> onStartCommand -> onResume -> 서비스실행 -> 서비스중단 -> onDestroy -> 서비스종료
원격 서비스는 onCreate -> onBind -> 서비스실행 -> 서비스중단 -> onUnbind -> onDestroy -> 서비스 종료
                                                       -> onRebind -> 서비스실행 ...
와 같이 동작한다.
원격 서비스를 사용할 경우 AIDL(Android Interface Definition Language) 라고 하는
클라이언트와 서비스가 모두 동의한 프로그램 인터페이스를 정의하여 IPC 통신을 하도록 한다.
*/
public class MusicServiceActivity extends AppCompatActivity
{
	private Activity activity;

	private ImageButton ibtn_back_click;

	private Button btn_local_music_start, btn_local_music_stop;

	private Button btn_remote_music_start, btn_remote_music_stop, btn_remote_music_time;

	private IRemoteMusicService iRemoteMusicService;

	private Intent musicIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_music);

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

		btn_local_music_start = findViewById(R.id.btn_local_music_start);

		btn_local_music_stop = findViewById(R.id.btn_local_music_stop);

		btn_remote_music_start = findViewById(R.id.btn_remote_music_start);

		btn_remote_music_stop = findViewById(R.id.btn_remote_music_stop);

		btn_remote_music_time = findViewById(R.id.btn_remote_music_time);
	}

	private void setting()
	{

	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		btn_local_music_start.setOnClickListener(listener_local_music_start);

		btn_local_music_stop.setOnClickListener(listener_local_music_stop);

		btn_remote_music_start.setOnClickListener(listener_remote_music_start);

		btn_remote_music_stop.setOnClickListener(listener_remote_music_stop);

		btn_remote_music_time.setOnClickListener(listener_remote_music_time);
	}

	private View.OnClickListener listener_back_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};

	private View.OnClickListener listener_local_music_start = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			checkLoaclMusicIntent();

			startService(musicIntent);
		}
	};

	private View.OnClickListener listener_local_music_stop = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			checkLoaclMusicIntent();

			LocalMusicService.intent.putExtra(FLAG_MUSIC_STOP, true);

			stopService(musicIntent);
		}
	};

	private View.OnClickListener listener_remote_music_start = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(RemoteMusicService.ACTION_NAME);
			intent.setPackage(getPackageName());

			bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
		}
	};

	private View.OnClickListener listener_remote_music_stop = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if(iRemoteMusicService != null)
			{
				unbindService(serviceConnection);
			}
		}
	};

	private View.OnClickListener listener_remote_music_time = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			String position = "";

			try
			{
				if(iRemoteMusicService != null)
				{
					position = iRemoteMusicService.getPosition();

					Toast.makeText(activity, "Position " + position, Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(activity, "음악이 실행 중이지 않습니다.", Toast.LENGTH_SHORT).show();
				}
			}
			catch(Exception ex)
			{
				LogService.error(activity, ex.getMessage(), ex);
			}
		}
	};

	private void checkLoaclMusicIntent()
	{
		if(LocalMusicService.intent == null)
		{
			musicIntent = new Intent(LocalMusicService.ACTION_NAME);
			musicIntent.setPackage(getPackageName());
		}
		else
		{
			musicIntent = LocalMusicService.intent;
		}
	}

	private ServiceConnection serviceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			iRemoteMusicService = IRemoteMusicService.Stub.asInterface(service);

			try
			{
				iRemoteMusicService.play();
			}
			catch(RemoteException rEx)
			{
				LogService.error(activity, rEx.getMessage(), rEx);
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			iRemoteMusicService = null;
		}
	};
}