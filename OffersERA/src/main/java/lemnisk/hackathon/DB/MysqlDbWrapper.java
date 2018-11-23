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

  static {
	  dbDriver ="com.mysql.jdbc.Driver";
    try {
      Class.forName(dbDriver).newInstance();
    } catch (Exception e) {
     System.out.println("Error in connecting to DB. Exiting..."+e);
      System.exit(2);
    }
  }
  
  public static void runQuery(String query) {
	  	Connection conn = null;
	    ResultSet resultSet = null;
	    Statement statement = null;
	    try {
	    	conn = DriverManager.getConnection("jdbc:mysql://10.20.0.4:3306/hackathon","root","hackathon");
	      if (conn != null) {
	    	  System.out.println("Database connection established.");
	    	  statement = conn.createStatement();
	    	  System.out.println(query);
	    	  statement.executeUpdate(query);

	      } 
	      else {
	       System.out.println("Database connection could not be established for getting result for query: {} "+query);
	      }
	    } catch (Exception e) {
	     System.out.println("Error in getting result for query: {} "+query+" "+e);
	    } 
  }
  
  public static List<Map<String, Object>> getResult(String query) {
    Connection conn = null;
    ResultSet resultSet = null;
    Statement statement = null;
    List<Map<String, Object>> list = null;

    try {
    	conn = DriverManager.getConnection("jdbc:mysql://10.20.0.4:3306/hackathon","root","hackathon");
      if (conn != null) {
       System.out.println("Database connection established.");
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
    } finally {
      if (conn != null) {
        try {
          if (resultSet != null) {
            resultSet.close();
          }
          if (statement != null) {
            statement.close();
          }
          conn.close();
         System.out.println("Database connection terminated for query: {}"+query);
        } catch (Exception e) {
         System.out.println("Error closing database connection for query: {} "+query+" "+e);
        }
      }
    }
    if (list != null) {
     System.out.println("Returning resultset of size: {} "+list.size());
    } else {
     System.out.println("Returning resultset of size: 0");
    }
    return list;
  }

  public static void insertToCampaignCards(String CampaignID, String CardType,String CardName) {
	  Connection conn = null;
	    PreparedStatement preparedStatement = null;
	    String query ="Insert into CampaignCards(CampaignID,CardType,CardName) values (?,?,?)";

	    try {	
	    	conn = DriverManager.getConnection("jdbc:mysql://10.20.0.4:3306/hackathon","root","hackathon");
	        if (conn != null) {
	        	System.out.println("yess");
	        }
	      preparedStatement = conn.prepareStatement(query);
	      preparedStatement.setString(1, CampaignID);
	      preparedStatement.setString(2, CardType);
	      preparedStatement.setString(3, CardName);
	      preparedStatement.executeUpdate();
	    } catch (SQLException ex) {
	    System.out.println("Error in getting result for query: {} "+query+" "+ex);
	    } finally {
	      if (conn != null) {
	        try {
	          if (preparedStatement != null) {
	            preparedStatement.close();
	          }
	          conn.close();
	         System.out.println("Database connection terminated for query: {} "+query);
	        } catch (Exception e) {
	         System.out.println("Error closing database connection for query: {} "+query+" "+e);
	        }
	      }
	    } 
  }
  
  public static void insertToCardDetails(Integer CampaignCardID, String CardName, String PlaceOfOffer, int Amount, Integer OfferPercentage, String StartDate,
      String EndDate, String Message, String OfferDays, String OfferStartTime,String OfferEndTime,Integer MaxUsage) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String query =
        "Insert into CardDetails(Id,CampaignCardID,CardName,PlaceOfOffer,Amount,OfferPercentage,StartDate,EndDate,Message,OfferDays,OfferStartTime,OfferEndTime,MaxUsage" + 
        ") values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

    try {
      conn = DriverManager.getConnection("jdbc:mysql://10.20.0.4/hackathon","root","hackathon");
      preparedStatement = conn.prepareStatement(query);
      preparedStatement.setInt(1, CampaignCardID);
      preparedStatement.setString(2, CardName);
      preparedStatement.setString(3, PlaceOfOffer);
      preparedStatement.setInt(4, Amount);
      preparedStatement.setInt(5, OfferPercentage);
      preparedStatement.setString(6, StartDate);
      preparedStatement.setString(7, EndDate);
      preparedStatement.setString(8, Message);
      preparedStatement.setString(9, OfferDays);
      preparedStatement.setString(10, OfferStartTime);
      preparedStatement.setString(11, OfferEndTime);
      preparedStatement.setInt(12, MaxUsage);

      preparedStatement.executeUpdate();

    } catch (SQLException ex) {
    System.out.println("Error in getting result for query: {} "+query+" "+ex);
    } finally {
      if (conn != null) {
        try {
          if (preparedStatement != null) {
            preparedStatement.close();
          }
          conn.close();
         System.out.println("Database connection terminated for query: {} "+query);
        } catch (Exception e) {
         System.out.println("Error closing database connection for query: {} "+query+" "+e);
        }
      }
    }
  }
}
