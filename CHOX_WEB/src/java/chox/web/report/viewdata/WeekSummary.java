/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report.viewdata;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 *
 * @author Emmanuel
 */
public class WeekSummary {

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
    private String weekCycleDate;

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

    private static Integer getIntegerValue(Object v) {
        if (v.getClass().equals(Integer.class)) {
            return (Integer) v;
        } else if (v.getClass().equals(BigInteger.class)) {
            return ((BigInteger) v).intValue();
        } else {
            return 0;
        }
    }

    public Integer getNewChoxNotification() {
        return newChoxNotification;
    }

    public void setNewChoxNotification(Integer newChoxNotification) {
        this.newChoxNotification = newChoxNotification;
    }

    public Integer getClaimWithdrawn() {
        return claimWithdrawn;
    }

    public void setClaimWithdrawn(Integer claimWithdrawn) {
        this.claimWithdrawn = claimWithdrawn;
    }

    public Integer getExistingClaim() {
        return existingClaim;
    }

    public void setExistingClaim(Integer existingClaim) {
        this.existingClaim = existingClaim;
    }

    public Integer getCumulativeClaim() {
        return cumulativeClaim;
    }

    public void setCumulativeClaim(Integer cumulativeClaim) {
        this.cumulativeClaim = cumulativeClaim;
    }

    public Integer getClaimOutOfScope() {
        return claimOutOfScope;
    }

    public void setClaimOutOfScope(Integer claimOutOfScope) {
        this.claimOutOfScope = claimOutOfScope;
    }

    public Integer getClaimInScope() {
        return claimInScope;
    }

    public void setClaimInScope(Integer claimInScope) {
        this.claimInScope = claimInScope;
    }

    public Integer getClaimNotificationContestedByRsa() {
        return claimNotificationContestedByRsa;
    }

    public void setClaimNotificationContestedByRsa(Integer claimNotificationContestedByRsa) {
        this.claimNotificationContestedByRsa = claimNotificationContestedByRsa;
    }

    public Integer getClaimPendingByRsa() {
        return claimPendingByRsa;
    }

    public void setClaimPendingByRsa(Integer claimPendingByRsa) {
        this.claimPendingByRsa = claimPendingByRsa;
    }

    public Integer getClaimNotificationAcceptedByRsa() {
        return claimNotificationAcceptedByRsa;
    }

    public void setClaimNotificationAcceptedByRsa(Integer claimNotificationAcceptedByRsa) {
        this.claimNotificationAcceptedByRsa = claimNotificationAcceptedByRsa;
    }

    public BigDecimal getInScopeClaimContestedPercentage() {
        return inScopeClaimContestedPercentage;
    }

    public void setInScopeClaimContestedPercentage(BigDecimal inScopeClaimContestedPercentage) {
        this.inScopeClaimContestedPercentage = inScopeClaimContestedPercentage;
    }

    public Integer getClaimFnolCreatedByRsa() {
        return claimFnolCreatedByRsa;
    }

    public void setClaimFnolCreatedByRsa(Integer claimFnolCreatedByRsa) {
        this.claimFnolCreatedByRsa = claimFnolCreatedByRsa;
    }

    public Integer getClaimInvoiced() {
        return claimInvoiced;
    }

    public void setClaimInvoiced(Integer claimInvoiced) {
        this.claimInvoiced = claimInvoiced;
    }

    public Integer getContestedinvoiceByRsa() {
        return contestedinvoiceByRsa;
    }

    public void setContestedinvoiceByRsa(Integer contestedinvoiceByRsa) {
        this.contestedinvoiceByRsa = contestedinvoiceByRsa;
    }

    public Integer getPendingInvoiceByRsa() {
        return pendingInvoiceByRsa;
    }

    public void setPendingInvoiceByRsa(Integer pendingInvoiceByRsa) {
        this.pendingInvoiceByRsa = pendingInvoiceByRsa;
    }

    public Integer getApprovedInvoiceByRsa() {
        return approvedInvoiceByRsa;
    }

    public void setApprovedInvoiceByRsa(Integer approvedInvoiceByRsa) {
        this.approvedInvoiceByRsa = approvedInvoiceByRsa;
    }

    public Integer getPaidInvoiceByRsa() {
        return paidInvoiceByRsa;
    }

    public void setPaidInvoiceByRsa(Integer paidInvoiceByRsa) {
        this.paidInvoiceByRsa = paidInvoiceByRsa;
    }

    public BigDecimal getPaidInvoicePercentage() {
        return paidInvoicePercentage;
    }

    public void setPaidInvoicePercentage(BigDecimal paidInvoicePercentage) {
        this.paidInvoicePercentage = paidInvoicePercentage;
    }

    public Integer getClaimTobeInvoiced() {
        return claimTobeInvoiced;
    }

    public void setClaimTobeInvoiced(Integer claimTobeInvoiced) {
        this.claimTobeInvoiced = claimTobeInvoiced;
    }

    public String getWeekCycleDate() {
        return weekCycleDate;
    }

    public void setWeekCycleDate(String weekCycleDate) {
        this.weekCycleDate = weekCycleDate;
    }
}
