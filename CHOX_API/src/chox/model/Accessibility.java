package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

public class Accessibility implements Serializable
{
	/** 
	 * This attribute maps to the column id in the accessibility table.
	 */
	protected Integer id;

	/** 
	 * This attribute maps to the column name in the accessibility table.
	 */
	protected String name;

	/** 
	 * This attribute represents the foreign key relationship from the accessibility_item table.
	 */
	protected Set accessibilityItem = new HashSet();

	/**
	 * Method 'Accessibility'
	 * 
	 */
	public Accessibility()
	{
	}

	/**
	 * Method 'getId'
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getId()
	{
		return id;
	}

	/**
	 * Method 'setId'
	 * 
	 * @param id
	 */
	public void setId(java.lang.Integer id)
	{
		this.id = id;
	}

	/**
	 * Method 'getName'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getName()
	{
		return name;
	}

	/**
	 * Method 'setName'
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name)
	{
		this.name = name;
	}

	/**
	 * Method 'getAccessibilityItem'
	 * 
	 * @return Set
	 */
	public Set getAccessibilityItem()
	{
		return accessibilityItem;
	}

	/**
	 * Method 'setAccessibilityItem'
	 * 
	 * @param accessibilityItem
	 */
	public void setAccessibilityItem(Set accessibilityItem)
	{
		this.accessibilityItem = accessibilityItem;
	}

}
