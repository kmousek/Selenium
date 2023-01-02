package com.kmousek.springboot.reserveCourt.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

import com.kmousek.springboot.reserveCourt.common.AES256;
import com.kmousek.springboot.reserveCourt.common.ClipBoard;
import com.kmousek.springboot.reserveCourt.common.CustomCalendar;

import com.kmousek.springboot.reserveCourt.DAO.YangjaeDao;

public class ReserveTask implements Runnable {

    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "D:/100-devTools/macro/chromedriver/108/chromedriver.exe";
    
    //ũ�Ѹ� �� URL
    private String naver_login_url = "https://nid.naver.com/nidlogin.login?mode=form&url=https%3A%2F%2Fwww.naver.com";
    
    //���̹� �α��� ����
//    private String naverId = "RUoRS0lyf9m8sWs9hk3kWg==";
//    private String naverPw = "GBRoWwdfulZHKg3AiNCr9w==";
    private static String naverId;
    private static String naverPw;

    //��ȣȭ
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
         driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
         try {
			Thread.sleep(loginDelay);
		 } catch (InterruptedException e1) {
			// TODO Auto-generated catch block
		 	System.out.println("Thread stop : " + Thread.currentThread().getName());
		 	driver.quit();
		 }
         
         try {
        	 naverLogin(driver);
         }catch(InterruptedException e2) {
        	 System.out.println("Thread stop : " + Thread.currentThread().getName());
 		 	 driver.quit(); 
         } catch (Exception e) {
			System.out.println("naverLogin Error : " + e.getMessage());
		 }
         
     	 
         Boolean exceptFlag = false;
     	 while(!Thread.interrupted()) {
     		 String prevUrl="";
   		 
   		 
     		 Collections.shuffle(reserveDateList);
   		 
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
   			 }catch(InterruptedException e1) {
   				System.out.println("Thread stop! : " + Thread.currentThread().getName());
   				driver.quit();
     		 }catch(Exception e) {
//   				System.out.println(Thread.currentThread() + ": ���ܹ߻� : " + e.getMessage());
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
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
//				driver.close();	
				System.out.println("Thread stop! : " + Thread.currentThread().getName());
				driver.quit();
			}
     	 }
     	 
     	 driver.quit();
	}
	
	
	
    public ArrayList<Thread> yangjaeTask(String id, String pw, String reserveStartDate, String reserveEndDate) {
    	naverId = id;
    	naverPw = pw;
    	
    	ArrayList<int[]> reserveDays = new ArrayList<>();

		reserveDays = CustomCalendar.getDateLst(reserveStartDate,reserveEndDate);
		ArrayList<Thread> arrLstOfThread = new ArrayList<>();
    	
    	int DelayTime = 1000; //1��
    	for(String reserveCourt : yangjaeDao.getReserveCourtArr()) {
    		Runnable task = new ReserveTask(reserveCourt, reserveDays, DelayTime);
    		Thread subThread = new Thread(task);
    		subThread.setName(reserveCourt);
    		arrLstOfThread.add(subThread);
    		DelayTime = DelayTime+5000;
    	}
        	
    	for(Thread t : arrLstOfThread) {
    		System.out.println("Thread info : " + t.toString());
    		t.start();
    	}
    	
    	System.out.println("main end");
    	return arrLstOfThread;
    }
    
    
    public void stopThread(ArrayList<Thread> arrLstOfThread) {
    	
    	System.out.println("arrLstOfThread.size() : " + arrLstOfThread.size());
//    	System.out.println("info00 : " + Thread.currentThread().toString());
    	
    	for(Thread t : arrLstOfThread) {
//    		System.out.println("Thread info00 : " + t.toString());
//    		System.out.println("Thread stop00 : " + Thread.currentThread());
    		t.interrupt();
//    		Thread.currentThread().interrupt();
    	}
    }
   
    public static void reserveCourtOfDays(WebDriver driver, String url, int[] reserveDate) throws Exception {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));


		int[] calIdx = CustomCalendar.getDayIndex(reserveDate[0]
				, reserveDate[1]
				, reserveDate[2]);  	
		String xpathCalDay = "//*[@id=\"calendar\"]/table/tbody[1]/tr["
								+ calIdx[0]	+ "]/td[" + calIdx[1] + "]";


		//��  //*[@id="calendar"]/div/strong/span[1]
		WebElement yearElement = wait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//*[@id=\"calendar\"]/div/strong/span[1]")));
		//��  //*[@id="calendar"]/div/strong/span[2]
		WebElement monthElement = wait.until(ExpectedConditions.presenceOfElementLocated(
			By.xpath("//*[@id=\"calendar\"]/div/strong/span[2]")));
		
		if((yearElement.getText()+monthElement.getText()).equals(
				Integer.toString(reserveDate[0])+Integer.toString(reserveDate[1])) == true) {
		
		}else{
			if(Integer.parseInt(yearElement.getText()) > reserveDate[0]) {
				//����⺸�� display���� Ŭ��
				// pre month button  //*[@id="calendar"]/div/a[1]/i
				WebElement preMonthElement = wait.until(ExpectedConditions.presenceOfElementLocated(
														By.xpath("//*[@id=\"calendar\"]/div/a[1]/i")));
				preMonthElement.click();
		
			}else if(Integer.parseInt(yearElement.getText()) < reserveDate[0]) {
				//����⺸�� display���� ������
				// next month button  //*[@id="calendar"]/div/a[2]/i
				WebElement nextMonthElement = wait.until(ExpectedConditions.presenceOfElementLocated(
														By.xpath("//*[@id=\"calendar\"]/div/a[2]/i")));
				nextMonthElement.click(); 			
			}else {
				//����⺸�� display���� ������
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
			//��  //*[@id="calendar"]/div/strong/span[1]
			WebElement yearElementAfter = wait.until(ExpectedConditions.presenceOfElementLocated(
									By.xpath("//*[@id=\"calendar\"]/div/strong/span[1]")));
			//��  //*[@id="calendar"]/div/strong/span[2]
			WebElement monthElementAfter = wait.until(ExpectedConditions.presenceOfElementLocated(
									By.xpath("//*[@id=\"calendar\"]/div/strong/span[2]")));
		
			if((yearElementAfter.getText()+monthElementAfter.getText()).equals(Integer.toString(reserveDate[0])+Integer.toString(reserveDate[1])) == true) {
				break;
			}
			

			Thread.sleep(1000);

		}
		
		
		Thread.sleep(1000);

		// �޷¿� ��¥ Ŭ��
		WebElement calDayElement;
		while(true) {
			calDayElement = driver.findElement(By.xpath(xpathCalDay));
			if(calDayElement.getAttribute("class").contains("calendar-unselectable") == true) {
//				System.out.println(Thread.currentThread() + ":���úҰ����� : " 
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
				Thread.sleep(500);

				calDayElement.click();

				Thread.sleep(500);

				displayTimeTabDay = wait.until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[1]/em/span[1]")))
						.getText().replaceAll(" ", "").split("\\.")[1];
			}
			
		
			if(clickDay.equals(displayTimeTabDay) == true) {
				break;
			}
		}
		
		
		//time ��ȸ
    	List<int[]> enableTimeList = new ArrayList<>();
		for(int g=1;g<=2;g++) {
			for(int t=1;t<=12;t++) {
				WebElement timeElement;
				try {
					timeElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div["
							+g+"]/ul/li["+t+"]/a"));	
				}catch(Exception e) {
					calDayElement.click();

					Thread.sleep(3000);

					timeElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div["
							+g+"]/ul/li["+t+"]/a"));
				}
				
				if(timeElement.getAttribute("class").contains("none") == false) {
					//�ָ��̳� ������ ��� �ð�
					if(yangjaeDao.chkHoliday(reserveDate) == true) {
						//�ָ��̳� ������ ��� �ð�
						enableTimeList.add(new int[] {g, t});
					}else {
						//������ ���� 7�ú��͸� ����
						if((g == 2) && (t > 7)) {
							enableTimeList.add(new int[] {g, t});
						}
					}
				}
			}
		} 
		
		if(enableTimeList.size() == 0) {
//			System.out.println(Thread.currentThread() + ":���డ���� �ð��� ����");
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

			System.out.println("[Thread][ReserveDate][timeFlag][timeStIdx][timeEndIdx] : "
					 		+ "[" + Thread.currentThread() + "]"
						    + "[" + reserveDate[0]+"-"+reserveDate[1]+"-"+reserveDate[2] + "]" 
							+ "["+ timeFlag + "]" 
							+ "["+ timeMax[0] + "]"
							+ "["+ timeMax[timeMax.length-1] + "]");
			
			//time start end point�� ���� ��
			WebElement timeElement;
			WebElement timeEndElement;
			if(timeMax.length == 1) {
				timeElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div["
						+timeFlag+"]/ul/li["+timeMax[0]+"]/a"));	
				//���� �ð� Ŭ��
				
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

					Thread.sleep(1000);

				}else {
					return;
				}
				
				if(timeElement.getAttribute("class").contains("none") == false) {
					timeEndElement.click();
				}else {
					return;
				}
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
	    	//js.executeScript("window.scrollTo(0,document.body.scrollHeight)");

	    	//�����ϱ� ��ư //*[@id="container"]/bk-freetime/div[2]/div[2]/bk-submit/div/button
	    	//System.out.println("source : " + driver.getTitle());
	    	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/div[2]/bk-submit/div/button")));
	    	WebElement payButtonElement = driver.findElement(By.xpath("//*[@id=\"container\"]/bk-freetime/div[2]/div[2]/bk-submit/div/button"));
	    	js.executeScript("arguments[0].scrollIntoView();", payButtonElement);
	    	payButtonElement.click();
	    	
	    	//�Ϲݰ��� ����  //*[@id="PAYMENT_WRAP"]/div[1]/div[1]/ul/li[4]/div[1]/label
	    	WebElement normalRadioElement = driver.findElement(By.xpath("//*[@id=\"PAYMENT_WRAP\"]/div[1]/div[1]/ul/li[4]/div[1]/label"));
	    	js.executeScript("arguments[0].scrollIntoView();", normalRadioElement);
	    	normalRadioElement.click();
	    	
	    	// ���߿� ���� radio //*[@id="PAYMENT_WRAP"]/div[1]/div[1]/ul/li[4]/ul/li[2]/label
	    	WebElement postPayRadioElement = driver.findElement(By.xpath("//*[@id=\"PAYMENT_WRAP\"]/div[1]/div[1]/ul/li[4]/ul/li[2]/label"));
	    	postPayRadioElement.click();
	    	
	    	//ȯ������� ����  //*[@id="PAYMENT_WRAP"]/div[1]/div[1]/div[1]/div[2]/div[2]/div/ul/li[2]/label
	    	WebElement refundChargeElement = driver.findElement(By.xpath("//*[@id=\"PAYMENT_WRAP\"]/div[1]/div[1]/div[1]/div[2]/div[2]/div/ul/li[2]/label"));
	    	refundChargeElement.click();
	    	
	    	//�Ա����� selectbox  //*[@id="bankCodeList"]/div/div/select
	    	Select combobox = new Select(driver.findElement(By.xpath("//*[@id=\"bankCodeList\"]/div/div/select")));
	    	//List<WebElement> optionList = combobox.getAllSelectedOptions();
	    	combobox.selectByValue("004");
	    	
	    	//�����ϱ� ��ư  //*[@id="Checkout_order__2yAvT"]/div[6]/button
	    	//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"Checkout_order__2yAvT\"]/div[6]/button")));
	    	WebElement payButton1Element = driver.findElement(By.xpath("//*[@id=\"Checkout_order__2yAvT\"]/div[6]/button"));
	    	js.executeScript("arguments[0].scrollIntoView();", payButton1Element);
	    	payButton1Element.click();
    	    	
	    	//���� �Ϸ� popup ���Ë����� ��޸���  //*[@id="root"]/div[3]/div/a/i
	    	WebDriverWait waitPayComplate = new WebDriverWait(driver, Duration.ofSeconds(60));
	    	waitPayComplate.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"root\"]/div[3]/div/a/i")));
	    	
	    	System.out.println(Thread.currentThread() + " : ����Ϸ�:[" 
	    						+ reserveDate[0]+"-"
	    						+ reserveDate[1]+"-"
	    						+ reserveDate[2]+" "
								+ "["+ timeFlag + "]" 
								+ "["+ timeMax[0] + "]"
								+ "["+ timeMax[timeMax.length-1] + "]"
	    						+ url);
		}
    }
    

    
    public void naverLogin(WebDriver driver) throws Exception {
        driver.get(naver_login_url);
		
//            cp.copyToClip(aes256.decrypt(naverId));
		cp.copyToClip(naverId);
		WebElement idElement = driver.findElement(By.id("id"));
		idElement.sendKeys(Keys.chord(Keys.CONTROL, "v"));
		
		//iframe ���ο��� pw �ʵ� Ž��
//            cp.copyToClip(aes256.decrypt(naverPw));
		cp.copyToClip(naverPw);
		WebElement pwElement = driver.findElement(By.id("pw"));
		pwElement.sendKeys(Keys.chord(Keys.CONTROL, "v"));
		
		//�α��� ��ư Ŭ��
		WebElement loginButtonElement = driver.findElement(By.id("log.login"));
		loginButtonElement.click();
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

