package com.android.sample.mainproj.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.vo.CustomMemberVo;

import java.util.List;

public class CustomAdapter extends BaseAdapter
{
	private Activity activity;

	private List<CustomMemberVo> memberList;

	private CustomHolder holder;

	public CustomAdapter(Activity activity, List<CustomMemberVo> memberList)
	{
		this.activity = activity;

		this.memberList = memberList;
	}

	public class CustomHolder
	{
		private ImageView iv_custom_profile;

		private TextView tv_custom_item_name;

		private TextView tv_custom_item_age;

		public CustomHolder(View view)
		{
			iv_custom_profile = view.findViewById(R.id.iv_custom_profile);

			tv_custom_item_name = view.findViewById(R.id.tv_custom_item_name);

			tv_custom_item_age = view.findViewById(R.id.tv_custom_item_age);
		}
	}

	@Override
	public int getCount()
	{
		return memberList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return memberList.get(position);
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

			holder = new CustomHolder(view);

			view.setTag(holder);
		}
		else
		{
			holder = (CustomHolder) view.getTag();
		}

		holder.tv_custom_item_name.setText(memberList.get(position).getName());
		holder.tv_custom_item_age.setText(memberList.get(position).getAge());

		if(position % 2 == 1)
		{
			holder.iv_custom_profile.setImageResource(R.drawable.ic_woman_profile);
		}

		return view;
	}

	public void addItem(String name, String age)
	{
		CustomMemberVo customMemberVo = new CustomMemberVo();

		customMemberVo.setName(name);

		customMemberVo.setAge(age);

		memberList.add(customMemberVo);

		notifyDataSetChanged();
	}
}
