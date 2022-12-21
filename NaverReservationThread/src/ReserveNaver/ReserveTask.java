package ReserveNaver;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Common.AES256;
import Common.ClipBoard;
import Common.SearchCalenderDayIndex;
import DAO.ReserveInfoDao;
import DAO.YangjaeDao;

public class ReserveTask implements Runnable {

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
    
    String url;
    List<int[]> reserveDateList;
    int loginDelay;
    
    public ReserveTask() {
    	
    }
    
    public ReserveTask(String url, List<int[]> reserveDateList, int loginDelay) {
    	this.url = url;
    	this.reserveDateList = reserveDateList;
    	this.loginDelay = loginDelay;  
    }
	
	@Override
	public void run() {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        //WebDriver
        WebDriver driver;
        
        //Driver SetUp
         ChromeOptions options = new ChromeOptions();
//         options.addArguments("disable-gpu");
//         options.addArguments("headless");
         options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.5359.100 Safari/537.36\r\n");
         options.setCapability("ignoreProtectedModeSettings", true);
         driver = new ChromeDriver(options);
         driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
         try {
			Thread.sleep(loginDelay);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
         
         naverLogin(driver);
     	 
     	 while(true) {
     		 for(int[] reserveDate : reserveDateList) {
	    		//create url
 				 try {
 					 reserveCourtOfDay(driver, url, reserveDate);
 				 }catch(Exception e) {
 					 System.out.println(Thread.currentThread() + " : 예외발생 : " + e.getMessage());
 				 }
     		 }
     		 try {
     			 System.out.println("----sleep----" + Thread.currentThread());
     			 Thread.sleep(15000);
     		 } catch (InterruptedException e) {
     			 e.printStackTrace();
     		 }
     	 }
	}
	
	
	
    public static void main(String[] args) {
    	
    	ArrayList<int[]> reserveDays = new ArrayList<>();
    	YangjaeDao yangjaeDao = new YangjaeDao();
//    	int[] reserveDay1 = {2022,12,17};
//    	int[] reserveDay2 = {2022,12,18};
    	int[] reserveDay3 = {2023,1,1};
    	int[] reserveDay4 = {2023,1,7};
    	
//    	reserveDays.add(reserveDay1);
//    	reserveDays.add(reserveDay2);
    	reserveDays.add(reserveDay3);
    	reserveDays.add(reserveDay4);
    	
//    	String[] reserveCourtList = {"12_A","12_B","12_C"};  //실내
    	String[] reserveCourtList = {"1_A","1_B","1_C"};  //실내
//    	String[] reserveCourtList = {"12_1","12_3"};  //실내
    	
    	List<Thread> threadList = new ArrayList<>();
    	
    	int DelayTime = 1000; //1초
    	for(String reserveCourt : reserveCourtList) {
    		Runnable task = new ReserveTask(yangjaeDao.getCourtUrl(reserveCourt), reserveDays, DelayTime);
    		Thread subThread = new Thread(task);
    		threadList.add(subThread);
    		DelayTime = DelayTime*5;
    	}
    	
    	for(Thread t : threadList) {
    		t.start();
    	}
    }
	
    
    public void naverLogin(WebDriver driver) {
        try {
            
        	driver.get(naver_login_url);
            
            cp.copyToClip(aes256.decrypt(naverId));
            WebElement idElement = driver.findElement(By.id("id"));
            idElement.sendKeys(Keys.chord(Keys.CONTROL, "v"));
            
            //iframe 내부에서 pw 필드 탐색
            cp.copyToClip(aes256.decrypt(naverPw));
            WebElement pwElement = driver.findElement(By.id("pw"));
            pwElement.sendKeys(Keys.chord(Keys.CONTROL, "v"));
            
            //로그인 버튼 클릭
            WebElement loginButtonElement = driver.findElement(By.id("log.login"));
            loginButtonElement.click();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static void reserveCourtOfDay(WebDriver driver, String url, int[] reserveDate) {
    	WebDriverWait wait = new WebDriverWait(driver, 20);

    	ReserveInfoDao reserveInfoDao = new ReserveInfoDao();
    	
    	int[] calIdx = SearchCalenderDayIndex.getDayIndex(reserveDate[0], reserveDate[1], reserveDate[2]);
    	
//    	reserveInfoDao.setxPathCalDay2_calCol(Integer.toString(calIdx[0]));
//    	reserveInfoDao.setxPathCalDay4_calRow(Integer.toString(calIdx[1]));
    	String xpathCalDay = "//*[@id=\"calendar\"]/table/tbody[1]/tr["
    						+ calIdx[0]
    						+ "]/td["
    						+ calIdx[1]
    						+ "]";
    	
    	driver.get(url);
    	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"container\"]/bk-freetime/div[1]/bk-breadcrumb/div/ul/li[2]/a/span")));
    	
    	//년  //*[@id="calendar"]/div/strong/span[1]
    	WebElement yearElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"calendar\"]/div/strong/span[1]")));
    	//월  //*[@id="calendar"]/div/strong/span[2]
    	WebElement monthElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"calendar\"]/div/strong/span[2]")));
    	
    	System.out.println("displayYearMonth : " + yearElement.getText()+monthElement.getText());
    	
    	if((yearElement.getText()
    		+monthElement.getText()).equals(Integer.toString(reserveDate[0])
    											+Integer.toString(reserveDate[0])) == false) {
    		// next month button  //*[@id="calendar"]/div/a[2]/i
    		WebElement nextMonthElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"calendar\"]/div/a[2]/i")));
    		nextMonthElement.click();    		
    	}

    	while(true) {
    		WebElement calDayElement = driver.findElement(By.xpath(xpathCalDay));
        	calDayElement.click();
        	
        	String clickDay = calDayElement.getText().split("\\n")[0];
        	String displayTimeTabDay = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[1]/em/span[1]"))
    				.getText().replaceAll(" ", "").split("\\.")[1];
        	
        	if(clickDay.equals(displayTimeTabDay) == true) {
        		break;
        	}
    	}
    	
    	List<int[]> enableTimeList = new ArrayList<>();
		for(int g=1;g<=2;g++) {
			for(int t=1;t<=12;t++) {
				WebElement timeElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div["
																+g+"]/ul/li["+t+"]/a"));
				if(timeElement.getAttribute("class").contains("none") == false) {
					int[] enableTIme = {g, t};
					enableTimeList.add(enableTIme);
					System.out.println(Thread.currentThread() + " : Enable time : " 
										+ reserveDate[0]+"-"+reserveDate[1]+"-"+reserveDate[2]+" "
										+ g +"," + t);
				}
			}	//end for t
		}   //enf for g 
    	
    	
    	for(int[] enableTime : enableTimeList) {
        	driver.get(url);
        	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"container\"]/bk-freetime/div[1]/bk-breadcrumb/div/ul/li[2]/a/span")));
        	
        	while(true) {
        		WebElement calDayElement = driver.findElement(By.xpath(xpathCalDay));
            	calDayElement.click();
            	
            	String clickDay = calDayElement.getText().split("\\n")[0];
            	String displayTimeTabDay = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[1]/em/span[1]"))
        				.getText().replaceAll(" ", "").split("\\.")[1];
            	
            	if(clickDay.equals(displayTimeTabDay) == true) {
            		break;
            	}
        	}
        	
        	WebElement timeElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div["
														+enableTime[0]+"]/ul/li["+enableTime[1]+"]/a"));
	    	//예약 시간 클릭
        	
        	if(timeElement.getAttribute("class").contains("none") == false) {
    			timeElement.click();
			}else {
				return;
			}
	    	
	    	//예약시간 선택 버튼 //*[@id="container"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[3]/button
	    	WebElement reserveButtonElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[3]/button"));
	    	while(true) {
	    		if(reserveButtonElement.getAttribute("class").contains("on") == true) {
	    			reserveButtonElement.click();
	    			break;
	    		}
	    	}
	    	
//	    	System.out.println("bb");
	    	// 다음단계 버튼  //*[@id="container"]/bk-freetime/div[2]/div[2]/bk-submit/div/button
	    	WebElement nextButtonElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/div[2]/bk-submit/div/button"));
	    	nextButtonElement.click();
	    	
//	    	System.out.println("aa");
	    	
	    	//스크롤
	    	JavascriptExecutor js = (JavascriptExecutor) driver;
//	    	js.executeScript("window.scrollTo(0,document.body.scrollHeight)");

	    	//결제하기 버튼 //*[@id="container"]/bk-freetime/div[2]/div[2]/bk-submit/div/button
	    	System.out.println("source : " + driver.getTitle());
	    	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/div[2]/bk-submit/div/button")));
	    	WebElement payButtonElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/div[2]/bk-submit/div/button"));
	    	js.executeScript("arguments[0].scrollIntoView();", payButtonElement);
	    	payButtonElement.click();
	    	
	    	//일반결제 선택  //*[@id="PAYMENT_WRAP"]/div[1]/div[1]/ul/li[4]/div[1]/label
	    	WebElement normalRadioElement = driver.findElement(By.xpath("//*[@id=\"PAYMENT_WRAP\"]/div[1]/div[1]/ul/li[4]/div[1]/label"));
	    	js.executeScript("arguments[0].scrollIntoView();", normalRadioElement);
	    	normalRadioElement.click();
	    	
	    	// 나중에 결제 radio //*[@id="PAYMENT_WRAP"]/div[1]/div[1]/ul/li[4]/ul/li[2]/label
	    	WebElement postPayRadioElement = driver.findElement(By.xpath("//*[@id=\"PAYMENT_WRAP\"]/div[1]/div[1]/ul/li[4]/ul/li[2]/label"));
	    	postPayRadioElement.click();
	    	
	    	//환불정산액 적립  //*[@id="PAYMENT_WRAP"]/div[1]/div[1]/div[1]/div[2]/div[2]/div/ul/li[2]/label
	    	WebElement refundChargeElement = driver.findElement(By.xpath("//*[@id=\"PAYMENT_WRAP\"]/div[1]/div[1]/div[1]/div[2]/div[2]/div/ul/li[2]/label"));
	    	refundChargeElement.click();
	    	
	    	//입금은행 selectbox  //*[@id="bankCodeList"]/div/div/select
	    	Select combobox = new Select(driver.findElement(By.xpath("//*[@id=\"bankCodeList\"]/div/div/select")));
//	    	List<WebElement> optionList = combobox.getAllSelectedOptions();
	    	combobox.selectByValue("004");
	    	
	    	//결제하기 버튼  //*[@id="Checkout_order__2yAvT"]/div[6]/button
//	    	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"Checkout_order__2yAvT\"]/div[6]/button")));
	    	WebElement payButton1Element = driver.findElement(By.xpath("//*[@id=\"Checkout_order__2yAvT\"]/div[6]/button"));
	    	js.executeScript("arguments[0].scrollIntoView();", payButton1Element);
	    	payButton1Element.click();
	    	
	    	System.out.println(Thread.currentThread() + " : 예약완료:[" 
	    						+ reserveDate[0]+"-"
	    						+ reserveDate[1]+"-"
	    						+ reserveDate[2]+" "
	    						+ enableTime[0]+":"
	    						+ enableTime[1]+"]"
	    						+ url);
	    	
    	} // end for 	
    }

}

