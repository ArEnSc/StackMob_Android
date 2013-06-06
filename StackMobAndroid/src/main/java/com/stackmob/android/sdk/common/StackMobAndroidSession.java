/**
 * Copyright 2011-2013 StackMob
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stackmob.android.sdk.common;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

import com.stackmob.sdk.api.StackMobSession;

public class StackMobAndroidSession extends StackMobSession {

    private static final String SERVER_TIME_KEY = "servertimediff";
    private static final String ACCESS_TOKEN_KEY = "accesstoken";
    private static final String ACCESS_TOKEN_EXPIRATION_KEY = "accesstokenexpiration";
    private static final String REFRESH_TOKEN_KEY = "refreshtoken";
    private static final String MAC_KEY_KEY = "mackey";
    private static final String USER_KEY = "user";
    private SharedPreferences.Editor serverTimeDiffEditor;
    private SharedPreferences.Editor oauth2Editor;
    private Date nextSaveTime = new Date();
    private static long SAVE_INTERVAL = 10 * 60 * 1000;

    public StackMobAndroidSession(Context context, StackMobSession session) {
        super(session);
        userAgentName = "Android";
        setLogger(new StackMobAndroidLogger());
        setCookieManager(new StackMobAndroidCookieManager(context));
        SharedPreferences serverTimeDiffPrefs = context.getSharedPreferences("stackmob.servertimediff", Context.MODE_PRIVATE);
        serverTimeDiffEditor = serverTimeDiffPrefs.edit();
        super.saveServerTimeDiff(serverTimeDiffPrefs.getLong(SERVER_TIME_KEY, 0));
        SharedPreferences oauth2Prefs = context.getSharedPreferences(session.getKey() + ".oauth2", Context.MODE_PRIVATE);
        oauth2Editor = oauth2Prefs.edit();
        String accessToken = oauth2Prefs.getString(ACCESS_TOKEN_KEY, null);
        String macKey = oauth2Prefs.getString(MAC_KEY_KEY, null);
        String refreshToken = oauth2Prefs.getString(REFRESH_TOKEN_KEY, null);
        long oauth2ExpiryTime = oauth2Prefs.getLong(ACCESS_TOKEN_EXPIRATION_KEY, -1);
        super.setOAuth2TokensAndExpiration(accessToken, macKey, refreshToken, oauth2ExpiryTime == -1 ? null : new Date(oauth2ExpiryTime));
        String loggedInUser = oauth2Prefs.getString(USER_KEY, null);
        if(loggedInUser != null) super.setLastUserLoginName(loggedInUser);
    }

    @Override
    protected void saveServerTimeDiff(long serverTimeDiff) {
        super.saveServerTimeDiff(serverTimeDiff);
        //We don't want to write to disk with every request
        if(nextSaveTime.before(new Date())) {
            serverTimeDiffEditor.putLong(SERVER_TIME_KEY, serverTimeDiff);
            serverTimeDiffEditor.commit();
            nextSaveTime.setTime(new Date().getTime() + SAVE_INTERVAL);
        }
    }
    @Override
    public void setOAuth2TokensAndExpiration(String accessToken, String macKey, String refreshToken, Date expiry) {
        super.setOAuth2TokensAndExpiration(accessToken, macKey, refreshToken, expiry);
        oauth2Editor.putString(ACCESS_TOKEN_KEY, accessToken);
        oauth2Editor.putString(MAC_KEY_KEY, macKey);
        oauth2Editor.putString(REFRESH_TOKEN_KEY, refreshToken);
        oauth2Editor.putLong(ACCESS_TOKEN_EXPIRATION_KEY, expiry.getTime());
        oauth2Editor.commit();
    }

    @Override
    public void setLastUserLoginName(String name) {
        super.setLastUserLoginName(name);
        oauth2Editor.putString(USER_KEY, name);
        oauth2Editor.commit();
    }
}
