package lemnisk.hackathon.UserCrawler;

import org.json.JSONObject;
import org.json.simple.JSONArray; 
import org.json.simple.parser.*; 

public class UserDataCrawler {
	private static JSONObject AerospikeData=new JSONObject();
	public static void main(String[] args)
	{
		String str = "{\"3625\":\"{\\\"nba\\\":\\\"{\\\"n\\\":\\\"Jaydev Acharya\\\",\\\"cc\\\":[\\\"MoneyBack\\\",\\\"Diners-Card\\\"]}\\\"}\",\"4234\":\"{\\\"nba\\\":\\\"{\\\"n\\\":\\\"Jaydev Acharya\\\",\\\"cc\\\":[\\\"rewards-card\\\"]}\\\"}\"}";
		
		JSONObject js = new JSONObject(str);
		
		System.out.println(js.getString("3625"));
		
		String data = js.getString("3625");
		
		JSONObject js1 = new JSONObject(data);
//		System.out.println(js1.getString("nba"));
		
	}

}
