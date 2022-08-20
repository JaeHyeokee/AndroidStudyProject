package com.android.sample.mainproj.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.sample.mainproj.R;

public class BasicActivity extends AppCompatActivity
{
	private EditText et_basic_input;

	private Button btn_basic_input;

	private TextView tv_basic_output;

	private TextView tv_basic_check;

	private ToggleButton tbtn_basic_check;

	private Switch sw_basic_check;

	private CheckBox cb_basic_check;

	private Button btn_basic_print;

	private TextView tv_basic_print;

	private Button btn_basic_log;

	private Button btn_basic_toast;

	private Button btn_basic_alert;

	private ImageView iv_basic;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_basic);

			init();

			setting();

			addListener();
		}
		catch(Exception ex)
		{
			Log.e(this.getClass().getName(), ex.getMessage(), ex);
		}
	}

	private void init()
	{
		et_basic_input = findViewById(R.id.et_basic_input);

		btn_basic_input = findViewById(R.id.btn_basic_input);

		tv_basic_output = findViewById(R.id.tv_basic_output);

		tv_basic_check = findViewById(R.id.tv_basic_check);

		tbtn_basic_check = findViewById(R.id.tbtn_basic_check);

		sw_basic_check = findViewById(R.id.sw_basic_check);

		cb_basic_check = findViewById(R.id.cb_basic_check);

		btn_basic_print = findViewById(R.id.btn_basic_print);

		tv_basic_print = findViewById(R.id.tv_basic_print);

		btn_basic_log = findViewById(R.id.btn_basic_log);

		btn_basic_toast = findViewById(R.id.btn_basic_toast);

		btn_basic_alert = findViewById(R.id.btn_basic_alert);

		iv_basic = findViewById(R.id.iv_basic);
	}

	private void setting()
	{
		iv_basic.setBackground(new ShapeDrawable(new OvalShape()));

		iv_basic.setClipToOutline(true);
	}

	private void addListener()
	{
		btn_basic_input.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String msg = et_basic_input.getText().toString();

				tv_basic_output.setText(msg);
			}
		});

		tbtn_basic_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(isChecked)
				{
					tv_basic_check.setText("On by TB");
					tv_basic_check.setBackgroundColor(Color.GREEN);
				}
				else
				{
					tv_basic_check.setText("Off by TB");
					tv_basic_check.setBackgroundColor(Color.RED);
				}
			}
		});

		sw_basic_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(isChecked)
				{
					tv_basic_check.setText("On by SW");
					tv_basic_check.setBackgroundColor(Color.GREEN);
				}
				else
				{
					tv_basic_check.setText("Off by SW");
					tv_basic_check.setBackgroundColor(Color.RED);
				}
			}
		});

		cb_basic_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(isChecked)
				{
					tv_basic_check.setText("On by CB");
					tv_basic_check.setBackgroundColor(Color.GREEN);
				}
				else
				{
					tv_basic_check.setText("Off by CB");
					tv_basic_check.setBackgroundColor(Color.RED);
				}
			}
		});

		btn_basic_print.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String msg = et_basic_input.getText().toString();
				boolean isTbChecked = tbtn_basic_check.isChecked();
				boolean isSwChecked = sw_basic_check.isChecked();
				boolean isCbChecked = cb_basic_check.isChecked();

				String result = "EditText : " + msg + "\n" +
								"ToogleButton : " + isTbChecked + "\n" +
								"Switch : " + isSwChecked + "\n" +
								"CheckBox : " + isCbChecked;

				tv_basic_print.setText(result);
			}
		});

		btn_basic_log.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Log.d(this.getClass().getName(), "debug log!!!");
				Log.i(this.getClass().getName(), "info log!!!");
				Log.w(this.getClass().getName(), "warn log!!!");
				Log.e(this.getClass().getName(), "error log!!!");
			}
		});

		btn_basic_toast.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Toast.makeText(BasicActivity.this, "안녕하세요.", Toast.LENGTH_LONG).show();
			}
		});

		btn_basic_alert.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(BasicActivity.this);

				builder.setTitle("타이틀");
				builder.setMessage("알럿 테스트");

				builder.setPositiveButton("긍정", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Toast.makeText(BasicActivity.this, "긍정 클릭!!!", Toast.LENGTH_LONG).show();
					}
				});

				builder.setNegativeButton("부정", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Toast.makeText(BasicActivity.this, "부정 클릭!!!", Toast.LENGTH_LONG).show();
					}
				});

				builder.setNeutralButton("중립", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Toast.makeText(BasicActivity.this, "중립 클릭!!!", Toast.LENGTH_LONG).show();
					}
				});
				
				builder.setCancelable(false);

				builder.show();
			}
		});
	}

}