package ReserveNaver;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import Common.FileFunc;
import Login.Naver;

public class SearchEnableCourtApp {

    //WebDriver
    private static WebDriver driver;
    
    private static WebElement element;
    
    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "D:/100-devTools/macro/chromedriver/chromedriver.exe";
    
    private Map<String, String> validateTimeMap = new HashMap<String, String>();
    private Map<String, String> busiIdNameMap = new HashMap<String, String>();
    
    private String reserveOpenXpath = "//*[@id=\"container\"]/bk-freetime/div[2]/div[1]/div/bk-sale-open-banner/div/div[2]/strong";
    
    /*
     * https://bookingapi.naver.com/v1/user/service/
     * --210031
     * /resource/search?noCache=1633346419447&businessId=
     * --210031
     * &lang=ko&validEndDate=
     * --2021-11-10
     * &validHourStr=
     * --000000111111111111111100
     * &validStartDate=
     * --2021-11-10
     */
    private String search_url01 = "https://bookingapi.naver.com/v1/user/service/";
    //businessId
    private String search_url02 = "/resource/search?noCache=1633346419447&businessId=";
    //businessId
    private String search_url03 = "&lang=ko&validEndDate=";
    //EndDate
    private String search_url04 = "&validHourStr=";
    //validateHour
    private String search_url05 = "&validStartDate=";
    //StartDate
   
    private static FileFunc ff = new FileFunc();
    
    private static List<String> searchList = new ArrayList<String>();
    
    public SearchEnableCourtApp() {
        super(); 
        //System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        
        validateTimeMap.put("6",  "000000100000000000000000");
        validateTimeMap.put("7",  "000000010000000000000000");
        validateTimeMap.put("8",  "000000001000000000000000");
        validateTimeMap.put("9",  "000000000100000000000000");
        validateTimeMap.put("10", "000000000010000000000000");
        validateTimeMap.put("11", "000000000001000000000000");
        validateTimeMap.put("12", "000000000000100000000000");
        validateTimeMap.put("13", "000000000000010000000000");
        validateTimeMap.put("14", "000000000000001000000000");
        validateTimeMap.put("15", "000000000000000100000000");
        validateTimeMap.put("16", "000000000000000010000000");
        validateTimeMap.put("17", "000000000000000001000000");
        validateTimeMap.put("18", "000000000000000000100000");
        validateTimeMap.put("19", "000000000000000000010000");
        validateTimeMap.put("20", "000000000000000000001000");
        validateTimeMap.put("21", "000000000000000000000100");
        
        
        busiIdNameMap.put("210031", "양재");
        busiIdNameMap.put("217811", "내곡");
        
        //Driver SetUp
         ChromeOptions options = new ChromeOptions();
//         options.addArguments("headless");
         options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36\r\n");
         options.setCapability("ignoreProtectedModeSettings", true);
         driver = new ChromeDriver(options);
         driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    
    
    public static void main(String[] args) throws ParseException {
    	SearchEnableCourtApp sa = new SearchEnableCourtApp();
    	Naver n = new Naver();
    	n.naverLogin(driver, element);
    	searchList = ff.fileRead(args[0]);
    	
    	for(String s : searchList){
    		sa.searchEnableCourtList(s);
    	}   
    	
    	
    	driver.close();
    }
    
    public void searchEnableCourtList(String chkInfo) throws ParseException {
    	
    	/* chkInfo format
    	 *	busiId		SearchStartTime		SearchEndTime 
    	 *  210031(양재)	6					21
    	 */
    	
    	Map<String, String> chkInfoMap = new HashMap<String, String>();
    	List<String> searchDaysList = new ArrayList<String>();
    	
    	
    	chkInfoMap.put("busiId", 			Arrays.asList(chkInfo.split(",")).get(0));
//    	chkInfoMap.put("SearchStartDate", 	Arrays.asList(chkInfo.split(",")).get(1));
//    	chkInfoMap.put("SearchEndDate", 	Arrays.asList(chkInfo.split(",")).get(2));
    	chkInfoMap.put("SearchStartTime", 	Arrays.asList(chkInfo.split(",")).get(1));
    	chkInfoMap.put("SearchEndTime", 	Arrays.asList(chkInfo.split(",")).get(2));
    	
    	int sTime = Integer.parseInt(chkInfoMap.get("SearchStartTime"));
    	int eTime = Integer.parseInt(chkInfoMap.get("SearchEndTime"));
    	
    	
    	//조회 시간 06~21시
    	List<String> searchTimeList = new ArrayList<String>();
    	for(int i=sTime;i<=eTime;i++) {
    		searchTimeList.add(validateTimeMap.get(Integer.toString(i)));
    	}
    	
    	//조회 일자 생성
    	
//    	System.out.println("searchTimeList.toString() : " + searchTimeList.toString());
    	searchDaysList = makeSearchDaysList();

    	for(String lDay : searchDaysList) {
    		for(String lTime : searchTimeList) {
    			List<String> courtInfo = new ArrayList<String>();
    			String searchBaseUrl = makeSearchBaseUrl(chkInfoMap.get("busiId"), lDay, lTime);
    			driver.get(searchBaseUrl);
    			String responseJSON = driver.findElement(By.cssSelector("pre")).getText();
    			
    			courtInfo = parseResponseJSON(responseJSON);
    			
    			LocalDate trlDay = LocalDate.parse(lDay, DateTimeFormatter.ISO_DATE);
    			String lDayOfWeek = trlDay.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
    			
    			String courtId = "";
    			for(String ctInfo : courtInfo) {
    				courtId = (ctInfo.split(","))[1];
    				driver.get(makeCourtUrl(chkInfoMap.get("busiId"), courtId));
    				//시간선택 팝업 닫기
    				
    				try {
    					driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[3]/a/i")).click();
    				}catch (Exception e){
    					
    				}
    		    	
    				
    				//System.out.println(element.findElement(By.xpath(reserveOpenXpath)).getText());
    				try {
    					element = driver.findElement(By.xpath(reserveOpenXpath));
    				}catch(Exception e) {
        				System.out.println("courtInfo : [" + busiIdNameMap.get(chkInfoMap.get("busiId"))
						   +"],["+lDay  //String.format("%01d", Integer.parseInt(lDay))
						   +"],["+lDayOfWeek
						   +"],["+String.format("%2s", convertTime(lTime)) 
						   +"],["+String.format("%30s", ctInfo) 
						   //+"],["+makeCourtUrl(chkInfoMap.get("busiId"), courtId)+"]" 
						   +"]"
						   );
    				}
	

    			}
    		}
    	}
    	System.out.println("============================================================");
    	
    }
    
    public String makeCourtUrl(String busiId, String courtId) {
    	//https://booking.naver.com/booking/10/bizes/217811/items/4113329
    	return "https://booking.naver.com/booking/10/bizes/"+busiId+"/items/"+courtId;
    }
    
    public String convertTime(String lTime) {
    	for(Object o : validateTimeMap.keySet()) {
            if(validateTimeMap.get(o).equals(lTime)) {
                return o.toString();
            }
        }
    	return null;
    }
    
    public List<String> parseResponseJSON(String rJSON) throws ParseException {
    	List<String> courtInfo = new ArrayList<String>();
    	JSONParser parser = new JSONParser();
    	Object obj = parser.parse(rJSON);
    	JSONObject jsonObj = (JSONObject) obj;
    	
//    	System.out.println(jsonObj.get("data"));
    	JSONArray dataArr = (JSONArray)jsonObj.get("data");
    	
    	for(int i=0; i<dataArr.size();i++) {
    		JSONObject dataObj = (JSONObject)dataArr.get(i);
//    		System.out.println((String)dataObj.get("resocName"));
//    		System.out.println((long)dataObj.get("resocId"));
    		if(((String)dataObj.get("resocName")).contains("다목적")) {
    			//다목적장 제외
    		}else {
    			courtInfo.add(((String)dataObj.get("resocName")).replace(",", "").replace(" ", "")
    						+","+(long)dataObj.get("resocId"));
    		}
    		
    	}
    	
    	return courtInfo;
    }
    
    public String makeSearchBaseUrl(String busiId, String searchDate, String validHour) {
    	
    	String rtnUrl = search_url01
    					+ busiId
    					+ search_url02
    					+ busiId
    					+ search_url03
    					+ searchDate
    					+ search_url04
    					+ validHour
    					+ search_url05
    					+ searchDate;
    	
    	return rtnUrl;
    }
    
    public List<String> makeSearchDaysList(){
    	List<String> searchDaysList = new ArrayList<String>();
    	
    	LocalDate toDate = LocalDate.now();
//    	LocalDate toDay = LocalDate.of(2021, 2, 28);  //test
//    	int toDay = toDate.getDayOfMonth();
    	int toDay = toDate.plusDays(1).getDayOfMonth();  //다음날부터!
    	int toMonth = toDate.getMonthValue();
    	int toMonthlastDay = YearMonth.from(toDate).atEndOfMonth().getDayOfMonth();
    	int toYear = toDate.getYear();
    	
    	int nextMonth = toDate.plusMonths(1).getMonthValue();
    	int nextMonthlastDay = YearMonth.from(toDate.plusMonths(1)).atEndOfMonth().getDayOfMonth();
    	
//    	System.out.println("toDate : " + toDate);
//    	System.out.println("toDay : " + String.format("%02d", toDay));
//    	System.out.println("toMonth : " + toMonth);
//    	System.out.println("nextMonth : " + nextMonth);
//    	System.out.println("toMonthlastDay : " + toMonthlastDay);
//    	System.out.println("nextMonthlastDay : " + nextMonthlastDay);
    	
    	for(int i = toDay;i<=toMonthlastDay;i++) {
    		searchDaysList.add(Integer.toString(toYear)+"-"+String.format("%02d", toMonth)+"-"+String.format("%02d", i));
    	}
    	
    	for(int i = 1;i<=nextMonthlastDay;i++) {
    		searchDaysList.add(Integer.toString(toYear)+"-"+String.format("%02d", nextMonth)+"-"+String.format("%02d", i));
    	}
    	
//    	System.out.println("searchDaysList.toString() : " + searchDaysList.toString());

    	return searchDaysList;
    }
    

    
    public String yyyymmddToyyyy_mm_dd(String yyyymmdd){
    	
    	LocalDate rtnDate = LocalDate.of(Integer.parseInt(yyyymmdd.substring(0, 4))
    			, Integer.parseInt(yyyymmdd.substring(4, 6))
    			, Integer.parseInt(yyyymmdd.substring(6, 8)));
    	
    	String tmpMonth = "";
		String tmpDay = "";
		if(rtnDate.getMonthValue()<10) {
			tmpMonth = "0"+Integer.toString(rtnDate.getMonthValue());
		}else {
			tmpMonth = Integer.toString(rtnDate.getMonthValue());
		}
		
		if(rtnDate.getDayOfMonth()<10) {
			tmpDay = "0"+Integer.toString(rtnDate.getDayOfMonth());
		}else {
			tmpDay = Integer.toString(rtnDate.getDayOfMonth());
		}
		
    	return Integer.toString(rtnDate.getYear())+"-"+tmpMonth+"-"+tmpDay;
    }
}
