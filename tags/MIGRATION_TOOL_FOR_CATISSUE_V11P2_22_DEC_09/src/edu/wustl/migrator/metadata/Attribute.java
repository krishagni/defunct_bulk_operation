
package edu.wustl.migrator.metadata;

public class Attribute
{

	String name;
	Object valueToSet;
	
	public Object getValueToSet()
	{
		return valueToSet;
	}


	
	public void setValueToSet(Object valueToSet)
	{
		this.valueToSet = valueToSet;
	}

	String isToSetNull;
	String dataType;

	
	

	
	public String getDataType()
	{
		return dataType;
	}

	
	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	

	public String getIsToSetNull()
	{
		return isToSetNull;
	}

	public void setIsToSetNull(String isToSetNull)
	{
		this.isToSetNull = isToSetNull;
	}
}