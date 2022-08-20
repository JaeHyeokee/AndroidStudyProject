package com.android.sample.mainproj.listener;

import android.content.DialogInterface;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;

import com.android.sample.mainproj.R;

public class RadioCheckedChangeListener implements RadioGroup.OnCheckedChangeListener
{
	private int beforeCheckedId;

	private OnCheckedChangedListener callback;

	public interface OnCheckedChangedListener
	{
		public void onCheckedChanged(RadioGroup group, int checkedId, int beforeCheckedId);
	}

	public RadioCheckedChangeListener(RadioGroup radioGroup, OnCheckedChangedListener callback)
	{
		this.beforeCheckedId = radioGroup.getCheckedRadioButtonId();

		this.callback = callback;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		this.onCheckedChanged(group, checkedId, beforeCheckedId);
	}

	public void onCheckedChanged(RadioGroup group, int checkedId, int beforeCheckedId)
	{
		this.callback.onCheckedChanged(group, checkedId, beforeCheckedId);

		this.beforeCheckedId = checkedId;
	}
}
