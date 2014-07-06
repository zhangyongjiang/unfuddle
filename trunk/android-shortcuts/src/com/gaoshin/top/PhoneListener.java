package com.gaoshin.top;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import common.util.web.JsonUtil;

public class PhoneListener extends BroadcastReceiver {
    private static final String tag = PhoneListener.class.getSimpleName();
    
    /**
     * For outgoing call, we will get msgs below
     *      1. null, null, outgoingnumber
     *      2. OFFHOOK, null, null
     *      3. IDLE, null, null
     * For incoming call, we will get msgs below
     *      1. RINGING, incomingnumber, null
     *      2. OFFHOOK, null, null
     *      3. IDLE, null, null
     *   or
     *      1. RINGING, incomingnumber, null
     *      2. IDLE, incomingnumber, null
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(null == bundle)
                return;
        
        CallEvent callState = new CallEvent();
        
        String extraState = bundle.getString(TelephonyManager.EXTRA_STATE);
        if(extraState != null) {
            try {
                callState.setType(CallState.valueOf(extraState));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        String number = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        callState.setIncomingNumber(number);
        
        number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        callState.setOutgoingNumber(number);
        if(callState.getOutgoingNumber() != null)
            callState.setType(CallState.CALL);
        
        String json = JsonUtil.toJsonString(callState);
        Log.i(tag, "CALL STATE CHANGE: " + json);
        
        Intent serviceIntent = new Intent(context, TopService.class);
        serviceIntent.setAction("CallEvent");
        serviceIntent.putExtra("data", json);
        context.startService(serviceIntent);
    }
}
