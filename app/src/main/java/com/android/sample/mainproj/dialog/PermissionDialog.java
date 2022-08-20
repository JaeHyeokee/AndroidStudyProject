package com.android.sample.mainproj.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.log.LogService;

public class PermissionDialog extends Dialog
{
	public interface OnDialogClickListener
	{
		public void onYesClick();

		public void onNoClick();
	}

	private Activity activity;

	private TextView tv_perm_msg;

	private Button btn_perm_yes, btn_perm_no;

	private PermissionDialog.OnDialogClickListener onDialogClickListener;

	private String permName;

	private String dialogText;

	public PermissionDialog(@NonNull Context context, String permName)
	{
		super(context, android.R.style.Theme_Translucent_NoTitleBar);

		activity = (Activity) context;

		this.permName = permName;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.dialog_permission);

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
		tv_perm_msg = findViewById(R.id.tv_perm_msg);

		btn_perm_yes = findViewById(R.id.btn_perm_yes);

		btn_perm_no = findViewById(R.id.btn_perm_no);
	}

	private void setting()
	{
		if(dialogText != null)
		{
			tv_perm_msg.setText(dialogText);
		}
		else
		{
			tv_perm_msg.setText(tv_perm_msg.getText().toString().replace("%PER%", permName));
		}
	}

	private void addListener()
	{
		btn_perm_yes.setOnClickListener(listener_perm_yes);

		btn_perm_no.setOnClickListener(listener_perm_no);
	}

	private View.OnClickListener listener_perm_yes = new View.OnClickListener()
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

	private View.OnClickListener listener_perm_no = new View.OnClickListener()
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

	public void setOnDialogClickListener(PermissionDialog.OnDialogClickListener onDialogClickListener)
	{
		this.onDialogClickListener = onDialogClickListener;
	}

	public void setDialogText(String text)
	{
		this.dialogText = text;
	}

	@Override
	public void onBackPressed()
	{
		activity.onBackPressed();
	}
}
