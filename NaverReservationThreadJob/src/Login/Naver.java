package Login;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
	
    //���̹� �α��� ����
    private String naverId = "RUoRS0lyf9m8sWs9hk3kWg==";
    private String naverPw = "GBRoWwdfulZHKg3AiNCr9w==";
	
    public void naverLogin(WebDriver driver, WebElement element) {
        try {
            //get page (= ���������� url�� �ּ�â�� ���� �� request �� �Ͱ� ����)
            driver.get(naver_login_url);
 
            //iframe���� ������ ���� �ش� ���������� ��ȯ��Ų��.
//            driver.switchTo().frame(driver.findElement(By.id("loginForm")));
            
            //iframe ���ο��� id �ʵ� Ž��
            
            cp.copyToClip(aes256.decrypt(naverId));
            element = driver.findElement(By.id("id"));
            element.sendKeys(Keys.chord(Keys.CONTROL, "v"));
            
            //iframe ���ο��� pw �ʵ� Ž��
            cp.copyToClip(aes256.decrypt(naverPw));
            element = driver.findElement(By.id("pw"));
            element.sendKeys(Keys.chord(Keys.CONTROL, "v"));
            
            //�α��� ��ư Ŭ��
            element = driver.findElement(By.id("log.login"));
            element.click();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
