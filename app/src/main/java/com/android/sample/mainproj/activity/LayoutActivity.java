package com.android.sample.mainproj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.log.LogService;

public class LayoutActivity extends AppCompatActivity
{
	public static final String LINEAR = "LINEAR";

	public static final String FRAME = "FRAME";

	public static final String RELATIVE = "RELATIVE";

	public static final String CONSTRAINT = "CONSTRAINT";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setting();
		}
		catch(Exception ex)
		{
			LogService.error(this, ex.getMessage(), ex);
		}
	}

	private void setting()
	{
		Intent intent = getIntent();

		String layout = intent.getStringExtra("LAYOUT");

		if(layout.equals(LINEAR))
		{
			setContentView(R.layout.activity_linear);
		}
		else if(layout.equals(FRAME))
		{
			setContentView(R.layout.activity_frame);
		}
		else if(layout.equals(RELATIVE))
		{
			setContentView(R.layout.activity_relative);
		}
		else if(layout.equals(CONSTRAINT))
		{
			setContentView(R.layout.activity_constraint);
		}
		else
		{
			finish();
		}
	}
}