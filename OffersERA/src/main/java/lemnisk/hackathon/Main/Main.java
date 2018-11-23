package lemnisk.hackathon.Main;

import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTime;
import lemnisk.hackathon.OffersData.CsvDataProcess;
import lemnisk.hackathon.OffersData.OffersDataCrawler;

public class Main {
	
	private static String timePattern = "HH:mm:ss";
	private static String datePattern = "yyyy-MM-dd";
	public static final Map<String,String> dayNameMap= new HashMap<String,String>();
	
	public static String returnDay(int i) {
		String res="day"+i;
		return res;
	}
	public static void main(String []args) {
		dayNameMap.put("day1","MON");
		dayNameMap.put("day2","TUE");
		dayNameMap.put("day3","WED");
		dayNameMap.put("day4","THU");
		dayNameMap.put("day5","FRI");
		dayNameMap.put("day6","SAT");
		dayNameMap.put("day7","SUN");
		
		
		//Phase 1 -> Moving Data from CSV to DB
//		CsvDataProcess csvDataProcess=new CsvDataProcess();
//		csvDataProcess.csvDataProcessFunction("/home/paavan/Desktop/hdfctab");
//		csvDataProcess.populateDB();
		
		// phase 2--> Getting relevant Data from the DB and store it in a DataStructure
		// DataStructure will have key as campaignId_CardType_CardName and mapped to other offer details
		DateTime currentDateTime=new DateTime("2018-11-27");
		String currentDate=currentDateTime.toString(datePattern);
		String currentDay=dayNameMap.get("day"+currentDateTime.getDayOfWeek());
		OffersDataCrawler.relavantDataExtracter(currentDay, currentDate);
		System.out.println(OffersDataCrawler.presentDayOffersAndDetailsMap.keySet());;
		// phase 3 --> Getting relavant User Profiles from the Db and store it in a DataStructure
		
		
	}
}
