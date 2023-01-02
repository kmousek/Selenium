package com.kmousek.springboot.reserveCourt.controller;

import java.io.File;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.google.common.io.Files;
import com.kmousek.springboot.reserveCourt.common.AES256;
import com.kmousek.springboot.reserveCourt.common.ClipBoard;
import com.kmousek.springboot.reserveCourt.common.CustomCalendar;
import com.kmousek.springboot.reserveCourt.task.ReserveTask;


@Controller
public class SessionController {
	ArrayList<String> cookieArrLst = new ArrayList<>();
	ArrayList<Cookie> cookieLst = new ArrayList<>();

	
	
    //소멸자
//    @Override
//    protected void finalize() throws Throwable {
//        System.out.println("소멸자 호출됨");
//        driver.close();
//    }
	
	ReserveTask task01 = new ReserveTask();
	ArrayList<Thread> arrLstOfThread = new ArrayList<>();
	
	@GetMapping("/ReserveYangjae/{id}/{pw}/{startDate}/{endDate}")
	public void ReserveYangjae(@PathVariable String id
							 , @PathVariable String pw
							 , @PathVariable String startDate
							 , @PathVariable String endDate) {
		arrLstOfThread = task01.yangjaeTask(id, pw, startDate, endDate);
		System.out.println("arrLstOfThread.size() : " + arrLstOfThread.size());
	}
	
	@GetMapping("/ReserveYangjae/stop")
	public void ReserveYangjae() {
		
		task01.stopThread(arrLstOfThread);
//		arrLstOfThread.clear();
	}
	
	
	@GetMapping("/Captcha")
	public void captchaTest() {
		String url = "https://sports.isdc.co.kr/rent/reservation/index/2023/01/04/1/SIMC01/07/26";
		WebDriver driver = getNewDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		driver = loginTanChun(driver);
		driver.get(url);

		//  scroll down display captchaText
		//  //*[@id="reservation_form"]/ul[2]/li[3]/table/tbody/tr/td[1]/div/div[1]/div[1]
		//  //*[@id="reservation_form"]/ul[2]/li[3]/table/tbody/tr/td[1]/div/div[1]/div[1]/text()[1]
		
    	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"reservation_form\"]/ul[2]/li[3]/table/tbody/tr/td[1]/div/div[1]/div[1]")));
    	WebElement captchaTextElement = driver.findElement(By.xpath("//*[@id=\"reservation_form\"]/ul[2]/li[3]/table/tbody/tr/td[1]/div/div[1]/div[1]"));
    	js.executeScript("arguments[0].scrollIntoView();", captchaTextElement);
    	
    	System.out.println("captchaTextElement");
    	System.out.println(captchaTextElement.getText());
    	

    	
    	File screenShout = captchaTextElement.getScreenshotAs(OutputType.FILE);

    	File DestFile=new File("D:/screenshot.png");
    	try {
			Files.copy(screenShout, DestFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	driver.close();	
	}
	
	
	
	@GetMapping("/Jsoup/{startDate}/{endDate}")
	public void jsoupTest(@PathVariable String startDate, @PathVariable String endDate) {
		ArrayList<String> dateArrLst = CustomCalendar.getDateLstStr(startDate, endDate);

		
		String url1 = "https://api.booking.naver.com/v3.0/businesses/210031/biz-items/";
		String url2 = "4444651";  //court seq
		String url3 = "/hourly-schedules?noCache=";
		String url4 = "1672399778131";  //noCache
		String url5 = "&endDateTime=";
		//String url6 = "2023-01-03";  //select date
		String url7 = "T00:00:00&startDateTime=";
		//String url8 = "2023-01-03";  //select date
		String url9 = "T00:00:00";
		
		for(String dateStr : dateArrLst) {
			String url = url1+url2+url3+url4+url5+dateStr+url7+dateStr+url9;
			Document doc = null;

			try {
				doc = Jsoup.connect(url).ignoreContentType(true).get();
//				System.out.println("connect");
			}catch(IOException e) {
				 e.printStackTrace();
			}
			
			JSONArray jArray = new JSONArray(doc.text());
			for(int i=0;i<jArray.length();i++) {
				JSONObject obj = jArray.getJSONObject(i);
				 
				if(obj.getBoolean("isUnitBusinessDay") == true && obj.getInt("unitBookingCount") == 0) {
					System.out.println("unitStartDateTime : " + obj.getString("unitStartDateTime"));
//					System.out.println("isUnitBusinessDay : " + obj.getBoolean("isUnitBusinessDay"));
//					 System.out.println("unitBookingCount : " + obj.getInt("unitBookingCount"));	 
				}
			}
		}
	}
	
	@GetMapping("/Session")
	public void sessionTest() {
		WebDriver driver = getNewDriver();
		
		
		driver = naverLogin(driver);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Set<Cookie> cookies = driver.manage().getCookies();
		
		WebDriver newDriver = getNewDriver();
		newDriver.get("https://nid.naver.com/nidlogin.login?mode=form&url=https%3A%2F%2Fwww.naver.com");
		
//		System.out.println(newDriver.manage().getCookies());
//		
//		for(Cookie c : cookies) {
//			newDriver.manage().addCookie(c);
//		}
//		newDriver.get("https://nid.naver.com/nidlogin.login?mode=form&url=https%3A%2F%2Fwww.naver.com");
		
		
		try {
			HttpURLConnection c= (HttpURLConnection)new URL("https://www.naver.com").openConnection();
			c.setRequestMethod("HEAD");
			c.connect();
			int r = c.getResponseCode();
		    System.out.println("Http response code: " + r);
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.close();
	}
	
    public WebDriver naverLogin(WebDriver driver) {
    	WebElement element;
    	AES256 aes256 = new AES256();
    	ClipBoard cp = new ClipBoard();
        //네이버 로그인 정보
    	String naverId = "RUoRS0lyf9m8sWs9hk3kWg==";
        String naverPw = "GBRoWwdfulZHKg3AiNCr9w==";
        
        try {
        	
        	driver.manage().deleteAllCookies();
        	
            //get page (= 브라우저에서 url을 주소창에 넣은 후 request 한 것과 같다)
        	//
            driver.get("https://nid.naver.com/nidlogin.login?mode=form&url=https%3A%2F%2Fwww.naver.com");
            
            //iframe으로 구성된 곳은 해당 프레임으로 전환시킨다.
//            driver.switchTo().frame(driver.findElement(By.id("loginForm")));
            
            //iframe 내부에서 id 필드 탐색
            System.out.println("aes256.decrypt(naverId) :" + aes256.decrypt(naverId));
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
        
        
//        System.out.println("driver.manage().getCookies() : " + driver.manage().getCookies().size());
		
        
		
//		for(Cookie c : driver.manage().getCookies()) {
//			cookieLst.add(c);
//			System.out.println("cookie : [" + c + "]");
//		}
		
//		for(String cookieInfo : cookieArrLst) {
//			System.out.println("cookieInfo : " + cookieInfo);
//		}
		
        
        return driver;
    }
	
	
    public WebDriver loginTanChun(WebDriver driver) {
    	WebElement element;
    	AES256 aes256 = new AES256();
    	ClipBoard cp = new ClipBoard();
        //네이버 로그인 정보
    	String naverId = "RUoRS0lyf9m8sWs9hk3kWg==";
        String naverPw = "GBRoWwdfulZHKg3AiNCr9w==";
        
        try {
        	
            //get page (= 브라우저에서 url을 주소창에 넣은 후 request 한 것과 같다)
        	//
            driver.get("https://sports.isdc.co.kr/member/login");
            
            //iframe으로 구성된 곳은 해당 프레임으로 전환시킨다.
//            driver.switchTo().frame(driver.findElement(By.id("loginForm")));
            
            //iframe 내부에서 id 필드 탐색
            System.out.println("aes256.decrypt(naverId) :" + aes256.decrypt(naverId));
            cp.copyToClip("kmousek");
            ////*[@id="input_memid"]
            element = driver.findElement(By.id("input_memid"));
            element.sendKeys(Keys.chord(Keys.CONTROL, "v"));
            
            Thread.sleep(2000);
            
            //iframe 내부에서 pw 필드 탐색
            cp.copyToClip("zpzpwnl30#!");
            ////*[@id="input_mempw"]
            element = driver.findElement(By.id("input_mempw"));
            element.sendKeys(Keys.chord(Keys.CONTROL, "v"));
            
            
            Thread.sleep(2000);
            
            //로그인 버튼 클릭
            ////*[@id="login_form"]/ul/li[2]/table/tbody/tr[1]/td[3]/input
            element = driver.findElement(By.xpath("//*[@id=\"login_form\"]/ul/li[2]/table/tbody/tr[1]/td[3]/input"));
            element.click();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return driver;
    }
    
    
    public WebDriver getNewDriver() {
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

