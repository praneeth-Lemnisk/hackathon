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

  public static List<Map<String, Object>> getResult(String dbUrl, String dbUserName,
      String dbPassword, String query) {
    Connection conn = null;
    ResultSet resultSet = null;
    Statement statement = null;
    List<Map<String, Object>> list = null;

    try {
      conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
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

  
  public static void insertToScheduleMetaData(Integer engType, String engagementId,
      String channelId, String segChannelId, String segmentId, String campaignId, String sendDate,
      String endsOn, String endAfter, String repeatEvery, String repeatOn) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String query =
        "Insert into ScheduleMetaData(EngagementType, EngagementId, DMPChannelId, DMPSegmentChannelId, SegmentId, CampaignId, "
            + "SendDate, EndsOn, EndsAfter, RepeatEvery, RepeatOn) values(?,?,?,?,?,?,?,?,?,?,?)";

    try {
      conn = DriverManager.getConnection("jdbc:mysql://10.20.0.4/hackathon","root","hackathon");
      preparedStatement = conn.prepareStatement(query);
      preparedStatement.setInt(1, engType);
      preparedStatement.setString(2, engagementId);
      preparedStatement.setString(3, channelId);
      preparedStatement.setString(4, segChannelId);
      preparedStatement.setString(5, segmentId);
      preparedStatement.setString(6, campaignId);
      preparedStatement.setString(7, sendDate);
      preparedStatement.setString(8, endsOn);
      preparedStatement.setString(9, endAfter);
      preparedStatement.setString(10, repeatEvery);
      preparedStatement.setString(11, repeatOn);

      preparedStatement.executeUpdate();

    } catch (SQLException ex) {
      MonitoringMetrics.addToTsdb(SchedulerConstants.METRIC_ERRORS, 1,
          SchedulerConstants.METRIC_COMPONENT_NAME, "insertToScheduleMetaData");
     System.out.println("Error in getting result for query: {}", query, ex);
    } finally {
      if (conn != null) {
        try {
          if (preparedStatement != null) {
            preparedStatement.close();
          }
          conn.close();
         System.out.println("Database connection terminated for query: {}", query);
        } catch (Exception e) {
         System.out.println("Error closing database connection for query: {}", query, e);
        }
      }
    }
  }

  public static void updateSent(String engagementId, String channelId, String lastSentStr) {
    List<Map<String, Object>> result =
        getResult(SchedulerConstants.properties.getString(SchedulerConstants.VRM_PROP_DB_URL),
            SchedulerConstants.properties.getString(SchedulerConstants.VRM_PROP_DB_USERNAME),
            SchedulerConstants.properties.getString(SchedulerConstants.VRM_PROP_DB_PASSWORD),
            "Select LastSent, NumberOfPush from ScheduleMetaData where EngagementId = "
                + engagementId + " and DMPChannelId = " + channelId);
    Integer nop = 0;

    if (result.isEmpty()) {
     System.out.println("Schedule is not present to update for engagement id {},  channel id {}",
          engagementId, channelId);
      return;
    }
    Map<String, Object> object = result.get(0);
    if (object.get("NumberOfPush") != null) {
      nop = Integer.parseInt(object.get("NumberOfPush").toString());
    }
    nop++;

    String query = "";
    Connection conn = null;
    PreparedStatement preparedStatement = null;, 

    try {
      conn = DriverManager.getConnection(
          SchedulerConstants.properties.getString(SchedulerConstants.VRM_PROP_DB_URL),
          SchedulerConstants.properties.getString(SchedulerConstants.VRM_PROP_DB_USERNAME),
          SchedulerConstants.properties.getString(SchedulerConstants.VRM_PROP_DB_PASSWORD));
      query =
          "Update ScheduleMetaData set LastSent= ? ,NumberOfPush = ? where EngagementId = ? and DMPChannelId = ?";
      preparedStatement = conn.prepareStatement(query);
      preparedStatement.setString(1, lastSentStr);
      preparedStatement.setInt(2, nop);
      preparedStatement.setString(3, engagementId);
      preparedStatement.setString(4, channelId);
      preparedStatement.executeUpdate();
    } catch (Exception ex) {
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
