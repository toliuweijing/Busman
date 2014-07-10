package com.polythinking.busman;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.w3c.dom.*;
import uk.org.siri.siri.*;

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
import java.util.List;

import static uk.org.siri.siri.VehicleActivityStructure.MonitoredVehicleJourney;

/**
 * Created by developer on 6/25/14.
 */
public class TaskServlet extends HttpServlet {

  public static ArrayList<Date> mTimestamps = new ArrayList<Date>();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    try {
      Siri siri = new SiriFetcher().getStopMonitoringSample();
      for (StopMonitoringDeliveryStructure a : siri.getServiceDelivery().getStopMonitoringDelivery()) {
        for (MonitoredStopVisitStructure b : a.getMonitoredStopVisit()) {
          MonitoredVehicleJourneyStructure c = b.getMonitoredVehicleJourney();
          System.out.println("====" + getLineNum(c) + " is " + getStopsFromCall(c) + " away");
        }
      }
    } catch (JAXBException e) {
      e.printStackTrace();
    }
  }

  private String getLineNum(MonitoredVehicleJourneyStructure j) {
    return j.getLineRef().getValue();
  }

  private String getStopName(MonitoredVehicleJourneyStructure j) {
    return j.getMonitoredCall().getStopPointName().getValue();
  }

  private int getStopsFromCall(MonitoredVehicleJourneyStructure j) {
    ExtensionsStructure extension = j.getMonitoredCall().getExtensions();
    Element distanceNode = (Element) extension.getAny();
    Node l=distanceNode.getLastChild();
    String text = l.getFirstChild().getTextContent();
    return Integer.valueOf(text);
  }

  static public void runAsync() {
    Queue queue = QueueFactory.getDefaultQueue();
    queue.add(TaskOptions.Builder.withUrl("/taskservlet"));
  }
}