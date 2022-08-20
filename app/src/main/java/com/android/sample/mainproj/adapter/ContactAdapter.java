package com.android.sample.mainproj.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.vo.ContactMemberVo;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder>
{
	private Activity activity;

	private List<ContactMemberVo> contactMemberList;

	private View.OnClickListener onClickListener;

	public ContactAdapter(Activity activity, List<ContactMemberVo> contactMemberList, View.OnClickListener onClickListener)
	{
		this.activity = activity;

		this.contactMemberList = contactMemberList;

		this.onClickListener = onClickListener;
	}

	@NonNull
	@Override
	public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = layoutInflater.inflate(R.layout.item_contact_info, parent, false);

		ContactAdapter.ContactHolder holder = new ContactHolder(view);

		return holder;
	}

	@Override
	public void onBindViewHolder(@NonNull ContactHolder holder, int position)
	{
		holder.tv_phone_name.setText(contactMemberList.get(position).getName());

		holder.tv_phone_number.setText(contactMemberList.get(position).getNumber());

		holder.layout_phone_name.setOnClickListener(onClickListener);

		holder.layout_phone_name.setTag(position);

		holder.layout_phone_number.setTag(false);
	}

	@Override
	public int getItemCount()
	{
		return contactMemberList.size();
	}

	public class ContactHolder extends RecyclerView.ViewHolder
	{
		private LinearLayout layout_phone_name;

		private LinearLayout layout_phone_number;

		private TextView tv_phone_name;

		private TextView tv_phone_number;

		public ContactHolder(@NonNull View itemView)
		{
			super(itemView);

			layout_phone_name = itemView.findViewById(R.id.layout_phone_name);

			layout_phone_number = itemView.findViewById(R.id.layout_phone_number);

			tv_phone_name = itemView.findViewById(R.id.tv_phone_name);

			tv_phone_number = itemView.findViewById(R.id.tv_phone_number);
		}
	}
}
