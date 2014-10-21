package edu.wustl.bulkoperator.util;

public class BulkOperationProperties {
	private static BulkOperationProperties instance;
	
	private BulkProcessor bulkProcessor;
	
	protected  BulkOperationProperties() {
		//to avoid instantiation
	}
	
	public static BulkOperationProperties getInstance() {
		if (instance == null) {
			instance = new BulkOperationProperties();
		}
		
		return instance;
	}
	
	public BulkProcessor getBulkProcessor() {
		return bulkProcessor;
	}
	
	public void setBulkProcessor(BulkProcessor bulkProcessor) {
		this.bulkProcessor = bulkProcessor;
	}
}