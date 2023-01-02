package Login;


import java.net.CookieStore;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.fluent.Executor;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.SessionId;

import Common.AES256;
import Common.ClipBoard;

public class Naver {
    //WebDriver
  //  private WebDriver driver;
    
//    private WebElement element;
    
    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "D:/100-devTools/macro/chromedriver/97/chromedriver.exe";
	
	private String naver_login_url = "https://nid.naver.com/nidlogin.login?mode=form&url=https%3A%2F%2Fwww.naver.com";
	
	private AES256 aes256 = new AES256();
	private ClipBoard cp = new ClipBoard();
	
    //네이버 로그인 정보
    private String naverId = "RUoRS0lyf9m8sWs9hk3kWg==";
    private String naverPw = "GBRoWwdfulZHKg3AiNCr9w==";
    
    private WebElement element;
    
	
    public void naverLogin(WebDriver driver) {
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
            
            
            driver.switchTo().frame("minime");
            
            // frame   minime
            
            // 로그인 성공시 id표시 /html/body/div/div/div[1]/div[1]/div/div[1]/a[1]/span
            
            // 로그아웃 버튼  /html/body/div/div/div[1]/div[1]/a[1]
            
            // 로그인 버튼   //*[@id="account"]/a
            
            ////*[@id="account"]/p  : text 네이버를 더 안전하고 편리하게 이용하세요
            try {
            	WebElement loginChkElement = driver.findElement(By.xpath("//*[@id=\"account\"]/p"));
                System.out.println("loginChkElement.getText() : " + loginChkElement.getText());
            }catch(Exception e) {
            	WebElement logoutButtonElement = driver.findElement(By.xpath("/html/body/div/div/div[1]/div[1]/a[1]"));
                System.out.println("logoutButtonElement.getText() : " + logoutButtonElement);
            }
            
          
            System.out.println(driver.manage().getCookies());
            WebDriver newDriver = getDriverHeadless();
            
            Set<Cookie> cookies = driver.manage().getCookies();
            
            driver.quit();
            
            
            CookieStore cookieStore = (CookieStore) convertBrowserCookie(cookies);
            Executor executor = Executor.newInstance();
//            cookieStore.get("https://nid.naver.com/nidlogin.login?mode=form&url=https%3A%2F%2Fwww.naver.com");
            
//            HttpSession session = request.getSession();

            
            // https://mail.naver.com/v2/folders/0/all
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public BasicCookieStore convertBrowserCookie(Set<Cookie> browserCookies)
    {
    	BasicCookieStore cookieStore = new BasicCookieStore();
        for(Cookie browserCookie : browserCookies)
        {
            BasicClientCookie basicClientCookie = new BasicClientCookie(browserCookie.getName(), browserCookie.getValue());
            basicClientCookie.setDomain(browserCookie.getDomain());
            basicClientCookie.setAttribute(BasicClientCookie.DOMAIN_ATTR, browserCookie.getDomain());
            basicClientCookie.setSecure(browserCookie.isSecure());
            basicClientCookie.setExpiryDate(browserCookie.getExpiry());
            basicClientCookie.setPath(browserCookie.getPath());
            cookieStore.addCookie(basicClientCookie);
        }
        return cookieStore;
    }
    
    public WebDriver getDriverHeadless() {
        //Properties
        final String WEB_DRIVER_ID = "webdriver.chrome.driver";
        final String WEB_DRIVER_PATH = "D:/100-devTools/macro/chromedriver/108/chromedriver.exe";
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        //WebDriver
        WebDriver driver;
        
        //Driver SetUp
         ChromeOptions options = new ChromeOptions();
//         options.addArguments("disa/ble-gpu");
//         options.addArguments("headless");
         options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.5359.100 Safari/537.36\r\n");
         options.setCapability("ignoreProtectedModeSettings", true);
         driver = new ChromeDriver(options);
         driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
         
         return driver;
    }
    
    
}
