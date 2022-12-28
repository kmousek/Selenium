package DAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

public class YangjaeDao {
	
	Map<String,String> courtNum = new HashMap<>();
	WebDriver driver;
	String[] courtNmArr = {"A","B","C","1","2","3","4","5","6","7","8"};
	String courtUrl = "https://booking.naver.com/booking/10/bizes/210031/items/";
	ArrayList<ReserveInfoDao> reserveInfos = new ArrayList<>();
	static ArrayList<int[]> holidayArrLst = new ArrayList<>();
	ArrayList<String> reserveCourtArrLst = new ArrayList<>();
	
	private YangjaeDao() {
		//예약 코트 셋팅
		for(String courtNm : courtNmArr) {
			reserveCourtArrLst.add(courtNm);
		}
		
		//'22년 12월
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
	
	private static class YangjaeDaoHelper{
		private static final YangjaeDao yangjaeDao = new YangjaeDao();
	}
	
	public static YangjaeDao getInstance() {
		return YangjaeDaoHelper.yangjaeDao;
	}
	
	public static boolean chkHoliday(int[] dateInt) {
		Calendar cal = Calendar.getInstance();
		cal.set(dateInt[0],dateInt[1]-1,dateInt[2]);
		int dayOfWeekNumber = cal.get(Calendar.DAY_OF_WEEK);  //1:일요일, 7:토요일		
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
	
	public ArrayList<String> getReserveCourtArr() {
		return this.reserveCourtArrLst;
	}
	
	public ArrayList<int[]> getHolidayArrLst(){
		return this.holidayArrLst;
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
	
	public ArrayList<String> getAllCourtUrl(){
		ArrayList<String> rtnCourtUrl = new ArrayList<>();
		
		for(String key : courtNum.keySet()) {
			rtnCourtUrl.add(courtUrl+courtNum.get(key));
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

