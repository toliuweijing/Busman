package com.polythinking.busman;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import uk.org.siri.siri.Siri;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by developer on 6/25/14.
 */
public class TaskServlet extends HttpServlet {

  public static ArrayList<Date> mTimestamps = new ArrayList<Date>();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String urlString = "http://api.prod.obanyc.com/api/siri/stop-monitoring.xml?key=cfb3c75b-5a43-4e66-b7f8-14e666b0c1c1&LineRef=MTA%20NYCT_B9&MonitoringRef=300071&DirectionRef=1";
    Siri siri = null;
    try {
      siri = getSiri(urlString);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    mTimestamps.add(siri.getServiceDelivery().getResponseTimestamp());
    if (mTimestamps.size() < 3) {
      Queue queue = QueueFactory.getDefaultQueue();
      queue.add(TaskOptions.Builder.withUrl("/taskservlet"));
    }
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