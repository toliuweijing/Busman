package com.polythinking.busman;

/**
 * Contains the client IDs and scopes for allowed clients consuming the helloworld API.
 */
public class Constants {
  public static final String API_EXPLORER_CLIENT_ID = com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID;
  public static final String WEB_CLIENT_ID = "604867009692-mutku7dnkv8fgscfam11190dk9emmo0b.apps.googleusercontent.com";
  public static final String ANDROID_CLIENT_ID = "replace this with your Android client ID";
  public static final String IOS_CLIENT_ID = "replace this with your iOS client ID";
  public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;

  public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";

  // ===========URL========
  public static final String URL_SIRI_ROOT_STOP_MONITORING_XML = "http://bustime.mta.info/api/siri/stop-monitoring.xml?";
  public static final String URL_SIRI_COMPONENT_KEY = "key=cfb3c75b-5a43-4e66-b7f8-14e666b0c1c1";
  public static final String URL_SIRI_PARAM_LINEREF = "LineRef";
  public static final String URL_SIRI_PARAM_MONITORINGREF = "MonitoringRef"; // stopid/code??
  public static final String URL_SIRI_PARAM_DIRECTIONREF = "DirectionRef";
}
