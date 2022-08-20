package com.android.sample.mainproj.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class TextChangedListener implements TextWatcher
{
	public interface OnTextChangeListener
	{
		public void onTextChanged(EditText editText, int beforeCursorPos, String beforeText, String currentText);
	}

	private int beforeCursorPos;

	private String beforeText;

	private String currentText;

	private EditText editText;

	private OnTextChangeListener callback;

	public TextChangedListener(EditText editText, OnTextChangeListener callback)
	{
		this.editText = editText;

		this.callback = callback;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
		beforeCursorPos = start;

		beforeText = s.toString();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		currentText = s.toString();
	}

	@Override
	public void afterTextChanged(Editable s)
	{
		this.callback.onTextChanged(editText, beforeCursorPos, beforeText, currentText);
	}
}
