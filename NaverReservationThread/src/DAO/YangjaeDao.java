package DAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

public class YangjaeDao {
	
	Map<String,String> courtNum = new HashMap<>();
	WebDriver driver;
	String[] courtNmArr = {"A","B","C","1","2","3","4","5","6","7","8"};
	String courtUrl = "https://booking.naver.com/booking/10/bizes/210031/items/";
	ArrayList<ReserveInfoDao> reserveInfos = new ArrayList<>();
	
	public YangjaeDao() {
		//'22³â 12¿ù
		courtNum.put("12_A","4394828");
		courtNum.put("12_B","4394829");
		courtNum.put("12_C","4394830");
		courtNum.put("12_1","4394832");
		courtNum.put("12_2","4394834");
		courtNum.put("12_3","4394835");
		courtNum.put("12_4","4394836");
		courtNum.put("12_5","4394837");
		courtNum.put("12_6","4394839");
		courtNum.put("12_7","4394840");
		courtNum.put("12_8","4394841");
		
		courtNum.put("1_A","4444647");
		courtNum.put("1_B","4444648");
		courtNum.put("1_C","4444650");
		courtNum.put("1_1","4444651");
		courtNum.put("1_2","4444653");
		courtNum.put("1_3","4444654");
		courtNum.put("1_4","4444655");
		courtNum.put("1_5","4444656");
		courtNum.put("1_6","4444657");
		courtNum.put("1_7","4444658");
		courtNum.put("1_8","4444660");
	}
	
	public void addReserveInfo(ReserveInfoDao reserveInfoDao) {
		reserveInfos.add(reserveInfoDao);
	}
	
	public ArrayList<ReserveInfoDao> getReserveInfo(){
		return this.reserveInfos;
	}
	
	public String[] getCourtNm() {
		return courtNmArr;
	}
	
	public ArrayList getAllCourtUrl(){
		ArrayList<String> rtnCourtUrl = new ArrayList<>();
		
		for(String key : courtNum.keySet()) {
			rtnCourtUrl.add(courtUrl+courtNum.get(key));
		}
		
		for(String s : rtnCourtUrl) {
//			System.out.println("url : " + s);
		}
		return rtnCourtUrl;
	}
	
	public String getCourtUrl(String courtName){
//		System.out.println("courtName : " + courtName);
		String rtnVal = "";
		
		for(String key : courtNum.keySet()) {
//			System.out.println("key : " + key);
			if(key.equals(courtName) == true) {
				rtnVal = courtUrl + courtNum.get(key);
			}
		}
//		System.out.println("court url : " + rtnVal);
		return rtnVal;
	}
	
	@Override
	public String toString() {
		return "YangjaeDao [courtNum=" + courtNum + ", driver=" + driver + ", courtNmArr=" + Arrays.toString(courtNmArr)
				+ ", courtUrl=" + courtUrl + ", reserveInfos=" + reserveInfos + "]";
	}

}
