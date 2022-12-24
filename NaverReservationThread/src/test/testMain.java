package test;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;



public class testMain {

	static ArrayList<int[]> holidayArrLst = new ArrayList<>();
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

//		holidayArrLst.add(new int[] {2023,1,1});
//		holidayArrLst.add(new int[] {2023,1,23});
//		holidayArrLst.add(new int[] {2023,1,24});
//		holidayArrLst.add(new int[] {2023,3,1});
//		holidayArrLst.add(new int[] {2023,5,5});
//		holidayArrLst.add(new int[] {2023,6,6});
//		holidayArrLst.add(new int[] {2023,8,15});
//		holidayArrLst.add(new int[] {2023,9,28});
//		holidayArrLst.add(new int[] {2023,9,29});
//		holidayArrLst.add(new int[] {2023,10,3});
//		holidayArrLst.add(new int[] {2023,10,9});
//		holidayArrLst.add(new int[] {2023,12,25});
//
//		
//		int[] tmpDateInt = {2023,1,23};
//		System.out.println("holiday chk : " + chkHoliday(tmpDateInt));
		
		
		getAfterfourDayDateLst("20230101", "20230104");
		
	}
	
	public static boolean chkHoliday(int[] dateInt) {
		Calendar cal = Calendar.getInstance();
		cal.set(dateInt[0],dateInt[1]-1,dateInt[2]);
		int dayOfWeekNumber = cal.get(Calendar.DAY_OF_WEEK);  //1:일요일, 7:토요일
		Date tmpDate = new Date(cal.getTimeInMillis());
		System.out.println(tmpDate);
		System.out.println(dayOfWeekNumber);
		
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
    	
    	for(int[] a : dateArrLst) {
    		System.out.println(a[0]+"-"+a[1]+"-"+a[2]);
    		
    	}
    	return dateArrLst;
    }
}


