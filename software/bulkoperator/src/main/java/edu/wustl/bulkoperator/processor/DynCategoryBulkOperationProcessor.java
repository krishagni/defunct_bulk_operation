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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wustl.bulkoperator.appservice.AbstractBulkOperationAppService;
import edu.wustl.bulkoperator.appservice.AppServiceInformationObject;
import edu.wustl.bulkoperator.csv.CsvReader;
import edu.wustl.bulkoperator.metadata.Attribute;
import edu.wustl.bulkoperator.metadata.BulkOperationClass;
import edu.wustl.bulkoperator.metadata.HookingInformation;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.bulkoperator.util.BulkOperationUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

public class DynCategoryBulkOperationProcessor extends AbstractBulkOperationProcessor
		implements
		IBulkOperationProcessor
{

	private static final Logger logger = Logger
			.getCommonLogger(DynCategoryBulkOperationProcessor.class);

	public DynCategoryBulkOperationProcessor(BulkOperationClass dynExtCategoryBOClass,
			AppServiceInformationObject serviceInformationObject)
	{
		super(dynExtCategoryBOClass, serviceInformationObject);
	}

	public Object process(CsvReader csvReader, int csvRowCounter,
			SessionDataBean sessionDataBean) throws BulkOperationException, Exception
	{

		HashMap<String, Object> dynExtObject = new HashMap<String, Object>();
		Long recordEntryId = null;
		try
		{
			AbstractBulkOperationAppService bulkOprAppService = AbstractBulkOperationAppService
					.getInstance(serviceInformationObject.getServiceImplementorClassName(), true,
							serviceInformationObject.getUserName(), null);
			processObject(dynExtObject, bulkOperationClass, csvReader, "", false, csvRowCounter);
			HookingInformation hookingInformationFromTag = bulkOperationClass.getHookingInformation();
			getinformationForHookingData(csvReader, hookingInformationFromTag);
			hookingInformationFromTag.setCategoryName(bulkOperationClass.getClassName());
			hookingInformationFromTag.setSessionDataBean(sessionDataBean);
		
			recordEntryId = bulkOprAppService
					.insertDEObject(bulkOperationClass.getClassName(), dynExtObject, hookingInformationFromTag);
			
			
		}
		catch (BulkOperationException bulkOprExp)
		{
			logger.error(bulkOprExp.getMessage(), bulkOprExp);
			throw bulkOprExp;
		}
		catch (Exception exp)
		{
			logger.error(exp.getMessage(), exp);
			throw exp;
		}
		return recordEntryId;
	}

	@Override
	Object processObject(Map<String, String> csvData) throws BulkOperationException
	{
		return null;
	}

	protected void setValueToObject(Object mainObj,
			BulkOperationClass mainMigrationClass,CsvReader csvReader,
			String columnSuffix, boolean validate, Attribute attribute) throws BulkOperationException {
		String csvDataValue=null;
		if(csvReader.getColumn(attribute.getCsvColumnName()+ columnSuffix)!=null)
		{
			csvDataValue=csvReader.getColumn(attribute.getCsvColumnName()+ columnSuffix);
		}
		if(Validator.isEmpty(csvDataValue) && attribute.getDefaultValue()!=null)
		{
			csvDataValue=attribute.getDefaultValue();
		}
		if (!Validator.isEmpty(csvDataValue)) {
			Map<String, Object> categoryDataValueMap = (Map<String, Object>) mainObj;
			
			if (csvDataValue == null || "".equals(csvDataValue)) {
				categoryDataValueMap.put(attribute.getName(), "");
			} else {
				categoryDataValueMap.put(attribute.getName(), csvDataValue);
			}
		}
	}

	/**
	 *
	 * @param mainObj
	 * @param bulkOperationClass
	 * @param csvData
	 * @param columnSuffix
	 * @param validate
	 * @throws BulkOperationException
	 */
	protected void processContainments(Object mainObj, BulkOperationClass bulkOperationClass,
			CsvReader csvReader, String columnSuffix, boolean validate, int csvRowNumber)
			throws BulkOperationException
	{
		try
		{
			Map<String, Object> categoryDataValueMap = (Map<String, Object>) mainObj;

			Iterator<BulkOperationClass> containmentItert = bulkOperationClass
					.getContainmentAssociationCollection().iterator();
			while (containmentItert.hasNext())
			{
				BulkOperationClass containmentObjectCollection = containmentItert.next();
				if (containmentObjectCollection.getCardinality() != null)
				{
					List<Map<Long, Object>> list = new ArrayList<Map<Long, Object>>();

					int maxNoOfRecords = containmentObjectCollection.getMaxNoOfRecords().intValue();
					for (int i = 1; i <= maxNoOfRecords; i++)
					{
						if (BulkOperationUtility.checkIfAtLeastOneColumnHasAValueForInnerContainment(csvRowNumber,containmentObjectCollection,
										columnSuffix + "#" + i,csvReader))
						{
							Object obj = new HashMap<Long, Object>();
							processObject(obj, containmentObjectCollection, csvReader, columnSuffix
									+ "#" + i, validate, csvRowNumber);
							list.add((Map<Long, Object>) obj);
						}
						categoryDataValueMap.put(containmentObjectCollection.getClassName(), list);
					}
				}
			}
		}
		catch (BulkOperationException bulkExp)
		{
			logger.error(bulkExp.getMessage(), bulkExp);
			throw new BulkOperationException(bulkExp.getErrorKey(), bulkExp, bulkExp.getMsgValues());
		}
		catch (Exception exp)
		{
			logger.error(exp.getMessage(), exp);
			ErrorKey errorkey = ErrorKey.getErrorKey("bulk.operation.issues");
			throw new BulkOperationException(errorkey, exp, exp.getMessage());
		}
	}
}