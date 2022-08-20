package com.android.sample.mainproj.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.log.LogService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomDialog extends Dialog
{
	public interface OnDialogClickListener
	{
		public void onYesClick();

		public void onNoClick();
	}

	public interface OnCalendarDialogClickListener
	{
		public void onDoneClick(Date selectDate);
	}

	public enum DialogMode { MODE_CONFIRM, MODE_CALENDAR };

	private Activity activity;

	private DialogMode dialogMode;

	private TextView tv_dialog_title;

	private ImageView ibtn_close;

	// Custom Dialog Object Declare
	private View layout_dialog_confirm;

	private TextView tv_dialog_content;

	private Button btn_dialog_no, btn_dialog_yes;

	// Calendar Dialog Object Declare
	private View layout_dialog_date;

	private CalendarView cv_dialog;

	private Button btn_dialog_done;

	private String title;

	private String content;

	private OnDialogClickListener onDialogClickListener;

	private OnCalendarDialogClickListener onCalendarDialogClickListener;

	public CustomDialog(@NonNull Context context, DialogMode dialogMode)
	{
		super(context, android.R.style.Theme_Translucent_NoTitleBar);

		this.activity = (Activity) context;

		this.dialogMode = dialogMode;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.dialog_custom);

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
		tv_dialog_title = findViewById(R.id.tv_dialog_title);

		ibtn_close = findViewById(R.id.ibtn_close);

		if(dialogMode == DialogMode.MODE_CONFIRM)
		{
			layout_dialog_confirm = findViewById(R.id.layout_dialog_confirm);

			tv_dialog_content = layout_dialog_confirm.findViewById(R.id.tv_dialog_content);

			btn_dialog_no = layout_dialog_confirm.findViewById(R.id.btn_dialog_no);

			btn_dialog_yes = layout_dialog_confirm.findViewById(R.id.btn_dialog_yes);
		}
		else if(dialogMode == DialogMode.MODE_CALENDAR)
		{
			layout_dialog_date = findViewById(R.id.layout_dialog_date);

			cv_dialog = layout_dialog_date.findViewById(R.id.cv_dialog);

			btn_dialog_done = layout_dialog_date.findViewById(R.id.btn_dialog_done);
		}
	}

	private void setting()
	{
		tv_dialog_title.setText(title);

		if(dialogMode == DialogMode.MODE_CONFIRM)
		{
			layout_dialog_confirm.setVisibility(View.VISIBLE);

			tv_dialog_content.setText(content);
		}
		else if(dialogMode == DialogMode.MODE_CALENDAR)
		{
			layout_dialog_date.setVisibility(View.VISIBLE);
		}
	}

	private void addListener()
	{
		ibtn_close.setOnClickListener(listener_btn_close);

		if(dialogMode == DialogMode.MODE_CONFIRM)
		{
			btn_dialog_no.setOnClickListener(listener_dialog_no);

			btn_dialog_yes.setOnClickListener(listener_dialog_yes);
		}
		else if(dialogMode == DialogMode.MODE_CALENDAR)
		{
			cv_dialog.setOnDateChangeListener(listner_change_date);

			btn_dialog_done.setOnClickListener(listener_dialog_done);
		}
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public void setDialogOnClickListener(OnDialogClickListener onDialogClickListener)
	{
		this.onDialogClickListener = onDialogClickListener;
	}

	public void setCalendarDialogClickListener(OnCalendarDialogClickListener onCalendarDialogClickListener)
	{
		this.onCalendarDialogClickListener = onCalendarDialogClickListener;
	}

	private View.OnClickListener listener_btn_close = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			dismiss();
		}
	};

	private View.OnClickListener listener_dialog_no = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			dismiss();

			if(onDialogClickListener != null)
			{
				onDialogClickListener.onNoClick();
			}
		}
	};

	private View.OnClickListener listener_dialog_yes = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			dismiss();

			if(onDialogClickListener != null)
			{
				onDialogClickListener.onYesClick();
			}
		}
	};

	private CalendarView.OnDateChangeListener listner_change_date = new CalendarView.OnDateChangeListener()
	{
		@Override
		public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
		{
			Calendar calendar = Calendar.getInstance();

			calendar.set(year, month, dayOfMonth);

			cv_dialog.setDate(calendar.getTimeInMillis());
		}
	};

	private View.OnClickListener listener_dialog_done = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			dismiss();

			Date selectDate = new Date(cv_dialog.getDate());

			if(onCalendarDialogClickListener != null)
			{
				onCalendarDialogClickListener.onDoneClick(selectDate);
			}
		}
	};
}
