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

package com.stackmob.android.sdk.net;

import java.io.IOException;
import java.net.URI;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.stackmob.android.sdk.api.StackMob;
import com.stackmob.android.sdk.exception.StackMobException;

public class HttpHelper {

  private static final String ACCEPT = "Accept";
  private static final int CONN_TIMEOUT = 20000;
  private static final String DEFAULT_CONTENT_TYPE_FMT = "application/vnd.stackmob+json; version=%d";
  private static String DEFAULT_CONTENT_TYPE;
  private static DefaultHttpClient mHttpClient;
  private static OAuthConsumer mConsumer;

  public static void maybeCreateHttpClient() {
    if (mHttpClient == null) {
      mHttpClient = setupHttpClient();
    }
  }

  public static void setVersion(int version) {
    DEFAULT_CONTENT_TYPE = String.format(DEFAULT_CONTENT_TYPE_FMT, version);
  }

  public static String doGet(URI uri) throws StackMobException {

    maybeCreateHttpClient();
    ResponseHandler<String> responseHandler = new BasicResponseHandler();

    HttpGet request = new HttpGet(uri);
    request.setHeader(HTTP.CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
    request.setHeader(ACCEPT, DEFAULT_CONTENT_TYPE);

    String ret = null;

    try {

      mConsumer.sign(request);
      ret = mHttpClient.execute(request, responseHandler);

    } catch (OAuthMessageSignerException e) {
      throw new StackMobException(e.getMessage());
    } catch (OAuthExpectationFailedException e) {
      throw new StackMobException(e.getMessage());
    } catch (OAuthCommunicationException e) {
      throw new StackMobException(e.getMessage());
    } catch (ClientProtocolException e) {
      throw new StackMobException(e.getMessage());
    } catch (IOException e) {
      throw new StackMobException(e.getMessage());
    }

    return ret;
  }

  public static String doPost(URI uri, HttpEntity entity)
      throws StackMobException {

    maybeCreateHttpClient();
    ResponseHandler<String> responseHandler = new BasicResponseHandler();

    HttpPost request = new HttpPost(uri);

    if (null != entity) {
      request.setEntity(entity);
    }

    request.setHeader(HTTP.CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
    request.setHeader(ACCEPT, DEFAULT_CONTENT_TYPE);

    String ret = null;

    try {
      mConsumer.sign(request);
      ret = mHttpClient.execute(request, responseHandler);
    } catch (OAuthMessageSignerException e) {
      throw new StackMobException(e.getMessage());
    } catch (OAuthExpectationFailedException e) {
      throw new StackMobException(e.getMessage());
    } catch (OAuthCommunicationException e) {
      throw new StackMobException(e.getMessage());
    } catch (ClientProtocolException e) {
      throw new StackMobException(e.getMessage());
    } catch (IOException e) {
      throw new StackMobException(e.getMessage());
    }

    return ret;
  }

  public static String doPut(URI uri, HttpEntity entity) throws StackMobException {
    maybeCreateHttpClient();
    ResponseHandler<String> responseHandler = new BasicResponseHandler();

    HttpPut request = new HttpPut(uri);

    if (null != entity) {
      request.setEntity(entity);
    }

    request.setHeader(HTTP.CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
    request.setHeader(ACCEPT, DEFAULT_CONTENT_TYPE);

    String ret = null;

    try {
      mConsumer.sign(request);
      ret = mHttpClient.execute(request, responseHandler);
    } catch (OAuthMessageSignerException e) {
      throw new StackMobException(e.getMessage());
    } catch (OAuthExpectationFailedException e) {
      throw new StackMobException(e.getMessage());
    } catch (OAuthCommunicationException e) {
      throw new StackMobException(e.getMessage());
    } catch (ClientProtocolException e) {
      throw new StackMobException(e.getMessage());
    } catch (IOException e) {
      throw new StackMobException(e.getMessage());
    }

    return ret;
  }

  public static String doDelete(URI uri) throws StackMobException {
    maybeCreateHttpClient();
    ResponseHandler<String> responseHandler = new BasicResponseHandler();

    HttpDelete request = new HttpDelete(uri);
    request.setHeader(HTTP.CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
    request.setHeader(ACCEPT, DEFAULT_CONTENT_TYPE);

    String ret = null;

    try {

      mConsumer.sign(request);
      ret = mHttpClient.execute(request, responseHandler);

    } catch (OAuthMessageSignerException e) {
      throw new StackMobException(e.getMessage());
    } catch (OAuthExpectationFailedException e) {
      throw new StackMobException(e.getMessage());
    } catch (OAuthCommunicationException e) {
      throw new StackMobException(e.getMessage());
    } catch (ClientProtocolException e) {
      throw new StackMobException(e.getMessage());
    } catch (IOException e) {
      throw new StackMobException(e.getMessage());
    }

    return ret;
  }

  private static DefaultHttpClient setupHttpClient() {
    HttpParams httpParams = new BasicHttpParams();
    setConnectionParams(httpParams);
    SchemeRegistry schemeRegistry = registerFactories();
    ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(
        httpParams, schemeRegistry);

    DefaultHttpClient client = new DefaultHttpClient(
        clientConnectionManager, httpParams);
    client.setRedirectHandler(new FollowPostRedirectHandler());

    StackMob stackmob = StackMob.getInstance();
    mConsumer = new CommonsHttpOAuthConsumer(stackmob.getSession()
        .getKey(), stackmob.getSession().getSecret());

    return client;
  }

  private static SchemeRegistry registerFactories() {
    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(new Scheme("http", PlainSocketFactory
        .getSocketFactory(), 80));
    schemeRegistry.register(new Scheme("https",
        new SimpleSSLSocketFactory(), 443));
    return schemeRegistry;
  }

  private static void setConnectionParams(HttpParams httpParams) {
    HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
    HttpConnectionParams.setConnectionTimeout(httpParams, CONN_TIMEOUT);
    HttpConnectionParams.setSoTimeout(httpParams, CONN_TIMEOUT);
  }
}
