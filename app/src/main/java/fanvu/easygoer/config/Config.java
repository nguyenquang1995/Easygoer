package fanvu.easygoer.config;

public class Config {
	
	public static String HTTP_WS="http://10.58.71.141:9010/PONWebservice/";
	//public static String URL_WS="http://easygo-fanvu.rhcloud.com/hello?wsdl"; //10.58.71.129
	public static String URL_WS="https://easygo-fanvu.rhcloud.com/rest/"; //10.58.71.129 ;
	//
	public static String URL = "http://10.58.71.141:9010/PONWebservice/AuthenticateWS?wsdl";
	
	public static String URL_WS_VSMART="http://10.60.7.190:8090/QLCTKT/rest/syncPon/syncPoles";
	
	public static String URL_WS_VSMART_TEST="http://10.60.7.190:8090/QLCTKT/resource/workMaintainController/onUpdateComplete";
	
	public static String Sub_version="_build_01_30.12.14";
	  
	  public static int HTTP_TIME_OUT = 150*1000;
	  public static int maximumPointCount = 30;//so luong point maximum truoc khi bi ngat thanh nhieu line
	  public static int minimumPointDistance = 1;//tham so ve duong, tinh bang met, khoang cach giua 2 diem lien tiep nho hon tham so nay thi khong lay
	  public static int maxmimunPointDistance = 100;// tham so ve duong, tinh bang met, khoang cach giua 2 diem lon hon tham so nay thi phai lay
	  public static int deltaAngle = 10;//loai bo cac point co do chenh lech goc nho hon deltaAngle
	  public static int maximumDistancePoint=1000;
	  /*
	   * Config WS log in
	   */
	  public static String SOAP_ACTION1 = "http://webservice.pon.viettel.com/AuthenticateWS/logInRequest";

	  public static String NAMESPACE = "http://webservice.pon.viettel.com/";

	  public static String METHOD_NAME1 = "logIn";
	  
	  public static String URL_LINK="10.58.65.188";
	  public static int URL_PORT=8081;
	  public static String URL_METHOD="/PONWebservice/AuthenticateWS?wsdl";
	  public static String URL_USERNAME="brcd";
	  public static String URL_PASSWORD="brcd12345";
	  
}
