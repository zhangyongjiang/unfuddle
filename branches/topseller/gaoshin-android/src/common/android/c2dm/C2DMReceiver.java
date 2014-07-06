/***
	Copyright (c) 2010 CommonsWare, LLC
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package common.android.c2dm;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import common.android.CommonAction;
import common.android.GenericApplication;

public class C2DMReceiver extends C2DMBaseReceiver {
	public C2DMReceiver() {
		super("this.is.not@real.biz");
	}

	@Override
	public void onRegistrered(Context context, String registrationId) {
		Log.w("C2DMReceiver-onRegistered ----- ", registrationId);
		sendRegidToServer(registrationId);
	}
	
	private void sendRegidToServer(String registrationId) {
		try {
            if (true)
                return;
            String fullUrl = "http://192.168.1.69:8080/bdserver/user/register-c2dm-id/6506366237/" + registrationId;
            DefaultHttpClient http = ((GenericApplication) getApplication()).getHttpClient(fullUrl);
			HttpPost httppost = new HttpPost(fullUrl);
			httppost.setHeader("Accept", "application/json;charset=utf-8");
			httppost.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
			HttpResponse response = http.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();
			String jsonResp = EntityUtils.toString(response.getEntity(), "UTF-8");
            Log.i(this.getClass().getName(), "send c2dm response " + statusCode + ", " + jsonResp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUnregistered(Context context) {
		Log.w("C2DMReceiver-onUnregistered ----- ", "got here!");
	}
	
	@Override
	public void onError(Context context, String errorId) {
		Log.w("C2DMReceiver-onError ----- ", errorId);
	}
	
	@Override
	protected void onMessage(Context context, Intent intent) {
		String payload = intent.getStringExtra("payload");
		Log.w("C2DMReceiver ----- ", payload);
		
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(CommonAction.C2DM.getAction());
		broadcastIntent.putExtra("data", payload);
		sendBroadcast(broadcastIntent);
	}
}
