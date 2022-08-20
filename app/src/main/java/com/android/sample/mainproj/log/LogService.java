package com.android.sample.mainproj.log;

import android.app.Activity;
import android.util.Log;

public class LogService
{
	public static void debug(Activity activity, String msg)
	{
		Log.d(activity.getPackageName() + " [" + activity.getClass().getSimpleName() + "]", msg);
	}

	public static void info(Activity activity, String msg)
	{
		Log.i(activity.getPackageName() + " [" + activity.getClass().getSimpleName() + "]", msg);
	}

	public static void warn(Activity activity, String msg)
	{
		Log.w(activity.getPackageName() + " [" + activity.getClass().getSimpleName() + "]", msg);
	}

	public static void error(Activity activity, String msg)
	{
		Log.e(activity.getPackageName() + " [" + activity.getClass().getSimpleName() + "]", msg);
	}

	public static void warn(Activity activity, String msg, Throwable ex)
	{
		Log.w(activity.getPackageName() + " [" + activity.getClass().getSimpleName() + "]", msg, ex);
	}

	public static void error(Activity activity, String msg, Throwable ex)
	{
		Log.e(activity.getPackageName() + " [" + activity.getClass().getSimpleName() + "]", msg, ex);
	}
}