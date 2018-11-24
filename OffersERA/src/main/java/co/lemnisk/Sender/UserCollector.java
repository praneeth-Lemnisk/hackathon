package co.lemnisk.Sender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import lemnisk.hackathon.aerospike.AerospikeDBClient;
import lemnisk.hackathon.aerospike.AerospikeDBConfig;

public class UserCollector {
	
	private static AerospikeDBConfig aeroConfig = new AerospikeDBConfig("10.20.0.6", 13000, "hackathon");
	private static Map<String,List<String>> map = new HashMap<String,List<String>>();
	//private static Map<String,String[]> map = new HashMap<String,String[]>();
	
	public static void getUsers()
	{
		
		aeroConfig.setTable("userFeed", "3625");
		AerospikeDBClient aerospikeClient = AerospikeDBClient.getInstance(aeroConfig);
		Map<String,String> data = aerospikeClient.scanAll();
		
		System.out.println(data);
		extractData(data, "3625");
		
		aeroConfig.setTable("userFeed", "4234");
		aerospikeClient = AerospikeDBClient.getInstance(aeroConfig);
		Map<String,String> data1 = aerospikeClient.scanAll();
		
		extractData(data1, "4234");
		
		
	}
	
	public static void extractData(Map<String,String> data,String campaignId)
	{
		
		Set<String> keySet = data.keySet();
		
		for(String email : keySet)
		{
			System.out.println(data.get(email));
			JSONObject js = new JSONObject(data.get(email));
			JSONObject nba = js.getJSONObject("nba");
			JSONArray ja = nba.getJSONArray("cc");
			for(int i=0;i<ja.length();i++)
			{
				String key = campaignId+"_"+ja.getString(i);
				if(map.containsKey(key))
				{
					List<String> temp = map.get(key);
					temp.add(email);					
					map.put(key, temp);
					
				}
				else
				{
					List<String> temp = new ArrayList<String>();
					map.put(key, temp);
				}				
		
			}			
		
		}
		
		
	}
	
	
	public static Map<String,List<String>> getMap()
	{
		getUsers();
		return map;
		
	}

}
