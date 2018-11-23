package lemnisk.hackathon.OffersData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import lemnisk.hackathon.DB.MysqlDbWrapper;

public class CollectClientData {
	
	public static List<OffersDataClass> getData(String url, String advid)
	{
		List<OffersDataClass> productData = new ArrayList<OffersDataClass>();
		OffersDataClass offerDetails;
		
		String response = ApiData.sendGetRequest(url);
		
		if(response!=null && response!="")
		{
			JSONObject js = new JSONObject(response);
			if(js.has("credit-card"))
			{
				JSONObject js1 = js.getJSONObject("credit-card");
				Set<String> creditCardTypes = js1.keySet(); 
				for(String S : creditCardTypes)
				{
					JSONObject vendorOffers = js1.getJSONObject(S);
					Set<String> vendorsSet = vendorOffers.keySet();
					for(String vendor:vendorsSet)
					{
						System.out.println("VendorName = "+vendor);
						offerDetails = new OffersDataClass();
						JSONObject vendorDetails = vendorOffers.getJSONObject(vendor);
						offerDetails.setCampaignId(advid);
						offerDetails.setCardType("Credit-Card");
						offerDetails.setCardName(S);
						offerDetails.setPlaceOfOffer(vendor);
						offerDetails.setAmount(vendorDetails.getString("amt"));
						offerDetails.setOfferPercentage(vendorDetails.getString("offerPer"));
						offerDetails.setStartDate(vendorDetails.getString("StartDate"));
						offerDetails.setEndDate(vendorDetails.getString("EndDate"));
						offerDetails.setMessage(vendorDetails.getString("Message"));
						offerDetails.setOfferDays(vendorDetails.getString("DayOfWeek"));
						offerDetails.setOfferStartTime(vendorDetails.getString("OfferStartTime"));
						offerDetails.setOfferEndTime(vendorDetails.getString("OfferEndTime"));
						offerDetails.setMaxUsage(vendorDetails.getString("maxTimes"));
						productData.add(offerDetails);						
					}
					
				}
				
			}
			
		}
		
		return productData;
		
	}
	
	
	public static void main(String args[])
	{
		List<OffersDataClass> productData = new ArrayList<OffersDataClass>();
		
		String campaignQuery = "select Id,CampaignName,CampaignDataUrl from Campaign";
		
		List<Map<String,Object>> queryResult = MysqlDbWrapper.getResult(campaignQuery);
		
		for(int i=0;i<queryResult.size();i++)
		{
			Map<String,Object> temp = queryResult.get(i);
			List<OffersDataClass> tempData = getData((String)temp.get("CampaignDataUrl"),Integer.toString((int)temp.get("Id")));			
			for(OffersDataClass temp1 : tempData)
			{
				productData.add(temp1);
			}
		}
		
		System.out.println("ProductData = "+productData);
		
		String resetQuery="delete from CardDetails";
		MysqlDbWrapper.runQuery(resetQuery);
		resetQuery="Alter table CardDetails AUTO_INCREMENT=1";
		MysqlDbWrapper.runQuery(resetQuery);
		resetQuery="delete from CampaignCards";
		MysqlDbWrapper.runQuery(resetQuery);
		resetQuery="Alter table CampaignCards AUTO_INCREMENT=1";
		MysqlDbWrapper.runQuery(resetQuery);
		
		for(OffersDataClass offerData : productData)
		{
			DataPushToDB.pushToCampaignCardsAndCardDetails(offerData);
			
		}
		
		
	}

}
