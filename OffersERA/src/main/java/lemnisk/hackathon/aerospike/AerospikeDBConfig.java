package lemnisk.hackathon.aerospike;

import java.util.HashMap;

public class AerospikeDBConfig {
	
	private String aerospikeIp;
	private final static String TAG = "AerospikeDBConfig";
	private int aerospikePort;
	private String namespace;
	private HashMap<String, AerospikeTable> tables = new HashMap<String, AerospikeTable>();
	private AerospikeTable currentTable = null;

	public AerospikeDBConfig(String ip, int port, String namespace) {
		this.aerospikeIp = ip;
		this.aerospikePort = port;
		this.namespace = namespace;
	}

	private class AerospikeTable {
	
		private String set;
		private String bin;

		private AerospikeTable(String set, String bin) {
			this.set = set;
			this.bin = bin;
		}

		private String getBin() {
			return this.bin;
		}

		private String getSet() {
			return this.set;
		}
	}

	public void setAerospikeIp(String aerospikeIp) {
		this.aerospikeIp = aerospikeIp;
	}

	public String getAerospikeIp() {
		return this.aerospikeIp;
	}

	public int getAerospikePort() {
		return this.aerospikePort;
	}

	public void setAerospikePort(int aerospikePort) {
		this.aerospikePort = aerospikePort;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getNamespace() {
		return this.namespace;
	}

	public AerospikeTable getTable() {
		return currentTable;
	}

	public void setTable(String set, String bin) {
		
		if (this.tables.containsKey(set + bin))
			this.currentTable = tables.get(set + bin);
		else {
			tables.put(set + bin, new AerospikeTable(set, bin));
			this.currentTable = tables.get(set + bin);
		}
	}

	public String getBin() {
		return this.currentTable.getBin();
	}

	public String getSet() {
		return this.currentTable.getSet();
	}

	@Override
	public boolean equals(Object object) {
		
		boolean result = false;
		
		if (object == null || object.getClass() != getClass()) {
			result = false;
		} else {
			AerospikeDBConfig otherConfig = (AerospikeDBConfig) object;
			if (this.aerospikeIp == otherConfig.aerospikeIp && this.aerospikePort == otherConfig.aerospikePort) {
				result = true;
			}
		}
		
		return result;
	}

	@Override
	public int hashCode() {
		int hash = 31;
		hash = 7 * hash + this.aerospikeIp.hashCode();
		hash = 7 * hash + this.aerospikePort;
		return hash;
	}
}
