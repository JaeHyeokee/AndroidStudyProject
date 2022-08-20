package com.android.sample.mainproj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.log.LogService;

import java.util.Arrays;

public class KeyBoardActivity extends AppCompatActivity
{
	private Activity activity;

	private ImageButton ibtn_back_click;

	private ImageView[] iv_pin = new ImageView[6];

	private ImageView[] iv_pin_choice = new ImageView[6];

	private EditText[] et_pin = new EditText[6];

	private String[] pin_value = new String[6];

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_pin);

			init();

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

		int[] pin_r_id = {R.id.iv_pin_1, R.id.iv_pin_2, R.id.iv_pin_3, R.id.iv_pin_4, R.id.iv_pin_5, R.id.iv_pin_6};

		int[] pin_choice_r_id = {R.id.iv_pin_choice_1, R.id.iv_pin_choice_2, R.id.iv_pin_choice_3, R.id.iv_pin_choice_4, R.id.iv_pin_choice_5, R.id.iv_pin_choice_6};

		int[] et_pin_r_id = {R.id.et_pin_1, R.id.et_pin_2, R.id.et_pin_3, R.id.et_pin_4, R.id.et_pin_5, R.id.et_pin_6};

		for(int i = 0; i < 6; i++)
		{
			iv_pin[i] = findViewById(pin_r_id[i]);
			iv_pin_choice[i] = findViewById(pin_choice_r_id[i]);
			et_pin[i] = findViewById(et_pin_r_id[i]);
			pin_value[i] = "";
		}
	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		for(int i = 0; i < 6; i++)
		{
			et_pin[i].setOnFocusChangeListener(listener_focus_change);
			et_pin[i].setOnKeyListener(listener_input_key);
			et_pin[i].addTextChangedListener(new PinTextWatcher(i));
		}
	}

	private View.OnFocusChangeListener listener_focus_change = new View.OnFocusChangeListener()
	{
		@Override
		public void onFocusChange(View v, boolean hasFocus)
		{
			if(hasFocus)
			{
				for(int i = 0; i < 6; i++)
				{
					if(et_pin[i] == v)
					{
						iv_pin_choice[i].setVisibility(View.VISIBLE);
						break;
					}
				}
			}
			else
			{
				for(int i = 0; i < 6; i++)
				{
					if(et_pin[i] == v)
					{
						iv_pin_choice[i].setVisibility(View.INVISIBLE);
						break;
					}
				}
			}
		}
	};

	private View.OnKeyListener listener_input_key = new View.OnKeyListener()
	{
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event)
		{
			if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL)
			{
				for(int i = 0; i < 6; i++)
				{
					if(et_pin[i].getId() == v.getId())
					{
						if(pin_value[i].equals("") && i > 0)
						{
							et_pin[i - 1].requestFocus();
						}
						else
						{
							pin_value[i] = "";
							iv_pin[i].setImageResource(R.drawable.pin_normal);
						}
					}
				}
			}

			LogService.info(activity, "keyCode : " + keyCode);

			return false;
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

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

		return true;
	}

	private class PinTextWatcher implements TextWatcher
	{
		private final int pinNumber;

		public PinTextWatcher(int pinNumber)
		{
			this.pinNumber = pinNumber;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{

		}

		@Override
			public void afterTextChanged(Editable s)
		{
			et_pin[pinNumber].removeTextChangedListener(this);
			et_pin[pinNumber].setText("");
			et_pin[pinNumber].addTextChangedListener(this);

			if(s.toString().equals("."))
			{
				return;
			}

			pin_value[pinNumber] = s.toString();

			iv_pin[pinNumber].setImageResource(R.drawable.pin_write);

			boolean completeCheck = true;

			for(int i = 0; i < 6; i++)
			{
				if(i == pinNumber || pin_value[i].isEmpty() == false)
				{
					continue;
				}
				else
				{
					et_pin[i].requestFocus();
					completeCheck = false;
					break;
				}
			}

			if(completeCheck)
			{
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et_pin[pinNumber].getWindowToken(), 0);

				Toast.makeText(activity, Arrays.toString(pin_value), Toast.LENGTH_SHORT).show();
			}
		}
	}
}