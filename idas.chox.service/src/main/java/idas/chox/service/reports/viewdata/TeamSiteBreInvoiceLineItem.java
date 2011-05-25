package idas.chox.service.reports.viewdata;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rajareddydodda
 */
public class TeamSiteBreInvoiceLineItem {

    private static final Logger LOG = LoggerFactory.getLogger(TeamSiteBreInvoiceLineItem.class);

    private String site;
    private String team;
    private Integer noInvoicesUploaded;
    private Integer noInvoicesApprovedBre;
    private Integer noInvoicesApprovedBreDisputed;
    private BigDecimal perInvoicesApprovedBreDisputed = BigDecimal.ZERO;
    private BigDecimal perInvoicesApprovedBreNotDisputedPaid15days = BigDecimal.ZERO;
    private BigDecimal perInvoicesApprovedBreNotDisputedPaid30days = BigDecimal.ZERO;
    private BigDecimal perInvoicesApprovedBreDisputedPaid15days = BigDecimal.ZERO;
    private BigDecimal perInvoicesApprovedBreDisputedPaid30days = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToHireCharge = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToHireDuration = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToLiabilityDispute = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToLikeForLike = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToQuantam = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToRepairCost = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToInvoiceAlreadyPaid = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToUndisclosed = BigDecimal.ZERO;
    private BigDecimal perInvoicesDisputedDueToOther = BigDecimal.ZERO;

    public static TeamSiteBreInvoiceLineItem getObject(Map data) {
        TeamSiteBreInvoiceLineItem result = new TeamSiteBreInvoiceLineItem();
        if (data.get("site") == null)
            result.setSite("");
        else
            result.setSite(data.get("site").toString());
        result.setTeam(data.get("team").toString());
        LOG.debug("Creating stats for: {}", result.getTeam());
        return result;
    }

    public void updateObject(Map data) {
        this.setNoInvoicesUploaded(((BigInteger)data.get("no_invoices_uploaded")).intValue());
        this.setNoInvoicesApprovedBre(((BigInteger)data.get("no_invoices_approved_bre")).intValue());
        this.setNoInvoicesApprovedBreDisputed(((BigInteger)data.get("no_invoices_approved_bre_disputed")).intValue());
        if (noInvoicesApprovedBre.intValue() != 0) {
            this.setPerInvoicesApprovedBreDisputed(new BigDecimal(getIntegerValue(data.get("no_invoices_approved_bre_disputed")) * 1.0 / noInvoicesApprovedBre).setScale(4, RoundingMode.HALF_UP));
            if (noInvoicesApprovedBre - noInvoicesApprovedBreDisputed != 0) {
                this.setPerInvoicesApprovedBreNotDisputedPaid15days(new BigDecimal(getIntegerValue(data.get("no_invoices_approved_bre_not_disputed_paid_15days"))*1.0 / (noInvoicesApprovedBre - noInvoicesApprovedBreDisputed)).setScale(4, RoundingMode.HALF_UP));
                this.setPerInvoicesApprovedBreNotDisputedPaid30days(new BigDecimal(getIntegerValue(data.get("no_invoices_approved_bre_not_disputed_paid_30days"))*1.0 / (noInvoicesApprovedBre - noInvoicesApprovedBreDisputed)).setScale(4, RoundingMode.HALF_UP));
            }
            if (noInvoicesApprovedBreDisputed.intValue() != 0) {
                this.setPerInvoicesApprovedBreDisputedPaid15days(new BigDecimal(getIntegerValue(data.get("no_invoices_approved_bre_disputed_paid_15days"))*1.0 / noInvoicesApprovedBreDisputed).setScale(4, RoundingMode.HALF_UP));
                this.setPerInvoicesApprovedBreDisputedPaid30days(new BigDecimal(getIntegerValue(data.get("no_invoices_approved_bre_disputed_paid_30days"))*1.0 / noInvoicesApprovedBreDisputed).setScale(4, RoundingMode.HALF_UP));
                this.setPerInvoicesDisputedDueToHireCharge(new BigDecimal(getIntegerValue(data.get("no_invoices_disputed_due_to_hire_charge"))*1.0 / noInvoicesApprovedBreDisputed).setScale(4, RoundingMode.HALF_UP));
                this.setPerInvoicesDisputedDueToHireDuration(new BigDecimal(getIntegerValue(data.get("no_invoices_disputed_due_to_hire_duration"))*1.0 / noInvoicesApprovedBreDisputed).setScale(4, RoundingMode.HALF_UP));
                this.setPerInvoicesDisputedDueToLiabilityDispute(new BigDecimal(getIntegerValue(data.get("no_invoices_disputed_due_to_liability_dispute"))*1.0 / noInvoicesApprovedBreDisputed).setScale(4, RoundingMode.HALF_UP));
                this.setPerInvoicesDisputedDueToLikeForLike(new BigDecimal(getIntegerValue(data.get("no_invoices_disputed_due_to_like_for_like"))*1.0 / noInvoicesApprovedBreDisputed).setScale(4, RoundingMode.HALF_UP));
                this.setPerInvoicesDisputedDueToQuantam(new BigDecimal(getIntegerValue(data.get("no_invoices_disputed_due_to_quantam"))*1.0 / noInvoicesApprovedBreDisputed).setScale(4, RoundingMode.HALF_UP));
                this.setPerInvoicesDisputedDueToRepairCost(new BigDecimal(getIntegerValue(data.get("no_invoices_disputed_due_to_repair_cost"))*1.0 / noInvoicesApprovedBreDisputed).setScale(4, RoundingMode.HALF_UP));
                this.setPerInvoicesDisputedDueToInvoiceAlreadyPaid(new BigDecimal(getIntegerValue(data.get("no_invoices_disputed_due_to_invoice_already_paid"))*1.0 / noInvoicesApprovedBreDisputed).setScale(4, RoundingMode.HALF_UP));
                this.setPerInvoicesDisputedDueToUndisclosed(new BigDecimal(getIntegerValue(data.get("no_invoices_disputed_due_to_undisclosed"))*1.0 / noInvoicesApprovedBreDisputed).setScale(4, RoundingMode.HALF_UP));
                this.setPerInvoicesDisputedDueToOther(new BigDecimal(getIntegerValue(data.get("no_invoices_disputed_due_to_other"))*1.0 / noInvoicesApprovedBreDisputed).setScale(4, RoundingMode.HALF_UP));
            }
        }
    }

    private static Integer getIntegerValue(Object v) {
        LOG.debug("inside IntegerValue method found class for the sent value is: {}", v.getClass());
        if (v.getClass().equals(Integer.class)) {
            return (Integer) v;
        } else if (v.getClass().equals(BigInteger.class)) {
            LOG.debug("return value {}", ((BigInteger) v).intValue());
            return ((BigInteger) v).intValue();
        } else {
            LOG.debug("no class match found returning 0");
            return 0;
        }
    }

    /**
     * @return the site
     */
    public String getSite() {
        return site;
    }

    /**
     * @param site the site to set
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * @return the team
     */
    public String getTeam() {
        return team;
    }

    /**
     * @param team the team to set
     */
    public void setTeam(String team) {
        this.team = team;
    }

    /**
     * @return the noInvoicesUploaded
     */
    public Integer getNoInvoicesUploaded() {
        return noInvoicesUploaded;
    }

    /**
     * @param noInvoicesUploaded the noInvoicesUploaded to set
     */
    public void setNoInvoicesUploaded(Integer noInvoicesUploaded) {
        this.noInvoicesUploaded = noInvoicesUploaded;
    }

    /**
     * @return the noInvoicesApprovedBre
     */
    public Integer getNoInvoicesApprovedBre() {
        return noInvoicesApprovedBre;
    }

    /**
     * @param noInvoicesApprovedBre the noInvoicesApprovedBre to set
     */
    public void setNoInvoicesApprovedBre(Integer noInvoicesApprovedBre) {
        this.noInvoicesApprovedBre = noInvoicesApprovedBre;
    }

    /**
     * @return the noInvoicesApprovedBreDisputed
     */
    public Integer getNoInvoicesApprovedBreDisputed() {
        return noInvoicesApprovedBreDisputed;
    }

    /**
     * @param noInvoicesApprovedBreDisputed the noInvoicesApprovedBreDisputed to set
     */
    public void setNoInvoicesApprovedBreDisputed(Integer noInvoicesApprovedBreDisputed) {
        this.noInvoicesApprovedBreDisputed = noInvoicesApprovedBreDisputed;
    }

    /**
     * @return the perInvoicesApprovedBreDisputed
     */
    public BigDecimal getPerInvoicesApprovedBreDisputed() {
        return perInvoicesApprovedBreDisputed;
    }

    /**
     * @param perInvoicesApprovedBreDisputed the perInvoicesApprovedBreDisputed to set
     */
    public void setPerInvoicesApprovedBreDisputed(BigDecimal perInvoicesApprovedBreDisputed) {
        this.perInvoicesApprovedBreDisputed = perInvoicesApprovedBreDisputed;
    }

    /**
     * @return the perInvoicesApprovedBreNotDisputedPaid15days
     */
    public BigDecimal getPerInvoicesApprovedBreNotDisputedPaid15days() {
        return perInvoicesApprovedBreNotDisputedPaid15days;
    }

    /**
     * @param perInvoicesApprovedBreNotDisputedPaid15days the perInvoicesApprovedBreNotDisputedPaid15days to set
     */
    public void setPerInvoicesApprovedBreNotDisputedPaid15days(BigDecimal perInvoicesApprovedBreNotDisputedPaid15days) {
        this.perInvoicesApprovedBreNotDisputedPaid15days = perInvoicesApprovedBreNotDisputedPaid15days;
    }

    /**
     * @return the perInvoicesApprovedBreNotDisputedPaid30days
     */
    public BigDecimal getPerInvoicesApprovedBreNotDisputedPaid30days() {
        return perInvoicesApprovedBreNotDisputedPaid30days;
    }

    /**
     * @param perInvoicesApprovedBreNotDisputedPaid30days the perInvoicesApprovedBreNotDisputedPaid30days to set
     */
    public void setPerInvoicesApprovedBreNotDisputedPaid30days(BigDecimal perInvoicesApprovedBreNotDisputedPaid30days) {
        this.perInvoicesApprovedBreNotDisputedPaid30days = perInvoicesApprovedBreNotDisputedPaid30days;
    }

    /**
     * @return the perInvoicesApprovedBreDisputedPaid15days
     */
    public BigDecimal getPerInvoicesApprovedBreDisputedPaid15days() {
        return perInvoicesApprovedBreDisputedPaid15days;
    }

    /**
     * @param perInvoicesApprovedBreDisputedPaid15days the perInvoicesApprovedBreDisputedPaid15days to set
     */
    public void setPerInvoicesApprovedBreDisputedPaid15days(BigDecimal perInvoicesApprovedBreDisputedPaid15days) {
        this.perInvoicesApprovedBreDisputedPaid15days = perInvoicesApprovedBreDisputedPaid15days;
    }

    /**
     * @return the perInvoicesApprovedBreDisputedPaid30days
     */
    public BigDecimal getPerInvoicesApprovedBreDisputedPaid30days() {
        return perInvoicesApprovedBreDisputedPaid30days;
    }

    /**
     * @param perInvoicesApprovedBreDisputedPaid30days the perInvoicesApprovedBreDisputedPaid30days to set
     */
    public void setPerInvoicesApprovedBreDisputedPaid30days(BigDecimal perInvoicesApprovedBreDisputedPaid30days) {
        this.perInvoicesApprovedBreDisputedPaid30days = perInvoicesApprovedBreDisputedPaid30days;
    }

    /**
     * @return the perInvoicesDisputedDueToHireCharge
     */
    public BigDecimal getPerInvoicesDisputedDueToHireCharge() {
        return perInvoicesDisputedDueToHireCharge;
    }

    /**
     * @param perInvoicesDisputedDueToHireCharge the perInvoicesDisputedDueToHireCharge to set
     */
    public void setPerInvoicesDisputedDueToHireCharge(BigDecimal perInvoicesDisputedDueToHireCharge) {
        this.perInvoicesDisputedDueToHireCharge = perInvoicesDisputedDueToHireCharge;
    }

    /**
     * @return the perInvoicesDisputedDueToHireDuration
     */
    public BigDecimal getPerInvoicesDisputedDueToHireDuration() {
        return perInvoicesDisputedDueToHireDuration;
    }

    /**
     * @param perInvoicesDisputedDueToHireDuration the perInvoicesDisputedDueToHireDuration to set
     */
    public void setPerInvoicesDisputedDueToHireDuration(BigDecimal perInvoicesDisputedDueToHireDuration) {
        this.perInvoicesDisputedDueToHireDuration = perInvoicesDisputedDueToHireDuration;
    }

    /**
     * @return the perInvoicesDisputedDueToLiabilityDispute
     */
    public BigDecimal getPerInvoicesDisputedDueToLiabilityDispute() {
        return perInvoicesDisputedDueToLiabilityDispute;
    }

    /**
     * @param perInvoicesDisputedDueToLiabilityDispute the perInvoicesDisputedDueToLiabilityDispute to set
     */
    public void setPerInvoicesDisputedDueToLiabilityDispute(BigDecimal perInvoicesDisputedDueToLiabilityDispute) {
        this.perInvoicesDisputedDueToLiabilityDispute = perInvoicesDisputedDueToLiabilityDispute;
    }

    /**
     * @return the perInvoicesDisputedDueToLikeForLike
     */
    public BigDecimal getPerInvoicesDisputedDueToLikeForLike() {
        return perInvoicesDisputedDueToLikeForLike;
    }

    /**
     * @param perInvoicesDisputedDueToLikeForLike the perInvoicesDisputedDueToLikeForLike to set
     */
    public void setPerInvoicesDisputedDueToLikeForLike(BigDecimal perInvoicesDisputedDueToLikeForLike) {
        this.perInvoicesDisputedDueToLikeForLike = perInvoicesDisputedDueToLikeForLike;
    }

    /**
     * @return the perInvoicesDisputedDueToQuantam
     */
    public BigDecimal getPerInvoicesDisputedDueToQuantam() {
        return perInvoicesDisputedDueToQuantam;
    }

    /**
     * @param perInvoicesDisputedDueToQuantam the perInvoicesDisputedDueToQuantam to set
     */
    public void setPerInvoicesDisputedDueToQuantam(BigDecimal perInvoicesDisputedDueToQuantam) {
        this.perInvoicesDisputedDueToQuantam = perInvoicesDisputedDueToQuantam;
    }

    /**
     * @return the perInvoicesDisputedDueToRepairCost
     */
    public BigDecimal getPerInvoicesDisputedDueToRepairCost() {
        return perInvoicesDisputedDueToRepairCost;
    }

    /**
     * @param perInvoicesDisputedDueToRepairCost the perInvoicesDisputedDueToRepairCost to set
     */
    public void setPerInvoicesDisputedDueToRepairCost(BigDecimal perInvoicesDisputedDueToRepairCost) {
        this.perInvoicesDisputedDueToRepairCost = perInvoicesDisputedDueToRepairCost;
    }

    /**
     * @return the perInvoicesDisputedDueToInvoiceAlreadyPaid
     */
    public BigDecimal getPerInvoicesDisputedDueToInvoiceAlreadyPaid() {
        return perInvoicesDisputedDueToInvoiceAlreadyPaid;
    }

    /**
     * @param perInvoicesDisputedDueToInvoiceAlreadyPaid the perInvoicesDisputedDueToInvoiceAlreadyPaid to set
     */
    public void setPerInvoicesDisputedDueToInvoiceAlreadyPaid(BigDecimal perInvoicesDisputedDueToInvoiceAlreadyPaid) {
        this.perInvoicesDisputedDueToInvoiceAlreadyPaid = perInvoicesDisputedDueToInvoiceAlreadyPaid;
    }

    /**
     * @return the perInvoicesDisputedDueToUndisclosed
     */
    public BigDecimal getPerInvoicesDisputedDueToUndisclosed() {
        return perInvoicesDisputedDueToUndisclosed;
    }

    /**
     * @param perInvoicesDisputedDueToUndisclosed the perInvoicesDisputedDueToUndisclosed to set
     */
    public void setPerInvoicesDisputedDueToUndisclosed(BigDecimal perInvoicesDisputedDueToUndisclosed) {
        this.perInvoicesDisputedDueToUndisclosed = perInvoicesDisputedDueToUndisclosed;
    }

    /**
     * @return the perInvoicesDisputedDueToOther
     */
    public BigDecimal getPerInvoicesDisputedDueToOther() {
        return perInvoicesDisputedDueToOther;
    }

    /**
     * @param perInvoicesDisputedDueToOther the perInvoicesDisputedDueToOther to set
     */
    public void setPerInvoicesDisputedDueToOther(BigDecimal perInvoicesDisputedDueToOther) {
        this.perInvoicesDisputedDueToOther = perInvoicesDisputedDueToOther;
    }



}
