package idas.chox.core.model;

import java.math.BigDecimal;
import java.util.Date;

public class BillingDetail extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2342966159416856075L;
	protected Claim claim;
	protected Date receivedDate;
	protected String comment;
	protected BigDecimal billAmount;
	protected BigDecimal amountReceived;
	protected boolean reconciled;
	protected Billing billing;
	
	public Claim getClaim() {
		return claim;
	}
	public void setClaim(Claim claim) {
		this.claim = claim;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public BigDecimal getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}
	public BigDecimal getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}
	public boolean isReconciled() {
		return reconciled;
	}
	public void setReconciled(boolean reconciled) {
		this.reconciled = reconciled;
	}
	public Billing getBilling() {
		return billing;
	}
	public void setBilling(Billing billing) {
		this.billing = billing;
	}

}