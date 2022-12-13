package ReserveNaver;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Common.AES256;
import Common.ClipBoard;
import Common.FileFunc;
import Common.SearchCalenderDayIndex;
import DAO.YangjaeDao;

public class ReserveApp {
    
    //WebDriver
    private static WebDriver driver;
    
    private WebElement element;
    
//    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    
    
    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "D:/100-devTools/macro/chromedriver/108/chromedriver.exe";
    
    //크롤링 할 URL
    private String naver_login_url = "https://nid.naver.com/nidlogin.login?mode=form&url=https%3A%2F%2Fwww.naver.com";
    
    //네이버 로그인 정보
    private String naverId = "RUoRS0lyf9m8sWs9hk3kWg==";
    private String naverPw = "GBRoWwdfulZHKg3AiNCr9w==";

    //복호화
    private AES256 aes256 = new AES256();
    
    //clipBoard
    private ClipBoard cp = new ClipBoard();
    
    //File Read to List
    private static FileFunc ff = new FileFunc();
    
    private static List<String> reserveList = new ArrayList();
    
    
    //달력 좌표 찾기
    private SearchCalenderDayIndex scdi = new SearchCalenderDayIndex();
    
    // xPath
    private String calXPath01 = "//*[@id=\"calendar\"]/table/tbody/tr[";
    private String calXPath02 = "]/td[";
    private String calXPath03 = "]/a/span[1]";
    
    
    private String reserveTimeXPath01 = "//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div[";
    private String reserveTimeXPath02 = "]/ul/li[";
    private String reserveTimeXPath03 = "]/a/span/span[1]";
    
    private String reserveTimeButton = "//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[3]/button";
    private String nextButton = "//*[@id=\"container\"]/bk-freetime/div[2]/div[2]/bk-submit/div/button";
    private String nPayButton = "/html/body/app/div[2]/div[2]/bk-freetime/div[2]/div[2]/bk-submit/div/button";
    private String nomalPay = "//*[@id=\"orderForm\"]/div/div[5]/div[1]/div[1]/ul[1]/li[3]/div[1]/span/span";
    private String postPay = "//*[@id=\"orderForm\"]/div/div[5]/div[1]/div[1]/ul[1]/li[3]/ul/li[2]/span[1]/span";
    private String bankDrop = "//*[@id=\"bankCodeList\"]/div/div";
    private String kukMinBank = "/html/body/div[5]/div/ul/li[2]";
    private String orderButton = "//*[@id=\"orderForm\"]/div/div[7]/button";
    private String orderDetailButton = "//*[@id=\"root\"]/div[3]/div/div[3]/a";   
    
    
    //Timestamp
    private static Timestamp ts;
    
    public ReserveApp() {
        super(); 
        //System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        
        //Driver SetUp
         ChromeOptions options = new ChromeOptions();
//         options.addArguments("headless");
         options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36\r\n");
         options.setCapability("ignoreProtectedModeSettings", true);
         driver = new ChromeDriver(options);
         driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    
    public static void main(String[] args) {
    	ArrayList<String> enableTimeList = new ArrayList<>();
    	YangjaeDao yangjaeDao = new YangjaeDao();
    	ReserveApp ra = new ReserveApp();
    	ra.naverLogin();
    	// input : 예약월
    	String reserveMonth = "12";
    	
    	for(String courtNm : yangjaeDao.getCourtNm()) {
    		enableTimeList = ra.checkEnableTime(yangjaeDao, reserveMonth+"_"+courtNm);
        	for(String t : enableTimeList) {
        		System.out.println("enable Time : " + t);
        	}
    	}
    	
    	
    	driver.close();
    }

    public ArrayList<String> checkEnableTime(YangjaeDao yangjaeDao, String courtNm) {
    	driver.get(yangjaeDao.getCourtUrl(courtNm));
    	WebDriverWait wait = new WebDriverWait(driver, 20);
    	ArrayList<String> enableTimeList = new ArrayList<>();
    	
    	// 메헌시민의숲(양재 테니스장) 화면 로딩될때까지 wait  //*[@id="container"]/bk-freetime/div[1]/bk-breadcrumb/div/ul/li[2]/a/span
    	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"container\"]/bk-freetime/div[1]/bk-breadcrumb/div/ul/li[2]/a/span")));
    	
    	/*달력 parsing //*[@id="calendar"]/table
    	 * 달력 열은 7개 (일,월,화,수,목,금,토)
    	 * 달력 행 구하기
    	*/
    	
    	//  //*[@id="calendar"]/table/tbody[1]/tr*
    	
    	//달력의 행
    	List<WebElement> calColElements = driver.findElements(By.xpath("//*[@id=\"calendar\"]/table/tbody[1]/*"));
//    	System.out.println("elements count : " + calColElements.size());
    	//달력 각 행의 열  //*[@id="calendar"]/table/tbody[1]/tr[1]
    	
    	for(int i=0;i<calColElements.size();i++) {
    		
    		for(int j=0;j<7;j++) { //열(일~토)
//    			System.out.println("i:j="+i+":"+j);
    			element = driver.findElement(By.xpath("//*[@id=\"calendar\"]/table/tbody[1]/tr["+(i+1)+"]/td["+(j+1)+"]"));
//    			System.out.println("class name : " + element.getClass());
//    			System.out.println(element.getTagName());
    			
    			
    			
    			String clickDay = element.getText().split("\\n")[0];
//    			System.out.println("getAttri : "+ element.getAttribute("class"));
    			
    			
    			Boolean satFlag = false;
    			Boolean sunFlag = false;
    			if(element.getAttribute("class").contains("calendar-sat") == true) {
    				satFlag = true;
    			}else if(element.getAttribute("class").contains("calendar-sun") == true) {
    				sunFlag = true;
    			}
    			
//    			System.out.println("clickDay : " + clickDay);
//    			System.out.println("class attribute : " + element.getAttribute("class"));
    			
    			
    			// 활성화 된 날짜만 클릭
    			if(element.getAttribute("class").contains("calendar-unselectable") == false) {
    				
    				while(true) {
        				element.click();
        				
        				//  //*[@id="container"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[1]/em/span[1]  
        				// text : 12. 19.(월), 시간을 선택하세요 최소 1시간 - 최대 4시간 이용가능
        				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[1]/em/span[1]")));
        				String displayTimeTabDay = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[1]/em/span[1]"))
        											.getText().replaceAll(" ", "").split("\\.")[1];
        				
//        				System.out.println("displayTimeTabDay : " + displayTimeTabDay);
//        				System.out.println("clickDay : " + clickDay);
        				
        				if(clickDay.equals(displayTimeTabDay) == true) {
        					break;
        				}else {
        					//0.5 sleep
        					try {
    							Thread.sleep(500);
    						} catch (InterruptedException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
        				}
    					
    				}
    				
    				
    				// 일자 TIME 테이블
    				// ex----------------------------------------------------------------------------------------
       				//                                                                          am         1~12 : 12시~11시
    				//*[@id="container"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div[1]/ul/li[1]/a
       				//                                                                          am         1~12 : 12시~11시
    				//*[@id="container"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div[2]/ul/li[1]/a
    				// 선택 못하는 class : class="anchor none"
    				
    				//2중 for문 오전, 오후, 시간
    				
    				for(int g=1;g<=2;g++) {
    					for(int t=1;t<=12;t++) {
    						WebElement timeElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div["
    																		+g+"]/ul/li["+t+"]/a"));
    						if(timeElement.getAttribute("class").contains("none") == false) {
    							
    							if(satFlag == true || sunFlag == true) {
    								if(satFlag == true) {
            							if(g == 1) {	//오전
            								enableTimeList.add(courtNm.split("\\_")[0]+"월 "
            												  +clickDay+"일(토):"
            												  +courtNm.split("\\_")[1]+"코트:오전:"
            												  +timeElement.getText());
            								
            							}else {		//오후
            								enableTimeList.add(courtNm.split("\\_")[0]+"월 "
  												  +clickDay+"일(토):"
  												  +courtNm.split("\\_")[1]+"코트:오후:"
  												  +timeElement.getText());
            							}
    								}else {
    									if(g == 1) {	//오전
    										enableTimeList.add(courtNm.split("\\_")[0]+"월 "
    												  +clickDay+"일(일):"
    												  +courtNm.split("\\_")[1]+"코트:오전:"
    												  +timeElement.getText());
            								
            							}else {		//오후
            								enableTimeList.add(courtNm.split("\\_")[0]+"월 "
  												  +clickDay+"일(일):"
  												  +courtNm.split("\\_")[1]+"코트:오후:"
  												  +timeElement.getText());
            							}
    								}
    							}else {
        							if(g == 1) {	//오전
//        								System.out.println(courtNm+":"+clickDay+":오전:"+timeElement.getText());
        								enableTimeList.add(courtNm.split("\\_")[0]+"월 "
												  +clickDay+"일:"
												  +courtNm.split("\\_")[1]+"코트:오전:"
												  +timeElement.getText());
        							}else {		//오후
//        								System.out.println(courtNm+":"+clickDay+":오후:"+timeElement.getText());
        								enableTimeList.add(courtNm.split("\\_")[0]+"월 "
												  +clickDay+"일:"
												  +courtNm.split("\\_")[1]+"코트:오후:"
												  +timeElement.getText());
        							}    								
    							}

    						}
    					}
    				}    				
    			} //end if
    		}
    	}
    	 
    	return enableTimeList;
    }
    
    public void naverLogin() {
        try {
            driver.get(naver_login_url);
            
            cp.copyToClip(aes256.decrypt(naverId));
            element = driver.findElement(By.id("id"));
            element.sendKeys(Keys.chord(Keys.CONTROL, "v"));
            
            //iframe 내부에서 pw 필드 탐색
            cp.copyToClip(aes256.decrypt(naverPw));
            element = driver.findElement(By.id("pw"));
            element.sendKeys(Keys.chord(Keys.CONTROL, "v"));
            
            //로그인 버튼 클릭
            element = driver.findElement(By.id("log.login"));
            element.click();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
