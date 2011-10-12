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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;
import com.stackmob.android.sdk.callback.StackMobCallback;
import com.stackmob.android.sdk.exception.StackMobException;
import com.stackmob.android.sdk.net.HttpHelper;
import com.stackmob.android.sdk.net.HttpVerb;

public class StackMobRequest {
  private static final String URL_FMT =  "api.mob1.stackmob.com";
  private static final String SECURE_SCHEME = "https";
  private static final String REGULAR_SCHEME = "http";

  private StackMobSession session;
  public String methodName;
  public boolean isUserBased = false;
  public Boolean isSecure = true;
  public HttpVerb httpMethod = HttpVerb.GET;
  public String method;
  //default to doing nothing
  public StackMobCallback callback = new StackMobCallback() {
	  @Override
	  public void success(String s) {}
	  @Override
	  public void failure(StackMobException e) {}
  };
  public HashMap<String, Object> params;
  public Object requestObject;

  public StackMobRequest(StackMobSession session) {
    this.session = session;
  }

  public void sendRequest() {

    try {
      String response = null;

      switch(this.httpMethod) {

      case GET:
        response = sendGetRequest();
        break;
        
      case POST:
        response = sendPostRequest();
        break;

      case PUT:
        response = sendPutRequest();
        break;

      case DELETE:
        response = sendDeleteRequest();
        break;

      }

      callback.success(response);

    } catch (StackMobException e) {
      callback.failure(e);
    }

  }

  private String sendGetRequest() throws StackMobException {

    URI uri = null;
    String ret = null;

    try {
      String query = null;
      if (null != this.params) {
        query = URLEncodedUtils.format(getParamsForRequest(),
            HTTP.UTF_8);
      }

      uri = URIUtils.createURI(getScheme(), getHost(), -1, getPath(),
          query, null);
      ret = HttpHelper.doGet(uri);

    } catch (URISyntaxException e) {
      throw new StackMobException(e.getMessage());
    }

    return ret;

  }

  private String sendPostRequest() throws StackMobException {
    URI uri = null;
    String ret = null;

    try {

      uri = URIUtils.createURI(getScheme(), getHost(), -1, getPath(),
          null, null);

      HttpEntity entity = null;
      if (null != this.params) {
        entity = new UrlEncodedFormEntity(getParamsForRequest(),
            HTTP.UTF_8);
      } else if (null != this.requestObject) {
        Gson gson = new Gson();
        entity = new StringEntity(gson.toJson(this.requestObject),
            HTTP.UTF_8);
      }

      ret = HttpHelper.doPost(uri, entity);

    } catch (URISyntaxException e) {
      throw new StackMobException(e.getMessage());
    } catch (UnsupportedEncodingException e) {
      throw new StackMobException(e.getMessage());
    }

    return ret;
  }

  private String sendPutRequest() throws StackMobException {
    URI uri = null;
    String ret = null;

    try {

      uri = URIUtils.createURI(getScheme(), getHost(), -1, getPath(),
          null, null);

      HttpEntity entity = null;
      if (null != this.params) {
        entity = new UrlEncodedFormEntity(getParamsForRequest(),
            HTTP.UTF_8);
      } else if (null != this.requestObject) {
        Gson gson = new Gson();
        entity = new StringEntity(gson.toJson(this.requestObject),
            HTTP.UTF_8);
      }

      ret = HttpHelper.doPut(uri, entity);

    } catch (URISyntaxException e) {
      throw new StackMobException(e.getMessage());
    } catch (UnsupportedEncodingException e) {
      throw new StackMobException(e.getMessage());
    }

    return ret;
  }

  private String sendDeleteRequest() throws StackMobException {
    URI uri = null;
    String ret = null;

    try {
      String query = null;
      if (null != this.params) {
        query = URLEncodedUtils.format(getParamsForRequest(),
            HTTP.UTF_8);
      }

      uri = URIUtils.createURI(getScheme(), getHost(), -1, getPath(),
          query, null);
      ret = HttpHelper.doDelete(uri);

    } catch (URISyntaxException e) {
      throw new StackMobException(e.getMessage());
    }

    return ret;
  }

  private String getPath() {
    if (this.isUserBased) {
      return "/" + this.session.getUserObjectName() + "/"
          + this.methodName;
    } else {
      return "/" + this.methodName;
    }

  }

  private String getScheme() {
    if (this.isSecure) {
      return SECURE_SCHEME;
    } else {
      return REGULAR_SCHEME;
    }
  }

  private String getHost() {
    return URL_FMT;
  }

  private List<NameValuePair> getParamsForRequest() {

    if (null == this.params) {
      return null;
    }

    List<NameValuePair> ret = new ArrayList<NameValuePair>(
        this.params.size());
    for (String key : this.params.keySet()) {
      ret.add(new BasicNameValuePair(key, this.params.get(key)
          .toString()));
    }

    return ret;
  }

}
