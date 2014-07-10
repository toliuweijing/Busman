package com.polythinking.busman;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.mortbay.util.ajax.JSONObjectConvertor;
import uk.org.siri.*;
import uk.org.siri.siri.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Date;

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
  public Object monitorB9(@Named("stopcode") String stopCode) throws IOException, JAXBException {
    if (stopCode.equals("-1")) {
      TaskServlet.stopCode = null;
      new NotificationCenter().send("request is stopped");
    } else {
      new NotificationCenter().send(stopCode + " request is accepted");
      if (TaskServlet.stopCode == null) {
        new NotificationCenter().send("server starts running");
        TaskServlet.stopCode = stopCode;
      } else {
        new NotificationCenter().send("server switch:" + TaskServlet.stopCode + " to " + stopCode);
        TaskServlet.stopCode = stopCode;
      }
      TaskServlet.runAsync();
    }

    return TaskServlet.mTimestamps;
  }
}
