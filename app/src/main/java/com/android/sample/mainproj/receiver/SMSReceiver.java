package com.android.sample.mainproj.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.android.sample.mainproj.log.LogService;

public class SMSReceiver extends BroadcastReceiver
{
	public static final String ACTION_RECEIVE_COMPLETE = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		try
		{
			String sender = "";
			String message = "";
			String action = intent.getAction();

			if(action.equals(ACTION_RECEIVE_COMPLETE))
			{
				Bundle bundle = intent.getExtras();

				if (bundle != null)
				{
					Object[] pdus = (Object[]) bundle.get("pdus");

					String format = bundle.getString("format");

					for(Object pdu : pdus)
					{
						SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu, format);
						message += smsMessage.getMessageBody();

						if(sender.equals(""))
						{
							sender = smsMessage.getOriginatingAddress();
						}
					}
				}
			}

			Toast.makeText(context, sender + " : " + message, Toast.LENGTH_LONG).show();
		}
		catch(Exception ex)
		{
			LogService.error((Activity) context, ex.getMessage(), ex);
		}
	}
}