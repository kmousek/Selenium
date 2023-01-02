package ReserveNaver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import DAO.YangjaeDao;

public class ReserveTask implements Runnable {

    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "D:/100-devTools/macro/chromedriver/108/chromedriver.exe";
    
    //크롤링 할 URL
    private String naver_login_url = "https://nid.naver.com/nidlogin.login?mode=form&url=https%3A%2F%2Fwww.naver.com";
    
    //네이버 로그인 정보
//    private String naverId = "RUoRS0lyf9m8sWs9hk3kWg==";
//    private String naverPw = "GBRoWwdfulZHKg3AiNCr9w==";
    private static String naverId;
    private static String naverPw;

    //복호화
    private AES256 aes256 = new AES256();
    
    //clipBoard
    private ClipBoard cp = new ClipBoard();
    
    String courtName;
    ArrayList<int[]> reserveDateList;
    int loginDelay;
    public static YangjaeDao yangjaeDao = YangjaeDao.getInstance();
   
    public ReserveTask() {
    	
    }
    
    public ReserveTask(String courtName, ArrayList<int[]> reserveDateList, int loginDelay) {
    	this.courtName = courtName;
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
     	 
         Boolean exceptFlag = false;
     	 while(true) {
     		 String prevUrl="";
     		 for(int[] reserveDate : reserveDateList) {
     			
     			 // org
     			 try {
     				String url = yangjaeDao.getCourtUrl(Integer.toString(reserveDate[1])+"_"+courtName);
     	    		if(Arrays.equals(reserveDateList.get(0), reserveDate) == true) {
     	    			prevUrl = url;
     	    			//System.out.println("preUrl : " + prevUrl);
     	    			while(true) {
     	    				driver.get(url);
     	    				if(driver.getCurrentUrl().equals(url) == false) {
     	    					try {
     								Thread.sleep(5000);
     							} catch (InterruptedException e) {
     								// TODO Auto-generated catch block
     								e.printStackTrace();
     							}
     	    				}else {
     	    					break;
     	    				}
     	    			}
     	    		}
     	    		
     	    		if(exceptFlag == true) {
     	    			driver.get(url);
     	    			exceptFlag = false;
     	    		}
     	    		
     	    		reserveCourtOfDays(driver, url, reserveDate);
     			 }catch(Exception e) {
//     				System.out.println(Thread.currentThread() + ": 예외발생 : " + e.getMessage());
     				exceptFlag = true;
     			 }
     			 
     			//test
     			/*
     			String url = yangjaeDao.getCourtUrl(Integer.toString(reserveDate[1])+"_"+courtName);
 	    		if(Arrays.equals(reserveDateList.get(0), reserveDate) == true) {
 	    			prevUrl = url;
					//System.out.println("preUrl : " + prevUrl);
 	    			while(true) {
 	    				driver.get(url);
 	    				if(driver.getCurrentUrl().equals(url) == false) {
 	    					try {
 								Thread.sleep(5000);
 							} catch (InterruptedException e) {
 								// TODO Auto-generated catch block
 								e.printStackTrace();
 							}
 	    				}else {
 	    					break;
 	    				}
 	    			}
 	    		}
 	    		reserveCourtOfDays(driver, url, reserveDate);
 	    		*/
     		 }
     		 
     		 try {
//     			 System.out.println("----sleep----" + Thread.currentThread());
     			 Thread.sleep(5000);
     		 } catch (InterruptedException e) {
     			 e.printStackTrace();
     		 }
     	 }
	}
	
	
	
    public static void main(String[] args) {
    	naverId = args[0];
    	naverPw = args[1];
    	
    	ArrayList<int[]> reserveDays = new ArrayList<>();
    	Map<int[], String[]> dateToUrl = new HashMap<>();
    	try {
			reserveDays = getAfterfourDayDateLst("20230108","20230131");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//    	String[] reserveCourtList = {"A","B"}; 
    	
    	
    	List<Thread> threadList = new ArrayList<>();
    	
    	int DelayTime = 1000; //1초
    	for(String reserveCourt : yangjaeDao.getReserveCourtArr()) {
    		Runnable task = new ReserveTask(reserveCourt, reserveDays, DelayTime);
    		Thread subThread = new Thread(task);
    		threadList.add(subThread);
    		DelayTime = DelayTime+5000;
    	}
    	
    	for(Thread t : threadList) {
    		t.start();
    	}
    }
    
   
    public static void reserveCourtOfDays(WebDriver driver, String url, int[] reserveDate) {
    	WebDriverWait wait = new WebDriverWait(driver, 20);


		int[] calIdx = SearchCalenderDayIndex.getDayIndex(reserveDate[0]
				, reserveDate[1]
				, reserveDate[2]);  	
		String xpathCalDay = "//*[@id=\"calendar\"]/table/tbody[1]/tr["
								+ calIdx[0]	+ "]/td[" + calIdx[1] + "]";


		//년  //*[@id="calendar"]/div/strong/span[1]
		WebElement yearElement = wait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//*[@id=\"calendar\"]/div/strong/span[1]")));
		//월  //*[@id="calendar"]/div/strong/span[2]
		WebElement monthElement = wait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//*[@id=\"calendar\"]/div/strong/span[2]")));
		
		if((yearElement.getText()+monthElement.getText()).equals(
				Integer.toString(reserveDate[0])+Integer.toString(reserveDate[1])) == true) {
		
		}else{
			if(Integer.parseInt(yearElement.getText()) > reserveDate[0]) {
				//예약년보다 display년이 클때
				// pre month button  //*[@id="calendar"]/div/a[1]/i
				WebElement preMonthElement = wait.until(ExpectedConditions.presenceOfElementLocated(
														By.xpath("//*[@id=\"calendar\"]/div/a[1]/i")));
				preMonthElement.click();
		
			}else if(Integer.parseInt(yearElement.getText()) < reserveDate[0]) {
				//예약년보다 display년이 작을때
				// next month button  //*[@id="calendar"]/div/a[2]/i
				WebElement nextMonthElement = wait.until(ExpectedConditions.presenceOfElementLocated(
														By.xpath("//*[@id=\"calendar\"]/div/a[2]/i")));
				nextMonthElement.click(); 			
			}else {
				//예약년보다 display년이 같을때
				if(Integer.parseInt(monthElement.getText()) > reserveDate[1]) {
					// pre month button  //*[@id="calendar"]/div/a[1]/i
					WebElement preMonthElement = wait.until(ExpectedConditions.presenceOfElementLocated(
															By.xpath("//*[@id=\"calendar\"]/div/a[1]/i")));
					preMonthElement.click();	            		
				}else if(Integer.parseInt(monthElement.getText()) < reserveDate[1]) {
					// next month button  //*[@id="calendar"]/div/a[2]/i
					WebElement nextMonthElement = wait.until(ExpectedConditions.presenceOfElementLocated(
															By.xpath("//*[@id=\"calendar\"]/div/a[2]/i")));
					nextMonthElement.click();
				}
			}   		    		
		} 
		
		while(true) {
			//년  //*[@id="calendar"]/div/strong/span[1]
			WebElement yearElementAfter = wait.until(ExpectedConditions.presenceOfElementLocated(
									By.xpath("//*[@id=\"calendar\"]/div/strong/span[1]")));
			//월  //*[@id="calendar"]/div/strong/span[2]
			WebElement monthElementAfter = wait.until(ExpectedConditions.presenceOfElementLocated(
									By.xpath("//*[@id=\"calendar\"]/div/strong/span[2]")));
		
			if((yearElementAfter.getText()+monthElementAfter.getText()).equals(Integer.toString(reserveDate[0])+Integer.toString(reserveDate[1])) == true) {
				break;
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// 달력에 날짜 클릭
		WebElement calDayElement;
		while(true) {
			calDayElement = driver.findElement(By.xpath(xpathCalDay));
			if(calDayElement.getAttribute("class").contains("calendar-unselectable") == true) {
//				System.out.println(Thread.currentThread() + ":선택불가일자 : " 
//														  + reserveDate[0]+","+reserveDate[1]+","+reserveDate[2] );
				return;
			}else {
				calDayElement.click();
			}
				
			String clickDay = calDayElement.getText().split("\\n")[0];
			String displayTimeTabDay;
			try {
				displayTimeTabDay = wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[1]/em/span[1]")))
						.getText().replaceAll(" ", "").split("\\.")[1];
			}catch(Exception e) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				calDayElement.click();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				displayTimeTabDay = wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[1]/em/span[1]")))
						.getText().replaceAll(" ", "").split("\\.")[1];
			}
			
		
			if(clickDay.equals(displayTimeTabDay) == true) {
				break;
			}
		}
		
		
		//time 조회
    	List<int[]> enableTimeList = new ArrayList<>();
		for(int g=1;g<=2;g++) {
			for(int t=1;t<=12;t++) {
				WebElement timeElement;
				try {
					timeElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div["
							+g+"]/ul/li["+t+"]/a"));	
				}catch(Exception e) {
					calDayElement.click();
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					timeElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div["
							+g+"]/ul/li["+t+"]/a"));
				}
				
				if(timeElement.getAttribute("class").contains("none") == false) {
					//주말이나 휴일은 모든 시간
					if(yangjaeDao.chkHoliday(reserveDate) == true) {
						//주말이나 휴일은 모든 시간
						enableTimeList.add(new int[] {g, t});
					}else {
						//평일은 저녁 7시부터만 예약
						if((g == 2) && (t > 7)) {
							enableTimeList.add(new int[] {g, t});
						}
					}
				}
			}
		} 
		
		if(enableTimeList.size() == 0) {
//			System.out.println(Thread.currentThread() + ":예약가능한 시간이 없음");
			return;
		}else {
			ArrayList<Integer> amTimeLst = new ArrayList<>();
			ArrayList<Integer> pmTimeLst = new ArrayList<>();
			for(int[] timeArr : enableTimeList) {
				if(timeArr[0] == 1) {
					amTimeLst.add(timeArr[1]);  //AM
				}else {
					pmTimeLst.add(timeArr[1]);  //PM
				}
			}

			int[] amTimeMax = getTimeIdxMax(amTimeLst);
			int[] pmTimeMax = getTimeIdxMax(pmTimeLst);
			int[] timeMax;
			int timeFlag = 0;
			
			if(amTimeMax[0] != -1 && pmTimeMax[0] != -1) {
				if(amTimeMax.length > pmTimeMax.length) {
					timeMax = amTimeMax;
					timeFlag = 1;
				}else {
					timeMax = pmTimeMax;
					timeFlag = 2;
				}
			}else if(amTimeMax[0] == -1) {
				timeMax = pmTimeMax;
				timeFlag = 2;
			}else {
				timeMax = amTimeMax;
				timeFlag = 1;
			}

			System.out.println("[timeFlag][timeStIdx][timeEndIdx] : "
							+ "["+ timeFlag + "]" 
							+ "["+ timeMax[0] + "]"
							+ "["+ timeMax[timeMax.length-1] + "]");
			
			//time start end point가 같을 때
			WebElement timeElement;
			WebElement timeEndElement;
			if(timeMax.length == 1) {
				timeElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div["
						+timeFlag+"]/ul/li["+timeMax[0]+"]/a"));	
				//예약 시간 클릭
				
				if(timeElement.getAttribute("class").contains("none") == false) {
					timeElement.click();
				}else {
					return;
				}
			}else {
				timeElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div["
						+timeFlag+"]/ul/li["+timeMax[0]+"]/a"));
				
				timeEndElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div["
						+timeFlag+"]/ul/li["+timeMax[timeMax.length-1]+"]/a"));
				
				
				if(timeElement.getAttribute("class").contains("none") == false) {
					timeElement.click();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					return;
				}
				
				if(timeElement.getAttribute("class").contains("none") == false) {
					timeEndElement.click();
				}else {
					return;
				}
			}
			
			//예약시간 선택 버튼 //*[@id="container"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[3]/button
			WebElement reserveButtonElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[3]/button"));
			while(true) {
				if(reserveButtonElement.getAttribute("class").contains("on") == true) {
					reserveButtonElement.click();
					break;
				}
			}
			
			// 다음단계 버튼  //*[@id="container"]/bk-freetime/div[2]/div[2]/bk-submit/div/button
			WebElement nextButtonElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/div[2]/bk-submit/div/button"));
			nextButtonElement.click();
			
	    	//스크롤
	    	JavascriptExecutor js = (JavascriptExecutor) driver;
	    	//js.executeScript("window.scrollTo(0,document.body.scrollHeight)");

	    	//결제하기 버튼 //*[@id="container"]/bk-freetime/div[2]/div[2]/bk-submit/div/button
	    	//System.out.println("source : " + driver.getTitle());
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
	    	//List<WebElement> optionList = combobox.getAllSelectedOptions();
	    	combobox.selectByValue("004");
	    	
	    	//결제하기 버튼  //*[@id="Checkout_order__2yAvT"]/div[6]/button
	    	//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"Checkout_order__2yAvT\"]/div[6]/button")));
	    	WebElement payButton1Element = driver.findElement(By.xpath("//*[@id=\"Checkout_order__2yAvT\"]/div[6]/button"));
	    	js.executeScript("arguments[0].scrollIntoView();", payButton1Element);
	    	payButton1Element.click();
    	    	
	    	//결제 완료 popup 나올떄까지 기달리기  //*[@id="root"]/div[3]/div/a/i
	    	WebDriverWait waitPayComplate = new WebDriverWait(driver, 60);
	    	waitPayComplate.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"root\"]/div[3]/div/a/i")));
	    	
	    	System.out.println(Thread.currentThread() + " : 예약완료:[" 
	    						+ reserveDate[0]+"-"
	    						+ reserveDate[1]+"-"
	    						+ reserveDate[2]+" "
								+ "["+ timeFlag + "]" 
								+ "["+ timeMax[0] + "]"
								+ "["+ timeMax[timeMax.length-1] + "]"
	    						+ url);
		}
    }
    
    public static ArrayList<int []> getAfterfourDayDateLst(String strStartDate, String strEndDate) throws ParseException {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    	Calendar cal = Calendar.getInstance();
    	ArrayList<int[]> dateArrLst = new ArrayList<>();
    	
    	Date startDate = simpleDateFormat.parse(strStartDate);
//    	Date endDate = simpleDateFormat.parse(strEndDate);
    	cal.setTime(startDate);

    	while(true) {
    		Date tmpDate = new Date(cal.getTimeInMillis());
    		int [] tmpDateInt = {cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE)};
    		dateArrLst.add(tmpDateInt);
    		cal.add(Calendar.DATE, 1);
    		if(simpleDateFormat.format(tmpDate).equals(strEndDate) == true) {
    			break;
    		}
    	}
    	return dateArrLst;
    }
    
    public void naverLogin(WebDriver driver) {
        try {
            
        	driver.get(naver_login_url);
            
//            cp.copyToClip(aes256.decrypt(naverId));
        	cp.copyToClip(naverId);
            WebElement idElement = driver.findElement(By.id("id"));
            idElement.sendKeys(Keys.chord(Keys.CONTROL, "v"));
            
            //iframe 내부에서 pw 필드 탐색
//            cp.copyToClip(aes256.decrypt(naverPw));
            cp.copyToClip(naverPw);
            WebElement pwElement = driver.findElement(By.id("pw"));
            pwElement.sendKeys(Keys.chord(Keys.CONTROL, "v"));
            
            //로그인 버튼 클릭
            WebElement loginButtonElement = driver.findElement(By.id("log.login"));
            loginButtonElement.click();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
	public static int[] getTimeIdxMax(ArrayList<Integer> tmpLst) {
		if(tmpLst.size() > 0) {
//			System.out.println("tmpLst : " + tmpLst);
			Collections.sort(tmpLst);
			int[] timeMax = getTimeIdxMaxAdd(tmpLst);
			return timeMax;	
		}
		return new int[] {-1};
	}
	
	public static int[] getTimeIdxMaxAdd(ArrayList<Integer> timeLst) {
		int currVal = 0;
		int prevVal = 0;
		ArrayList<int[]> tmpLst = new ArrayList<>();
		ArrayList<Integer> inTmpLst = new ArrayList<>();
		
		
//		for(int i: timeLst) {
//			System.out.println("getTimeIdxMaxAdd : " + i);
//		}
		
		for(int timeInt : timeLst) {
			currVal = timeInt;
			
			if(timeLst.get(0) == timeInt) {
				if(timeLst.size() > 1) {
					prevVal = currVal;
					inTmpLst.add(currVal);	
				}else {
					tmpLst.add(new int[] {timeInt});
					break;
				}
			}else {
				if((prevVal+1) == currVal && inTmpLst.size()<4) {
					if(timeLst.indexOf(timeInt) == timeLst.size() - 1) {
						inTmpLst.add(currVal);
						Integer[] tmpInteger = inTmpLst.toArray(new Integer[inTmpLst.size()]);
						int[] tmp = Arrays.stream(tmpInteger).mapToInt(i->i).toArray();
						tmpLst.add(tmp);
						inTmpLst.clear();
					}else {
						inTmpLst.add(currVal);
						prevVal = currVal;	
					}
					
				}else {
//					System.out.println("amTime : " + amTime);
					Integer[] tmpInteger = inTmpLst.toArray(new Integer[inTmpLst.size()]);
					int[] tmp = Arrays.stream(tmpInteger).mapToInt(i->i).toArray();
					tmpLst.add(tmp);
					inTmpLst.clear();
					inTmpLst.add(currVal);
					prevVal = currVal;
				}				
			}
		}
		
		int preLen = 0;
		int maxLenIdx = 0;
		for(int[] timeArr : tmpLst) {
			int len = timeArr.length;
			if(tmpLst.indexOf(timeArr) == 0) {
				preLen = len;
				maxLenIdx = tmpLst.indexOf(timeArr);
			}else {
				if(len > preLen) {
					preLen = len;
					maxLenIdx = tmpLst.indexOf(timeArr);
				}
			}
		}
		
//		for(int[] t : tmpLst) {
//			System.out.println();
//			for(int t_1 : t) {
//				System.out.print(t_1+",");
//			}
//			System.out.println();
//		}
//		System.out.println(maxLenIdx);
//		System.out.println("tmpLst.get(0) : " + tmpLst.get(0));
//		System.out.println("tmpLst.get(maxLenIdx) : " + tmpLst.get(maxLenIdx));
		return tmpLst.get(maxLenIdx);
	}
    


}

