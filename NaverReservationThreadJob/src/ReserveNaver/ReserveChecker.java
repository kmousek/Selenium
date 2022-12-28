package ReserveNaver;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import javax.xml.crypto.Data;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import Common.ClipBoard;
import Common.FileFunc;
import Login.Naver;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ReserveChecker {

    //WebDriver
    private static WebDriver driver;
    
    private static WebElement element;
    
    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "D:/100-devTools/macro/chromedriver/chromedriver.exe";
    
    private static String base_url = "https://bookingapi.naver.com/v1/user/service/217811/resource/search?noCache=1633346419446&businessId=217811&lang=ko&validEndDate=2021-10-06&validHourStr=000000001000000000000000&validStartDate=2021-10-06";
    
    private String naver_mail_url = "https://mail.naver.com/#%7B%22fClass%22%3A%22write%22%2C%22oParameter%22%3A%7B%22orderType%22%3A%22toMe%22%2C%22sMailList%22%3A%22%22%7D%7D";
    
    
    //                                                                                                                                                         000000001000000000000000
    //https://bookingapi.naver.com/v1/user/service/210031/resource/search?noCache=0            &businessId=210031&lang=ko&validEndDate=2021-10-07&validHourStr=000000000100000000000000&validStartDate=2021-10-07
    //https://bookingapi.naver.com/v1/user/service/210031/resource/search?noCache=1633413737008&businessId=210031&lang=ko&validEndDate=2021-10-07&validHourStr=000000000100000000000000&validStartDate=2021-10-07
    //https://bookingapi.naver.com/v1/user/service/210031/resource/search?noCache=1633346419446&businessId=210031&lang=ko&validEndDate=2021-10-06&validHourStr=000000001000000000000000&validStartDate=2021-10-06
    
//    validEndDate
//    1  2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24
//    0  0 0 0 0 0 0 0 0 1  0  0  0  0  0  0  0  0  0  0  0  0  0  0
//    12 1 2 3 4 5 6 7 8 9  10 11 12 13 14 15 16 17 18 19 20 21 22 23
    
    private String reserve_chk_base_url01 = "https://bookingapi.naver.com/v1/user/service/";
    //business id
    private String reserve_chk_base_url02 = "/resource/search?noCache=1633346419446&businessId=";
    //business id
    private String reserve_chk_base_url03 = "&lang=ko&validEndDate=";
    //endDate
    private String reserve_chk_base_url04 = "&validHourStr=000000000100000000000000&validStartDate=";
    //StartDate
	
    
    
    //https://booking.naver.com/booking/10/bizes/210031/items/4067313
    private String court_url01 = "https://booking.naver.com/booking/";
    //현재월
    private String court_url02 = "/bizes/";
    //bussinessId
    private String court_url03 = "/items/";
    //resocId (courtId)
    
    
    //https://api.booking.naver.com/v3.0/businesses/210031/biz-items/4067313/hourly-schedules?noCache=1633444875911&endDateTime=2021-10-07T00:00:00&startDateTime=2021-10-07T00:00:00
    private String court_time_url01 = "https://api.booking.naver.com/v3.0/businesses/";
    //businessId
    private String court_time_url02 = "/biz-items/";
    //resocId (courtId)
    private String court_time_url03 = "/hourly-schedules?noCache=1633444875911&endDateTime=";
    //endDateTime
    private String court_time_url04 = "T00:00:00&startDateTime=";
    //startDateTime
    private String court_time_url05 = "T00:00:00";
    
    
    private static FileFunc ff = new FileFunc();
    
    //Timestamp
    private static Timestamp ts;
    
    
    private static List<String> reserveChkList = new ArrayList();
    
    private static List<String> enableCourtList = new ArrayList();
    
    
    private List<String> validateTime = new ArrayList<String>();
    
    public ReserveChecker() {
        super(); 
        //System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        
        validateTime.add("000000100000000000000000");
        validateTime.add("000000010000000000000000");
        validateTime.add("000000001000000000000000");
        validateTime.add("000000000100000000000000");
        validateTime.add("000000000010000000000000");
        validateTime.add("000000000001000000000000");
        validateTime.add("000000000000100000000000");
        validateTime.add("000000000000010000000000");
        validateTime.add("000000000000001000000000");
        validateTime.add("000000000000000100000000");
        validateTime.add("000000000000000010000000");
        validateTime.add("000000000000000001000000");
        validateTime.add("000000000000000000100000");
        validateTime.add("000000000000000000010000");
        validateTime.add("000000000000000000001000");
        validateTime.add("000000000000000000000100");
        
        //Driver SetUp
         ChromeOptions options = new ChromeOptions();
//         options.addArguments("headless");
         options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36\r\n");
         options.setCapability("ignoreProtectedModeSettings", true);
         driver = new ChromeDriver(options);
         driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    
    public static void main(String[] args) throws ParseException, java.text.ParseException {
    	ReserveChecker rc = new ReserveChecker();
    	Naver naver = new Naver();
    	naver.naverLogin(driver, element);
//    	rc.sendNaverEmail();
    	
    	reserveChkList = ff.fileRead(args[0]);
    	
    	boolean reserveStatus = false;
    	
    	while(true) {
    		ts = new Timestamp(System.currentTimeMillis());
        	for(String s : reserveChkList){
        	// s : bussinessId, ChkStartDate, ChkEndDate
        	//ex : 210031(양재), 20211008, 20211031
        		
        		rc.chkReserveList(s);
        		
        	}   
        	

        	
        	rc.chkEnableTime();
        	enableCourtList.clear();
        	System.out.println("---------------------------------------------");
        	try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    }
    
    public void chkEnableTime() {
    	
    /*
    	//코트                  ,BussinessId,  courtId,  year, month, day, reserveDate
    	//10월 4번코트(실외,인조잔디),210031      , 4067313,  2021, 10     ,7 , 2021-10-07
   //index    0                   1            2         3    4      5      6
    	
    	for(String s :  enableCourtList) {
    		String[] enableDayInfo = s.split(",");
			String court_url = court_url01+enableDayInfo[4]+court_url02+enableDayInfo[1]+court_url03+enableDayInfo[2];
			System.out.println("court_url : " + court_url);
			driver.get(court_url);
			
    	}
    */
    	
//        //https://api.booking.naver.com/v3.0/businesses/210031/biz-items/4067313/hourly-schedules?noCache=1633444875911&endDateTime=2021-10-07T00:00:00&startDateTime=2021-10-07T00:00:00
//        private String court_time_url01 = "https://api.booking.naver.com/v3.0/businesses/";
//        //businessId
//        private String court_time_url02 = "/biz-items/";
//        //resocId (courtId)
//        private String court_time_url03 = "/hourly-schedules?noCache=1633444875911&endDateTime=";
//        //endDateTime
//        private String court_time_url04 = "T00:00:00&startDateTime=";
//        //startDateTime
//        private String court_time_url05 = "T00:00:00";
    	
    	for(String s :  enableCourtList) {
    		String[] enableDayInfo = s.split(",");
    		String court_time_url = court_time_url01
    				+enableDayInfo[1]
    				+court_time_url02
    				+enableDayInfo[2]
    				+court_time_url03
    				+enableDayInfo[6]
    				+court_time_url04
    				+enableDayInfo[6]
    				+court_time_url05;
    		
    		driver.get(court_time_url);
    		String responseJSON = driver.findElement(By.cssSelector("pre")).getText();
    		System.out.println("responseJSON : " + responseJSON);
    	}
    	
    	
    }
    
    public void chkReserveList(String reserveChkInfo) throws ParseException, java.text.ParseException {
    	List<String> rcInfo = new ArrayList<String>();
    	int[] calIndex = new int[2];
    	boolean rtnVal = true;
    	
    	rcInfo = Arrays.asList(reserveChkInfo.split(","));
    	// s : bussinessId, ChkStartDate, ChkEndDate
    	//ex : 210031(양재), 20211008, 20211031
    	
//    	System.out.println(Integer.parseInt(rcInfo.get(1).substring(0, 4)));
//    	System.out.println(Integer.parseInt(rcInfo.get(1).substring(4, 6)));
//    	System.out.println(Integer.parseInt(rcInfo.get(1).substring(6, 8)));
//    	
//    	System.out.println(Integer.parseInt(rcInfo.get(2).substring(0, 4)));
//    	System.out.println(Integer.parseInt(rcInfo.get(2).substring(4, 6)));
//    	System.out.println(Integer.parseInt(rcInfo.get(2).substring(6, 8)));
    	
    	
    	
    	
    	LocalDate startDate = LocalDate.of(Integer.parseInt(rcInfo.get(1).substring(0, 4))
    			, Integer.parseInt(rcInfo.get(1).substring(4, 6))
    			, Integer.parseInt(rcInfo.get(1).substring(6, 8)));
    	LocalDate endDate = LocalDate.of(Integer.parseInt(rcInfo.get(2).substring(0, 4))
    			, Integer.parseInt(rcInfo.get(2).substring(4, 6))
    			, Integer.parseInt(rcInfo.get(2).substring(6, 8)));
    	
    	
    	int days = (endDate.minusDays(startDate.get(ChronoField.DAY_OF_MONTH))).getDayOfMonth() + 1;
    	
//    	System.out.println("days : " + days);
    	
    	List<String> chkDateList = new ArrayList<String>();
    	for(int i=0;i<days;i++) {
    		String tmpMonth = "";
    		String tmpDay = "";
    		if(startDate.getMonthValue()<10) {
    			tmpMonth = "0"+Integer.toString(startDate.getMonthValue());
    		}else {
    			tmpMonth = Integer.toString(startDate.getMonthValue());
    		}
    		
    		if((startDate.getDayOfMonth()+i)<10) {
    			tmpDay = "0"+Integer.toString((startDate.getDayOfMonth()+i));
    		}else {
    			tmpDay = Integer.toString((startDate.getDayOfMonth()+i));
    		}
    		
    		chkDateList.add(Integer.toString(startDate.getYear()) + 
    				"-" + tmpMonth + 
    				"-" + tmpDay);
    	}
    	
//    	System.out.println("chkDateList.toString() : " + chkDateList.toString());
    	String reserve_chk_base_url = "";
    	for(int i=0;i<chkDateList.size();i++) {
        	reserve_chk_base_url = reserve_chk_base_url01
        			+rcInfo.get(0)
        			+reserve_chk_base_url02
        			+rcInfo.get(0)
        			+reserve_chk_base_url03
        			+chkDateList.get(i)
        			+reserve_chk_base_url04
        			+chkDateList.get(i);
        	
        	driver.get(reserve_chk_base_url);
//        	System.out.println("reserve_chk_base_url : " + reserve_chk_base_url);
        	
        	String responseJSON = driver.findElement(By.cssSelector("pre")).getText();
        	
        	JSONParser parser = new JSONParser();
        	Object obj = parser.parse(responseJSON);
        	JSONObject jsonObj = (JSONObject) obj;
        	JSONArray dataArr = (JSONArray)jsonObj.get("data");

        	String chkDay = "";

        	for(int j=0; j<dataArr.size();j++) {
        		JSONObject dataObj = (JSONObject)dataArr.get(j);
        		JSONObject baseDateObj = (JSONObject) dataObj.get("baseDate");
        		
        		JSONArray valuesArr = (JSONArray)baseDateObj.get("values");
//        		System.out.println("valuesArr : " + valuesArr);
//        		System.out.println("valuesArr.size() : " + valuesArr.size());
//        		System.out.println("valuesArr.get(0) : " + valuesArr.get(0));
        		
        		chkDay = valuesArr.get(0)+","+valuesArr.get(1)+","+valuesArr.get(2);
        		
        		enableCourtList.add(((String)dataObj.get("resocName")).replace(" ", "").replace(",", "") + 
        				"," + rcInfo.get(0) +   //bussinessID
        				"," + (long)dataObj.get("resocId")  + 
        				"," + chkDay +
        				"," + chkDateList.get(i));
        		System.out.println(((String)dataObj.get("resocName")).replace(" ", "").replace(",", "") + 
        				"," + rcInfo.get(0) + 
        				"," + (long)dataObj.get("resocId")  + 
        				"," + chkDay +
        				"," + chkDateList.get(i));
        		chkDay = "";
        	}
        	
//        	System.out.println("resonseJSON : " + responseJSON);
    	}
    	
    	
//    	Date startDate = stringToDate(rcInfo.get(1));
//    	Date endDate = stringToDate(rcInfo.get(2));
    	

    	
//    	for(int i=0;i<calDateDays;i++) {
//    		
//    		chkDateList.add(Integer.toString(startDate.getYear()) + "-");
//    	}
//    	
//    	System.out.println("calDateDays : " + calDateDays);
//    	
//    	System.out.println("startDate : " + startDate);
//    	System.out.println("endDate : " + endDate);
    	

    }
    
    
    public void sendNaverEmail() {
    	ClipBoard cp = new ClipBoard();
    	
    	driver.get(naver_mail_url);
    	
    	element = driver.findElement(By.xpath("//*[@id=\"subject\"]"));
    	element.click();
    	element.sendKeys("test");
    	
    	cp.copyToClip("kekemousek@gmail.com");
    	element = driver.findElement(By.xpath("/html/body"));
    	element.sendKeys(Keys.chord(Keys.CONTROL, "v"));
    	
//    	element = driver.findElement(By.xpath("//*[@id=\"sendBtn\"]"));
//    	element.click();
    	
    	
    	
//    	cp.copyToClip("kekemousek@gmail.com");
//    	element = driver.findElement(By.xpath("//*[@id=\"identifierId\"]"));
//    	element.sendKeys(Keys.chord(Keys.CONTROL, "v"));
//    	
//    	driver.findElement(By.xpath("//*[@id=\"identifierNext\"]/div/button/span")).click();
    	
    }
    
    public void checkReserveList() throws ParseException {
    	driver.get(base_url);
//    	System.out.println(driver.getPageSource());
    	String text = driver.findElement(By.cssSelector("pre")).getText();
//    	System.out.println("[text : " + text+"]");

    	
    	JSONParser parser = new JSONParser();
    	Object obj = parser.parse(text);
    	JSONObject jsonObj = (JSONObject) obj;
    	
//    	System.out.println(jsonObj.get("data"));
    	JSONArray dataArr = (JSONArray)jsonObj.get("data");
    	
    	for(int i=0; i<dataArr.size();i++) {
    		JSONObject dataObj = (JSONObject)dataArr.get(i);
    		System.out.println((String)dataObj.get("resocName"));
    		System.out.println((long)dataObj.get("resocId"));
    	}
    }
    
}
