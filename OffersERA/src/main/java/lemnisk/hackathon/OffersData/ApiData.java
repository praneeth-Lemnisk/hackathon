package lemnisk.hackathon.OffersData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiData {
	
	public static String sendGetRequest(String url)
	{
		System.out.println("request Url="+url);
		String responseData = null;
		List<OffersDataClass> productData= new ArrayList<OffersDataClass>();
		URL obj;
		HttpURLConnection con;
		try {
			obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();					
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if (responseCode == 200) 
			{	
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				System.out.println(response.toString());
				responseData = response.toString();
			} 
			else 
			{
				System.out.println("GET request not worked");
				responseData=null;
			}
		
		} catch (MalformedURLException e) {		
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return responseData;
		
	}
	
	public static void main(String[] args)
	{
		sendGetRequest("http://hdfcbank.mocklab.io/offer-details/CreditCard");
		
	}

}
