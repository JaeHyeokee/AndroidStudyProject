package com.android.sample.mainproj.util;

import android.app.Activity;
import android.util.TypedValue;

public class UnitUtil
{
	public static int ConvertSizeToDP(Activity activity, int size)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, activity.getResources().getDisplayMetrics());
	}
}
