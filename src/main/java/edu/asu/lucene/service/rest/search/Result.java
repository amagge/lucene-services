package edu.asu.lucene.service.rest.search;

import java.util.Map;
import java.util.List;

/**
 * Result object as a reply to requested lucene query
 * records 		containing records as a list of hashmaps (fieldname->fieldvalue). 
 * 				it treats all fields as strings before returning as json.
 * retrieved 	contains the number of records returned with the object
 * available 	may contain number of total records that may match the query, 
 * 				-1 if not requested (since it is an expensive operation)
 * @author amagge
 */
public class Result {
	
	private List<Map<String,String>> records;
	private int retrieved=0;
	private int available=0;
	
	
	public Result(List<Map<String, String>> records, int retrieved, int available) {
		super();
		this.records = records;
		this.retrieved = retrieved;
		this.available = available;
	}


	public List<Map<String, String>> getRecords() {
		return records;
	}


	public int getRetrieved() {
		return retrieved;
	}


	public int getAvailable() {
		return available;
	}

	
}