package com.android.sample.mainproj.activity;

import static com.android.sample.mainproj.config.ReqCodeConfig.REQ_CODE;
import static com.android.sample.mainproj.config.ReqCodeConfig.REQ_MAIN_ACTIVITY;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.log.LogService;

public class ResultCodeActivity extends AppCompatActivity
{
	private ImageButton ibtn_back_click;

	private Button btn_res_code_send;

	private Button btn_res_param_send;

	private Button btn_req_code_send;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_result_code);

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
		ibtn_back_click = findViewById(R.id.ibtn_back_click);

		btn_res_code_send = findViewById(R.id.btn_res_code_send);

		btn_res_param_send = findViewById(R.id.btn_res_param_send);

		btn_req_code_send = findViewById(R.id.btn_req_code_send);
	}

	private void setting()
	{

	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		btn_res_code_send.setOnClickListener(listener_res_code_send);

		btn_res_param_send.setOnClickListener(listener_res_param_send);

		btn_req_code_send.setOnClickListener(listener_req_code_send);
	}

	private View.OnClickListener listener_back_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			setResult(RESULT_CANCELED);
			finish();
		}
	};

	private View.OnClickListener listener_res_code_send = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			setResult(RESULT_OK);
			finish();
		}
	};

	private View.OnClickListener listener_res_param_send = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.putExtra("DATA", "TEST_DATA");
			setResult(RESULT_OK, intent);
			finish();
		}
	};

	private View.OnClickListener listener_req_code_send = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = getIntent();

			if(intent.getIntExtra(REQ_CODE, -1) == REQ_MAIN_ACTIVITY)
			{
				intent.putExtra("DATA", "MAIN_DATA");
			}

			setResult(RESULT_OK, intent);
			finish();
		}
	};
}