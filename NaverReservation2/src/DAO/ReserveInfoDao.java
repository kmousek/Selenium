package DAO;

public class ReserveInfoDao {
	String url;
	String courtName;
	Boolean satFlag = false;
	Boolean sunFlag = false;
	
//	String calCol;
//	String calRow;
	final String xPathCalDay1 = "//*[@id=\"calendar\"]/table/tbody[1]/tr[";
	String xPathCalDay2_calCol;	//calCol 매핑
	final String xPathCalDay3 = "]/td[";
	String xPathCalDay4_calRow;	//calRow 매핑
	final String xPathCalDay5 = "]";
	
	
	
//	String amPmFlag;  //1:AM, 2:PM
//	String timeFlag;  //1:12:00, 2:1:00, 3:2:00, 4:3:00, 5:4:00, 6:5:00, 7:6:00, 8:7:00, 9:8:00, 10:9:00, 11:10:00, 12:11:00
	final String xPathTime1 = "//*[@id=\"container\"]/bk-freetime/div[2]/bk-select-time-schedule/div/div[2]/div[";
	String xPathTime2_amPmFlag;
	final String xPathTime3 = "]/ul/li[";
	String xPathTime4_timeFlag;
	final String xPathTime5 = "]/a";

	public String getXpathCalDay() {
		return this.xPathCalDay1+this.xPathCalDay2_calCol+this.xPathCalDay3+this.xPathCalDay4_calRow+this.xPathCalDay5;
	}
	
	public String getXpathTime() {
		return this.xPathTime1+this.xPathTime2_amPmFlag+this.xPathTime3+this.xPathTime4_timeFlag+this.xPathTime5;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public Boolean getSatFlag() {
		return satFlag;
	}
	public void setSatFlag(Boolean satFlag) {
		this.satFlag = satFlag;
	}
	public Boolean getSunFlag() {
		return sunFlag;
	}
	public void setSunFlag(Boolean sunFlag) {
		this.sunFlag = sunFlag;
	}
	public String getxPathCalDay2_calCol() {
		return xPathCalDay2_calCol;
	}
	public void setxPathCalDay2_calCol(String xPathCalDay2_calCol) {
		this.xPathCalDay2_calCol = xPathCalDay2_calCol;
	}
	public String getxPathCalDay4_calRow() {
		return xPathCalDay4_calRow;
	}
	public void setxPathCalDay4_calRow(String xPathCalDay4_calRow) {
		this.xPathCalDay4_calRow = xPathCalDay4_calRow;
	}
	public String getxPathTime2_amPmFlag() {
		return xPathTime2_amPmFlag;
	}
	public void setxPathTime2_amPmFlag(String xPathTime2_amPmFlag) {
		this.xPathTime2_amPmFlag = xPathTime2_amPmFlag;
	}
	public String getxPathTime4_timeFlag() {
		return xPathTime4_timeFlag;
	}
	public void setxPathTime4_timeFlag(String xPathTime4_timeFlag) {
		this.xPathTime4_timeFlag = xPathTime4_timeFlag;
	}

	@Override
	public String toString() {
		return "ReserveInfoDao [url=" + url + ", courtName=" + courtName + ", satFlag=" + satFlag + ", sunFlag="
				+ sunFlag + ", xPathCalDay1=" + xPathCalDay1 + ", xPathCalDay2_calCol=" + xPathCalDay2_calCol
				+ ", xPathCalDay3=" + xPathCalDay3 + ", xPathCalDay4_calRow=" + xPathCalDay4_calRow + ", xPathCalDay5="
				+ xPathCalDay5 + ", xPathTime1=" + xPathTime1 + ", xPathTime2_amPmFlag=" + xPathTime2_amPmFlag
				+ ", xPathTime3=" + xPathTime3 + ", xPathTime4_timeFlag=" + xPathTime4_timeFlag + ", xPathTime5="
				+ xPathTime5 + "]";
	}
}
