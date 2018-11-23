package lemnisk.hackathon.OffersData;

import java.util.List;
import java.util.Map;

import lemnisk.hackathon.DB.MysqlDbWrapper;

public class DataPushToDB {
	
	public static void  pushToCampaignCardsAndCardDetails(OffersDataClass obj) {
		int id=getId(obj);
		if(id==-1) {
			MysqlDbWrapper.insertToCampaignCards(obj.getCampaignId(), obj.getCardType(), obj.getCardName());
			int insertedId=getId(obj);
			MysqlDbWrapper.insertToCardDetails(insertedId, obj.getCardName(), obj.getPlaceOfOffer(), obj.getAmount(), obj.getOfferPercentage(), obj.getStartDate(), obj.getEndDate(), obj.getMessage(), obj.getOfferDays(), obj.getOfferStartTime(), obj.getOfferEndTime(), obj.getMaxUsage());
		}
		else {
			MysqlDbWrapper.insertToCardDetails(id, obj.getCardName(), obj.getPlaceOfOffer(), obj.getAmount(), obj.getOfferPercentage(), obj.getStartDate(), obj.getEndDate(), obj.getMessage(), obj.getOfferDays(), obj.getOfferStartTime(), obj.getOfferEndTime(), obj.getMaxUsage());
		}
	}
	
	private  static int getId(OffersDataClass obj) {
		String query="SELECT Id From CampaignCards where CardType='"+obj.getCardType()+"' and CampaignId='"+obj.getCampaignId()+"' and CardName='"+obj.getCardName()+"'";
		int Id=-1;
		try {
			List<Map<String, Object>> result=MysqlDbWrapper.getResult(query);
			if(result.size()==0) {
				return -1;
			}
			else {
				Id=(Integer)result.get(0).get("Id");
				return Id;
			}	
		} catch (NullPointerException e) {
			// TODO: handle exception
			System.out.println("Error in retrieving Last Inserted Id");
			return -1;
		}
	}
	
}
