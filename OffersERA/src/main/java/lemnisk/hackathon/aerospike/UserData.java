package lemnisk.hackathon.aerospike;

import java.util.ArrayList;
import java.util.List;

public class UserData {
	
	private String hashedEmailId;
	private String Name;
	private List<String> cardNames = new ArrayList<String>();
	
	public String getHashedEmailId() {
		return hashedEmailId;
	}
	public void setHashedEmailId(String hashedEmailId) {
		this.hashedEmailId = hashedEmailId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public List<String> getCardNames() {
		return cardNames;
	}
	public void setCardNames(String card) {
		this.cardNames.add(card);
	}

}
