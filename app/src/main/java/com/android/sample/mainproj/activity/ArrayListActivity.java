package com.android.sample.mainproj.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.log.LogService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayListActivity extends AppCompatActivity
{
	private Activity activity;

	private ImageButton ibtn_back_click;

	private EditText et_item;

	private Button btn_item_add;

	private Button btn_item_edit;

	private Button btn_item_del;

	private ListView lv_array;

	private List<String> itemList = new ArrayList<>(Arrays.asList("첫 번째", "두 번째"));

	private ArrayAdapter<String> arrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_array);

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

		et_item = findViewById(R.id.et_item);

		btn_item_add = findViewById(R.id.btn_item_add);

		btn_item_edit = findViewById(R.id.btn_item_edit);

		btn_item_del = findViewById(R.id.btn_item_del);

		lv_array = findViewById(R.id.lv_array);

		arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, itemList);
	}

	private void setting()
	{
		lv_array.setAdapter(arrayAdapter);

		lv_array.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		btn_item_add.setOnClickListener(listener_item_add);

		btn_item_edit.setOnClickListener(listener_item_edit);

		btn_item_del.setOnClickListener(listener_item_del);

		lv_array.setOnItemClickListener(listener_item_click);
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
			itemList.add(et_item.getText().toString());
			arrayAdapter.notifyDataSetChanged();
		}
	};

	private View.OnClickListener listener_item_edit = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			int index = lv_array.getCheckedItemPosition();

			if(index < 0)
			{
				Toast.makeText(activity, "편집 대상을 선택하세요.", Toast.LENGTH_SHORT).show();
			}
			else
			{
				itemList.set(index, et_item.getText().toString());
				lv_array.clearChoices();
				et_item.setText("");
				arrayAdapter.notifyDataSetChanged();
			}
		}
	};

	private View.OnClickListener listener_item_del = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			int index = lv_array.getCheckedItemPosition();

			if(index < 0)
			{
				Toast.makeText(activity, "삭제 대상을 선택하세요.", Toast.LENGTH_SHORT).show();
			}
			else
			{
				itemList.remove(index);
				lv_array.clearChoices();
				arrayAdapter.notifyDataSetChanged();
			}
		}
	};

	private AdapterView.OnItemClickListener listener_item_click = new AdapterView.OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			String item = itemList.get(position);
			//String item = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();

			Toast.makeText(activity, item, Toast.LENGTH_SHORT).show();
		}
	};




}