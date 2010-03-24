package idas.chox.core.model;

import java.io.Serializable;

public class BillingInsurerDetail extends BillingDetail implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5929697232347441256L;

	@Override
	public BillingInsurer getBilling() {
		return (BillingInsurer) super.getBilling();
	}

	
}