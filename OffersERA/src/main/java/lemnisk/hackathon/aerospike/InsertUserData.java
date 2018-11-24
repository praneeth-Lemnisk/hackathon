package lemnisk.hackathon.aerospike;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class InsertUserData {
	public static AerospikeDBConfig aeroConfig = new AerospikeDBConfig("10.20.0.6", 13000, "hackathon");
	
	public static void main(String[] args) throws Exception
	{
		String hdfcNbaData = "/home/jaydevacharya/Hackathon/hdfcNbaOriginal.csv";
		String citiNbaData = "/home/jaydevacharya/Hackathon/citiNbaOriginalName.csv";
		
		List<UserData> userData = processCsvData(hdfcNbaData);		
		insertIntoAerospike("3625", userData);		
		List<UserData> userData1 = processCsvData(citiNbaData);		
		insertIntoAerospike("4234", userData1);
		
	}
	
	public static void insertIntoAerospike(String binName,List<UserData> userData) throws Exception
	{
		
		aeroConfig.setTable("userFeed", binName);
		AerospikeDBClient aerospikeClient = AerospikeDBClient.getInstance(aeroConfig);
		
		for(UserData user : userData)
		{
			JSONObject nbaJson = new JSONObject();
			JSONObject nba = new JSONObject();
			nba.put("n", user.getName());
			nba.put("cc", user.getCardNames());
			nbaJson.put("nba", nba);
			
			aerospikeClient.writeKey(user.getHashedEmailId(), nbaJson.toString());
			
		}
		
	}
	
	public static List<UserData> processCsvData(String file)
	{
		List<UserData> userDataList = new ArrayList<UserData>();
		
		String cvsSplitBy = "	";
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file)); 
	    	String line;
	    	//String[] userData=line.split(cvsSplitBy);//omitting Headings from csv
	    	// Data insertion
	    	while ((line = br.readLine()) != null) {
	    		String[] userData = line.split(cvsSplitBy);
	    		System.out.println(userData.length);
	    		UserData temp = new UserData();
	    		System.out.println(userData[0]);
	    		temp.setHashedEmailId(userData[0]);
	    		temp.setName(userData[1]);
	    		for(int i=2;i<userData.length;i++)
	    		{
	    			System.out.println(userData[i]);
	    			temp.setCardNames(userData[i]);
	    			
	    		}
	    		userDataList.add(temp);	    	
	    	}
        br.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }	
		
		return userDataList;
    	
	}

}
