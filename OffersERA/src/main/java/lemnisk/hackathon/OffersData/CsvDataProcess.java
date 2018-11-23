package lemnisk.hackathon.OffersData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lemnisk.hackathon.DB.*;

public class CsvDataProcess 
{
	private static Map<Integer,OffersDataClass> OfferDetails= new HashMap<Integer,OffersDataClass>(); 
	private static int count=1;
	
    public void csvDataProcessFunction(String csvFile) {
	    String cvsSplitBy = "	";// Tab separated file 
	    try {
	    	BufferedReader br = new BufferedReader(new FileReader(csvFile)); 
	    	String line = br.readLine();
	    	String[] productCache=line.split(cvsSplitBy);//omitting Headings from csv
	    	// Data insertion
	    	while ((line = br.readLine()) != null) {
	    		Map<String,String> m=new HashMap<String,String>();
	    		productCache= line.split(cvsSplitBy);
	            OffersDataClass obj=new OffersDataClass();
	            obj.setCampaignId(productCache[0]);
	            obj.setCardType(productCache[1].toUpperCase());
	    		obj.setCardName(productCache[2].toUpperCase());
	            obj.setPlaceOfOffer(productCache[3]);
	            obj.setAmount(productCache[4]);
	            obj.setOfferPercentage(productCache[5]);
	            obj.setStartDate(productCache[6]);
	            obj.setEndDate(productCache[7]);
	            obj.setMessage(productCache[8]);
	            obj.setOfferDays(productCache[9]);
	            obj.setOfferStartTime(productCache[10]);
	            obj.setOfferEndTime(productCache[11]);
	            obj.setMaxUsage(productCache[12]);
	            OfferDetails.put(count, obj);
	            count++;
	        }
	        br.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    }
	
	public void populateDB() {
	    // Clearing DB Before starting the process
		String resetQuery="delete from CardDetails";
		MysqlDbWrapper.runQuery(resetQuery);
		resetQuery="Alter table CardDetails AUTO_INCREMENT=1";
		MysqlDbWrapper.runQuery(resetQuery);
		resetQuery="delete from CampaignCards";
		MysqlDbWrapper.runQuery(resetQuery);
		resetQuery="Alter table CampaignCards AUTO_INCREMENT=1";
		MysqlDbWrapper.runQuery(resetQuery);
		for(int i=1;i<=OfferDetails.size();i++) {
			DataPushToDB.pushToCampaignCardsAndCardDetails(OfferDetails.get(i));
		}
//		String query="Select *from CampaignCards";
//		System.out.println(MysqlDbWrapper.getResult(query));
    }    
    
}
