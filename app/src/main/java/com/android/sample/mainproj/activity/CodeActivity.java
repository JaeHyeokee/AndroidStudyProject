package com.android.sample.mainproj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.log.LogService;
import com.android.sample.mainproj.util.UnitUtil;

import java.util.ArrayList;
import java.util.List;

public class CodeActivity extends AppCompatActivity
{
	private Activity activity;

	private LinearLayout mainLayout;

	private ImageButton ibtn_back_click;

	private List<Button> buttonList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView();

			init();

			addListener();
		}
		catch(Exception ex)
		{
			LogService.error(this, ex.getMessage(), ex);
		}
	}

	private void setContentView()
	{
		mainLayout = new LinearLayout(this);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
		(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.MATCH_PARENT
		);

		mainLayout.setLayoutParams(layoutParams);

		mainLayout.setOrientation(LinearLayout.VERTICAL);

		mainLayout.setBackgroundColor(Color.rgb(239, 228, 176));

		// 메뉴 레이아웃 생성
		LinearLayout menuLayout = new LinearLayout(this);

		LinearLayout.LayoutParams menuLayoutParams = new LinearLayout.LayoutParams
		(
			LinearLayout.LayoutParams.MATCH_PARENT,
			UnitUtil.ConvertSizeToDP(this, 46)
		);

		menuLayout.setLayoutParams(menuLayoutParams);

		menuLayout.setOrientation(LinearLayout.HORIZONTAL);

		menuLayout.setBackgroundColor(Color.rgb(255, 255, 255));

		ibtn_back_click = new ImageButton(this);

		ibtn_back_click.setLayoutParams
        (
			new LinearLayout.LayoutParams
			(
				UnitUtil.ConvertSizeToDP(this, 100),
				LinearLayout.LayoutParams.WRAP_CONTENT
			)
        );

		ibtn_back_click.setImageResource(R.drawable.ic_back_arrow);

		ibtn_back_click.setBackgroundColor(Color.TRANSPARENT);

		ibtn_back_click.setScaleType(ImageView.ScaleType.FIT_START);

		ibtn_back_click.setPadding
        (
			UnitUtil.ConvertSizeToDP(this, 35),
			UnitUtil.ConvertSizeToDP(this, 15),
			0,
			UnitUtil.ConvertSizeToDP(this, 15)
        );

		menuLayout.addView(ibtn_back_click);

		mainLayout.addView(menuLayout);

		Button button;

		buttonList = new ArrayList<>();

		for(int i = 1; i <= 10; i++)
		{
			button = new Button(this);

			button.setText(i + "번째 버튼");

			button.setTag(i);

			buttonList.add(button);

			mainLayout.addView(button);
		}

		setContentView(mainLayout);
	}

	private void init()
	{
		activity = this;
	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		for(int i = 0; i < buttonList.size(); i++)
		{
			buttonList.get(i).setOnClickListener(listener_btn_list_click);
		}
	}

	private View.OnClickListener listener_back_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};

	private View.OnClickListener listener_btn_list_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			int tag = (int) v.getTag();

			if(tag == 1)
			{
				Toast.makeText(activity, "첫번째 버튼 클릭", Toast.LENGTH_SHORT).show();
			}
			else if(tag == 2)
			{
				Toast.makeText(activity, "두번째 버튼 클릭", Toast.LENGTH_SHORT).show();
			}
			else if(tag == 3)
			{
				Toast.makeText(activity, "세번째 버튼 클릭", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(activity, "그 외 버튼 클릭", Toast.LENGTH_SHORT).show();
			}
		}
	};
}