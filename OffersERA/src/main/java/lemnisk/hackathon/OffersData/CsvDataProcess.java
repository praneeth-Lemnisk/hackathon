package lemnisk.hackathon.OffersData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CsvDataProcess 
{
	private static Map<Integer,OffersDataClass> OfferDetails= new HashMap<Integer,OffersDataClass>();
	
    public void csvDataProcessFunction(String csvFile) {
	    String cvsSplitBy = "	";// Tab separated file 
	    try {
	    	BufferedReader br = new BufferedReader(new FileReader(csvFile)); 
	    	String line = br.readLine();
	    	// Reading Headings 
	    	String[] productCache=line.split(cvsSplitBy);
//	    	String CardName="CardName";	
//	    	String PlaceOfOffer="PlaceOfOffer";	
//	    	String Amount="Amount";
//	    	String OfferPercentage="OfferPercentage";
//	    	String StartDate="StartDate";	
//	    	String EndDate="EndDate";	
//	    	String Message="Message";
//	    	String OfferDays="OfferDays";
//	    	String OfferStartTime="OfferStartTime";	
//	    	String OfferEndTime="OfferEndTime";	
//	    	String MaxUsage="MaxUsage";
	    	// Data insertion
	    	int count=1;
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
	
	public static void main(String[] args) {
	    CsvDataProcess a =new CsvDataProcess();
		a.csvDataProcessFunction("/home/paavan/Desktop/hdfctab");
		System.out.println(OfferDetails.size());
    }    
    
}
