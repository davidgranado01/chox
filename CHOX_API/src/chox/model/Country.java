package chox.model;

import java.io.Serializable;

public class Country implements Serializable
{
	/** 
	 * This attribute maps to the column id in the country table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column name in the country table.
	 */
	protected String name;

	/** 
	 * This attribute maps to the column currency in the country table.
	 */
	protected String currency;

	/** 
	 * This attribute maps to the column vat_rate in the country table.
	 */
	protected long vatRate;

	/**
	 * Method 'Country'
	 * 
	 */
	public Country()
	{
	}
        
        public Country(int id)
	{
            this.id = id;
	}

	/**
	 * Method 'getId'
	 * 
	 * @return int
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Method 'setId'
	 * 
	 * @param id
	 */
	public void setId(int id)
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
	 * Method 'getCurrency'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCurrency()
	{
		return currency;
	}

	/**
	 * Method 'setCurrency'
	 * 
	 * @param currency
	 */
	public void setCurrency(java.lang.String currency)
	{
		this.currency = currency;
	}

	/**
	 * Method 'getVatRate'
	 * 
	 * @return long
	 */
	public long getVatRate()
	{
		return vatRate;
	}

	/**
	 * Method 'setVatRate'
	 * 
	 * @param vatRate
	 */
	public void setVatRate(long vatRate)
	{
		this.vatRate = vatRate;
	}

}
