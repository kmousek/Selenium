package Common;

public class Calendar1 {
	
	public void createCal(int year, int month) {
		
		int[][] cal = new int[6][7];
		
		//출력
		String[] strWeek= {"일","월","화","수","목","금","토"};
		System.out.println(year+"년도 "+month+"월");
		   
		for(String week:strWeek){
			System.out.print(week+"\t");
		}
       
		int total=(year-1)*365
					+(year-1)/4
					-(year-1)/100
					+(year-1)/400;
       
		System.out.println("total : " + total);
		
       //전달
		int[] lastDay= {31,28,31,30,31,30,31,31,30,31,30,31};
		if((year%4==0 && year%100!=0)||(year%400==0)) { //윤년 => 2월 29일
			lastDay[1]=29;
		}else {
			lastDay[1]=28;
		}
		
		for(int i=0;i<month-1;i++){
			total+=lastDay[i];
		}
		//1일자의 요일
		total++;
		
		System.out.println("total1 : " + total);
       
		int week=total%7;
		System.out.println("week : " + week);
		//달력 출력
		System.out.println();
		
		int weekCnt = 0;
		
		for(int i=1;i<=lastDay[month-1];i++){
			if(i==1){
				for(int j=0;j<week;j++){
					System.out.print("\t");
					cal[i-1][j] = 0;
				}
			}
			System.out.printf("%2d\t",i);
			
			cal[weekCnt][i] = i;
			
//			System.out.println("i : " + i);
//			System.out.println("week " + week);
			
			week++;
			weekCnt++;
			if(week>6){
				week=0;
				System.out.println();
			}
		}		
	}
}
