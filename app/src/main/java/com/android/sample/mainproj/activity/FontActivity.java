package com.android.sample.mainproj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.log.LogService;

public class FontActivity extends AppCompatActivity
{
	private ImageButton ibtn_back_click;

	private TextView tv_font_change;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_font);

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

		tv_font_change = findViewById(R.id.tv_font_change);
	}

	private void setting()
	{
		Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/yeongdo.ttf");

		tv_font_change.setTypeface(typeface);
	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);
	}

	private View.OnClickListener listener_back_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};
}