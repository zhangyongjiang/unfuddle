package com.gaoshin.sms.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSListener extends BroadcastReceiver {
	private static final String TAG = SMSListener.class.getSimpleName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
			Log.i(TAG,"SMS onReceive");
		    Bundle b = intent.getExtras();
			Object messages[] = (Object[])b.get("pdus");
			SmsMessage smsMessage[] = new SmsMessage[messages.length];
			StringBuffer sb = new StringBuffer(160*messages.length);
			
			for(int i = 0; i< messages.length; i++){
				smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
				sb.append(smsMessage[i].getDisplayMessageBody());
			}
			
			String msg = sb.toString();
		}
	}

	public static String toHexString(byte[] array) {
		char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','A', 'B', 'C', 'D', 'E', 'F' };
		int length = array.length;
		char[] buf = new char[length * 2];

		int bufIndex = 0;
		for (int i = 0 ; i < length; i++)
		{
		byte b = array[i];
		buf[bufIndex++] = HEX_DIGITS[(b >>> 4) & 0x0F];
		buf[bufIndex++] = HEX_DIGITS[b & 0x0F];
		}

		return new String(buf);
	}
}
