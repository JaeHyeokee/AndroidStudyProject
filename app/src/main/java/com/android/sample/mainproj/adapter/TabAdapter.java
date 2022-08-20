package com.android.sample.mainproj.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class TabAdapter extends FragmentStateAdapter
{
	private List<Fragment> fragmentList;

	public TabAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragmentList)
	{
		super(fragmentActivity);

		this.fragmentList = fragmentList;
	}

	@NonNull
	@Override
	public Fragment createFragment(int position)
	{
		return fragmentList.get(position);
	}

	@Override
	public int getItemCount()
	{
		return fragmentList.size();
	}
}
