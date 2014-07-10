package com.polythinking.busman;

import uk.org.siri.siri.Siri;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by toliuweijing on 7/10/14.
 */
public class SiriFetcher {

  public Siri getSiri(String urlString) throws JAXBException {
    SiriXmlSerializer serializer = new SiriXmlSerializer();
    Siri siri = serializer.fromXml(getMTAResponse(urlString));
    return siri;
  }

  public Siri getStopMonitoringSample() throws JAXBException {
    String urlString = "http://api.prod.obanyc.com/api/siri/stop-monitoring.xml?key=cfb3c75b-5a43-4e66-b7f8-14e666b0c1c1&MonitoringRef=300067";
    return getSiri(urlString);
  }

  public Siri getStopMonitoring(String stopCode) throws JAXBException {
    String urlString = "http://api.prod.obanyc.com/api/siri/stop-monitoring.xml?key=cfb3c75b-5a43-4e66-b7f8-14e666b0c1c1&MonitoringRef=" + stopCode;
    return getSiri(urlString);
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
