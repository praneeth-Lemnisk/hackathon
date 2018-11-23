package lemnisk.hackathon.DB;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;
import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Host;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.ScanCallback;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.Priority;
import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.client.policy.WritePolicy;
import co.lemnisk.tc.scheduler.common.SchedulerConstants;
import co.lemnisk.tc.scheduler.metrics.MonitoringMetrics;

public class Aerospike {
  private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Aerospike.class);

  private static String serverAddress;
  private static int serverPort;
  private static Host[] serverAddressArray;
  private static int ttl;
  private static int defaultTimeOut;
  private static AerospikeClient client;
  private static WritePolicy ttlWritePolicy;
  private static Object sync = new Object();
  private static String namespace;
  private static String webpushbin, smsbin, emailbin, trapibin;
  private static String ramanujanSet, ramanujanBin;

  static {
    serverAddress = SchedulerConstants.properties.getString("AerospikeServerAddress");
    serverPort = SchedulerConstants.properties.getInt("AerospikeServerPort");
    String servers[] = serverAddress.split(",");
    if (servers.length > 1) {
      serverAddressArray = new Host[servers.length];
      for (int i = 0; i < servers.length; i++)
        serverAddressArray[i] = new Host(servers[i], serverPort);
    } else {
      serverAddressArray = null;
    }
    defaultTimeOut = SchedulerConstants.properties.getInt("AerospikeTimeOut");
    ttl = SchedulerConstants.properties.getInt("AerospikeTTL");
    ttlWritePolicy = new WritePolicy();
    ttlWritePolicy.expiration = ttl;
    ttlWritePolicy.timeout = defaultTimeOut;
    ttlWritePolicy.sendKey = true;

    try {
      if (serverAddressArray == null) {
        client = new AerospikeClient(serverAddress, serverPort);
      } else {
        client = new AerospikeClient(new ClientPolicy(), serverAddressArray);
      }
    } catch (Exception e) {
      LOGGER.error("Fatal error: Failed to create Aerospike connection", e);
      System.exit(2);
    }
    namespace = SchedulerConstants.properties.getString("AerospikeNamespace");
    webpushbin = SchedulerConstants.properties.getString("AerospikeWebPushBin");
    smsbin = SchedulerConstants.properties.getString("AerospikeSmsBin");
    emailbin = SchedulerConstants.properties.getString("AerospikeEmailBin");
    trapibin = SchedulerConstants.properties.getString("AerospikeTriggerApiBin");
    ramanujanSet = SchedulerConstants.properties.getString("AerospikeRamanujanSet");
    ramanujanBin = SchedulerConstants.properties.getString("AerospikeRamanujanBin");
  }

  public static Map<String, String> scanAllRecords(String set, String channelId) {
    LOGGER.info(
        "Getting All records from Aerospike [" + namespace + ", " + set + ", " + channelId + " ]");
    final Map<String, String> map = new HashMap<String, String>();
    long startScan = System.nanoTime();

    try {
      ScanPolicy policy = new ScanPolicy();
      policy.concurrentNodes = false;
      policy.priority = Priority.HIGH;
      policy.includeBinData = true;
      synchronized (sync) {
        client.scanAll(policy, namespace, set, new ScanCallback() {
          @Override
          public void scanCallback(Key key, Record record) throws AerospikeException {
            try {
              if (key.userKey != null) {
                String userKey = key.userKey.toString();
                // key can be crmid/hashedMobile/hashedEmail/trApiIdentifier etc
                // pf bin is for webpush, sms for sms, email for email, trapi for trigger Api
                if (userKey != null) {
                  String bin = null;
                  switch (channelId) {
                    case "10":
                      bin = webpushbin;
                      break;
                    case "4":
                      bin = smsbin;
                      break;
                    case "5":
                      bin = emailbin;
                      break;
                    case "15":
                      bin = trapibin;
                      break;
                    default:
                      LOGGER.error("ChannelId {} is not present", channelId);
                      return;
                  }
                  if (bin != null) {
                    String binValue = record.getString(bin);
                    if (binValue != null) {
                      LOGGER.debug("Callback received for UserKey : {}", userKey);
                      map.put(userKey, binValue);
                    }
                  }
                }
              } else {
                LOGGER.error("user key is null for key " + key.toString() + " so ignoring it");
              }
            } catch (Exception e) {
              LOGGER.error("Exception in scanning aerospike ", e);
            }
          }
        });
      }
      long scanTime = (System.nanoTime() - startScan) / 1000;
      MonitoringMetrics.addToTsdb(SchedulerConstants.METRIC_AEROSPIKE_SCAN_TIME, scanTime,
          SchedulerConstants.METRIC_COMPONENT_NAME, set);
    } catch (AerospikeException ae) {
      LOGGER.error("Failed to scan data for Segment : " + set + ". Exception thrown : ", ae);
    } catch (Exception e) {
      LOGGER.error("Failed to scan data for Segment : " + set + ". Exception thrown : ", e);
    }
    LOGGER.info("scanned aerospike length " + map.size());
    return map;
  }

  public static Map<String, String> scanAllRamanujanRecords() {
    LOGGER.info("Getting All records from Aerospike [" + namespace + ", " + ramanujanSet + " ]");
    final Map<String, String> map = new HashMap<String, String>();
    long startScan = System.nanoTime();

    try {
      ScanPolicy policy = new ScanPolicy();
      policy.concurrentNodes = false;
      policy.priority = Priority.HIGH;
      policy.includeBinData = true;
      synchronized (sync) {
        client.scanAll(policy, namespace, ramanujanSet, new ScanCallback() {
          @Override
          public void scanCallback(Key key, Record record) throws AerospikeException {
            try {
              if (key.userKey != null) {
                String userKey = key.userKey.toString();
                LOGGER.debug("Callback received for UserKey : {}", userKey);
                // key can be subid/hashedMobile/hashedEmail etc
                if (userKey != null) {
                  String binValue = record.getString(ramanujanBin);
                  map.put(userKey, binValue);
                  LOGGER.debug("Scanned Value {} from the set {} for key {}", binValue,
                      ramanujanSet, userKey);
                }
              } else {
                LOGGER.error("user key is null for key " + key.toString() + " so ignoring it");
              }
            } catch (Exception e) {
              LOGGER.error("Exception in scanning aerospike ", e);
            }
          }
        });
      }
      long scanTime = (System.nanoTime() - startScan) / 1000;
      MonitoringMetrics.addToTsdb(SchedulerConstants.METRIC_AEROSPIKE_SCAN_TIME, scanTime,
          SchedulerConstants.METRIC_COMPONENT_NAME, ramanujanSet);
    } catch (AerospikeException ae) {
      LOGGER.error("Failed to scan data for Segment : {}, Exception thrown : ", ramanujanSet, ae);
    } catch (Exception e) {
      LOGGER.error("Failed to scan data for Segment : {}, Exception thrown : ", ramanujanSet, e);
    }
    LOGGER.info("scanned aerospike length " + map.size());
    return map;
  }

  public static String getRecord(String set, String bin, String key) {
    LOGGER.debug("Getting from Aerospike [ {}, {}, {}, {} ]", namespace, set, bin, key);
    String value = "{}";
    try {
      Key aeroKey = new Key(namespace, set, key);
      Record record = client.get(client.readPolicyDefault, aeroKey, bin);
      if (record != null) {
        Object valueObj = record.getValue(bin);
        if (valueObj != null) {
          value = valueObj.toString();
        }
      }
    } catch (Exception e) {
      LOGGER.error("Aerospike error in reading record for {} from [namespace {}, set {}] - {}", key,
          namespace, set, e);
    }
    return value;
  }

  public static Record getRecord(String set, String origKey) {
    String key = origKey;
    Record record = null;
    try {
      Key aeroKey = new Key(namespace, set, key);
      record = client.get(client.readPolicyDefault, aeroKey);
    } catch (Exception e) {
      LOGGER.error("Aerospike error in reading record for {} from [namespace {}, set {}] - {}",
          origKey, namespace, set, e);
    }
    return record;
  }

  public void shutDown() {
    LOGGER.error("Shutting down Aerospike client");
    client.close();
  }
}
