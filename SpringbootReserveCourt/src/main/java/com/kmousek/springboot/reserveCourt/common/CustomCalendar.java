package com.kmousek.springboot.reserveCourt.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CustomCalendar {

	
    public static ArrayList<int []> getDateLst(String strStartDate, String strEndDate) {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    	Calendar cal = Calendar.getInstance();
    	ArrayList<int[]> dateArrLst = new ArrayList<>();
    	
    	Date startDate = null;
		try {
			startDate = simpleDateFormat.parse(strStartDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
    
    public static ArrayList<String> getDateLstStr(String strStartDate, String strEndDate) {
    	SimpleDateFormat formatyyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat formatyyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar cal = Calendar.getInstance();
    	ArrayList<String> dateArrLst = new ArrayList<>();
    	
    	Date startDate = null;
		try {
			startDate = formatyyyyMMdd.parse(strStartDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	Date endDate = simpleDateFormat.parse(strEndDate);
    	cal.setTime(startDate);

    	while(true) {
    		Date tmpDate = new Date(cal.getTimeInMillis());
    		int [] tmpDateInt = {cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE)};
    		dateArrLst.add(formatyyyy_MM_dd.format(tmpDate).toString());
    		cal.add(Calendar.DATE, 1);
    		if(formatyyyyMMdd.format(tmpDate).equals(strEndDate) == true) {
    			break;
    		}
    	}
    	return dateArrLst;
    }
	
	
	public static int[] getDayIndex(int year, int month, int day) {
		int[] dayIndex = new int[2];
		int[][] custCal = new int[6][7];
		// 1. LocalDate ����
		LocalDate date = LocalDate.of(year, month, 1);
		// LocalDateTime date = LocalDateTime.of(2021, 12, 25, 1, 15, 20);
//		System.out.println(date); // // 2021-12-25
		// 2. DayOfWeek ��ü ���ϱ�
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		// 3. ���� ���� ���ϱ�
		int dayOfWeekNumber = dayOfWeek.getValue();
		// 4. ���� ���� ���
//		System.out.println(dayOfWeekNumber); // 6
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, 1);
		int lastDay = cal.getMaximum(cal.DAY_OF_MONTH);
		
//		System.out.println("lastDay : " + lastDay);
		
		//   7/1/2/3/4/5/6
		//   1/2/3/4/5/6/7
		
		int week = 0;
		int startDayIdx = 0;
		
		switch(dayOfWeekNumber) {
			case 7 : startDayIdx = 1; break;
			case 1 : startDayIdx = 2; break;
			case 2 : startDayIdx = 3; break;
			case 3 : startDayIdx = 4; break;
			case 4 : startDayIdx = 5; break;
			case 5 : startDayIdx = 6; break;
			case 6 : startDayIdx = 7; break;
			default : startDayIdx = 0; break;
		}
		 
		int dayCnt = 1;
//		System.out.println("startDayIdx : " + startDayIdx);
		
		for(int i=1; i<=6;i++) {
			for(int j=1;j<=7;j++) {
				if(i==1 && j<startDayIdx) {
					custCal[i-1][j-1] = 0;
				}else if(i==1 && j>=startDayIdx) {
					custCal[i-1][j-1] = dayCnt;
					dayCnt++;
				}else if(i>1 && dayCnt<=lastDay) {
					custCal[i-1][j-1] = dayCnt;
					dayCnt++;
				}				
			}
		}
		
		for(int i=0; i<6; i++) {
			for(int j=0; j<7; j++) {
				if(day == custCal[i][j]) {
					dayIndex[0] = i+1;
					dayIndex[1] = j+1;
					break;
				}
			}
		}
		
		return dayIndex;
	}
}
