package com.android.sample.mainproj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.adapter.TabAdapter;
import com.android.sample.mainproj.fragment.HomeFragment;
import com.android.sample.mainproj.fragment.PostFragment;
import com.android.sample.mainproj.log.LogService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity
{
	private Activity activity;

	private TabLayout tl_main;

	private ViewPager2 vp_main;

	private List<Fragment> fragmentList;

	private TabAdapter tabAdapter;

	private TabLayoutMediator tabLayoutMediator;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_tab);

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

		tl_main = findViewById(R.id.tl_main);

		vp_main = findViewById(R.id.vp_main);

		fragmentList = new ArrayList<>();

		tabAdapter = new TabAdapter(this, fragmentList);
	}

	private void setting()
	{
		fragmentList.add(new PostFragment(this));

		fragmentList.add(new HomeFragment());

		vp_main.setAdapter(tabAdapter);

		for(int i = 0; i < fragmentList.size(); i++)
		{
			tl_main.addTab(tl_main.newTab());
		}

		tabLayoutMediator = new TabLayoutMediator(tl_main, vp_main, listener_configure_tab);

		tabLayoutMediator.attach();

		vp_main.setCurrentItem(0);
	}

	private void addListener()
	{
		tl_main.addOnTabSelectedListener(listener_tab_click);
	}

	private TabLayoutMediator.TabConfigurationStrategy listener_configure_tab = new TabLayoutMediator.TabConfigurationStrategy()
	{
		@Override
		public void onConfigureTab(@NonNull TabLayout.Tab tab, int position)
		{
			TextView textView = new TextView(activity);

			if(position == 0)
			{
				textView.setText("Post");
			}
			else
			{
				textView.setText("Home");
			}

			tab.setCustomView(textView);
		}
	};

	private TabLayout.OnTabSelectedListener listener_tab_click = new TabLayout.OnTabSelectedListener()
	{
		@Override
		public void onTabSelected(TabLayout.Tab tab)
		{
			LogService.info(activity, ((TextView)tab.getCustomView()).getText().toString() + " 선택");
		}

		@Override
		public void onTabUnselected(TabLayout.Tab tab)
		{
			LogService.info(activity, ((TextView)tab.getCustomView()).getText().toString() + " 비선택");
		}

		@Override
		public void onTabReselected(TabLayout.Tab tab)
		{
			LogService.info(activity, ((TextView)tab.getCustomView()).getText().toString() + " 재선택");
		}
	};
}