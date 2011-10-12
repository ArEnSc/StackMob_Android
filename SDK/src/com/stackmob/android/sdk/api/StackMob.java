/**
 * Copyright 2011 StackMob
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

package com.stackmob.android.sdk.api;

import java.util.Map;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;

import com.stackmob.android.sdk.activity.FacebookActivity;
import com.stackmob.android.sdk.activity.TwitterActivity;
import com.stackmob.android.sdk.callback.StackMobCallback;
import com.stackmob.android.sdk.callback.StackMobFacebookCallback;
import com.stackmob.android.sdk.callback.StackMobTwitterCallback;
import com.stackmob.android.sdk.exception.StackMobException;
import com.stackmob.android.sdk.net.HttpHelper;
import com.stackmob.android.sdk.net.HttpVerb;
import com.stackmob.android.sdk.util.EnvironmentInfoUtil;

public class StackMob {

  private static StackMob instance = new StackMob();
  private StackMobSession session;
  private StackMobTwitterCallback mTwitterCallback;
  private StackMobFacebookCallback mFacebookCallback;
  
  protected static class RegistrationIDAndUser {
	  public String userId;
	  public Map<String, String> token = new HashMap<String, String>();
	  public RegistrationIDAndUser(String registrationID, String user) {
		  this.userId = user;
		  token.put("token", registrationID);
		  token.put("type", "android");
	  }
  }
  
  public static StackMob getInstance() {
    return instance;
  }

  private StackMob() {
  }

  public void setApplication(String apiKey, String apiSecret,
      String appName, String subDomain, String domain,
      String userObjectName, Integer apiVersionNumber) {

    setSession(new StackMobSession(apiKey, apiSecret, appName,
        subDomain, domain, userObjectName, apiVersionNumber));
  }

  public void getTwitterUserToken(Context ctx, StackMobTwitterCallback callback) {
    mTwitterCallback = callback;
    ctx.startActivity(new Intent(ctx, TwitterActivity.class));
  }

  public void setTwitterTokens(String token, String tokenSecret, Throwable e) {
    if ( null == e ) {
      mTwitterCallback.success(token, tokenSecret);
    } else {
      mTwitterCallback.failure(new StackMobException(e.getMessage()));
    }
  }

  public void setTwitterConsumer(String consumerKey, String consumerSecret) {
    session.setTwitterConsumerKey(consumerKey);
    session.setTwitterConsumerSecret(consumerSecret);
  }

  public void getFacebookUserToken(Context ctx, StackMobFacebookCallback callback) {
    mFacebookCallback = callback;
    ctx.startActivity(new Intent(ctx, FacebookActivity.class));
  }

  public void setFacebookToken(String token, long accessExpires, Throwable e) {
    if (null == e) {
      mFacebookCallback.success(token, accessExpires);
    } else {
      mFacebookCallback.failure(new StackMobException(e.getMessage()));
    }
  }

  public void setFacebookAppId(String appId) {
    session.setFacebookAppId(appId);
  }

  public void login(HashMap<String, Object> params,
      StackMobCallback callback) {
    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = "login";
    request.httpMethod = HttpVerb.GET;
    request.isUserBased = true;
    request.isSecure = true;
    request.callback = callback;
    request.params = params;

    request.sendRequest();
  }

  public void logout(StackMobCallback callback) {
    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = "logout";
    request.httpMethod = HttpVerb.GET;
    request.isUserBased = true;
    request.isSecure = true;
    request.callback = callback;

    request.sendRequest();
  }

  public void startSession(Context ctx, StackMobCallback callback) {
    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = "startsession";
    request.httpMethod = HttpVerb.POST;
    request.isSecure = true;
    request.callback = callback;
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("cd", EnvironmentInfoUtil.getApplicationInfo(ctx));
    request.requestObject = params;

    request.sendRequest();
  }

  public void endSession(StackMobCallback callback) {
    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = "endsession";
    request.httpMethod = HttpVerb.POST;
    request.isSecure = true;
    request.callback = callback;

    request.sendRequest();
  }

  public void twitterLogin(String token, String secret,
      StackMobCallback callback) {
    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = "twitterLogin";
    request.httpMethod = HttpVerb.GET;
    request.isUserBased = true;
    request.isSecure = true;
    request.callback = callback;

    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("tw_tk", token);
    params.put("tw_ts", secret);
    request.params = params;

    request.sendRequest();
  }

  public void twitterStatusUpdate(String message,
      StackMobCallback callback) {
    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = "twitterStatusUpdate";
    request.httpMethod = HttpVerb.GET;
    request.isUserBased = true;
    request.isSecure = true;
    request.callback = callback;

    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("tw_st", message);
    request.params = params;

    request.sendRequest();
  }

  public void registerWithTwitterToken(String token, String secret,
      String username, StackMobCallback callback) {

    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = "createUserWithTwitter";
    request.httpMethod = HttpVerb.GET;
    request.isUserBased = true;
    request.isSecure = true;
    request.callback = callback;

    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("tw_tk", token);
    params.put("tw_ts", secret);
    params.put("username", username);
    request.params = params;

    request.sendRequest();
  }

  public void linkUserWithTwitterToken(String token, String secret,
      StackMobCallback callback) {

    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = "linkUserWithTwitter";
    request.httpMethod = HttpVerb.GET;
    request.isUserBased = true;
    request.isSecure = true;
    request.callback = callback;

    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("tw_tk", token);
    params.put("tw_ts", secret);
    request.params = params;

    request.sendRequest();
  }

  public void facebookLogin(String token, StackMobCallback callback) {
    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = "facebookLogin";
    request.httpMethod = HttpVerb.GET;
    request.isUserBased = true;
    request.isSecure = true;
    request.callback = callback;

    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("fb_at", token);
    request.params = params;

    request.sendRequest();
  }

  public void registerWithFacebookToken(String token, String username, StackMobCallback callback) {
    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = "createUserWithFacebook";
    request.httpMethod = HttpVerb.GET;
    request.isUserBased = true;
    request.isSecure = true;
    request.callback = callback;

    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("fb_at", token);
    params.put("username", username);
    request.params = params;

    request.sendRequest();
  }

  public void linkUserWithFacebookToken(String token, String secret,
      StackMobCallback callback) {

    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = "linkUserWithFacebook";
    request.httpMethod = HttpVerb.GET;
    request.isUserBased = true;
    request.isSecure = true;
    request.callback = callback;

    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("fb_at", token);
    request.params = params;

    request.sendRequest();
  }

  public void facebookPostMessage(String msg, StackMobCallback callback) {
    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = "postFacebookMessage";
    request.httpMethod = HttpVerb.GET;
    request.isUserBased = true;
    request.isSecure = true;
    request.callback = callback;

    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("message", msg);
    request.params = params;

    request.sendRequest();
  }

  public void getFacebookUserInfo(StackMobCallback callback) {
    get("getFacebookUserInfo", callback);
  }
  
  public void registerForPushWithUser(String username, String registrationID, StackMobCallback callback) {
	  RegistrationIDAndUser tokenAndUser = new RegistrationIDAndUser(registrationID, username);
	  post("/push/register_device_token_universal", tokenAndUser, callback);
  }

  public void get(String path, StackMobCallback callback) {
    get(path, null, callback);
  }

  public void get(String path, HashMap<String, Object> arguments,
      StackMobCallback callback) {
    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = path;
    request.httpMethod = HttpVerb.GET;
    request.isUserBased = false;
    request.isSecure = true;
    request.callback = callback;
    request.params = arguments;

    request.sendRequest();
  }

  public void post(String path, Object requestObject,
      StackMobCallback callback) {
    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = path;
    request.httpMethod = HttpVerb.POST;
    request.isUserBased = false;
    request.isSecure = true;
    request.callback = callback;
    request.requestObject = requestObject;

    request.sendRequest();

  }

  public void put(String path, String id, Object requestObject,
      StackMobCallback callback) {
    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = path + "/" + id;
    request.httpMethod = HttpVerb.PUT;
    request.isUserBased = false;
    request.isSecure = true;
    request.callback = callback;
    request.requestObject = requestObject;

    request.sendRequest();

  }

  public void delete(String path, String id, StackMobCallback callback) {

    StackMobRequest request = new StackMobRequest(this.session);
    request.methodName = path + "/" + id;
    request.httpMethod = HttpVerb.DELETE;
    request.isUserBased = false;
    request.isSecure = true;
    request.callback = callback;

    request.sendRequest();
  }

  public void setSession(StackMobSession session) {
    this.session = session;
    HttpHelper.setVersion(this.session.getApiVersionNumber());
  }

  public StackMobSession getSession() {
    return session;
  }
}
