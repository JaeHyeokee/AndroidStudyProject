package com.android.sample.mainproj.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.dialog.CustomDialog;
import com.android.sample.mainproj.log.LogService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomDialogActivity extends AppCompatActivity
{
	private Activity activity;

	private ImageButton ibtn_back_click;

	private TextView tv_dialog_result;

	private Button btn_call_custom_dialog;

	private Button btn_call_calender_dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_custom_dialog);

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

		tv_dialog_result = findViewById(R.id.tv_dialog_result);

		btn_call_custom_dialog = findViewById(R.id.btn_call_custom_dialog);

		btn_call_calender_dialog = findViewById(R.id.btn_call_calender_dialog);
	}

	private void setting()
	{

	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		btn_call_custom_dialog.setOnClickListener(listener_call_custom_dialog);

		btn_call_calender_dialog.setOnClickListener(listener_call_calender_dialog);
	}

	private View.OnClickListener listener_back_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};

	private View.OnClickListener listener_call_custom_dialog = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			CustomDialog customDialog = new CustomDialog(activity, CustomDialog.DialogMode.MODE_CONFIRM);

			customDialog.setTitle("커스텀 다이얼 로그 호출");

			customDialog.setContent("커스텀 다이얼 로그를 호출하였습니다.");

			customDialog.setDialogOnClickListener(new CustomDialog.OnDialogClickListener()
			{
				@Override
				public void onYesClick()
				{
					tv_dialog_result.setText("커스텀 다이얼로그의 예 버튼 클릭");
				}

				@Override
				public void onNoClick()
				{
					tv_dialog_result.setText("커스텀 다이얼로그의 아니오 버튼 클릭");
				}
			});
			
			customDialog.show();
		}
	};

	private View.OnClickListener listener_call_calender_dialog = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			CustomDialog customDialog = new CustomDialog(activity, CustomDialog.DialogMode.MODE_CALENDAR);

			customDialog.setTitle("캘린더 다이얼로그");
			
			// onDoneClick 콜백 함수가 호출
			customDialog.setCalendarDialogClickListener(new CustomDialog.OnCalendarDialogClickListener()
			{
				@Override
				public void onDoneClick(Date selectDate)
				{
					String result = new SimpleDateFormat("yyyy년 MM월 dd일").format(selectDate);

					tv_dialog_result.setText(result);
				}
			});

			customDialog.show();
		}
	};
}