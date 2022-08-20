package com.android.sample.mainproj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.listener.TextChangedListener;
import com.android.sample.mainproj.log.LogService;
import com.android.sample.mainproj.toast.CustomToast;

public class CustomToastActivity extends AppCompatActivity implements View.OnClickListener
{
	private Activity activity;

	private ImageButton ibtn_back_click;

	private Spinner sp_toast_gravity;

	private EditText et_toast_x_pos;

	private EditText et_toast_y_pos;

	private Button btn_show_toast;

	private Button btn_show_custom_toast;

	private Button btn_custom_toast_red, btn_custom_toast_green, btn_custom_toast_blue;

	private int toastGravity;

	private TextChangedListener editXChangedListener;

	private TextChangedListener editYChangedListener;

	private int toastXPos = 0;

	private int toastYPos = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_custom_toast);

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

		sp_toast_gravity = findViewById(R.id.sp_toast_gravity);

		et_toast_x_pos = findViewById(R.id.et_toast_x_pos);

		et_toast_y_pos = findViewById(R.id.et_toast_y_pos);

		btn_show_toast = findViewById(R.id.btn_show_toast);

		btn_show_custom_toast = findViewById(R.id.btn_show_custom_toast);

		btn_custom_toast_red = findViewById(R.id.btn_custom_toast_red);

		btn_custom_toast_green = findViewById(R.id.btn_custom_toast_green);

		btn_custom_toast_blue = findViewById(R.id.btn_custom_toast_blue);
	}

	private void setting()
	{
		editXChangedListener = new TextChangedListener(et_toast_x_pos, listener_toast_pos_change);

		editYChangedListener = new TextChangedListener(et_toast_y_pos, listener_toast_pos_change);
	}

	private void addListener()
	{
		sp_toast_gravity.setOnItemSelectedListener(listener_change_toast_gravity);

		ibtn_back_click.setOnClickListener(listener_back_click);

		btn_show_toast.setOnClickListener(listener_show_toast);

		btn_show_custom_toast.setOnClickListener(listener_show_custom_toast);

		et_toast_x_pos.addTextChangedListener(editXChangedListener);

		et_toast_y_pos.addTextChangedListener(editYChangedListener);

		btn_custom_toast_red.setOnClickListener(this);

		btn_custom_toast_green.setOnClickListener(this);

		btn_custom_toast_blue.setOnClickListener(this);
	}

	private AdapterView.OnItemSelectedListener listener_change_toast_gravity = new AdapterView.OnItemSelectedListener()
	{
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			TextView textView = (TextView) view;

			String data = textView.getText().toString();

			if(data.equals("TOP"))
			{
				toastGravity = Gravity.TOP;
			}
			else if(data.equals("CENTER"))
			{
				toastGravity = Gravity.CENTER;
			}
			else if(data.equals("BOTTOM"))
			{
				toastGravity = Gravity.BOTTOM;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent)
		{

		}
	};

	private View.OnClickListener listener_back_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};

	private View.OnClickListener listener_show_toast = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Toast toast = Toast.makeText(activity, "토스트 메시지 표시", Toast.LENGTH_SHORT);

			toast.setGravity(toastGravity, toastXPos , toastYPos);

			toast.show();
		}
	};

	private View.OnClickListener listener_show_custom_toast = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.toast_custom_image, null);

			TextView textView = layout.findViewById(R.id.tv_toast_img_msg);
			
			textView.setText("커스텀 토스트 메시지 표시");

			Toast toast = new Toast(activity);

			toast.setDuration(Toast.LENGTH_SHORT);

			toast.setGravity(toastGravity, toastXPos, toastYPos);

			toast.setView(layout);

			toast.show();
		}
	};

	private TextChangedListener.OnTextChangeListener listener_toast_pos_change = new TextChangedListener.OnTextChangeListener()
	{
		@Override
		public void onTextChanged(EditText editText, int beforeCursorPos, String beforeText, String currentText)
		{
			TextChangedListener textChangedListener = null;

			if(editText.getId() == et_toast_x_pos.getId())
			{
				textChangedListener = editXChangedListener;
			}
			else if(editText.getId() == et_toast_y_pos.getId())
			{
				textChangedListener = editYChangedListener;
			}

			editText.removeTextChangedListener(textChangedListener);

			String regex = "^[+-]?\\d*$";

			if(currentText.matches(regex) == false)
			{
				editText.setText(beforeText);
				editText.setSelection(beforeCursorPos);
			}

			int pos = 0;

			try
			{
				pos = Integer.parseInt(editText.getText().toString());
			}
			catch(Exception ex){}

			if(editText.getId() == et_toast_x_pos.getId())
			{
				toastXPos = pos;
			}
			else if(editText.getId() == et_toast_y_pos.getId())
			{
				toastYPos = pos;
			}

			editText.addTextChangedListener(textChangedListener);
		}
	};

	@Override
	public void onClick(View v)
	{
		Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/yeongdo.ttf");

		if(v.getId() == R.id.btn_custom_toast_red)
		{
			CustomToast.makeToast(activity, "basic first message").setTextTypeface(typeface).setTextColor(Color.RED).show();
		}
		else if(v.getId() == R.id.btn_custom_toast_green)
		{
			CustomToast.makeToast(activity, "basic first message").setTextTypeface(typeface).setTextColor(Color.GREEN).show();
		}
		else if(v.getId() == R.id.btn_custom_toast_blue)
		{
			CustomToast.makeToast(activity, "basic first message").setTextTypeface(typeface).setTextColor(Color.BLUE).show();
		}
	}
}