package com.polythinking.busman;

import com.google.api.server.spi.config.Api;

import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.users.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import uk.org.siri.siri.*;

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
public class VehicleMonitor {

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
    SiriXmlSerializer serializer = new SiriXmlSerializer();
    Siri siri = serializer.fromXml(getMTAResponse());
    return siri;
  }

  private String getMTAResponse() {
      final String urlString = "http://api.prod.obanyc.com/api/siri/vehicle-monitoring.xml?key=cfb3c75b-5a43-4e66-b7f8-14e666b0c1c1&LineRef=MTA%20NYCT_B9";
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
