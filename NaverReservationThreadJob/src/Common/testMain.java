package Common;

import java.util.List;

import Common.AES256;

public class testMain {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
//암호화 복호화		
//		AES256 aes256 = new AES256();
//		String text = "";
//		String cipherText = aes256.encrypt(text);
//		
//		System.out.println("cipherText : " + cipherText);
		
		//RUoRS0lyf9m8sWs9hk3kWg==
		//GBRoWwdfulZHKg3AiNCr9w==

		
//File Read test
//		String filePath = "D:/100-devTools/yangjae.sql";
//		FileFunc f = new FileFunc();
//		List rList = f.fileRead(filePath);
//		
//		for(Object object : rList) {
//			System.out.println(object);
//		}
		
		
//달력
		SearchCalenderDayIndex c = new SearchCalenderDayIndex();
		int[] dayIndex = new int[2];
		dayIndex = c.getDayIndex(2021, 10, 11);
		
		for(int i=0;i<dayIndex.length;i++) {
			System.out.println(dayIndex[i]);
		}
		
	}

}


