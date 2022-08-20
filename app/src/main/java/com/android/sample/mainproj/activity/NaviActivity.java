package com.android.sample.mainproj.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.sample.mainproj.R;
import com.android.sample.mainproj.fragment.HomeFragment;
import com.android.sample.mainproj.fragment.PostFragment;
import com.android.sample.mainproj.log.LogService;
import com.google.android.material.navigation.NavigationView;

public class NaviActivity extends AppCompatActivity
{
	private Activity activity;

	private DrawerLayout layout_navi;

	private Toolbar tb_navi;

	private NavigationView nv_navi;

	private TextView tv_navi_login_id;

	private PostFragment postFragment;

	private HomeFragment homeFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			setContentView(R.layout.activity_navi);

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

		layout_navi = findViewById(R.id.layout_navi);

		tb_navi = findViewById(R.id.tb_navi);

		nv_navi = findViewById(R.id.nv_navi);

		tv_navi_login_id = nv_navi.getHeaderView(0).findViewById(R.id.tv_navi_login_id);

		postFragment = new PostFragment(this);

		homeFragment = new HomeFragment();
	}

	private void setting()
	{
		Intent intent = getIntent();

		String loginId = intent.getStringExtra("ID");

		tv_navi_login_id.setText(loginId);

		setSupportActionBar(tb_navi);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
	}

	private void addListener()
	{
		nv_navi.setNavigationItemSelectedListener(listener_navi_click);
	}

	private NavigationView.OnNavigationItemSelectedListener listener_navi_click = new NavigationView.OnNavigationItemSelectedListener()
	{
		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item)
		{
			if(item.getItemId() == R.id.icon_post)
			{
				changeFragmentView(postFragment);
			}
			else if(item.getItemId() == R.id.icon_home)
			{
				changeFragmentView(homeFragment);
			}
			else if(item.getItemId() == R.id.icon_list)
			{
				Toast.makeText(activity, "List Menu Clicked!!", Toast.LENGTH_SHORT).show();
			}

			layout_navi.closeDrawer(GravityCompat.START);

			return false;
		}
	};

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item)
	{
		if(item.getItemId() == android.R.id.home)
		{
			layout_navi.openDrawer(GravityCompat.START);
		}

		return super.onOptionsItemSelected(item);
	}

	private void changeFragmentView(Fragment fragment)
	{
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

		fragmentTransaction.replace(R.id.fl_navi, fragment);

		fragmentTransaction.commit();
	}
}