package test;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import Common.ArrayStack;



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
		
		
//		getAfterfourDayDateLst("20230101", "20230104");
		
//		ArrayList<int[]> tmpLst = new ArrayList<>();
		
//		tmpLst.add(new int[] {1,1});
//		tmpLst.add(new int[] {1,2});
//		tmpLst.add(new int[] {1,3});
//		tmpLst.add(new int[] {1,4});
//		tmpLst.add(new int[] {1,5});
//		tmpLst.add(new int[] {1,6});
//		tmpLst.add(new int[] {1,7});
//		tmpLst.add(new int[] {1,8});
//		tmpLst.add(new int[] {1,9});
//		tmpLst.add(new int[] {1,10});
//		tmpLst.add(new int[] {1,11});
//		tmpLst.add(new int[] {1,12});
		
//		tmpLst.add(new int[] {0,1});
//		tmpLst.add(new int[] {0,2});
//		tmpLst.add(new int[] {0,3});
//		tmpLst.add(new int[] {0,4});
//		tmpLst.add(new int[] {0,5});
//		tmpLst.add(new int[] {0,6});
//		tmpLst.add(new int[] {0,7});
//		tmpLst.add(new int[] {0,8});
//		tmpLst.add(new int[] {0,9});
//		tmpLst.add(new int[] {0,10});
//		tmpLst.add(new int[] {0,11});
//		tmpLst.add(new int[] {0,12});
		
		
		ArrayList<Integer> inputVal = new ArrayList<>();
		
		inputVal.add(1);
//		inputVal.add(3);
//		inputVal.add(4);
//		inputVal.add(5);
		
		
		int[] rtnVal = getTimeIdxMax(inputVal);
		for(int i : rtnVal) {
			System.out.println(i);	
		}
		
		
		
	}
	
	
	public static int[] getTimeIdxMax(ArrayList<Integer> tmpLst) {
		if(tmpLst.isEmpty() != true) {
			Collections.sort(tmpLst);
			int[] timeMax = getTimeIdxMaxAdd(tmpLst);
			return timeMax;	
		}
		return new int[] {-1};
	}
	
	public static int[] getTimeIdxMaxAdd(ArrayList<Integer> timeLst) {
		
		System.out.println("timeLst : " + timeLst);
		int currVal = 0;
		int prevVal = 0;
		ArrayList<int[]> tmpLst = new ArrayList<>();
		ArrayList<Integer> inTmpLst = new ArrayList<>();
		
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
		
		System.out.println(tmpLst);
		
		int preLen = 0;
		int maxLenIdx = 0;
		for(int[] timeArr : tmpLst) {
			int len = timeArr.length;
			System.out.println("len : " + len);
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
		return tmpLst.get(maxLenIdx);
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


