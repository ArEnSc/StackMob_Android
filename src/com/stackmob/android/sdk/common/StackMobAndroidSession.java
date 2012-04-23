package com.stackmob.android.sdk.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.stackmob.sdk.api.StackMobSession;

public class StackMobAndroidSession extends StackMobSession {
	
	private static final String SERVER_TIME_KEY = "servertimediff";
	private SharedPreferences.Editor editor;

	public StackMobAndroidSession(Context context, StackMobSession session) {
		super(session);
		SharedPreferences prefs = context.getSharedPreferences("stackmob.servertimediff", 0);
		editor = prefs.edit();
		super.saveServerTimeDiff(prefs.getLong(SERVER_TIME_KEY, 0));
	}
	
	@Override
    protected void saveServerTimeDiff(long serverTimeDiff) {
		super.saveServerTimeDiff(serverTimeDiff);
		editor.putLong(SERVER_TIME_KEY, serverTimeDiff);
    }
}
