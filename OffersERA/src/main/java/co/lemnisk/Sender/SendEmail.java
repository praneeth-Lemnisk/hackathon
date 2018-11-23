package co.lemnisk.Sender;
/**
 * This call sends an email to one recipient, using a validated sender address
 * Do not forget to update the sender address used in the sample
 */
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Email;
import org.json.JSONArray;
import org.json.JSONObject;

public class SendEmail {
    public static void SendMail(String Message,String To) throws Exception {
      MailjetClient client;
      MailjetRequest request;
      MailjetResponse response;
      client = new MailjetClient("565ee4c2374fff601ea083d2679808ab","ab76fba5001a5d08be16c49121009dec");
      request = new MailjetRequest(Email.resource)
                        .property(Email.FROMEMAIL, "praneethkatragadda@gmail.com")
                        .property(Email.FROMNAME, "OfferEra")
                        .property(Email.SUBJECT, "Hurry!!!Todays Offers")
                        .property(Email.TEXTPART, Message)
                        .property(Email.RECIPIENTS, new JSONArray()
                        .put(new JSONObject()
                        .put("Email", To)));
      response = client.post(request);
      System.out.println(response.getData());
    }
}
