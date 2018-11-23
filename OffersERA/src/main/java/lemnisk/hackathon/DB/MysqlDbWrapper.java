package lemnisk.hackathon.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlDbWrapper {
	
  private static String dbDriver;
  private static Connection conn=null;
  static {
	  dbDriver ="com.mysql.jdbc.Driver";
    try {
    	conn = DriverManager.getConnection("jdbc:mysql://10.20.0.4:3306/hackathon","root","hackathon");
    	 if(conn!=null) {
    		 System.out.println("Database connection established");
    	 }
	      else {
	       System.out.println("Database connection could not be established");
	      }
    	Class.forName(dbDriver).newInstance();
    } catch (Exception e) {
     System.out.println("Error in connecting to DB. Exiting..."+e);
      System.exit(2);
    }
  }
  
  public static void runQuery(String query) {
	    ResultSet resultSet = null;
	    Statement statement = null;
	    try {
	      if (conn != null) {
	    	  statement = conn.createStatement();
	    	  System.out.println(query);
	    	  statement.executeUpdate(query);
	      }
	    } catch (Exception e) {
	     System.out.println("Error in getting result for query: {} "+query+" "+e);
	    } 
  }
  
  public static List<Map<String, Object>> getResult(String query) {
    ResultSet resultSet = null;
    Statement statement = null;
    List<Map<String, Object>> list = null;

    try {
      if (conn != null) {
    	  statement = conn.createStatement();
    	  System.out.println(query);
    	  resultSet = statement.executeQuery(query);

	      resultSet = statement.getResultSet();
	      ResultSetMetaData resultMetadata = resultSet.getMetaData();
	      int columns = resultMetadata.getColumnCount();
	      list = new ArrayList<Map<String, Object>>();

        while (resultSet.next()) {
          HashMap<String, Object> row = new HashMap<String, Object>(columns);
          for (int i = 1; i <= columns; i++) {
            row.put(resultMetadata.getColumnLabel(i), resultSet.getObject(i));
          }
          list.add(row);
        }
      } else {
       System.out.println("Database connection could not be established for getting result for query: {} "+query);
      }
    } catch (Exception e) {
     System.out.println("Error in getting result for query: {} "+query+" "+e);
      list = null;
    }
    return list;
  }

  public static void insertToCampaignCards(String CampaignID, String CardType,String CardName) {
		  PreparedStatement preparedStatement = null;
		  String query ="Insert into CampaignCards(CampaignID,CardType,CardName) values (?,?,?)";
		  try {
		  preparedStatement = conn.prepareStatement(query);
	      preparedStatement.setString(1, CampaignID);
	      preparedStatement.setString(2, CardType);
	      preparedStatement.setString(3, CardName);
	      preparedStatement.executeUpdate();
	    } catch (SQLException ex) {
	    System.out.println("Error in getting result for query: {} "+query+" "+ex);
	    } 
  }
  
  public static void insertToCardDetails(Integer CampaignCardID, String CardName, String PlaceOfOffer, String Amount, String OfferPercentage, String StartDate,
    String EndDate, String Message, String OfferDays, String OfferStartTime,String OfferEndTime,String MaxUsage) {
    PreparedStatement preparedStatement = null;
    String query =
        "Insert into CardDetails(CampaignCardID,CardName,PlaceOfOffer,Amount,OfferPercentage,StartDate,EndDate,Message,OfferDays,OfferStartTime,OfferEndTime,MaxUsage" + 
        ") values (?,?,?,?,?,?,?,?,?,?,?,?)";

    try {
      preparedStatement = conn.prepareStatement(query);
      preparedStatement.setInt(1, CampaignCardID);
      preparedStatement.setString(2, CardName);
      preparedStatement.setString(3, PlaceOfOffer);
      preparedStatement.setString(4, Amount);
      preparedStatement.setString(5, OfferPercentage);
      preparedStatement.setString(6, StartDate);
      preparedStatement.setString(7, EndDate);
      preparedStatement.setString(8, Message);
      preparedStatement.setString(9, OfferDays);
      preparedStatement.setString(10, OfferStartTime);
      preparedStatement.setString(11, OfferEndTime);
      preparedStatement.setString(12, MaxUsage);

      preparedStatement.executeUpdate();

    } catch (SQLException ex) {
    	System.out.println("Error in getting result for query: {} "+query+" "+ex);
    }
  }
  
}
