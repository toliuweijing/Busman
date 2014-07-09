package com.polythinking.busman;

import com.google.api.server.spi.config.Api;

import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.users.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import uk.org.siri.*;
import uk.org.siri.siri.*;
import uk.org.siri.siri.Siri;

import javax.inject.Named;
import javax.xml.bind.JAXBException;

/**
 * Defines v1 of a helloworld API, which provides simple "greeting" methods.
 */
@Api(
    name = "monitor",
    version = "v1",
    scopes = {Constants.EMAIL_SCOPE},
    clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID},
    audiences = {Constants.ANDROID_AUDIENCE}
)
public class StopMonitor {

  public class Response {
    public Response() {
      this.r ="200";
    }
    public String r;
  }

  @ApiMethod(name = "stopMonitor.monitorB9", httpMethod = "post")
  public Object monitorB9() throws IOException, JAXBException {
    TaskServlet.mTimestamps.add(new Date());
    if (TaskServlet.mTimestamps.size() < 5) {
      //schedule
      Queue queue = QueueFactory.getDefaultQueue();
      queue.add(TaskOptions.Builder.withUrl("/taskservlet"));
      return new Response();
    }
    return TaskServlet.mTimestamps;
  }
}
