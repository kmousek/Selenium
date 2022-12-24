package ReserveNaver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import Common.SearchCalenderDayIndex;

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
    
    static ArrayList<int[]> holidayArrLst = new ArrayList<>();
   
    public ReserveTask() {
    	
    }
    
    public ReserveTask(String url, List<int[]> reserveDateList, int loginDelay) {
    	this.url = url;
    	this.reserveDateList = reserveDateList;
    	this.loginDelay = loginDelay;  
    	holidayArrLst.add(new int[] {2023,1,1});
    	holidayArrLst.add(new int[] {2023,1,23});
    	holidayArrLst.add(new int[] {2023,1,24});
    	holidayArrLst.add(new int[] {2023,3,1});
    	holidayArrLst.add(new int[] {2023,5,5});
    	holidayArrLst.add(new int[] {2023,6,6});
    	holidayArrLst.add(new int[] {2023,8,15});
    	holidayArrLst.add(new int[] {2023,9,28});
    	holidayArrLst.add(new int[] {2023,9,29});
    	holidayArrLst.add(new int[] {2023,10,3});
    	holidayArrLst.add(new int[] {2023,10,9});
    	holidayArrLst.add(new int[] {2023,12,25});
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
//     				 System.out.println("input param : url ["+url
//     						 			+"] reserveDate ["+reserveDate[0]
//     						 						  +"-"+reserveDate[1]
//     						 					      +"-"+reserveDate[2]+"]");
     				reserveCourtOfDay(driver, url, reserveDate);
     			 }catch(Exception e) {
     				System.out.println(Thread.currentThread() + ": 예외발생 : " + e.getMessage() + ":" + e.getLocalizedMessage());
     			 }
 				 
//     			 System.out.println("input param : url ["+url
//		 			+"] reserveDate ["+reserveDate[0]
//		 						  +"-"+reserveDate[1]
//		 					      +"-"+reserveDate[2]+"]");
// 				 reserveCourtOfDay(driver, url, reserveDate);
     			 
     			 
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
    	try {
			reserveDays = getAfterfourDayDateLst("20230101","20230131");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
//    	String[] reserveCourtList = {"12_A","12_B","12_C"};  //실내
//    	String[] reserveCourtList = {"1_A","1_B","1_C"};  //실내
//    	String[] reserveCourtList = {"12_1","12_3"};  //실내
    	String[] reserveCourtList = {
//    								"12_A","12_B","12_C"
//    								,
    								"1_A","1_B","1_C" //실내
    								, 
    								"1_1","1_2","1_3","1_4","1_5","1_6","1_7","1_8" //실외
    			
//    			"1_1"
    								}; 
    	
    	
    	List<Thread> threadList = new ArrayList<>();
    	
    	int DelayTime = 1000; //1초
    	for(String reserveCourt : reserveCourtList) {
    		Runnable task = new ReserveTask(yangjaeDao.getCourtUrl(reserveCourt), reserveDays, DelayTime);
    		Thread subThread = new Thread(task);
    		threadList.add(subThread);
    		DelayTime = DelayTime+5000;
    	}
    	
    	for(Thread t : threadList) {
    		t.start();
    	}

    }
	

    

    

    
    
    public static void reserveCourtOfDay(WebDriver driver, String url, int[] reserveDate) {
    	WebDriverWait wait = new WebDriverWait(driver, 20);

//    	ReserveInfoDao reserveInfoDao = new ReserveInfoDao();
    	
    	int[] calIdx = SearchCalenderDayIndex.getDayIndex(reserveDate[0], reserveDate[1], reserveDate[2]);
    	
//    	reserveInfoDao.setxPathCalDay2_calCol(Integer.toString(calIdx[0]));
//    	reserveInfoDao.setxPathCalDay4_calRow(Integer.toString(calIdx[1]));
    	String xpathCalDay = "//*[@id=\"calendar\"]/table/tbody[1]/tr["
    						+ calIdx[0]
    						+ "]/td["
    						+ calIdx[1]
    						+ "]";

		while(true) {
        	try {
    			driver.get(url);
            	//  //*[@id="container"]/bk-freetime/div[1]/bk-breadcrumb/div/ul/li[2]/a/span
            	wait.until(ExpectedConditions.presenceOfElementLocated(
            			By.xpath("//*[@id=\"container\"]/bk-freetime/div[1]/bk-breadcrumb/div/ul/li[2]/a/span")));
            	//  /html/body/app/bk-alert/div/div[2]/a/i
            	
            	break;
        	}catch(Exception e) {
        		try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
		}
		
    	//년  //*[@id="calendar"]/div/strong/span[1]
    	WebElement yearElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"calendar\"]/div/strong/span[1]")));
    	//월  //*[@id="calendar"]/div/strong/span[2]
    	WebElement monthElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"calendar\"]/div/strong/span[2]")));
    	
//    	System.out.println("displayYearMonth : " + yearElement.getText()+monthElement.getText());
    	
    	if((yearElement.getText()
    		+monthElement.getText()).equals(Integer.toString(reserveDate[0])
    									   +Integer.toString(reserveDate[1])) == true) {

    	}else {
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

    	
    	//년  //*[@id="calendar"]/div/strong/span[1]
    	WebElement yearElementAfter = 
    			wait.until(ExpectedConditions.presenceOfElementLocated(
    					By.xpath("//*[@id=\"calendar\"]/div/strong/span[1]")));
    	//월  //*[@id="calendar"]/div/strong/span[2]
    	WebElement monthElementAfter = 
    			wait.until(ExpectedConditions.presenceOfElementLocated(
    					By.xpath("//*[@id=\"calendar\"]/div/strong/span[2]")));
    	
    	while(true) {
//    		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"calendar\"]")));
    		
    		try {
//    			wait.until(ExpectedConditions.elementToBeSelected(By.xpath("//*[@id=\"calendar\"]/table/tbody[1]/tr[3]/td[3]")));
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        	if((yearElementAfter.getText()
            		+monthElementAfter.getText()).equals(Integer.toString(reserveDate[0])
            									+Integer.toString(reserveDate[1])) == true) {
        		break;
            }
    	}

    	while(true) {
    		WebElement calDayElement = driver.findElement(By.xpath(xpathCalDay));
        	calDayElement.click();
        	try {
        		//popup 닫기 		/html/body/app/bk-alert/div/div[2]/a/i
        		WebElement popupCloseElement = 
        				wait.until(ExpectedConditions.presenceOfElementLocated(
        						By.xpath("/html/body/app/bk-alert/div/div[2]/a/i")));
        		popupCloseElement.click();
//        		driver.switchTo().alert().dismiss();
//        		wait.until(ExpectedConditions.alertIsPresent());
        		
        		System.out.println(Thread.currentThread() 
        							+ ": 선택 불가 날짜 : " 
        							+ reserveDate[0] 
        							+ "-" + reserveDate[1]
        							+ "-" + reserveDate[2]);
        		
        		return;
        	}catch(Exception e) {
//        		System.out.println(Thread.currentThread() 
//        							+ " : 예외발생 : " 
//        							+ e.getMessage());
        	}
        	
        	String clickDay = calDayElement.getText().split("\\n")[0];
        	String displayTimeTabDay = 
        			driver.findElement(
        			By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[1]/em/span[1]"))
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
//					System.out.println(Thread.currentThread() + " : Enable time : " 
//										+ reserveDate[0]+"-"+reserveDate[1]+"-"+reserveDate[2]+" "
//										+ g +"," + t);
				}
			}	//end for t
		}   //enf for g 
    	
		
		//평일은 예약시간 저녁 7~10시, 주말이나 공휴일은 모든 시간대
		List<int[]> enableTimeListTmp = new ArrayList<>();
		if(chkHoliday(reserveDate) == true) {
			enableTimeListTmp = enableTimeList;
		}else {
			
			for(int[] timeInt : enableTimeList) {
				if((timeInt[0] == 2) && (timeInt[1]>7)) {
					enableTimeListTmp.add(timeInt);
				}
			}
		}
		
		
		enableTimeList=enableTimeListTmp;
		
		for(int [] et : enableTimeList) {
			System.out.println(Thread.currentThread() + " : choise time : " 
					+ reserveDate[0]+"-"+reserveDate[1]+"-"+reserveDate[2]+" "
					+ et[0] +"," + et[1]);
		}

		if(enableTimeList.size() == 0) {
			return;
		}
    	
    	for(int[] enableTime : enableTimeList) {
    		
    		while(true) {
            	try {
        			driver.get(url);
                	//  //*[@id="container"]/bk-freetime/div[1]/bk-breadcrumb/div/ul/li[2]/a/span
                	wait.until(ExpectedConditions.presenceOfElementLocated(
                			By.xpath("//*[@id=\"container\"]/bk-freetime/div[1]/bk-breadcrumb/div/ul/li[2]/a/span")));
                	//  /html/body/app/bk-alert/div/div[2]/a/i
                	
                	break;
            	}catch(Exception e) {
            		try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            	}
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
//	    	System.out.println("source : " + driver.getTitle());
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
    	    	
	    	//결제 완료 popup 나올떄까지 기달리기  //*[@id="root"]/div[3]/div/a/i
	    	WebDriverWait waitPayComplate = new WebDriverWait(driver, 60);
	    	waitPayComplate.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"root\"]/div[3]/div/a/i")));
	    	
	    	
	    	
	    	System.out.println(Thread.currentThread() + " : 예약완료:[" 
	    						+ reserveDate[0]+"-"
	    						+ reserveDate[1]+"-"
	    						+ reserveDate[2]+" "
	    						+ enableTime[0]+":"
	    						+ enableTime[1]+"]"
	    						+ url);
	    	
    	} // end for 	
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
    
	public static boolean chkHoliday(int[] dateInt) {
		Calendar cal = Calendar.getInstance();
		cal.set(dateInt[0],dateInt[1]-1,dateInt[2]);
		int dayOfWeekNumber = cal.get(Calendar.DAY_OF_WEEK);  //1:일요일, 7:토요일
		Date tmpDate = new Date(cal.getTimeInMillis());
//		System.out.println(tmpDate);
//		System.out.println(dayOfWeekNumber);
		
		if(dayOfWeekNumber == 1 || dayOfWeekNumber == 7) {
			return true;
		}else {
			for(int[] hol : holidayArrLst) {
				if(Arrays.equals(hol, dateInt)) {
					return true;
				}
			}
		}
		return false;
	}

}

