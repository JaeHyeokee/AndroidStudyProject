package com.android.sample.mainproj.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.vo.RecycleMemberVo;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleHolder>
{
	private Activity activity;

	private List<RecycleMemberVo> memberList;

	public class RecycleHolder extends RecyclerView.ViewHolder
	{
		private LinearLayout layout_list_item;

		private ImageView iv_custom_profile;

		private TextView tv_custom_item_name;

		private TextView tv_custom_item_age;

		public RecycleHolder(@NonNull View itemView)
		{
			super(itemView);

			layout_list_item = itemView.findViewById(R.id.layout_list_item);

			iv_custom_profile = itemView.findViewById(R.id.iv_custom_profile);

			tv_custom_item_name = itemView.findViewById(R.id.tv_custom_item_name);

			tv_custom_item_age = itemView.findViewById(R.id.tv_custom_item_age);
		}
	}

	public RecycleAdapter(Activity activity, List<RecycleMemberVo> memberList)
	{
		this.activity = activity;

		this.memberList = memberList;
	}

	@NonNull
	@Override
	public RecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = layoutInflater.inflate(R.layout.item_custom_list, parent, false);

		RecycleHolder viewHolder = new RecycleHolder(view);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull RecycleHolder holder, int position)
	{
		holder.tv_custom_item_name.setText(memberList.get(position).getName());

		holder.tv_custom_item_age.setText(memberList.get(position).getAge());

		if(position % 2 == 1)
		{
			holder.iv_custom_profile.setImageResource(R.drawable.ic_woman_profile);
		}
		else
		{
			holder.iv_custom_profile.setImageResource(R.drawable.ic_profile);
		}

		holder.layout_list_item.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.down));
	}

	@Override
	public int getItemCount()
	{
		return memberList.size();
	}
}
