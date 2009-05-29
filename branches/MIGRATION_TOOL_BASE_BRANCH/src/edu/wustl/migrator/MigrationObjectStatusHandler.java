package edu.wustl.migrator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import edu.wustl.migrator.dao.SandBoxDao;
import edu.wustl.migrator.metadata.MigrationClass;
import edu.wustl.migrator.metadata.ObjectIdentifierMap;
import edu.wustl.migrator.util.MigrationException;



public class MigrationObjectStatusHandler
{
	static MigrationObjectStatusHandler failurehandler=null;
	private MigrationObjectStatusHandler()
	{
		
	}
	
	public static MigrationObjectStatusHandler getInstance()
	{
		if(failurehandler==null)
		{
			failurehandler = new MigrationObjectStatusHandler();
		}
		return failurehandler;
	}
	
	public void handleFailedMigrationObject(Object failedObject,String message,Throwable throwable)
	{
		throwable.printStackTrace();
	}
	
	public void handleSuccessfullyMigratedObject(Object mainObject, MigrationClass mainMigrationClass, ObjectIdentifierMap objectIdentifierMap) throws MigrationException
	{

		Collection<MigrationClass> containmentCollection = mainMigrationClass
				.getContainmentAssociationCollection();
		processContainmentObjectIdentifierMap(mainObject, mainMigrationClass, objectIdentifierMap,
				containmentCollection);

	}

	/**
	 * @param mainObject
	 * @param mainMigrationClass
	 * @param objectIdentifierMap
	 * @param containmentCollection
	 * @throws MigrationException
	 */
	private void processContainmentObjectIdentifierMap(Object mainObject,
			MigrationClass mainMigrationClass, ObjectIdentifierMap objectIdentifierMap,
			Collection<MigrationClass> containmentCollection) throws MigrationException
	{
		if (containmentCollection != null)
		{
			Map<String, LinkedHashSet<ObjectIdentifierMap>> containmentObjectIdentifierMap = objectIdentifierMap
					.getContainmentObjectIdentifierMap();
			Iterator<MigrationClass> containmentMigrationClassIter = containmentCollection
					.iterator();
			while (containmentMigrationClassIter.hasNext())
			{
				MigrationClass containmentMigrationClassObj = containmentMigrationClassIter.next();
				String containmentRollName = containmentMigrationClassObj.getRoleName();
				String containmentClassName = containmentMigrationClassObj.getClassName();
				if (containmentObjectIdentifierMap.containsKey(containmentRollName))
				{
					LinkedHashSet<ObjectIdentifierMap> containmentObjectIdentifierSet = (LinkedHashSet<ObjectIdentifierMap>) containmentObjectIdentifierMap
							.get(containmentRollName);

					Object containmentObj = mainMigrationClass.invokeGetterMethod(
							containmentRollName, null, mainObject, null);
					if (containmentObj != null)
					{
						if (containmentObj instanceof Collection)
						{

							List sortedList = new ArrayList((Collection) containmentObj);
							Collections.sort(sortedList, new SortObject());
							Collection containmentObjectCollection = new LinkedHashSet(sortedList);
							if (containmentObjectIdentifierSet.size() == containmentObjectCollection
									.size())
							{
								Iterator containmentObjIter = containmentObjectCollection
										.iterator();
								Iterator<ObjectIdentifierMap> containmentObjectIdentifierSetIter = containmentObjectIdentifierSet
										.iterator();
								while (containmentObjIter.hasNext())
								{
									ObjectIdentifierMap objectIdentifier = containmentObjectIdentifierSetIter
											.next();
									Object containment = containmentObjIter.next();
									Long newId = containmentMigrationClassObj
											.invokeGetIdMethod(containment);
									objectIdentifier.setNewId(newId);
									SandBoxDao.insertMapEntries(objectIdentifier);
									Collection<MigrationClass> containmentInContainment =  containmentMigrationClassObj.getContainmentAssociationCollection();
									processContainmentObjectIdentifierMap(containment, containmentMigrationClassObj, objectIdentifier, containmentInContainment);
								
								}
							}
						}
						else
						{
							ObjectIdentifierMap objectIdentifier = containmentObjectIdentifierSet
									.iterator().next();
							Long newId = containmentMigrationClassObj
									.invokeGetIdMethod(containmentObj);
							objectIdentifier.setNewId(newId);
							SandBoxDao.insertMapEntries(objectIdentifier);
							Collection<MigrationClass> containmentInContainment =  containmentMigrationClassObj.getContainmentAssociationCollection();
							processContainmentObjectIdentifierMap(containmentObj, containmentMigrationClassObj, objectIdentifier, containmentInContainment);
						}
						
					}
				}
			}
		}
	}
	
}
