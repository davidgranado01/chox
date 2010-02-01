package idas.chox.core.model;

import java.io.Serializable;

public class BillingChoDetail extends BillingDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2664056489054098409L;

	@Override
	public BillingCho getBilling() {
		return (BillingCho) super.getBilling();
	}

}