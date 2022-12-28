package ReserveNaver;

import java.sql.Timestamp;
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

import Common.AES256;
import Common.ClipBoard;
import Common.FileFunc;
import Common.SearchCalenderDayIndex;

public class urlTest {
    
    //WebDriver
    private WebDriver driver;
    
    private WebElement element;
    
    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "D:/100-devTools/macro/chromedriver/chromedriver.exe";
    
    //크롤링 할 URL
    private String naver_login_url = "https://nid.naver.com/nidlogin.login?mode=form&url=https%3A%2F%2Fwww.naver.com";
    
    //네이버 로그인 정보
    private String naverId = "RUoRS0lyf9m8sWs9hk3kWg==";
    private String naverPw = "GBRoWwdfulZHKg3AiNCr9w==";

    //복호화
    private AES256 aes256 = new AES256();
    
    //clipBoard
    private ClipBoard cp = new ClipBoard();
    
    
    public urlTest() {
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
   	 
    	urlTest ra = new urlTest();
    	ra.naverLogin();
//    	reserveList = ff.fileRead(args[0]);
    	
    	ra.testUrl();
    	
    }
    
    public void testUrl() {
    	String base_url = "https://booking.naver.com/booking/10/bizes/217811/items/4113330";
    	String xPath = "//*[@id=\"container\"]/bk-freetime/div[2]/div[1]/div/bk-sale-open-banner/div/div[2]/strong";
    	
    	driver.get(base_url);
    	
    	//시간선택 팝업 닫기
    	driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[3]/a/i")).click();
    	element = driver.findElement(By.xpath(xPath));
    	
    	System.out.println("element.getText() : " + element.getText());
    }

    public void naverLogin() {
        try {
            //get page (= 브라우저에서 url을 주소창에 넣은 후 request 한 것과 같다)
            driver.get(naver_login_url);
 
            //iframe으로 구성된 곳은 해당 프레임으로 전환시킨다.
//            driver.switchTo().frame(driver.findElement(By.id("loginForm")));
            
            //iframe 내부에서 id 필드 탐색
            
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
