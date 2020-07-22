package com.expedite.apps.kumkum.common;

import com.expedite.apps.kumkum.BuildConfig;

/*
 public class GetIp {
 private static String ip = "192.168.0.104";	
 public static String ip(){
 return "http://"+ip+"/Expedite/Service.asmx?wsdl";		
 }
 public static String imgStr(){
 return "http://"+ip+"/Expedite/Images/";
 }
 public static String resultStr(){
 return "http://"+ip+"/Expedite/results/";
 }
 }
 */
public class GetIp {

    private static String ip = "app.macademic.in";
    private static String ip_test = "testapp.macademic.in";
    private static String ip_vehicle = "testvts.vayuna.com";
    public static String NAMESPACE = "http://tempuri.org/";

    public static String ip() {
        //return "http://" + ip + "/Service.asmx";
        if (BuildConfig.DEBUG) {
            return "http://" + ip_test + "/Service.asmx";
        } else {
            return "https://" + ip + "/Service.asmx";
        }
    }

    public static String ipvehicle() {
        return "http://" + ip_vehicle + "/service.asmx";
    }

}
