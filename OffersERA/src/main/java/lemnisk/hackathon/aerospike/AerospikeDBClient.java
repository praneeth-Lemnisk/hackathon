package lemnisk.hackathon.aerospike;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.ScanCallback;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.Priority;
import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.client.policy.WritePolicy;

public class AerospikeDBClient implements ScanCallback{
	
	private AerospikeClient client;
	private final static String TAG = "AerospikeDBClient";
	private static HashMap<AerospikeDBConfig, AerospikeDBClient> aerospikeFactory = new HashMap<AerospikeDBConfig, AerospikeDBClient>();
	private AerospikeDBConfig currentConfig;

	private AerospikeDBClient(AerospikeDBConfig config) throws AerospikeException {
		this.currentConfig = config;
		try
		{
			this.client = new AerospikeClient(currentConfig.getAerospikeIp(), currentConfig.getAerospikePort());
		}
		catch(AerospikeException e)
		{
			System.out.println( "Exception creating Aerospike client");
			e.printStackTrace();
			throw e;
		}
		
	}

	public static AerospikeDBClient getInstance(AerospikeDBConfig config) {
		
		if (aerospikeFactory.containsKey(config))
		{
			
			return aerospikeFactory.get(config);
		}
		else 
		{
			AerospikeDBClient newClient = new AerospikeDBClient(config);
			aerospikeFactory.put(config, newClient);
			return newClient;
		}
	}

	public void writeKey(String keyName, String value) throws Exception{
		
		System.out.println( "KeyName ="+keyName + "Value = "+value);
		Key key;		
		try {
			WritePolicy policy = new WritePolicy();
			policy.sendKey=true;
			key = new Key(currentConfig.getNamespace(), currentConfig.getSet(), keyName);
			System.out.println("aerospike bin name" + currentConfig.getBin());
			Bin bin = new Bin(currentConfig.getBin(), value);
			client.put(policy, key, bin);
		} catch (AerospikeException e) {
			System.out.println( "Aerospike error in writeKey for " + keyName + ", " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	public String getValue(String keyName) {
		
		Key key;
		Record record = null;
		
		try {
			Policy policy = new Policy();
			policy.timeout = 10000;
			policy.sendKey=true;
			key = new Key(currentConfig.getNamespace(), currentConfig.getSet(), keyName);
			if (key != null && policy != null) {
				record = client.get(policy, key);
			} else {
				System.out.println( "Key = " + key);
			}
		} catch (AerospikeException e) {
			System.out.println( "Aerospike error in getValue for " + keyName + ", " + e.getMessage());
			e.printStackTrace();
		}
		
		if (record != null && record.getValue(currentConfig.getBin()) != null) {
			return record.getValue(currentConfig.getBin()).toString();
		} else {
			return "";
		}
	}
	
	// This is implemented where there are different bins and the bin names are unknown like feedcache
	public Map<String,Object> getCompleteValue(String keyName)
	{
		Key key;
		Record record = null;
		
		try {
			Policy policy = new Policy();
			policy.timeout = 10000;
			policy.sendKey=true;
			key = new Key(currentConfig.getNamespace(), currentConfig.getSet(), keyName);
			if (key != null && policy != null) {
				record = client.get(policy, key);
			} else {
				System.out.println( "Key = " + key);
			}
		} catch (AerospikeException e) {
			System.out.println( "Aerospike error in getValue for " + keyName + ", " + e.getMessage());
			e.printStackTrace();
		}
		
		if (record != null) {
			return record.bins;
		} else {
			
			return null;
		
		}
		
	}
	
	
	public boolean deleteElement(String keyName) {
		
		boolean success = true;
		
		try {
			Key aeroKey = new Key(currentConfig.getNamespace(), currentConfig.getSet(), keyName);
			client.delete(null, aeroKey);
		} catch (Exception e) {
			System.out.println( "Aerospike error in deleteElement for " + keyName + ", " + e.getMessage());
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	public int getRecordTTL(String keyName) throws Exception{
		
		Key key;
		Record record = null;
		
		try {
			key = new Key(currentConfig.getNamespace(), currentConfig.getSet(), keyName);
			record = client.get(null, key);
		} catch (AerospikeException e) {
			System.out.println( "Aerospike error in getRecordTTL for " + keyName + ", " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		if(record==null)
			System.out.println("Specified Bin is not present");
		int TTL = record.getTimeToLive();
		System.out.println( "AerospikeValueRecord TTL = " + TTL);
		return TTL;
	}

	public void shutDown() {
		System.out.println( "Shutting down Aerospike client");
		client.close();
	}

	public AerospikeDBConfig getClientConfig() {
		return currentConfig;
	}

	public void setClientConfig(AerospikeDBConfig clientConfig) {
		this.currentConfig = clientConfig;
	}
	
	private Map<String,String> completeAerospikeData;
	
	public Map<String,String> scanAll()
	{
		completeAerospikeData = new HashMap<String,String>();
		try {
            ScanPolicy policy = new ScanPolicy();
            policy.concurrentNodes = false;
            policy.priority = Priority.HIGH;
            policy.includeBinData = true;
            policy.timeout=60000;
            policy.sendKey=true;

            client.scanAll(policy, currentConfig.getNamespace(), currentConfig.getSet(), this);   
            System.out.println("Hello");
        }
        finally {
            
        }
		
		return completeAerospikeData;
		
    }

	
    public void scanCallback(Key key, Record record) {
    
    	System.out.println("hellooo"+key.userKey);
    	if (key.userKey != null) {
    		String userKey = key.userKey.toString();
    		System.out.println(userKey);
    		String binValue = record.getString(currentConfig.getBin());
    		if(binValue!=null&&binValue!="")
    		completeAerospikeData.put(userKey, binValue);    		
    	}
        
    }
	
}

