package Common;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import Common.AES256;
import Login.Naver;

public class testMain {
    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "D:/100-devTools/macro/chromedriver/108/chromedriver.exe";
    
    //크롤링 할 URL
    private String naver_login_url = "https://nid.naver.com/nidlogin.login?mode=form&url=https%3A%2F%2Fwww.naver.com";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
//암호화 복호화		
//		AES256 aes256 = new AES256();
//		String text = "";
//		String cipherText = aes256.encrypt(text);
//		
//		System.out.println("cipherText : " + cipherText);
		
		//RUoRS0lyf9m8sWs9hk3kWg==
		//GBRoWwdfulZHKg3AiNCr9w==

		
//File Read test
//		String filePath = "D:/100-devTools/yangjae.sql";
//		FileFunc f = new FileFunc();
//		List rList = f.fileRead(filePath);
//		
//		for(Object object : rList) {
//			System.out.println(object);
//		}
		
		
//달력
//		SearchCalenderDayIndex c = new SearchCalenderDayIndex();
//		int[] dayIndex = new int[2];
//		dayIndex = c.getDayIndex(2021, 10, 11);
//		
//		for(int i=0;i<dayIndex.length;i++) {
//			System.out.println(dayIndex[i]);
//		}
		
		
//Naver login test
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
		
         Naver naver = new Naver();
         naver.naverLogin(driver);	
		
	}

}


