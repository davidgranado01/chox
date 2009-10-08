/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report.viewdata;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public class WeekSummary {

    private String weekCycleDate;
    private Integer claimsBFwd = 0;
    private Integer claimsNotification = 0;
    private Integer reopenClaims = 0;
    private Integer claimsOutOfScope = 0;
    private Integer rejectedClaims = 0;
    private Integer nonThisInsurerClaims = 0;
    private Integer insurerClaimsClosed = 0;
    private Integer claimsPaid = 0;
    private Integer claimsNotificationContestedByInsurer = 0;
    private BigDecimal claimsContestedAsPercentageOfChox = new BigDecimal(0.00);
    private BigDecimal invoicePaidAsPercentageOfInvoicing = new BigDecimal(0.00);
    private Integer claimsPendingByInsurer = 0;
    private Integer claimsNotificationAcceptedByInsurer = 0;
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
        result.setClaimsNotification(getIntegerValue(data.get("claimsNotification".toLowerCase())));
        // Added by Calrson @ 20091001
        result.setReopenClaims(getIntegerValue(data.get("reopenClaims".toLowerCase())));
        result.setClaimsOutOfScope(getIntegerValue(data.get("claimsOutOfScope".toLowerCase())));
        // Added by Carlson @ 20091001
        result.setRejectedClaims(getIntegerValue(data.get("rejectedClaims".toLowerCase())));
        result.setNonThisInsurerClaims(getIntegerValue(data.get("nonThisInsurerClaims".toLowerCase())));
        // Added by Carlson @ 20091001
        result.setInsurerClaimsClosed(getIntegerValue(data.get("insurerClaimsClosed".toLowerCase())));
        result.setClaimsPaid(getIntegerValue(data.get("claimsPaid".toLowerCase())));
        result.setClaimsNotificationContestedByInsurer(getIntegerValue(data.get("claimsNotificationContestedByInsurer".toLowerCase())));
        result.setClaimsPendingByInsurer(getIntegerValue(data.get("claimsPendingByInsurer".toLowerCase())));
        result.setClaimsNotificationAcceptedByInsurer(getIntegerValue(data.get("claimsNotificationAcceptedByInsurer".toLowerCase())));
        result.setClaimsFNOLCreatedByInsurer(getIntegerValue(data.get("claimsFNOLCreatedByInsurer".toLowerCase())));
        result.setClaimsInvoiced(getIntegerValue(data.get("claimsInvoiced".toLowerCase())));
        result.setInvoiceContestedByInsurer(getIntegerValue(data.get("invoiceContestedByInsurer".toLowerCase())));
        result.setInvoicePendingByInsurer(getIntegerValue(data.get("invoicePendingByInsurer".toLowerCase())));
        result.setInvoiceApprovedByInsurer(getIntegerValue(data.get("invoiceApprovedByInsurer".toLowerCase())));
        result.setInvoicePaidByInsurer(getIntegerValue(data.get("invoicePaidByInsurer".toLowerCase())));
        result.setClaimsToBeInvoiced(getIntegerValue(data.get("claimsToBeInvoiced".toLowerCase())));
        
        return result;
    }

    /*
    private Integer newChoxNotification;
    private Integer claimWithdrawn;
    private Integer existingClaim;
    private Integer cumulativeClaim;
    private Integer claimOutOfScope;
    private Integer claimInScope;
    private Integer claimNotificationContestedByRsa;
    private Integer claimPendingByRsa;
    private Integer claimNotificationAcceptedByRsa;
    private BigDecimal inScopeClaimContestedPercentage;
    private Integer claimFnolCreatedByRsa;
    private Integer claimInvoiced;
    private Integer contestedinvoiceByRsa;
    private Integer pendingInvoiceByRsa;
    private Integer approvedInvoiceByRsa;
    private Integer paidInvoiceByRsa;
    private BigDecimal paidInvoicePercentage;
    private Integer claimTobeInvoiced;

    public static WeekSummary getObject(Map data) {
        WeekSummary result = new WeekSummary();

        result.setNewChoxNotification(getIntegerValue(data.get("newChoxNotification".toLowerCase())));
        result.setClaimWithdrawn(getIntegerValue(data.get("claimWithdrawn".toLowerCase())));
        result.setExistingClaim(getIntegerValue(data.get("existingClaim".toLowerCase())));
        result.setCumulativeClaim(getIntegerValue(data.get("cumulativeClaim".toLowerCase())));
        result.setClaimOutOfScope(getIntegerValue(data.get("claimOutOfScope".toLowerCase())));
        result.setClaimInScope(getIntegerValue(data.get("claimInScope".toLowerCase())));
        result.setClaimNotificationContestedByRsa(getIntegerValue(data.get("claimNotificationContestedByRsa".toLowerCase())));
        result.setClaimPendingByRsa(getIntegerValue(data.get("claimPendingByRsa".toLowerCase())));
        result.setClaimNotificationAcceptedByRsa(getIntegerValue(data.get("claimNotificationAcceptedByRsa".toLowerCase())));
        //result.setInScopeClaimContestedPercentage(getIntegerValue(data.get("inScopeClaimContestedPercentage".toLowerCase())));
        result.setClaimFnolCreatedByRsa(getIntegerValue(data.get("claimFnolCreatedByRsa".toLowerCase())));
        result.setClaimInvoiced(getIntegerValue(data.get("claimInvoiced".toLowerCase())));
        result.setContestedinvoiceByRsa(getIntegerValue(data.get("contestedinvoiceByRsa".toLowerCase())));
        result.setPendingInvoiceByRsa(getIntegerValue(data.get("pendingInvoiceByRsa".toLowerCase())));
        result.setApprovedInvoiceByRsa(getIntegerValue(data.get("approvedInvoiceByRsa".toLowerCase())));
        result.setPaidInvoiceByRsa(getIntegerValue(data.get("paidInvoiceByRsa".toLowerCase())));
        //result.setPaidInvoicePercentage(getIntegerValue(data.get("paidInvoicePercentage".toLowerCase())));
        result.setClaimTobeInvoiced(getIntegerValue(data.get("claimTobeInvoiced".toLowerCase())));
        result.setWeekCycleDate(data.get("weekCycleDate").toString());
        //calculated field
        BigDecimal cPaidInvoicePercentage = new BigDecimal(result.getClaimInvoiced() * result.getPaidInvoiceByRsa() / 100);
        BigDecimal cInScopeClaimContestedPercentage = result.getClaimInScope() > 0 ? new BigDecimal(result.getClaimNotificationContestedByRsa() / result.getClaimInScope() * 100) : BigDecimal.ZERO;
        result.setPaidInvoicePercentage(cPaidInvoicePercentage);
        
        result.setInScopeClaimContestedPercentage(cInScopeClaimContestedPercentage);
        return result;
    }
    */
    public static Integer getIntegerValue(Object v) {
        if (v.getClass().equals(Integer.class)) {
            return (Integer) v;
        } else if (v.getClass().equals(BigInteger.class)) {
            return ((BigInteger) v).intValue();
        } else {
            return 0;
        }
    }

    // Added by Carlson @ 20091001
    public Integer getReopenClaims() {
        return reopenClaims;
    }

    // Added by Carlson @ 20091001
    public void setReopenClaims(Integer reopenClaims) {
        this.reopenClaims = reopenClaims;
    }

    // Added by Carlson @ 20091001
    public Integer getRejectedClaims() {
        return rejectedClaims;
    }

    // Added by Carlson @ 20091001
    public void setRejectedClaims(Integer rejectedClaims) {
        this.rejectedClaims = rejectedClaims;
    }

    // Added by Carlson @ 20091001
    public Integer getInsurerClaimsClosed() {
        return insurerClaimsClosed;
    }

    // Added by Carlson @ 20091001
    public void setInsurerClaimsClosed(Integer insurerClaimsClosed) {
        this.insurerClaimsClosed = insurerClaimsClosed;
    }

    public Integer getClaimsBFwd() {
        return claimsBFwd;
    }

    public void setClaimsBFwd(Integer claimsBFwd) {
        this.claimsBFwd = claimsBFwd;
    }

    public Integer getClaimsCFwd() {
        // return claimsBFwd + claimsNotification - claimsOutOfScope - nonThisInsurerClaims - claimsPaid;
        // Added by Carlson @ 20091001
        return claimsBFwd + claimsNotification + reopenClaims - claimsOutOfScope - rejectedClaims - nonThisInsurerClaims - insurerClaimsClosed - claimsPaid;
    }
    
    public void setClaimsContestedAsPercentageOfChox(BigDecimal claimsContestedAsPercentageOfChox) {
        this.claimsContestedAsPercentageOfChox = claimsContestedAsPercentageOfChox;
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

    public Integer getClaimsOutOfScope() {
        return claimsOutOfScope;
    }

    public void setClaimsOutOfScope(Integer claimsOutOfScope) {
        this.claimsOutOfScope = claimsOutOfScope;
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

    public Integer getNonThisInsurerClaims() {
        return nonThisInsurerClaims;
    }

    public void setNonThisInsurerClaims(Integer nonThisInsurerClaims) {
        this.nonThisInsurerClaims = nonThisInsurerClaims;
    }

    public String getWeekCycleDate() {
        return weekCycleDate;
    }

    public void setWeekCycleDate(String weekCycleDate) {
        this.weekCycleDate = weekCycleDate;
    }  
    
}
