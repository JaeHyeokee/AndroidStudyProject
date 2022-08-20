package com.android.sample.mainproj.activity;

import static com.android.sample.mainproj.config.ReqCodeConfig.REQ_CODE;
import static com.android.sample.mainproj.config.ReqCodeConfig.REQ_MAIN_ACTIVITY;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.log.LogService;

public class MainActivity extends AppCompatActivity
{
	private Activity activity;

	private ListView lv_main;

	private ListAdapter listAdapter;

	private final String[] items =
	{
		"기본 기능", "레이아웃 사용 방법", "액티비티 결과 확인", "외부 폰트 사용",
		"코드 화면 작성", "스크롤 뷰 활용", "키보드 기능 활용", "리스트 뷰 응용",
		"커스텀 리스트 뷰 활용", "리사이클 뷰 응용", "클릭 이벤트 응용", "네비게이션 뷰 활용",
		"탭 뷰 활용", "공유 설정 활용", "핸들러 활용", "비동기 작업 사용", "커스텀 다이얼로그 활용",
		"커스텀 토스트 활용", "컨텐트 프로바이더 사용", "URL 스키마 활용", "웹뷰 활용",
		"브로드캐스트 리시버 사용", "알림 사용", "맵 정보 확인"
	};

	private ActivityResultLauncher<Intent> resultLauncher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_main);

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

		lv_main = findViewById(R.id.lv_main);

		listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
	}

	private void setting()
	{
		resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResultCallback);

		lv_main.setAdapter(listAdapter);
	}

	private void addListener()
	{
		lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				// 화면 이동, 서비스 작업 실행, 브로드캐스터 리시버 실행과 같이
				// 특수한 작업을 수행하거나 이동 작업을 수행 시 사용되는 객체
				Intent intent = null;

				String item = items[position];

				if(item.equals("기본 기능"))
				{
					intent = new Intent(activity, BasicActivity.class);
				}
				else if(item.equals("레이아웃 사용 방법"))
				{
					intent = new Intent(activity, LayoutActivity.class);
					intent.putExtra("LAYOUT", LayoutActivity.CONSTRAINT);
				}
				else if(item.equals("액티비티 결과 확인"))
				{
					intent = new Intent(activity, ResultCodeActivity.class);
					intent.putExtra(REQ_CODE, REQ_MAIN_ACTIVITY);
				}
				else if(item.equals("외부 폰트 사용"))
				{
					intent = new Intent(activity, FontActivity.class);
				}
				else if(item.equals("코드 화면 작성"))
				{
					intent = new Intent(activity, CodeActivity.class);
				}
				else if(item.equals("스크롤 뷰 활용"))
				{
					intent = new Intent(activity, ScrollActivity.class);
				}
				else if(item.equals("키보드 기능 활용"))
				{
					intent = new Intent(activity, KeyBoardActivity.class);
				}
				else if(item.equals("리스트 뷰 응용"))
				{
					intent = new Intent(activity, ArrayListActivity.class);
				}
				else if(item.equals("커스텀 리스트 뷰 활용"))
				{
					intent = new Intent(activity, CustomListActivity.class);
				}
				else if(item.equals("리사이클 뷰 응용"))
				{
					intent = new Intent(activity, RecycleActivity.class);
				}
				else if(item.equals("클릭 이벤트 응용"))
				{
					intent = new Intent(activity, ClickApplyActivity.class);
				}
				else if(item.equals("네비게이션 뷰 활용"))
				{
					intent = new Intent(activity, NaviActivity.class);
					intent.putExtra("ID", "hmwoo");
				}
				else if(item.equals("탭 뷰 활용"))
				{
					intent = new Intent(activity, TabActivity.class);
				}
				else if(item.equals("공유 설정 활용"))
				{
					intent = new Intent(activity, SharedPrefActivity.class);
				}
				else if(item.equals("핸들러 활용"))
				{
					intent = new Intent(activity, HandlerActivity.class);
				}
				else if(item.equals("비동기 작업 사용"))
				{
					intent = new Intent(activity, AsyncTaskActivity.class);
				}
				else if(item.equals("커스텀 다이얼로그 활용"))
				{
					intent = new Intent(activity, CustomDialogActivity.class);
				}
				else if(item.equals("커스텀 토스트 활용"))
				{
					intent = new Intent(activity, CustomToastActivity.class);
				}
				else if(item.equals("컨텐트 프로바이더 사용"))
				{
					intent = new Intent(activity, ContentProviderActivity.class);
				}
				else if(item.equals("URL 스키마 활용"))
				{
					intent = new Intent(activity, UrlSchemaActivity.class);
				}
				else if(item.equals("웹뷰 활용"))
				{
					intent = new Intent(activity, WebViewActivity.class);
				}
				else if(item.equals("브로드캐스트 리시버 사용"))
				{
					intent = new Intent(activity, BroadCastReceiverActivity.class);
				}
				else if(item.equals("알림 사용"))
				{
					intent = new Intent(activity, NotificationActivity.class);
				}
				else if(item.equals("맵 정보 확인"))
				{
					intent = new Intent(activity, MapActivity.class);
				}



				if(intent != null)
				{
					// startActivity(intent);
					//startActivityForResult(intent, 11);
					resultLauncher.launch(intent);
				}
			}
		});
	}

	private ActivityResultCallback<ActivityResult> activityResultCallback = new ActivityResultCallback<ActivityResult>()
	{
		@Override
		public void onActivityResult(ActivityResult result)
		{
			int resultCode = result.getResultCode();

			Intent data = result.getData();

			if(resultCode == RESULT_OK)
			{
				if(data != null)
				{
					Toast.makeText(activity, "성공 결과 처리[" + data.getStringExtra("DATA") + "]", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(activity, "성공 결과 처리", Toast.LENGTH_SHORT).show();
				}
			}
			else if(resultCode == RESULT_CANCELED)
			{
				Toast.makeText(activity, "취소 결과 처리", Toast.LENGTH_SHORT).show();
			}
		}
	};

	/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == 11)
		{
			if(resultCode == RESULT_OK)
			{
				if(data != null)
				{
					Toast.makeText(activity, "성공 결과 처리[" + data.getStringExtra("DATA") + "]", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(activity, "성공 결과 처리", Toast.LENGTH_SHORT).show();
				}
			}
			else if(resultCode == RESULT_CANCELED)
			{
				Toast.makeText(activity, "취소 결과 처리", Toast.LENGTH_SHORT).show();
			}
		}
	}
	*/
}