package lemnisk.hackathon.OffersData;


import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import lemnisk.hackathon.DB.MysqlDbWrapper;

public class OffersDataCrawler {
	public static Map<String,OffersDataClass> presentDayOffersAndDetailsMap= new HashMap<String,OffersDataClass>();
	public static Map<Integer, String> IdToCampaignMapping=new HashMap<Integer, String>();
	public static Map<Integer, String> IdToCardTypeMapping=new HashMap<Integer, String>();
	
	static {
		// TODO Auto-generated constructor stub
		String queryBuild="select distinct(Id),CampaignID,CardType from CampaignCards ";
		List<Map<String, Object>> res=MysqlDbWrapper.getResult(queryBuild);
		for(int i=0;i<res.size();i++) {
			if(!IdToCampaignMapping.containsKey(Integer.parseInt(res.get(i).get("Id").toString()))) {
				IdToCampaignMapping.put(Integer.parseInt(res.get(i).get("Id").toString()),res.get(i).get("CampaignID").toString());
				IdToCardTypeMapping.put(Integer.parseInt(res.get(i).get("Id").toString()),res.get(i).get("CardType").toString());
			}
		}
		System.out.println(IdToCampaignMapping.toString());
	}
	
	public static void relavantDataExtracter(String currentDay,String currentDate) {
		String queryBuild="select *from CardDetails where StartDate <= '"+currentDate+"'  and OfferDays like '%"+currentDay+"%' and EndDate >= '"+currentDate+"'";
		List<Map<String, Object>> resultMap=MysqlDbWrapper.getResult(queryBuild);
		dumpIntoOffersDataClass(resultMap);	
	}
	private static void dumpIntoOffersDataClass(List<Map<String, Object>> relavantDataMap ) {
		List<OffersDataClass> map=null;
		int count=0;
		for(int i=0;i<relavantDataMap.size();i++) {
			OffersDataClass obj=new OffersDataClass();
			obj.setCampaignId((IdToCampaignMapping.get(Integer.parseInt(relavantDataMap.get(i).get("CampaignCardID").toString()))));
            obj.setCardType((IdToCardTypeMapping.get(Integer.parseInt(relavantDataMap.get(i).get("CampaignCardID").toString()))).toUpperCase());
    		obj.setCardName(relavantDataMap.get(i).get("CardName").toString().toUpperCase());
            obj.setPlaceOfOffer(relavantDataMap.get(i).get("PlaceOfOffer").toString());
            obj.setAmount(relavantDataMap.get(i).get("Amount").toString());
            obj.setOfferPercentage(relavantDataMap.get(i).get("OfferPercentage").toString());
            obj.setStartDate(relavantDataMap.get(i).get("StartDate").toString());
            obj.setEndDate(relavantDataMap.get(i).get("EndDate").toString());
            obj.setMessage(relavantDataMap.get(i).get("Message").toString());
            obj.setOfferDays(relavantDataMap.get(i).get("OfferDays").toString());
            obj.setOfferStartTime(relavantDataMap.get(i).get("OfferStartTime").toString());
            obj.setOfferEndTime(relavantDataMap.get(i).get("OfferEndTime").toString());
            obj.setMaxUsage(relavantDataMap.get(i).get("MaxUsage").toString());
            presentDayOffersAndDetailsMap.put(obj.getCampaignId()+"_"+obj.getCardName()+"_"+obj.getPlaceOfOffer(),obj);
//            System.out.println(relavantDataMap.get(i).toString());
//            System.out.println(obj.getCampaignId()+" "+obj.getCardType()+" "+obj.getCardName());
		}
	}
	
}
