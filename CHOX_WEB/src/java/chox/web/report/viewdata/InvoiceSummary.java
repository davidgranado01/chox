/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report.viewdata;

import chox.Util.MathHelper;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 *
 * @author Emmanuel
 */
public class InvoiceSummary {

    private String orgName;
    private Integer noInvoiceSubmitted;
    private BigDecimal totalInvoiceValue;
    private Integer noInvoicesPaid;
    private BigDecimal valueOfPaidInvoices;
    private BigDecimal averageInvoiceValue;
    private Integer noInvoiceAwaitingPayment;
    private BigDecimal invoiceAwaitingPaymentValue;
    private Integer noInvoicePending;
    private BigDecimal invoicePendingValue;
    private Integer noInvoiceWithdrawn;
    private BigDecimal invoiceWithdrawnValue;
    private Integer noOfInvoicesWithPenalties;
    private BigDecimal valueOfInvoicesWithPenalties;
    private BigDecimal faceValueOfPaidInvoiceValue;
    private BigDecimal submittedInvoicesSettlePerc;
    private Integer invoiceDisputedSettled;
    private Integer InvoiceSettledCat0Days;
    private Integer InvoiceSettledCat30Days;
    private Integer InvoiceSettledCat60Days;
    private Integer InvoiceSettledCat90Days;
    private Integer averageNoDaysOfInvoiceSettlement;
    private Integer averageAgeDaysOfPendingInvoices;
            
    public static InvoiceSummary getObject(Map data) {
        InvoiceSummary result = new InvoiceSummary();
        result.setOrgName((String)data.get("name".toLowerCase()));
        result.setNoInvoiceSubmitted(((BigInteger)data.get("noInvoiceSubmitted".toLowerCase())).intValue());
        result.setTotalInvoiceValue((BigDecimal)data.get("totalInvoiceValue".toLowerCase()));
        result.setNoInvoicesPaid(((BigInteger)data.get("noInvoicesPaid".toLowerCase())).intValue());
        result.setValueOfPaidInvoices((BigDecimal)data.get("valueOfPaidInvoices".toLowerCase()));
        
        BigDecimal dAverageInvoiceValue = BigDecimal.ZERO;
        Double dTotalInvoiceValue = result.getTotalInvoiceValue().doubleValue();
        
        if(dTotalInvoiceValue>0 && result.getNoInvoiceSubmitted()>0){
            BigDecimal bdNoInvoiceSubmitted = new BigDecimal(result.getNoInvoiceSubmitted()); 
            dAverageInvoiceValue = result.getTotalInvoiceValue().divide(bdNoInvoiceSubmitted,2);
        }
        
        result.setAverageInvoiceValue(dAverageInvoiceValue);
        result.setNoInvoiceAwaitingPayment(((BigInteger)data.get("noInvoiceAwaitingPayment".toLowerCase())).intValue());
        result.setInvoiceAwaitingPaymentValue((BigDecimal) data.get("invoiceAwaitingPaymentValue".toLowerCase()));
        result.setNoInvoicePending(((BigInteger)data.get("noInvoicePending".toLowerCase())).intValue());
        result.setInvoicePendingValue((BigDecimal) data.get("invoicePendingValue".toLowerCase()));
        result.setNoInvoiceWithdrawn(((BigInteger)data.get("noInvoiceWithdrawn".toLowerCase())).intValue());
        result.setInvoiceWithdrawnValue((BigDecimal) data.get("invoiceWithdrawnValue".toLowerCase()));
        result.setNoOfInvoicesWithPenalties(((BigInteger)data.get("noOfInvoicesWithPenalties".toLowerCase())).intValue());
        result.setValueOfInvoicesWithPenalties((BigDecimal) data.get("valueOfInvoicesWithPenalties".toLowerCase()));
        result.setFaceValueOfPaidInvoiceValue((BigDecimal) data.get("faceValueOfPaidInvoiceValue".toLowerCase()));
        result.setInvoiceDisputedSettled(((BigInteger)data.get("invoiceDisputedSettled".toLowerCase())).intValue());
        result.setInvoiceSettledCat0Days(((BigInteger)data.get("invoiceSettledCat0Days".toLowerCase())).intValue());
        result.setInvoiceSettledCat30Days(((BigInteger)data.get("invoiceSettledCat30Days".toLowerCase())).intValue());
        result.setInvoiceSettledCat60Days(((BigInteger)data.get("invoiceSettledCat60Days".toLowerCase())).intValue());
        result.setInvoiceSettledCat90Days(((BigInteger)data.get("invoiceSettledCat90Days".toLowerCase())).intValue());
        result.setAverageAgeDaysOfPendingInvoices(((BigInteger)data.get("averageAgeDaysOfPendingInvoices".toLowerCase())).intValue());
        result.setAverageNoDaysOfInvoiceSettlement(((BigInteger)data.get("averageNoDaysOfInvoiceSettlement".toLowerCase())).intValue());
        
        
        

        return result;
    }

    public Integer getNoInvoiceSubmitted() {
        return noInvoiceSubmitted;
    }

    public void setNoInvoiceSubmitted(Integer noInvoiceSubmitted) {
        this.noInvoiceSubmitted = noInvoiceSubmitted;
    }

    public BigDecimal getTotalInvoiceValue() {
        return totalInvoiceValue;
    }

    public void setTotalInvoiceValue(BigDecimal totalInvoiceValue) {
        this.totalInvoiceValue = totalInvoiceValue;
    }

    public Integer getNoInvoicesPaid() {
        return noInvoicesPaid;
    }

    public void setNoInvoicesPaid(Integer noInvoicesPaid) {
        this.noInvoicesPaid = noInvoicesPaid;
    }

    public BigDecimal getValueOfPaidInvoices() {
        return valueOfPaidInvoices;
    }

    public void setValueOfPaidInvoices(BigDecimal valueOfPaidInvoices) {
        this.valueOfPaidInvoices = valueOfPaidInvoices;
    }

    public BigDecimal getAverageInvoiceValue() {
        return averageInvoiceValue;
    }

    public void setAverageInvoiceValue(BigDecimal averageInvoiceValue) {
        this.averageInvoiceValue = averageInvoiceValue;
    }

    public Integer getNoInvoiceAwaitingPayment() {
        return noInvoiceAwaitingPayment;
    }

    public void setNoInvoiceAwaitingPayment(Integer noInvoiceAwaitingPayment) {
        this.noInvoiceAwaitingPayment = noInvoiceAwaitingPayment;
    }

    public BigDecimal getInvoiceAwaitingPaymentValue() {
        return invoiceAwaitingPaymentValue;
    }

    public void setInvoiceAwaitingPaymentValue(BigDecimal invoiceAwaitingPaymentValue) {
        this.invoiceAwaitingPaymentValue = invoiceAwaitingPaymentValue;
    }

    public Integer getNoInvoicePending() {
        return noInvoicePending;
    }

    public void setNoInvoicePending(Integer noInvoicePending) {
        this.noInvoicePending = noInvoicePending;
    }

    public BigDecimal getInvoicePendingValue() {
        return invoicePendingValue;
    }

    public void setInvoicePendingValue(BigDecimal invoicePendingValue) {
        this.invoicePendingValue = invoicePendingValue;
    }

    public Integer getNoInvoiceWithdrawn() {
        return noInvoiceWithdrawn;
    }

    public void setNoInvoiceWithdrawn(Integer noInvoiceWithdrawn) {
        this.noInvoiceWithdrawn = noInvoiceWithdrawn;
    }

    public BigDecimal getInvoiceWithdrawnValue() {
        return invoiceWithdrawnValue;
    }

    public void setInvoiceWithdrawnValue(BigDecimal invoiceWithdrawnValue) {
        this.invoiceWithdrawnValue = invoiceWithdrawnValue;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getNoOfInvoicesWithPenalties() {
        return noOfInvoicesWithPenalties;
    }

    public void setNoOfInvoicesWithPenalties(Integer noOfInvoicesWithPenalties) {
        this.noOfInvoicesWithPenalties = noOfInvoicesWithPenalties;
    }

    public BigDecimal getValueOfInvoicesWithPenalties() {
        return valueOfInvoicesWithPenalties;
    }

    public void setValueOfInvoicesWithPenalties(BigDecimal valueOfInvoicesWithPenalties) {
        this.valueOfInvoicesWithPenalties = valueOfInvoicesWithPenalties;
    }

    public void setInvoiceSettledCat0Days(Integer InvoiceSettledCat0Days) {
        this.InvoiceSettledCat0Days = InvoiceSettledCat0Days;
    }

    public void setInvoiceSettledCat30Days(Integer InvoiceSettledCat30Days) {
        this.InvoiceSettledCat30Days = InvoiceSettledCat30Days;
    }

    public void setInvoiceSettledCat60Days(Integer InvoiceSettledCat60Days) {
        this.InvoiceSettledCat60Days = InvoiceSettledCat60Days;
    }

    public void setInvoiceSettledCat90Days(Integer InvoiceSettledCat90Days) {
        this.InvoiceSettledCat90Days = InvoiceSettledCat90Days;
    }

    public void setAverageAgeDaysOfPendingInvoices(Integer averageAgeDaysOfPendingInvoices) {
        this.averageAgeDaysOfPendingInvoices = averageAgeDaysOfPendingInvoices;
    }

    public void setAverageNoDaysOfInvoiceSettlement(Integer averageNoDaysOfInvoiceSettlement) {
        this.averageNoDaysOfInvoiceSettlement = averageNoDaysOfInvoiceSettlement;
    }

    public void setFaceValueOfPaidInvoiceValue(BigDecimal faceValueOfPaidInvoiceValue) {
        this.faceValueOfPaidInvoiceValue = faceValueOfPaidInvoiceValue;
    }

    public void setInvoiceDisputedSettled(Integer invoiceDisputedSettled) {
        this.invoiceDisputedSettled = invoiceDisputedSettled;
    }

    public void setSubmittedInvoicesSettlePerc(BigDecimal submittedInvoicesSettlePerc) {
        this.submittedInvoicesSettlePerc = submittedInvoicesSettlePerc;
    }

    public Integer getInvoiceSettledCat0Days() {
        return InvoiceSettledCat0Days;
    }

    public Integer getInvoiceSettledCat30Days() {
        return InvoiceSettledCat30Days;
    }

    public Integer getInvoiceSettledCat60Days() {
        return InvoiceSettledCat60Days;
    }

    public Integer getInvoiceSettledCat90Days() {
        return InvoiceSettledCat90Days;
    }

    public BigDecimal getInvoiceSettledCat0DaysPerc() {
        //return new BigDecimal(InvoiceSettledCat0Days.floatValue()/noInvoiceSubmitted.floatValue());
        return MathHelper.devide(InvoiceSettledCat0Days, noInvoiceSubmitted);
    }

    public BigDecimal getInvoiceSettledCat30DaysPerc() {
        //return new BigDecimal(InvoiceSettledCat30Days.floatValue()/noInvoiceSubmitted.floatValue());
        return MathHelper.devide(InvoiceSettledCat30Days, noInvoiceSubmitted);
    }

    public BigDecimal getInvoiceSettledCat60DaysPerc() {
        //return new BigDecimal(InvoiceSettledCat60Days.floatValue()/noInvoiceSubmitted.floatValue());
        return MathHelper.devide(InvoiceSettledCat60Days, noInvoiceSubmitted);
    }

    public BigDecimal getInvoiceSettledCat90DaysPerc() {
        //return new BigDecimal(InvoiceSettledCat90Days.floatValue()/noInvoiceSubmitted.floatValue());
        return MathHelper.devide(InvoiceSettledCat90Days, noInvoiceSubmitted);
    }
    
    public Integer getAverageAgeDaysOfPendingInvoices() {
        return averageAgeDaysOfPendingInvoices;
    }

    public Integer getAverageNoDaysOfInvoiceSettlement() {
        return averageNoDaysOfInvoiceSettlement;
    }

    public BigDecimal getFaceValueOfPaidInvoiceValue() {
        return faceValueOfPaidInvoiceValue;
    }

    public Integer getInvoiceDisputedSettled() {
        return invoiceDisputedSettled;
    }

    public BigDecimal getInvoiceDisputedSettledPerc() {
        return MathHelper.devide(invoiceDisputedSettled, noInvoiceSubmitted);
    }
    
    public BigDecimal getSubmittedInvoicesSettlePerc() {
        //return new BigDecimal(noInvoicesPaid.floatValue()/noInvoiceSubmitted.floatValue());
        return MathHelper.devide(noInvoicesPaid, noInvoiceSubmitted);
    }

    
    
    
    
    
}
