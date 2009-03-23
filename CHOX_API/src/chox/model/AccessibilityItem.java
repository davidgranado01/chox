package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

public class AccessibilityItem implements Serializable
{
	/** 
	 * This attribute maps to the column role in the accessibility_item table.
	 */
	protected String role;

	/** 
	 * This attribute maps to the column right in the accessibility_item table.
	 */
	protected Short accessRight;

	/** 
	 * This attribute maps to the column accessibility_id in the accessibility_item table.
	 */
	protected Accessibility accessibility;

	/** 
	 * This attribute maps to the column id in the accessibility_item table.
	 */
	protected Integer id;

	/**
	 * Method 'AccessibilityItem'
	 * 
	 */
	public AccessibilityItem()
	{
	}

	/**
	 * Method 'getRole'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getRole()
	{
		return role;
	}

	/**
	 * Method 'setRole'
	 * 
	 * @param role
	 */
	public void setRole(java.lang.String role)
	{
		this.role = role;
	}

	/**
	 * Method 'getRight'
	 * 
	 * @return java.lang.Short
	 */
	public java.lang.Short getAccessRight()
	{
		return accessRight;
	}

	/**
	 * Method 'setRight'
	 * 
	 * @param right
	 */
	public void setAccessRight(java.lang.Short accessRight)
	{
		this.accessRight = accessRight;
	}

	/**
	 * Method 'getAccessibilityId'
	 * 
	 * @return java.lang.Integer
	 */
	public Accessibility getAccessibility()
	{
		return accessibility;
	}

	/**
	 * Method 'setAccessibilityId'
	 * 
	 * @param accessibilityId
	 */
	public void setAccessibility(Accessibility accessibility)
	{
		this.accessibility = accessibility;
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

}
