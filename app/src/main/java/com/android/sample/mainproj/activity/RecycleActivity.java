package com.android.sample.mainproj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.adapter.RecycleAdapter;
import com.android.sample.mainproj.log.LogService;
import com.android.sample.mainproj.vo.RecycleMemberVo;

import java.util.ArrayList;
import java.util.List;

public class RecycleActivity extends AppCompatActivity
{
	private Activity activity;

	private ImageButton ibtn_back_click;

	private EditText et_recycle_item_name, et_recycle_item_age;

	private Button btn_recycle_item_add;

	/*
	기존의 ListView는 커스터마이징 하기에 힘들었고, 구조적인 문제로 성능상의 문제도 있었다.
	RecyclerView는 ListView의 문제를 해결하기 위해 개발자에게 더 다양한 형태로 커스터마이징 할 수 있도록 개발되었다.
	RecyclerView와 ListView의 가장 큰 차이점은 Layoutmanager와 ViewHolder 패턴의 의무적인 사용,
	Item에 대한 뷰의 변형을 애니메이션 할 수 있는 개념이 추가
	*/
	private RecyclerView rv_item;

	/*
	TODO RecyclerView.Adapter Example 1
	private RecyclerView.Adapter recycleAdapter;
	*/

	private RecycleAdapter recycleAdapter;

	private List<RecycleMemberVo> memberList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_recycle);

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

		et_recycle_item_name = findViewById(R.id.et_recycle_item_name);

		et_recycle_item_age = findViewById(R.id.et_recycle_item_age);

		btn_recycle_item_add = findViewById(R.id.btn_recycle_item_add);

		rv_item = findViewById(R.id.rv_item);

		memberList = new ArrayList<>();

		recycleAdapter = new RecycleAdapter(activity, memberList);

		/*
		TODO RecyclerView.Adapter Example 1
		recycleAdapter = new RecyclerView.Adapter()
		{
			private ImageView iv_custom_profile;

			private TextView tv_custom_item_name;

			private TextView tv_custom_item_age;

			@NonNull
			@Override
			public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
			{
				LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				View view = layoutInflater.inflate(R.layout.item_custom_list, parent, false);

				RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view){};

				iv_custom_profile = view.findViewById(R.id.iv_custom_profile);

				tv_custom_item_name = view.findViewById(R.id.tv_custom_item_name);

				tv_custom_item_age = view.findViewById(R.id.tv_custom_item_age);

				return viewHolder;
			}

			@Override
			public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
			{
				tv_custom_item_name.setText(memberList.get(position).getName());

				tv_custom_item_age.setText(memberList.get(position).getAge());
			}

			@Override
			public int getItemCount()
			{
				return memberList.size();
			}
		};
		*/
	}

	private void setting()
	{
		rv_item.setAdapter(recycleAdapter);

		GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 1);

		rv_item.setLayoutManager(gridLayoutManager);
	}

	private void addListener()
	{
		ibtn_back_click.setOnClickListener(listener_back_click);

		btn_recycle_item_add.setOnClickListener(listener_item_add);
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
			String name = et_recycle_item_name.getText().toString();

			String age = et_recycle_item_age.getText().toString();

			RecycleMemberVo recycleMemberVo = new RecycleMemberVo();

			recycleMemberVo.setName(name);

			recycleMemberVo.setAge(age);

			memberList.add(recycleMemberVo);

			recycleAdapter.notifyDataSetChanged();
		}
	};
}