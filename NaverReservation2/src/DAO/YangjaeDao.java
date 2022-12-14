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
	}
	
	public void addReserveInfo(ReserveInfoDao reserveInfoDao) {
		reserveInfos.add(reserveInfoDao);
	}
	
	public ArrayList<ReserveInfoDao> getReserveInfo(){
		return this.reserveInfos;
	}
	
	@Override
	public String toString() {
		return "YangjaeDao [courtNum=" + courtNum + ", driver=" + driver + ", courtNmArr=" + Arrays.toString(courtNmArr)
				+ ", courtUrl=" + courtUrl + ", reserveInfos=" + reserveInfos + "]";
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
	
	
	
	
	
	
}
