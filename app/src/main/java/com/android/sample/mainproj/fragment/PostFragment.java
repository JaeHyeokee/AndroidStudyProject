package com.android.sample.mainproj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.sample.mainproj.R;
// 과제
// Home Fragment를 참조하여 PostFragment를 구성하고
// NaviActivity 화면에서 Post 메뉴를 클릭했을 때 PostFragment를 표시하도록 하여라.
// PostFragment 에서 사용되는 화면은 [ activity_basic ] 을 사용
// 하여 activity_basic 을 표시한 후 녹화본을 오픈채팅방에 올려주세요 [ ~ 12:05 ]

// 심화 과제
// 입력 버튼을 클릭하였을 경우 [ 입력!!! ] 이라고 하는 Toast 메시지를 표시
public class PostFragment extends Fragment
{
	private Activity activity;

	public PostFragment(Activity activity)
	{
		this.activity = activity;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.activity_basic, container, false);

		Button btn_basic_input = view.findViewById(R.id.btn_basic_input);

		btn_basic_input.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Toast.makeText(activity, "입력!!!", Toast.LENGTH_SHORT).show();
			}
		});

		return view;
	}
}
