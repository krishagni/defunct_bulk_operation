package edu.wustl.bulkoperator.util;

import edu.wustl.common.beans.SessionDataBean;

public interface BulkProcessor {
	public Object processObject(Object o, ServiceAction action, SessionDataBean sessionDataBean);
	
}
