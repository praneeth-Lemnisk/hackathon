package co.lemnisk.Sender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


import lemnisk.hackathon.OffersData.OffersDataClass;
import lemnisk.hackathon.OffersData.OffersDataCrawler;

public class MessageBuilder {
	public static Map<String,String[]> userData=new HashMap<String,String []>();
	public static Map<String,Map<String,OffersDataClass>> presentDayOffersAndDetailsMap=OffersDataCrawler.presentDayOffersAndDetailsMap;
	public static Map<String,List<Map<String, OffersDataClass>>> userEmailMap=new HashMap<String,List<Map<String, OffersDataClass>>>(); 
	
	static {
		String str[]={"praneethkatragadda1997@gmail.com","paavan.praneeth@lemnisk.co"};
		userData.put("3625_DINERS-CARD",str);
		userData.put("4234_REWARDS-CARD",str);
		userDataBuilder();
		messageBuilder();
	}
	public static void userDataBuilder() {
		
		for(Map.Entry<String, String[]> entry:userData.entrySet()) {
			String []recepients=entry.getValue();
			System.out.println(entry.getKey().toUpperCase());
			Map<String, OffersDataClass> obj=presentDayOffersAndDetailsMap.get(entry.getKey().toUpperCase());
			for(int i=0;i<recepients.length;i++) {
				if(obj==null) {
					break;
				}
				else if(userEmailMap.containsKey(recepients[i])) {
					List<Map<String, OffersDataClass>>list=new ArrayList<Map<String,OffersDataClass>>();
					list=userEmailMap.get(recepients[i]);
					list.add(obj);
					System.out.println(recepients[i]+" "+list.toString());
					userEmailMap.put(recepients[i],list);
				}
				else {
					List<Map<String, OffersDataClass>>list1=new ArrayList<Map<String,OffersDataClass>>();
					list1.add(obj);
					System.out.println(recepients[i]+" "+list1.toString());
					userEmailMap.put(recepients[i],list1);
				}
			}
		}
	}
	
	public static void messageBuilder() {

		for(Map.Entry<String,List<Map<String, OffersDataClass>>> entry:userEmailMap.entrySet()) {
			String recepient=entry.getKey();
			List<Map<String, OffersDataClass>> OfferList=entry.getValue();
			String messageToSend="";
			for(int i=0;i<OfferList.size();i++) {
				Map<String, OffersDataClass> map=OfferList.get(i);
				for(Map.Entry<String,OffersDataClass> entry2:map.entrySet()) {
					OffersDataClass obj=entry2.getValue();
					messageToSend=messageToSend+"\n"+obj.getMessage()+". Offer Amount is "+obj.getAmount()+"\n";
				}
			}
			try {
//				SendEmail.SendMail(messageToSend,recepient);
				System.out.println(messageToSend+" "+recepient);
				System.out.println();
				System.out.println();
				System.out.println();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
}
