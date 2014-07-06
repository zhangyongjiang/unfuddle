package com.gaoshin.android;

import java.lang.reflect.Method;

import android.content.Context;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

public class TelephonyService {
	private TelephonyManager myManager;
	private static String TAG = "TelephonyUtil";
	private Context myContext;
	private ITelephony telephonyService;
	public TelephonyService(Context context) {
		myContext = context;
		myManager =(TelephonyManager)myContext.getSystemService(Context.TELEPHONY_SERVICE);
		myManager.listen(new PhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
		connectToTelephonyService();
	}
	
	private void connectToTelephonyService(){
		try{
			Class<?> c = Class.forName(myManager.getClass().getName());
			Method m = c.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			telephonyService = (ITelephony)m.invoke(myManager);
			Log.i(TAG, "connected to Telephony Service");
		}catch(Exception e) {
			e.printStackTrace();
			Log.e(TAG, "FATAL ERROR: could not connect to telephony subsytem");
			Log.e(TAG, "Exception object: " +e);
		}
	}
	
	public void Call(String number) {
		try {
			telephonyService.call(number);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	public void EndCall() {
		try {
			telephonyService.endCall();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void AnswerCall(){
		try {
			telephonyService.answerRingingCall();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	  
	public void IgnoreCall() {
		try {
			telephonyService.silenceRinger();
			telephonyService.endCall();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	  
	public int getCallState(){
		return myManager.getCallState();
	}
	  
	public void ShowInCallScreen(){
		try {
			telephonyService.showCallScreenWithDialpad(true);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
