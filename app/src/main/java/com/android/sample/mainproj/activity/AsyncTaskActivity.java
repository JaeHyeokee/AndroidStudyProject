package com.android.sample.mainproj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.log.LogService;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AsyncTaskActivity extends AppCompatActivity
{
	private Activity activity;

	private Disposable backgroundTask;

	private ProgressDialog progress;

	private ImageButton ibtn_back_click;

	private TextView tv_server_conn_status;

	private Button btn_server_conn;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_async);

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

		tv_server_conn_status = findViewById(R.id.tv_server_conn_status);

		btn_server_conn = findViewById(R.id.btn_server_conn);
	}

	private void setting()
	{

	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		btn_server_conn.setOnClickListener(listener_server_conn);
	}

	private View.OnClickListener listener_back_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};

	private View.OnClickListener listener_server_conn = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			asyncTask();
		}
	};

	// 기존에는 어싱크태스크를 안드로이드에서 제공하여 백그라운드에서 원격의 이미지 파일을 다운로드 하는 등의
	// 소비적인 작업을 메인 스레드에 영향을 주지 않으면서 처리하였다.
	// 그렇게 사용되던 AsyncTask 가 메모리 누수등의 문제로 인해
	// 2019년 11월 8일 Deplecate 가 되었다.
	// 하여 AsyncTask 의 대체수단인 rxjava 에 대해서 알아보자.
	// rxjava 는 안드로이드 새로 생성시 기본적으로 제공되는 라이브러리가 아니기 때문에
	// build.gradle에서 rxjava를 추가하여 사용하여야 한다.
	private void asyncTask()
	{
		disableUI();

		// onPreExcute(task 시작 전 실행될 코드를 여기에 작성)
		progress = new ProgressDialog(activity);
		progress.setMessage("서버 접속 시도 중 입니다...");
		progress.setCancelable(false);
		progress.show();

		backgroundTask = Observable.fromCallable(new Callable<Object>()
		{
			@Override
			public Object call() throws Exception
			{
				String data = "";

				// doInBackground(task에서 실행할 코드를 여기에 작성)
				try
				{
					data = connectServer();
				}
				catch(Exception ex)
				{
					LogService.error(activity, ex.getMessage(), ex);
				};

				return data;
			}
		})
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(new Consumer<Object>()
		{
			@Override
			public void accept(Object o) throws Exception
			{
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						enableUI();

						tv_server_conn_status.setText(o.toString());
					}
				});

				backgroundTask.dispose();

				// onPostExcute(task가 끝난 후 실행 될 코드를 여기에 작성)
				progress.dismiss();
			}
		});
	}

	private String connectServer()
	{
		String addr = "http://192.168.1.30:8081/conn";

		InputStream inputStream = null;

		InputStreamReader inputStreamReader = null;

		BufferedReader bufferedReader = null;

		StringBuffer sb = null;

		String resBody = "";

		String resultMsg = "";

		try
		{
			URL url = new URL(addr);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			// 웹 서버 연결 최대 시간을 최대 5초로 설정
			connection.setConnectTimeout(5000);

			// 웹 서버로 부터 데이터를 읽어오는 최대 시간을 5초로 설정
			connection.setReadTimeout(5000);

			connection.setRequestMethod("GET");

			connection.setRequestProperty("Accept", "application/json");

			connection.setRequestProperty("Content-Type", "application/json");

			// 녹스와 같은 AVD(Android Virtual Device)는 에러를 내뱉지 않지만
			// API 28 버전 이후부터 http 접속을 시도하려면
			// AndroidManifest 에
			// usesClearTraffic="true" 추가하여 주어야 한다.
			// 또한 android.permission.INTERNET 권한을 추가하여
			// 네트워크 접속 권한을 주어야한다.
			connection.connect();

			int code = connection.getResponseCode();

			LogService.info(activity, "resCode : " + code);

			inputStream = connection.getInputStream();

			inputStreamReader = new InputStreamReader(inputStream);

			bufferedReader = new BufferedReader(inputStreamReader);

			sb = new StringBuffer();

			String line;

			while((line = bufferedReader.readLine()) != null)
			{
				sb.append(line);
			}

			resBody = sb.toString();

			connection.disconnect();

			LogService.info(activity, resBody);

			JSONObject jsonObject = new JSONObject(resBody);

			if(jsonObject.get("result").equals(true))
			{
				resultMsg = (String) jsonObject.get("msg");
			}
		}
		catch(SocketTimeoutException stEx)
		{
			resultMsg = "서버 타임아웃이 초과되어 연결에 실패하였습니다.";
			LogService.error(activity, resultMsg, stEx);
		}
		catch(Exception ex)
		{
			resultMsg = "서버 연결 중 알수 없는 에러가 발생하였습니다.";
			LogService.error(activity, resultMsg, ex);
		}
		finally
		{
			try
			{
				if(bufferedReader != null)
				{
					bufferedReader.close();
				}

				if(inputStreamReader != null)
				{
					inputStreamReader.close();
				}

				if(inputStream != null)
				{
					inputStream.close();
				}
			}
			catch(IOException ioEx)
			{
				LogService.error(activity, ioEx.getMessage());
			}
		}

		return resultMsg;
	}

	private void enableUI()
	{
		ibtn_back_click.setEnabled(true);

		btn_server_conn.setEnabled(true);
	}

	private void disableUI()
	{
		ibtn_back_click.setEnabled(false);

		btn_server_conn.setEnabled(false);
	}
}