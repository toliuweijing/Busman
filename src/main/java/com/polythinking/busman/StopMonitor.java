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
    if (TaskServlet.mTimestamps.size() < 3) {
      //schedule
      Queue queue = QueueFactory.getDefaultQueue();
      queue.add(TaskOptions.Builder.withUrl("/taskservlet"));
      return new Response();
    }
    return TaskServlet.mTimestamps;
  }

  @ApiMethod(
      path = "stopmonitor/monitor",
      httpMethod = "post")
  public Response monitor(
      @Named("stopid") String stopId,
      @Named("routeId") String routeId,
      @Named("direction") Integer direction) throws IOException {
    Response r = new Response();
    r.r = "200";
    return r;
  }

  // ====================Junk===================
  public static ArrayList<HelloGreeting> greetings = new ArrayList<HelloGreeting>();

  static {
    greetings.add(new HelloGreeting("hello world!"));
    greetings.add(new HelloGreeting("goodbye world!"));
  }

  public ArrayList<HelloGreeting> listGreeting() {
    return greetings;
  }

  @ApiMethod(name = "greetings.multiply", httpMethod = "post")
  public HelloGreeting insertGreeting(@Named("times") Integer times, HelloGreeting greeting) {
    HelloGreeting response = new HelloGreeting();
    StringBuilder responseBuilder = new StringBuilder();
    for (int i = 0; i < times; i++) {
      responseBuilder.append(greeting.getMessage());
    }
    response.setMessage(responseBuilder.toString());
    return response;
  }

  @ApiMethod(name = "greetings.authed", path = "hellogreeting/authed")
  public HelloGreeting authedGreeting(User user) {
    HelloGreeting response = new HelloGreeting("hello " + user.getEmail());
    return response;
  }


    @ApiMethod(name = "greetings.getmta", path="hellogreeting/getmta")
  public Siri getMTA() throws JAXBException {
    final String urlString = "http://api.prod.obanyc.com/api/siri/vehicle-monitoring.xml?key=cfb3c75b-5a43-4e66-b7f8-14e666b0c1c1&LineRef=MTA%20NYCT_B9";
      return getSiri(urlString);
//    SiriXmlSerializer serializer = new SiriXmlSerializer();
//    final String urlString = "http://api.prod.obanyc.com/api/siri/vehicle-monitoring.xml?key=cfb3c75b-5a43-4e66-b7f8-14e666b0c1c1&LineRef=MTA%20NYCT_B9";
//    Siri siri = serializer.fromXml(getMTAResponse(urlString));
//    return siri;
  }

  private Siri getSiri(String urlString) throws JAXBException {
    SiriXmlSerializer serializer = new SiriXmlSerializer();
    Siri siri = serializer.fromXml(getMTAResponse(urlString));
    return siri;
  }

  private String getMTAResponse(String urlString) {
       try {
           URL url = new URL(urlString);
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();

           BufferedReader in = new BufferedReader(
                   new InputStreamReader(connection.getInputStream()));
           String buffer = "";
           String inputLine;
           while ((inputLine = in.readLine()) != null) {
                buffer += inputLine;// + "\n";
           }
           return buffer;
       } catch (Exception e) {
           return "error";
       }
   }

}
