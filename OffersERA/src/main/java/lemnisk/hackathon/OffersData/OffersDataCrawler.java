package lemnisk.hackathon.OffersData;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lemnisk.hackathon.DB.MysqlDbWrapper;

public class OffersDataCrawler {
	
	public static List<Map<String, Object>> IdToCampaignMapping=null;
	
	static {
		// TODO Auto-generated constructor stub
		String queryBuild="select distinct(Id),CampaignID from CampaignCards ";
		IdToCampaignMapping=MysqlDbWrapper.getResult(queryBuild);
		System.out.println(IdToCampaignMapping.toString());
	}
	
	public static void relavantDataExtracter(String currentDay,String currentDate) {
		String queryBuild="select *from CardDetails where StartDate <= '"+currentDate+"'  and OfferDays like '%"+currentDay+"%' and EndDate >= '"+currentDate+"'";
		List<Map<String, Object>> resultMap=MysqlDbWrapper.getResult(queryBuild);
		dumpIntoOffersDataClass(resultMap);	
	}
	private static void dumpIntoOffersDataClass(List<Map<String, Object>> relavantDataMap ) {
		Map<Integer,OffersDataClass> map=new HashMap<Integer,OffersDataClass>();
		int count=0;
		for(int i=0;i<relavantDataMap.size();i++) {
			OffersDataClass obj=new OffersDataClass();
			obj.setCampaignId(relavantDataMap.get(0).get("CampaignCardID"));
            obj.setCardType(relavantDataMap.get(1).toUpperCase());
    		obj.setCardName(relavantDataMap.get(2].toUpperCase());
            obj.setPlaceOfOffer(relavantDataMap.get(3]);
            obj.setAmount(Integer.parseInt(relavantDataMap.get(4]));
            obj.setOfferPercentage(Integer.parseInt(relavantDataMap.get(5]));
            obj.setStartDate(relavantDataMap.get(6]);
            obj.setEndDate(relavantDataMap.get(7]);
            obj.setMessage(relavantDataMap.get(8]);
            obj.setOfferDays(relavantDataMap.get(9]);
            obj.setOfferStartTime(relavantDataMap.get(10]);
            obj.setOfferEndTime(relavantDataMap.get(11]);
            obj.setMaxUsage(Integer.parseInt(relavantDataMap.get(12]));
		}
	}
	
}
