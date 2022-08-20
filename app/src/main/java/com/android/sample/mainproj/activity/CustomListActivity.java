package com.android.sample.mainproj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.adapter.CustomAdapter;
import com.android.sample.mainproj.log.LogService;
import com.android.sample.mainproj.vo.CustomMemberVo;

import java.util.ArrayList;
import java.util.List;

public class CustomListActivity extends AppCompatActivity
{
	private Activity activity;

	private ImageButton ibtn_back_click;

	private EditText et_custom_item_name, et_custom_item_age;

	private Button btn_custom_item_add;

	private ListView lv_custom_item;

	/*
	TODO BaseAdapter Example 1
	private BaseAdapter baseAdapter;

	private List<String> list_name;
	private List<String> list_age;
	*/

	private CustomAdapter customAdapter;

	private List<CustomMemberVo> memberList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_custom_list);

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

		et_custom_item_name = findViewById(R.id.et_custom_item_name);

		et_custom_item_age = findViewById(R.id.et_custom_item_age);

		btn_custom_item_add = findViewById(R.id.btn_custom_item_add);

		lv_custom_item = findViewById(R.id.lv_custom_item);

		memberList = new ArrayList<>();

		customAdapter = new CustomAdapter(activity, memberList);
		/*
		TODO baseAdapter Example 1
		list_name = new ArrayList<>();

		list_age = new ArrayList<>();

		baseAdapter = new BaseAdapter()
		{
			private ImageView iv_custom_profile;

			private TextView tv_custom_item_name;

			private TextView tv_custom_item_age;

			@Override
			public int getCount()
			{
				return list_name.size();
			}

			@Override
			public Object getItem(int position)
			{
				return list_name.get(position);
			}

			@Override
			public long getItemId(int position)
			{
				return position;
			}

			@Override
			public View getView(int position, View view, ViewGroup parent)
			{
				if(view == null)
				{
					view = LayoutInflater.from(activity).inflate(R.layout.item_custom_list, parent, false);

					iv_custom_profile = view.findViewById(R.id.iv_custom_profile);

					tv_custom_item_name = view.findViewById(R.id.tv_custom_item_name);

					tv_custom_item_age = view.findViewById(R.id.tv_custom_item_age);
				}

				tv_custom_item_name.setText(list_name.get(position));

				tv_custom_item_age.setText(list_age.get(position));

				if(position % 2 == 1)
				{
					iv_custom_profile.setImageResource(R.drawable.ic_woman_profile);
				}

				return view;
			}
		};
		*/
	}

	private void setting()
	{
		// lv_custom_item.setAdapter(baseAdapter);
		lv_custom_item.setAdapter(customAdapter);
	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		btn_custom_item_add.setOnClickListener(listener_item_add);
	}

	private View.OnClickListener listener_back_click = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};

	private View.OnClickListener listener_item_add = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			String name = et_custom_item_name.getText().toString();
			String age = et_custom_item_age.getText().toString();

			customAdapter.addItem(name, age);

			/*
			TODO BaseAdapter Eample 1
			list_name.add(name);
			list_age.add(age);

			baseAdapter.notifyDataSetChanged();
			*/
		}
	};



}