
package edu.wustl.bulkoperator.controller;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.bulkoperator.appservice.AppServiceInformationObject;
import edu.wustl.bulkoperator.metadata.BulkOperationClass;
import edu.wustl.bulkoperator.processor.DynEntityBulkOperationProcessor;
import edu.wustl.bulkoperator.processor.IDynamicBulkOperationProcessor;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.bulkoperator.util.BulkOperationUtility;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

public class BulkOperationControllerFactory
{
	private static final Logger logger = Logger.getCommonLogger(BulkOperationControllerFactory.class);
	private static BulkOperationControllerFactory factory = null;

	private BulkOperationControllerFactory()
	{}

	public static BulkOperationControllerFactory getInstance()
	{
		if (factory == null)
		{
			factory = new BulkOperationControllerFactory();
		}
		return factory;
	}

	public List<IDynamicBulkOperationProcessor> getAllDynamicBulkOperationProcessor(
			BulkOperationClass bulkOperationClass,
			AppServiceInformationObject serviceInformationObject) throws BulkOperationException
	{
		List<IDynamicBulkOperationProcessor> dynamicBulkOperationClassList = new ArrayList<IDynamicBulkOperationProcessor>();
		try
		{	
			if (bulkOperationClass.checkForDEAssociationCollectionTag(bulkOperationClass))
			{
				BulkOperationClass DEbulkOperationClass = BulkOperationUtility.checkForDEObject(bulkOperationClass);
				if(DEbulkOperationClass == null)
				{
					logger.error("Error while creating DEAssocationClass instance", null);
					ErrorKey errorkey = ErrorKey.getErrorKey("bulk.error.creating.de.bulkoperation.class");
					throw new BulkOperationException(errorkey, null, "");
				}
				else
				{
					dynamicBulkOperationClassList.add(new DynEntityBulkOperationProcessor(
						DEbulkOperationClass, serviceInformationObject));
					logger.debug("In getAllDynamicBulkOperationProcessor method. DE Object list size: " + 
						dynamicBulkOperationClassList.size());
				}
			}
		}
		catch (BulkOperationException bulkOprExp)
		{
			throw bulkOprExp;
		}
		return dynamicBulkOperationClassList;
	}
}