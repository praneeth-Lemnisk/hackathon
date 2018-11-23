package lemnisk.hackathon.OffersData;

public class OffersDataClass {
	private  String CampaignId;
	private  String CardType;
	private  String CardName;	
	private  String PlaceOfOffer;	
	private  int Amount; 	
	private  int OfferPercentage;	
	private  String StartDate;
	private  String EndDate;	
	private  String Message;	
	private  String OfferDays;	
	private  String OfferStartTime;	
	private  String OfferEndTime;	
	private  int MaxUsage;
	
	public  String getCardName() {
		return CardName;
	}
	public  void setCardName(String cardName) {
		CardName = cardName;
	}
	public  String getPlaceOfOffer() {
		return PlaceOfOffer;
	}
	public  void setPlaceOfOffer(String placeOfOffer) {
		PlaceOfOffer = placeOfOffer;
	}
	public  int getAmount() {
		return Amount;
	}
	public  void setAmount(int amount) {
		Amount = amount;
	}
	public  int getOfferPercentage() {
		return OfferPercentage;
	}
	public  void setOfferPercentage(int offerPercentage) {
		OfferPercentage = offerPercentage;
	}
	public  String getStartDate() {
		return StartDate;
	}
	public  void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public  String getEndDate() {
		return EndDate;
	}
	public  void setEndDate(String endDate) {
		EndDate = endDate;
	}
	public  String getOfferEndTime() {
		return OfferEndTime;
	}
	public  void setOfferEndTime(String offerEndTime) {
		OfferEndTime = offerEndTime;
	}
	public  int getMaxUsage() {
		return MaxUsage;
	}
	public  void setMaxUsage(int maxUsage) {
		MaxUsage = maxUsage;
	}
	public  String getOfferStartTime() {
		return OfferStartTime;
	}
	public  void setOfferStartTime(String offerStartTime) {
		OfferStartTime = offerStartTime;
	}
	public  String getOfferDays() {
		return OfferDays;
	}
	public  void setOfferDays(String offerDays) {
		OfferDays = offerDays;
	}
	public  String getMessage() {
		return Message;
	}
	public  void setMessage(String message) {
		Message = message;
	}
	public String getCampaignId() {
		return CampaignId;
	}
	public void setCampaignId(String campaignId) {
		CampaignId = campaignId;
	}
	public String getCardType() {
		return CardType;
	}
	public void setCardType(String cardType) {
		CardType = cardType;
	}

}
