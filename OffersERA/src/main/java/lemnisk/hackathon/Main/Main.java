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
		dayNameMap.put("day1","MONDAY");
		dayNameMap.put("day2","TUESDAY");
		dayNameMap.put("day3","WEDNESDAY");
		dayNameMap.put("day4","THURSDAY");
		dayNameMap.put("day5","FRIDAY");
		dayNameMap.put("day6","SATURDAY");
		dayNameMap.put("day7","SUNDAY");
		
		
		//Phase 1 -> Moving Data from CSV to DB
		CsvDataProcess csvDataProcess=new CsvDataProcess();
		csvDataProcess.csvDataProcessFunction("/home/paavan/Desktop/hdfctab");
		csvDataProcess.populateDB();
		
		// phase 2--> Getting relevant Data from the DB and store it in a DataStructure
//		DateTime currentDateTime=DateTime.now();
//		String currentDate=currentDateTime.toString(datePattern);
//		String currentDay="MON";//dayNameMap.get("day"+currentDateTime.getDayOfWeek());
//		OffersDataCrawler.relavantDataExtracter(currentDay, currentDate);
	}
}
