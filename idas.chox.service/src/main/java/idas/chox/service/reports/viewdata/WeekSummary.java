package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public class WeekSummary {

    private String weekCycleDate;
    private Integer claimsBFwd = 0;
    private Integer claimsCFwd = 0;
    private Integer claimsNotification = 0;
    private Integer claimsPaid = 0;
    private Integer claimsNotificationContestedByInsurer = 0;
    private BigDecimal invoicePaidAsPercentageOfInvoicing = new BigDecimal(0.00);
    private Integer claimsPendingByInsurer = 0;
    private Integer claimsNotificationAcceptedByInsurer = 0;
    private Integer claimsFNOLReferrals = 0;
    private Integer claimsFNOLCreatedByInsurer = 0;
    private Integer claimsInvoiced = 0;
    private Integer invoiceContestedByInsurer = 0;
    private Integer invoicePendingByInsurer = 0;
    private Integer invoiceApprovedByInsurer = 0;
    private Integer invoicePaidByInsurer = 0;
    private Integer claimsToBeInvoiced = 0;
    
    // ClaimsCFwd - FORMULA
    
    public static WeekSummary getObject(Map data) {
        
        WeekSummary result = new WeekSummary();
        
        result.setWeekCycleDate(data.get("weekCycleDate").toString());
        result.setClaimsBFwd(getIntegerValue(data.get("claimsBFwd".toLowerCase())));
        result.setClaimsCFwd(getIntegerValue(data.get("claimsCFwd".toLowerCase())));
        result.setClaimsNotification(getIntegerValue(data.get("claimsNotification".toLowerCase())));
        result.setClaimsPaid(getIntegerValue(data.get("claimsPaid".toLowerCase())));
        result.setClaimsNotificationContestedByInsurer(getIntegerValue(data.get("claimsNotificationContestedByInsurer".toLowerCase())));
        result.setClaimsPendingByInsurer(getIntegerValue(data.get("claimsPendingByInsurer".toLowerCase())));
        result.setClaimsNotificationAcceptedByInsurer(getIntegerValue(data.get("claimsNotificationAcceptedByInsurer".toLowerCase())));
        result.setClaimsFNOLReferrals(getIntegerValue(data.get("claimsFNOLReferrals".toLowerCase())));
        result.setClaimsFNOLCreatedByInsurer(getIntegerValue(data.get("claimsFNOLCreatedByInsurer".toLowerCase())));
        result.setClaimsInvoiced(getIntegerValue(data.get("claimsInvoiced".toLowerCase())));
        result.setInvoiceContestedByInsurer(getIntegerValue(data.get("invoiceContestedByInsurer".toLowerCase())));
        result.setInvoicePendingByInsurer(getIntegerValue(data.get("invoicePendingByInsurer".toLowerCase())));
        result.setInvoiceApprovedByInsurer(getIntegerValue(data.get("invoiceApprovedByInsurer".toLowerCase())));
        result.setInvoicePaidByInsurer(getIntegerValue(data.get("invoicePaidByInsurer".toLowerCase())));
        result.setClaimsToBeInvoiced(getIntegerValue(data.get("claimsToBeInvoiced".toLowerCase())));
        
        return result;
    }

    public static Integer getIntegerValue(Object v) {
        if (v.getClass().equals(Integer.class)) {
            return (Integer) v;
        } else if (v.getClass().equals(BigInteger.class)) {
            return ((BigInteger) v).intValue();
        } else {
            return 0;
        }
    }

  
    public Integer getClaimsBFwd() {
        return claimsBFwd;
    }

    public void setClaimsBFwd(Integer claimsBFwd) {
        this.claimsBFwd = claimsBFwd;
    }

    public Integer getClaimsCFwd() {
        return claimsCFwd;
    }

    public void setClaimsCFwd(Integer claimsCFwd) {
        this.claimsCFwd = claimsCFwd;
    }
    
    
    public BigDecimal getClaimsContestedAsPercentageOfChox() {
        
        BigDecimal bReturnValue = new BigDecimal("0.00");
        float fClaimsNotificationContestedByInsurer = claimsNotificationContestedByInsurer.longValue();
        float fClaimsNotificationAcceptedByInsurer = claimsNotificationAcceptedByInsurer.longValue();
                
        if(fClaimsNotificationAcceptedByInsurer>0){
            
            float fTtlProcessClaim = fClaimsNotificationContestedByInsurer + fClaimsNotificationAcceptedByInsurer;
            bReturnValue = new BigDecimal(fClaimsNotificationAcceptedByInsurer/fTtlProcessClaim);
        }
      
        return bReturnValue;
    }

    
    public Integer getClaimsFNOLReferrals() {
        return claimsFNOLReferrals;
    }

    public void setClaimsFNOLReferrals(Integer claimsFNOLReferrals) {
        this.claimsFNOLReferrals = claimsFNOLReferrals;
    }

    public Integer getClaimsFNOLCreatedByInsurer() {
        return claimsFNOLCreatedByInsurer;
    }

    public void setClaimsFNOLCreatedByInsurer(Integer claimsFNOLCreatedByInsurer) {
        this.claimsFNOLCreatedByInsurer = claimsFNOLCreatedByInsurer;
    }

    public Integer getClaimsInvoiced() {
        return claimsInvoiced;
    }

    public void setClaimsInvoiced(Integer claimsInvoiced) {
        this.claimsInvoiced = claimsInvoiced;
    }

    public Integer getClaimsNotification() {
        return claimsNotification;
    }

    public void setClaimsNotification(Integer claimsNotification) {
        this.claimsNotification = claimsNotification;
    }

    public Integer getClaimsNotificationAcceptedByInsurer() {
        return claimsNotificationAcceptedByInsurer;
    }

    public void setClaimsNotificationAcceptedByInsurer(Integer claimsNotificationAcceptedByInsurer) {
        this.claimsNotificationAcceptedByInsurer = claimsNotificationAcceptedByInsurer;
    }

    public Integer getClaimsNotificationContestedByInsurer() {
        return claimsNotificationContestedByInsurer;
    }

    public void setClaimsNotificationContestedByInsurer(Integer claimsNotificationContestedByInsurer) {
        this.claimsNotificationContestedByInsurer = claimsNotificationContestedByInsurer;
    }

    public Integer getClaimsPaid() {
        return claimsPaid;
    }

    public void setClaimsPaid(Integer claimsPaid) {
        this.claimsPaid = claimsPaid;
    }

    public Integer getClaimsPendingByInsurer() {
        return claimsPendingByInsurer;
    }

    public void setClaimsPendingByInsurer(Integer claimsPendingByInsurer) {
        this.claimsPendingByInsurer = claimsPendingByInsurer;
    }

    public Integer getClaimsToBeInvoiced() {
        return claimsToBeInvoiced;
    }

    public void setClaimsToBeInvoiced(Integer claimsToBeInvoiced) {
        this.claimsToBeInvoiced = claimsToBeInvoiced;
    }

    public Integer getInvoiceApprovedByInsurer() {
        return invoiceApprovedByInsurer;
    }

    public void setInvoiceApprovedByInsurer(Integer invoiceApprovedByInsurer) {
        this.invoiceApprovedByInsurer = invoiceApprovedByInsurer;
    }

    public Integer getInvoiceContestedByInsurer() {
        return invoiceContestedByInsurer;
    }

    public void setInvoiceContestedByInsurer(Integer invoiceContestedByInsurer) {
        this.invoiceContestedByInsurer = invoiceContestedByInsurer;
    }

    public BigDecimal getInvoicePaidAsPercentageOfInvoicing() {
        return invoicePaidAsPercentageOfInvoicing;
    }

    public void setInvoicePaidAsPercentageOfInvoicing(Integer iClaimsInvoicedHis, Integer iInvoicePaidByInsurerHis) {
        
        BigDecimal dInvoicePaidAsPercentageOfInvoicing = new BigDecimal(0.00);
        
        if(iClaimsInvoicedHis>0 && iInvoicePaidByInsurerHis>0){
            float fInvoicePaidByInsurerHis = iInvoicePaidByInsurerHis.longValue();
            float fClaimsInvoicedHis = iClaimsInvoicedHis.longValue();
            dInvoicePaidAsPercentageOfInvoicing = new BigDecimal(fInvoicePaidByInsurerHis / fClaimsInvoicedHis);
        }
        
        this.invoicePaidAsPercentageOfInvoicing = dInvoicePaidAsPercentageOfInvoicing;
    }
    
    public Integer getInvoicePaidByInsurer() {
        return invoicePaidByInsurer;
    }

    public void setInvoicePaidByInsurer(Integer invoicePaidByInsurer) {
        this.invoicePaidByInsurer = invoicePaidByInsurer;
    }

    public Integer getInvoicePendingByInsurer() {
        return invoicePendingByInsurer;
    }

    public void setInvoicePendingByInsurer(Integer invoicePendingByInsurer) {
        this.invoicePendingByInsurer = invoicePendingByInsurer;
    }

    public String getWeekCycleDate() {
        return weekCycleDate;
    }

    public void setWeekCycleDate(String weekCycleDate) {
        this.weekCycleDate = weekCycleDate;
    }  
    
}
