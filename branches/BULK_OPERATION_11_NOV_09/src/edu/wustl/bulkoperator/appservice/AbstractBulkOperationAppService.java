
package edu.wustl.bulkoperator.appservice;

import java.lang.reflect.Constructor;
import java.util.List;

import edu.wustl.bulkoperator.util.BulkOperationConstants;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.common.exception.ErrorKey;

public abstract class AbstractBulkOperationAppService
{
	public AbstractBulkOperationAppService(boolean isAuthenticationRequired,
			String userName, String password) throws Exception
	{
		this.isAuthRequired = isAuthenticationRequired;
		initialize(userName, password);
	}

	protected transient boolean isAuthRequired = true;

	public boolean isAuthenticationRequired()
	{
		return isAuthRequired;
	}

	public static AbstractBulkOperationAppService getInstance(String migrationAppClassName,
	boolean isAuthenticationRequired, String userName, String password) throws BulkOperationException
	{
		if (migrationAppClassName == null)
		{
			migrationAppClassName = BulkOperationConstants.CA_CORE_MIGRATION_APP_SERVICE;
		}
		AbstractBulkOperationAppService appService = null;
		try
		{
			Class migrationServiceTypeClass = Class.forName(migrationAppClassName);
			Class[] constructorParameters = new Class[3];
			constructorParameters[0] = boolean.class;
			constructorParameters[1] = String.class;
			constructorParameters[2] = String.class;
			Constructor constructor = migrationServiceTypeClass
					.getDeclaredConstructor(constructorParameters);
			appService = (AbstractBulkOperationAppService) constructor.newInstance(
					isAuthenticationRequired, userName, password);
		}
		catch (Exception exp)
		{
			ErrorKey errorKey = ErrorKey.getErrorKey("bulk.invalid.username.password");
			throw new BulkOperationException(errorKey, exp, "");
		}
		return appService;
	}

	abstract public void initialize(String userName, String password) throws Exception;

	abstract public void authenticate(String userName, String password) throws BulkOperationException;

	public Object insert(Object obj) throws Exception
	{
		return insertObject(obj);
	}

	public Object insertDEObject(Object dynExtObject, Object staticObject) throws Exception
	{
		return insertDynExtObject(dynExtObject, staticObject);
	}

	public Object search(Object obj) throws Exception
	{
		return searchObject(obj);
	}

	public Object update(Object obj) throws Exception
	{
		return updateObject(obj);
	}

	public List<Object> hookStaticDEObject(Object hookingInformationObject) throws Exception
	{
		return hookStaticDynExtObject(hookingInformationObject);		
	}

	abstract protected Object insertObject(Object obj) throws Exception;

	abstract public void deleteObject(Object obj) throws Exception;

	abstract protected Object updateObject(Object obj) throws Exception;

	abstract protected Object searchObject(Object obj) throws Exception;

	abstract protected Object insertDynExtObject(Object obj1, Object obj2) throws Exception;

	abstract protected List<Object> hookStaticDynExtObject(Object hookingInformationObject) throws Exception;
}