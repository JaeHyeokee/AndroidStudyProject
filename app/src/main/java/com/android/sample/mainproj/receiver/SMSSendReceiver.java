package com.android.sample.mainproj.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.sample.mainproj.log.LogService;

public class SMSSendReceiver extends BroadcastReceiver
{
	public static final String ACTION_SEND_COMPLETE = "com.android.receiver.SEND_SMS";

	public static final String ACTION_DELIVERY_COMPLETE = "com.android.receiver.DILIVERY_SMS";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(intent.getAction().equals(ACTION_SEND_COMPLETE))
		{
			if(getResultCode() == Activity.RESULT_OK)
			{
				LogService.info((Activity)context, "메시지 전송 성공");
			}
			else
			{
				LogService.info((Activity)context, "메시지 전송 실패");
			}
		}
		else if(intent.getAction().equals(ACTION_DELIVERY_COMPLETE))
		{
			if(getResultCode() == Activity.RESULT_OK)
			{
				LogService.info((Activity)context, "메시지 전달 성공");
			}
			else
			{
				LogService.info((Activity)context, "메시지 전달 실패");
			}
		}
	}
}
