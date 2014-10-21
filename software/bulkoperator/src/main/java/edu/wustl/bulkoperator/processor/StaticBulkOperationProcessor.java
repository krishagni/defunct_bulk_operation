/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package edu.wustl.bulkoperator.processor;

import java.util.ArrayList;
import java.util.Map;

import edu.wustl.bulkoperator.appservice.AbstractBulkOperationAppService;
import edu.wustl.bulkoperator.appservice.AppServiceInformationObject;
import edu.wustl.bulkoperator.csv.CsvReader;
import edu.wustl.bulkoperator.metadata.BulkOperationClass;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.bulkoperator.util.BulkOperationUtility;
import edu.wustl.bulkoperator.util.BulkOperationProperties;
import edu.wustl.bulkoperator.util.BulkProcessor;
import edu.wustl.bulkoperator.util.ServiceAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;

public class StaticBulkOperationProcessor extends AbstractBulkOperationProcessor
		implements
		IBulkOperationProcessor
{
	private static final Logger logger = Logger.getCommonLogger(StaticBulkOperationProcessor.class);

	public StaticBulkOperationProcessor(BulkOperationClass bulkOperationClass,
			AppServiceInformationObject serviceInformationObject)
	{
		super(bulkOperationClass, serviceInformationObject);
	}

	@Override
	Object processObject(Map<String, String> csvData) throws BulkOperationException
	{
		return null;
	}

	public Object process(CsvReader csvReader, int csvRowNumber,SessionDataBean sessionDataBean)
			throws BulkOperationException, Exception
	{
		Object staticObject = null;

		try
		{
			staticObject = getEntityObject(csvReader);
			processObject(staticObject, bulkOperationClass, csvReader, "", false, csvRowNumber);
			BulkProcessor bulkProcessor = BulkOperationProperties.getInstance().getBulkProcessor();
			
			if (bulkOperationClass.isUpdateOperation())
			{
				staticObject = bulkProcessor.processObject(staticObject, ServiceAction.UPDATE, sessionDataBean);
			}
			else
			{
				staticObject = bulkProcessor.processObject(staticObject, ServiceAction.ADD, sessionDataBean);
			}
		}
		catch (Exception exp){
			logger.error(exp.getMessage(), exp);
			throw exp;
		}
		return staticObject;
	}
}