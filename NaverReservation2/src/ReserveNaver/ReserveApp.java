package ReserveNaver;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import Common.FileFunc;
import Common.SearchCalenderDayIndex;
import DAO.ReserveInfoDao;
import DAO.YangjaeDao;

public class ReserveApp {
    
    //WebDriver
    private static WebDriver driver;
    
    private WebElement element;
    
//    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    
    
    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "D:/100-devTools/macro/chromedriver/108/chromedriver.exe";
    
    //ũ�Ѹ� �� URL
    private String naver_login_url = "https://nid.naver.com/nidlogin.login?mode=form&url=https%3A%2F%2Fwww.naver.com";
    
    //���̹� �α��� ����
    private String naverId = "RUoRS0lyf9m8sWs9hk3kWg==";
    private String naverPw = "GBRoWwdfulZHKg3AiNCr9w==";

    //��ȣȭ
    private AES256 aes256 = new AES256();
    
    //clipBoard
    private ClipBoard cp = new ClipBoard();
    
    //�޷� ��ǥ ã��
    private SearchCalenderDayIndex scdi = new SearchCalenderDayIndex();
    
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
    
    public static void main(String[] args) throws ParseException {
    	ArrayList<String> enableTimeList = new ArrayList<>();
    	YangjaeDao yangjaeDao = new YangjaeDao();
    	ReserveApp ra = new ReserveApp();
    	ra.naverLogin();
    	
    	ArrayList<int[]> reserveDays = new ArrayList<>();
    	
//    	int[] reserveDay1 = {2022,12,17};
//    	int[] reserveDay2 = {2022,12,18};
    	int[] reserveDay3 = {2022,12,24};
    	int[] reserveDay4 = {2022,12,25};
//    	reserveDays.add(reserveDay1);
//    	reserveDays.add(reserveDay2);
    	reserveDays.add(reserveDay3);
    	reserveDays.add(reserveDay4);
    	
    	String[] reserveCourtList = {"A","B","C"};  //�ǳ�
    	
    	while(true) {
        	for(int[] reserveDate : reserveDays) {
        		//create url
        		for(String courtList : reserveCourtList) {
        			String url = yangjaeDao.getCourtUrl(Integer.toString(reserveDate[1])+"_"+courtList);
        			
        			try {
        				reserveCourtOfDay(url, reserveDate);
        			}catch(Exception e) {
        				System.out.println("���ܹ߻�");
        			}
        			
        		}
        	}
        	
        	try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	

    	// input : �����
//    	String reserveMonth = "12";
    	
    	
//    	int[] idx = SearchCalenderDayIndex.getDayIndex(2022,12,15);
    	
//    	for(int i : idx) {
//    		System.out.println("index : " + i);
//    	}
    	
//    	for(String courtNm : yangjaeDao.getCourtNm()) {
//    		yangjaeDao = ra.checkEnableTime(yangjaeDao, reserveMonth+"_"+courtNm);
//    	}
//
//    	reserveCourt(yangjaeDao.getReserveInfo().get(yangjaeDao.getReserveInfo().size()-1));
    	
    	
    	
    	
    	//test code
//    	ReserveInfoDao r = new ReserveInfoDao();
//    	r.setUrl("https://booking.naver.com/booking/10/bizes/210031/items/4394841");  //12�� 8����Ʈ 
//    	r.setxPathCalDay2_calCol("4");
//    	r.setxPathCalDay4_calRow("2"); //27��
//    	r.setxPathTime2_amPmFlag("2"); //AM
//    	r.setxPathTime4_timeFlag("700"
//    			+ ""
//    			+ ""
//    			); //8:00
//    	
//    	reserveCourt(r);
    	
    	
    	
//    	dayOfWeek(stringToDate("20221214"));
    	
//    	driver.close();
    }
    
    


    
    public static void reserveCourtOfDay(String url, int[] reserveDate) {
    	WebDriverWait wait = new WebDriverWait(driver, 20);

    	ReserveInfoDao reserveInfoDao = new ReserveInfoDao();
    	
    	int[] calIdx = SearchCalenderDayIndex.getDayIndex(reserveDate[0], reserveDate[1], reserveDate[2]);
    	
    	reserveInfoDao.setxPathCalDay2_calCol(Integer.toString(calIdx[0]));
    	reserveInfoDao.setxPathCalDay4_calRow(Integer.toString(calIdx[1]));
    	
    	driver.get(url);
    	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"container\"]/bk-freetime/div[1]/bk-breadcrumb/div/ul/li[2]/a/span")));
    	
    	while(true) {
    		WebElement calDayElement = driver.findElement(By.xpath(reserveInfoDao.getXpathCalDay()));
        	calDayElement.click();
        	
        	String clickDay = calDayElement.getText().split("\\n")[0];
        	String displayTimeTabDay = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[1]/em/span[1]"))
    				.getText().replaceAll(" ", "").split("\\.")[1];
        	
        	if(clickDay.equals(displayTimeTabDay) == true) {
        		break;
        	}
    	}
    	
		for(int g=1;g<=2;g++) {
			for(int t=1;t<=12;t++) {
				WebElement timeElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div["
																+g+"]/ul/li["+t+"]/a"));
				if(timeElement.getAttribute("class").contains("none") == false) {
					reserveInfoDao.setxPathTime2_amPmFlag(Integer.toString(g));
					reserveInfoDao.setxPathTime4_timeFlag(Integer.toString(t));
					
			    	//���� �ð� Ŭ��
			    	try {
			    		timeElement.click();
			    	}catch(Exception e) {
			    		e.getStackTrace();
			    	}
			    	
			    	//����ð� ���� ��ư //*[@id="container"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[3]/button
			    	WebElement reserveButtonElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[3]/button"));
			    	while(true) {
			    		if(reserveButtonElement.getAttribute("class").contains("on") == true) {
			    			reserveButtonElement.click();
			    			break;
			    		}
			    	}
			    	
			    	// �����ܰ� ��ư  //*[@id="container"]/bk-freetime/div[2]/div[2]/bk-submit/div/button
			    	WebElement nextButtonElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/div[2]/bk-submit/div/button"));
			    	nextButtonElement.click();
			    	
			    	//��ũ��
			    	JavascriptExecutor js = (JavascriptExecutor) driver;
			    	js.executeScript("window.scrollTo(0,document.body.scrollHeight)");

			    	//�����ϱ� ��ư //*[@id="container"]/bk-freetime/div[2]/div[2]/bk-submit/div/button
			    	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/div[2]/bk-submit/div/button")));
			    	WebElement payButtonElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/div[2]/bk-submit/div/button"));
			    	payButtonElement.click();
			    	
			    	//�Ϲݰ��� ����  //*[@id="PAYMENT_WRAP"]/div[1]/div[1]/ul/li[4]/div[1]/label
			    	WebElement normalRadioElement = driver.findElement(By.xpath("//*[@id=\"PAYMENT_WRAP\"]/div[1]/div[1]/ul/li[4]/div[1]/label"));
			    	js.executeScript("arguments[0].scrollIntoView();", normalRadioElement);
			    	normalRadioElement.click();
			    	
			    	// ���߿� ���� radio //*[@id="PAYMENT_WRAP"]/div[1]/div[1]/ul/li[4]/ul/li[2]/label
			    	WebElement postPayRadioElement = driver.findElement(By.xpath("//*[@id=\"PAYMENT_WRAP\"]/div[1]/div[1]/ul/li[4]/ul/li[2]/label"));
			    	postPayRadioElement.click();
			    	
			    	//�Ա����� selectbox  //*[@id="bankCodeList"]/div/div/select
			    	Select combobox = new Select(driver.findElement(By.xpath("//*[@id=\"bankCodeList\"]/div/div/select")));
//			    	List<WebElement> optionList = combobox.getAllSelectedOptions();
			    	combobox.selectByValue("004");
			    	
			    	//ȯ������� ����  //*[@id="PAYMENT_WRAP"]/div[1]/div[1]/div[1]/div[2]/div[2]/div/ul/li[2]/label
			    	WebElement refundChargeElement = driver.findElement(By.xpath("//*[@id=\"PAYMENT_WRAP\"]/div[1]/div[1]/div[1]/div[2]/div[2]/div/ul/li[2]/label"));
			    	refundChargeElement.click();
			    	
			    	
			    	//�����ϱ� ��ư  //*[@id="Checkout_order__2yAvT"]/div[6]/button
//			    	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"Checkout_order__2yAvT\"]/div[6]/button")));
			    	WebElement payButton1Element = driver.findElement(By.xpath("//*[@id=\"Checkout_order__2yAvT\"]/div[6]/button"));
			    	js.executeScript("arguments[0].scrollIntoView();", payButton1Element);
			    	payButton1Element.click();
				}
			}	//end for t
		}   //enf for g 	
    }
    
    
    public static void reserveCourt(ReserveInfoDao reserveInfoDao ) {
    	WebDriverWait wait = new WebDriverWait(driver, 20);
    	System.out.println("reserveInfoDao : " + reserveInfoDao.toString());
    	driver.get(reserveInfoDao.getUrl());
    	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"container\"]/bk-freetime/div[1]/bk-breadcrumb/div/ul/li[2]/a/span")));
//    	reserveInfoDao.getXpathCalDay();
//    	reserveInfoDao.getXpathTime();
    	
    	while(true) {
    		WebElement calDayElement = driver.findElement(By.xpath(reserveInfoDao.getXpathCalDay()));
        	calDayElement.click();
        	
        	String clickDay = calDayElement.getText().split("\\n")[0];
        	String displayTimeTabDay = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[1]/em/span[1]"))
    				.getText().replaceAll(" ", "").split("\\.")[1];
        	
        	if(clickDay.equals(displayTimeTabDay) == true) {
        		break;
        	}
    	}
    	
    	//���� �ð� Ŭ��
    	WebElement timeElement = driver.findElement(By.xpath(reserveInfoDao.getXpathTime()));
    	try {
    		timeElement.click();
    	}catch(Exception e) {
    		e.getStackTrace();
    	}
    	
    	
    	//����ð� ���� ��ư //*[@id="container"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[3]/button
    	WebElement reserveButtonElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[3]/button"));
    	while(true) {
    		if(reserveButtonElement.getAttribute("class").contains("on") == true) {
    			reserveButtonElement.click();
    			break;
    		}
    	}
    	
    	
    	// �����ܰ� ��ư  //*[@id="container"]/bk-freetime/div[2]/div[2]/bk-submit/div/button
    	WebElement nextButtonElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/div[2]/bk-submit/div/button"));
    	nextButtonElement.click();
    	
    	//��ũ��
    	JavascriptExecutor js = (JavascriptExecutor) driver;
    	js.executeScript("window.scrollTo(0,document.body.scrollHeight)");

    	
    	
    	//�����ϱ� ��ư //*[@id="container"]/bk-freetime/div[2]/div[2]/bk-submit/div/button
    	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/div[2]/bk-submit/div/button")));
    	WebElement payButtonElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/div[2]/bk-submit/div/button"));
    	payButtonElement.click();

    	
    	//�Ϲݰ��� radio  //*[@id="PAYMENT_WRAP"]/div[1]/div[1]/ul/li[4]/div/span/span
    	
    	//  //*[@id="PAYMENT_WRAP"]/div[1]/div[1]/dl/dt/a

    	
    	
    	
//    	WebElement normalRadioElement = driver.findElement(By.xpath("//*[@id=\"PAYMENT_WRAP\"]/div[1]/div[1]/ul/li[4]/div/span/span"));
//    	normalRadioElement.click();
    	
    	//�Ϲݰ��� ����  //*[@id="PAYMENT_WRAP"]/div[1]/div[1]/ul/li[4]/div[1]/label
    	WebElement normalRadioElement = driver.findElement(By.xpath("//*[@id=\"PAYMENT_WRAP\"]/div[1]/div[1]/ul/li[4]/div[1]/label"));
    	js.executeScript("arguments[0].scrollIntoView();", normalRadioElement);
    	normalRadioElement.click();
    	
    	// ���߿� ���� radio //*[@id="PAYMENT_WRAP"]/div[1]/div[1]/ul/li[4]/ul/li[2]/label
    	WebElement postPayRadioElement = driver.findElement(By.xpath("//*[@id=\"PAYMENT_WRAP\"]/div[1]/div[1]/ul/li[4]/ul/li[2]/label"));
    	postPayRadioElement.click();
    	
    	//�Ա����� selectbox  //*[@id="bankCodeList"]/div/div/select
    	Select combobox = new Select(driver.findElement(By.xpath("//*[@id=\"bankCodeList\"]/div/div/select")));
//    	List<WebElement> optionList = combobox.getAllSelectedOptions();
    	combobox.selectByValue("004");
    	
    	//���¼��� Ŭ�� //*[@id="refund-account-section"]
//    	WebElement accountElement = driver.findElement(By.xpath("//*[@id=\"refund-account-section\"]"));
//    	accountElement.click();
    	
    	//ȯ������� ����  //*[@id="PAYMENT_WRAP"]/div[1]/div[1]/div[1]/div[2]/div[2]/div/ul/li[2]/label
    	WebElement refundChargeElement = driver.findElement(By.xpath("//*[@id=\"PAYMENT_WRAP\"]/div[1]/div[1]/div[1]/div[2]/div[2]/div/ul/li[2]/label"));
    	refundChargeElement.click();
    	
    	
    	//�����ϱ� ��ư  //*[@id="Checkout_order__2yAvT"]/div[6]/button
//    	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"Checkout_order__2yAvT\"]/div[6]/button")));
    	WebElement payButton1Element = driver.findElement(By.xpath("//*[@id=\"Checkout_order__2yAvT\"]/div[6]/button"));
    	js.executeScript("arguments[0].scrollIntoView();", payButton1Element);
    	payButton1Element.click();
    	

    	
    	
    }
    
    
    
    public YangjaeDao checkEnableTime(YangjaeDao yangjaeDao, String courtNm) {
    	driver.get(yangjaeDao.getCourtUrl(courtNm));
    	WebDriverWait wait = new WebDriverWait(driver, 20);
//    	ArrayList<String> enableTimeList = new ArrayList<>();
    	
    	try{
        	// ����ù��ǽ�(���� �״Ͻ���) ȭ�� �ε��ɶ����� wait  //*[@id="container"]/bk-freetime/div[1]/bk-breadcrumb/div/ul/li[2]/a/span
        	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"container\"]/bk-freetime/div[1]/bk-breadcrumb/div/ul/li[2]/a/span")));	
    	}catch(Exception e) {
    		System.out.println("���� url : " + courtNm + " : " + yangjaeDao.getCourtUrl(courtNm));
    		return yangjaeDao;
    	}

    	
    	/*�޷� parsing //*[@id="calendar"]/table
    	 * �޷� ���� 7�� (��,��,ȭ,��,��,��,��)
    	 * �޷� �� ���ϱ�
    	*/
    	
    	//  //*[@id="calendar"]/table/tbody[1]/tr*
    	
    	//�޷��� ��
    	List<WebElement> calColElements = driver.findElements(By.xpath("//*[@id=\"calendar\"]/table/tbody[1]/*"));
//    	System.out.println("elements count : " + calColElements.size());
    	//�޷� �� ���� ��  //*[@id="calendar"]/table/tbody[1]/tr[1]
    	
    	for(int i=1;i<=calColElements.size();i++) {
    		
    		for(int j=1;j<=7;j++) { //��(��~��)
//    			System.out.println("i:j="+i+":"+j);
    			element = driver.findElement(By.xpath("//*[@id=\"calendar\"]/table/tbody[1]/tr["+i+"]/td["+j+"]"));
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
    			
    			
    			// Ȱ��ȭ �� ��¥�� Ŭ��
    			if(element.getAttribute("class").contains("calendar-unselectable") == false) {
    				
    				while(true) {
        				element.click();
        				
        				//  //*[@id="container"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[1]/em/span[1]  
        				// text : 12. 19.(��), �ð��� �����ϼ��� �ּ� 1�ð� - �ִ� 4�ð� �̿밡��
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
    				
    				
    				// ���� TIME ���̺�
    				// ex----------------------------------------------------------------------------------------
       				//                                                                          am         1~12 : 12��~11��
    				//*[@id="container"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div[1]/ul/li[1]/a
       				//                                                                          am         1~12 : 12��~11��
    				//*[@id="container"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div[2]/ul/li[1]/a
    				// ���� ���ϴ� class : class="anchor none"
    				
    				//2�� for�� ����, ����, �ð�
    				
    				for(int g=1;g<=2;g++) {
    					for(int t=1;t<=12;t++) {
    						WebElement timeElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div["
    																		+g+"]/ul/li["+t+"]/a"));
    						if(timeElement.getAttribute("class").contains("none") == false) {
    							ReserveInfoDao reserveInfoDao = new ReserveInfoDao();
    							reserveInfoDao.setUrl(yangjaeDao.getCourtUrl(courtNm));
    							reserveInfoDao.setCourtName(courtNm);
    							if(satFlag == true) {
    								reserveInfoDao.setSatFlag(true);
    							}
    							if(sunFlag == true) {
    								reserveInfoDao.setSunFlag(true);
    							}
    							reserveInfoDao.setxPathCalDay2_calCol(Integer.toString(i));
    							reserveInfoDao.setxPathCalDay4_calRow(Integer.toString(j));
    							reserveInfoDao.setxPathTime2_amPmFlag(Integer.toString(g));
    							reserveInfoDao.setxPathTime4_timeFlag(Integer.toString(t));
    							
    							
    							yangjaeDao.addReserveInfo(reserveInfoDao);
    							
    							
    							/*
    							if(satFlag == true || sunFlag == true) {
    								if(satFlag == true) {
            							if(g == 1) {	//����
            								enableTimeList.add(courtNm.split("\\_")[0]+"�� "
            												  +clickDay+"��(��):"
            												  +courtNm.split("\\_")[1]+"��Ʈ:����:"
            												  +timeElement.getText());
            								
            							}else {		//����
            								enableTimeList.add(courtNm.split("\\_")[0]+"�� "
  												  +clickDay+"��(��):"
  												  +courtNm.split("\\_")[1]+"��Ʈ:����:"
  												  +timeElement.getText());
            							}
    								}else {
    									if(g == 1) {	//����
    										enableTimeList.add(courtNm.split("\\_")[0]+"�� "
    												  +clickDay+"��(��):"
    												  +courtNm.split("\\_")[1]+"��Ʈ:����:"
    												  +timeElement.getText());
            								
            							}else {		//����
            								enableTimeList.add(courtNm.split("\\_")[0]+"�� "
  												  +clickDay+"��(��):"
  												  +courtNm.split("\\_")[1]+"��Ʈ:����:"
  												  +timeElement.getText());
            							}
    								}
    							}else {
        							if(g == 1) {	//����
//        								System.out.println(courtNm+":"+clickDay+":����:"+timeElement.getText());
        								enableTimeList.add(courtNm.split("\\_")[0]+"�� "
												  +clickDay+"��:"
												  +courtNm.split("\\_")[1]+"��Ʈ:����:"
												  +timeElement.getText());
        							}else {		//����
//        								System.out.println(courtNm+":"+clickDay+":����:"+timeElement.getText());
        								enableTimeList.add(courtNm.split("\\_")[0]+"�� "
												  +clickDay+"��:"
												  +courtNm.split("\\_")[1]+"��Ʈ:����:"
												  +timeElement.getText());
        							}    								
    							}
    							*/

    						}
    					}	//end for t
    				}   //enf for g 				
    			} //end if
    		}
    	}
    	 
    	return yangjaeDao;
    }
    
    public void naverLogin() {
        try {
            driver.get(naver_login_url);
            
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
    
    public static int dayOfWeek(Date date) {
//    	Date curDate = new Date();
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	int dayOfWeekNum = cal.get(Calendar.DAY_OF_WEEK);
    	
    	System.out.println("dayOfWeekNum : " + dayOfWeekNum);
    	return dayOfWeekNum;  //1~7 7:sat, 1:sun
    }
    
    public static Date stringToDate(String dateStr) throws ParseException {
    	// yyyyMMdd
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	Date date = formatter.parse(dateStr);
    	System.out.println("date : " + date);
    	
    	return date;
    }
}
