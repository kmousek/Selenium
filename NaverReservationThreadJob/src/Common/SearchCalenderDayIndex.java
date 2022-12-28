package Common;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

public class SearchCalenderDayIndex {
	public static int[] getDayIndex(int year, int month, int day) {
		int[] dayIndex = new int[2];
		int[][] custCal = new int[6][7];
		// 1. LocalDate 생성
		LocalDate date = LocalDate.of(year, month, 1);
		// LocalDateTime date = LocalDateTime.of(2021, 12, 25, 1, 15, 20);
//		System.out.println(date); // // 2021-12-25
		// 2. DayOfWeek 객체 구하기
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		// 3. 숫자 요일 구하기
		int dayOfWeekNumber = dayOfWeek.getValue();
		// 4. 숫자 요일 출력
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
