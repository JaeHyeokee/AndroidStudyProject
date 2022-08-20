package com.android.sample.mainproj.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.sample.mainproj.activity.BroadCastReceiverActivity;

public class BootReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// BraodCastReceiverActivity를 띄우도록 설정
		Intent bootIntent = new Intent(context, BroadCastReceiverActivity.class);
		
		// Activity가 아닌 다른 곳에서 화면을 띄우려고 할때 다음 플래그를 설정
		bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(bootIntent);
	}
}