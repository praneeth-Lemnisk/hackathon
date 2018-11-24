package lemnisk.hackathon.test;

import java.util.Map;

import org.json.JSONObject;

import lemnisk.hackathon.aerospike.AerospikeDBClient;
import lemnisk.hackathon.aerospike.AerospikeDBConfig;
import lemnisk.hackathon.aerospike.UserData;

public class Testing {

	public static void main(String[] args) throws Exception {
		
		AerospikeDBConfig aeroConfig = new AerospikeDBConfig("10.20.0.6", 13000, "hackathon");
		aeroConfig.setTable("userFeed", "3625");
		AerospikeDBClient aerospikeClient = AerospikeDBClient.getInstance(aeroConfig);
		/*String nba = "{\"nba\":{\"n\":\"jaydev Acharya\",\"cc\":[\"MoneyBack\",\"Diners-Card\"]}}";
		String nba1 = "{\"nba\":{\"n\":\"jaydev Acharya\",\"cc\":[\"rewards-card\"]}}}";
		aerospikeClient.writeKey("abcde", nba1);
		//Map<String,Object> data = aerospikeClient.getCompleteValue("abcde");
		//System.out.println(data);
		Map<String,String> data = aerospikeClient.scanAll();
		System.out.println(data);*/
		
		boolean val = aerospikeClient.deleteElement("abcdefghi9");
		System.out.println(val);
		
		
	}
	
	public static void test(UserData user)
	{
		
			JSONObject nbaJson = new JSONObject();
			JSONObject nba = new JSONObject();
			nba.put("n", user.getName());
			nba.put("cc", user.getCardNames());
			nbaJson.put("nba", nba);
		System.out.println(nbaJson.toString());
		
	}

}
