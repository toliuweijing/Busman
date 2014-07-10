package com.polythinking.busman;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by toliuweijing on 7/10/14.
 */
public class NotificationCenter {
  private static final String APPLICATION_ID = "A5MS5swaJSd4dakhY9biHmsHDhNbaQ5zG5XJrSy3";
  private static final String REST_API_KEY = "wcW5946hJmOawnE9ht6x3T7G8URewXwta4PdflrA";
  private static final String PUSH_URL = "https://api.parse.com/1/push";

  public void send(String msg) {
    String type = "android";
    Map<String, String> data = new HashMap<String, String>();
    data.put("alert", msg);
    String[] channels = new String[]{"testsddg"};
    try {
      sendPost(channels, type, data);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private void sendPost(String[] channels, String type, Map<String, String> data) throws Exception {
    JSONObject jo = new JSONObject();
    jo.put("where", new HashMap<String, String>());
    if(type != null) {
      //如果type为空，默认android和ios都发送
//      jo.put("type", type);
    }
    jo.put("data", data);

    this.pushData(jo.toString());
  }

  private void pushData(String postData) throws Exception {
    DefaultHttpClient httpclient = new DefaultHttpClient();
    HttpResponse response = null;
    HttpEntity entity = null;
    String responseString = null;
    HttpPost httpost = new HttpPost(PUSH_URL);
    httpost.addHeader("X-Parse-Application-Id", APPLICATION_ID);
    httpost.addHeader("X-Parse-REST-API-Key", REST_API_KEY);
    httpost.addHeader("Content-Type", "application/json");
    StringEntity reqEntity = new StringEntity(postData);
    httpost.setEntity(reqEntity);
    response = httpclient.execute(httpost);
    entity = response.getEntity();
    if (entity != null) {
      responseString = EntityUtils.toString(response.getEntity());
    }

    System.out.println(responseString);
  }
}
