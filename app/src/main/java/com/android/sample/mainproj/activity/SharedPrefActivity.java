package com.android.sample.mainproj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.log.LogService;

public class SharedPrefActivity extends AppCompatActivity
{
	private Activity activity;

	private ImageButton ibtn_back_click;

	private EditText et_pref_id;

	private EditText et_pref_pw;

	private EditText et_pref_name;

	private EditText et_pref_age;

	private Button btn_pref_save;

	private Button btn_pref_load;

	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_pref);

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

		et_pref_id = findViewById(R.id.et_pref_id);

		et_pref_pw = findViewById(R.id.et_pref_pw);

		et_pref_name = findViewById(R.id.et_pref_name);

		et_pref_age = findViewById(R.id.et_pref_age);

		btn_pref_save = findViewById(R.id.btn_pref_save);

		btn_pref_load = findViewById(R.id.btn_pref_load);

		preferences = getSharedPreferences("PREF_SETTING", MODE_PRIVATE);
	}

	private void setting()
	{
		loadSharedPrefInfo();
	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		btn_pref_save.setOnClickListener(listener_pref_save);

		btn_pref_load.setOnClickListener(listener_pref_load);
	}

	private View.OnClickListener listener_back_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};

	private View.OnClickListener listener_pref_save = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			SharedPreferences.Editor editor = preferences.edit();

			editor.putString("id", et_pref_id.getText().toString());

			editor.putString("pw", et_pref_pw.getText().toString());

			editor.putString("name", et_pref_name.getText().toString());

			editor.putString("age", et_pref_age.getText().toString());

			editor.apply();
		}
	};

	private View.OnClickListener listener_pref_load = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			loadSharedPrefInfo();
		}
	};

	private void loadSharedPrefInfo()
	{
		et_pref_id.setText(preferences.getString("id", ""));

		et_pref_pw.setText(preferences.getString("pw", ""));

		et_pref_name.setText(preferences.getString("name", ""));

		et_pref_age.setText(preferences.getString("age", ""));
	}
}