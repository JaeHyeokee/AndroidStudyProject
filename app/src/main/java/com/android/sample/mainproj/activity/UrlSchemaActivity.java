package com.android.sample.mainproj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.log.LogService;

public class UrlSchemaActivity extends AppCompatActivity
{
	private Activity activity;

	private ImageButton ibtn_back_click;

	private EditText et_run_package;

	private Button btn_run_package;

	private EditText et_schema_send_id,	et_schema_send_msg;

	private TextView tv_schema_res_msg;

	private Button btn_url_schema;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_url_schema);

			init();

			setting();

			addListener();
		}
		catch(Exception ex)
		{
			LogService.error(activity, ex.getMessage(), ex);
		}
	}

	private void init()
	{
		activity = this;

		ibtn_back_click = findViewById(R.id.ibtn_back_click);

		et_run_package = findViewById(R.id.et_run_package);

		btn_run_package = findViewById(R.id.btn_run_package);

		et_schema_send_id = findViewById(R.id.et_schema_send_id);

		et_schema_send_msg = findViewById(R.id.et_schema_send_msg);

		tv_schema_res_msg = findViewById(R.id.tv_schema_res_msg);

		btn_url_schema = findViewById(R.id.btn_url_schema);
	}

	private void setting()
	{
		checkURLSchema();
	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		btn_run_package.setOnClickListener(listener_run_package);

		btn_url_schema.setOnClickListener(listener_url_schema);
	}

	private View.OnClickListener listener_back_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};

	private View.OnClickListener listener_run_package = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			try
			{
				String packageName = et_run_package.getText().toString();

				if(packageName.isEmpty() == false)
				{
					Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);

					if(intent == null)
					{
						Intent marketIntent = new Intent(Intent.ACTION_VIEW);
						marketIntent.setData(Uri.parse("market://details?id=" + packageName));
						startActivity(marketIntent);
					}
					else
					{
						intent.addCategory(Intent.CATEGORY_DEFAULT);

						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

						startActivity(intent);
					}
				}
				else
				{
					Toast.makeText(activity, "실행 패키지 명을 입력하여 주세요.", Toast.LENGTH_SHORT).show();
				}
			}
			catch(Exception ex)
			{
				LogService.error(activity, ex.getMessage(), ex);
			}
		}
	};

	private View.OnClickListener listener_url_schema = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			try
			{
				String id = et_schema_send_id.getText().toString();

				String msg = et_schema_send_msg.getText().toString();

				// URL 스키마로 설치되어 있는 해당하는 URL 스키마를 가지고 있는 앱을 실행 할 수있습니다.
				// android.intent.action.VIEW : 사용자에게 데이터를 넘길 수 있음
				// android.intent.category.BROWSABLE : 웹 브라우저에서도 호출 할 수 있음
				// android.intent.category.DEFAULT : 암시적 인텐트를 수신할 수 있음
				// 위 내용이 호출하고자 하는 앱에 설정되어 있어야한다.
				// http도 스키마의 한 종류이기 떄문에 http://www.naver.com 의 경우
				// http : 스키마, www.naver.com을 호스트로 생각하면 된다.
				String urlSchme = "supp://android?id=" + id + "&message=" + msg;

				Intent intent = Intent.parseUri(urlSchme, Intent.URI_INTENT_SCHEME);

				startActivity(intent);
			}
			catch(Exception ex)
			{
				LogService.error(activity, ex.getMessage(), ex);
			}
		}
	};

	private void checkURLSchema()
	{
		Intent intent = getIntent();

		if(Intent.ACTION_VIEW.equals(intent.getAction()))
		{
			Uri uri = intent.getData();

			if(uri != null)
			{
				if(uri.getScheme().equals("mpp") && uri.getHost().equals("android"))
				{
					String message = uri.getQueryParameter("message");

					tv_schema_res_msg.setText(message);
				}
			}
		}
	}
}