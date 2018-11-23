package lemnisk.hackathon.OffersData;

import java.util.List;
import java.util.Map;

import lemnisk.hackathon.DB.MysqlDbWrapper;

public class DataPushToDB {
	
	public static void pushToCampaignCards(OffersDataClass obj) {
		MysqlDbWrapper.insertToCampaignCards(obj.getCampaignId(), obj.getCardType(), obj.getCardName());
//		String query="SELECT LAST_INSERT_ID(Id) From CampaignCards";
//		try {
//			List<Map<String, Object>> result=MysqlDbWrapper.getResult(query);
//			if(result==null) {
//				System.out.println("Error in retrieving Last Inserted Id");
//				return -1; 
//			}
//			else {
//				return (Integer)result.get(0).get("Id");
//			}
//		} catch (NullPointerException e) {
//			// TODO: handle exception
//			System.out.println("Error in retrieving Last Inserted Id");
//			return -1;
//		}
	}
	
}
