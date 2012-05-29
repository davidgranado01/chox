package idas.chox.service.reports.viewdata;

import idas.chox.core.model.ReasonOfRejection;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
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
    private List<BigDecimal> rorLineItems;

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

    public void updateObject(Map data, List<ReasonOfRejection> reasonsOfRejection) {
        this.setNoInvoicesUploaded(((BigInteger)data.get("no_invoices_uploaded")).intValue());
        this.setNoInvoicesApprovedBre(((BigInteger)data.get("no_invoices_approved_bre")).intValue());
        this.setNoInvoicesApprovedBreDisputed(((BigInteger)data.get("no_invoices_approved_bre_disputed")).intValue());
        rorLineItems = new ArrayList<BigDecimal>();
        if (noInvoicesApprovedBre.intValue() != 0) {
            this.setPerInvoicesApprovedBreDisputed(new BigDecimal(getIntegerValue(data.get("no_invoices_approved_bre_disputed")) * 1.0 / noInvoicesApprovedBre).setScale(4, RoundingMode.HALF_UP));
            if (noInvoicesApprovedBre - noInvoicesApprovedBreDisputed != 0) {
                this.setPerInvoicesApprovedBreNotDisputedPaid15days(new BigDecimal(getIntegerValue(data.get("no_invoices_approved_bre_not_disputed_paid_15days"))*1.0 / (noInvoicesApprovedBre - noInvoicesApprovedBreDisputed)).setScale(4, RoundingMode.HALF_UP));
                this.setPerInvoicesApprovedBreNotDisputedPaid30days(new BigDecimal(getIntegerValue(data.get("no_invoices_approved_bre_not_disputed_paid_30days"))*1.0 / (noInvoicesApprovedBre - noInvoicesApprovedBreDisputed)).setScale(4, RoundingMode.HALF_UP));
            }
            if (noInvoicesApprovedBreDisputed.intValue() != 0) {
                this.setPerInvoicesApprovedBreDisputedPaid15days(new BigDecimal(getIntegerValue(data.get("no_invoices_approved_bre_disputed_paid_15days"))*1.0 / noInvoicesApprovedBreDisputed).setScale(4, RoundingMode.HALF_UP));
                this.setPerInvoicesApprovedBreDisputedPaid30days(new BigDecimal(getIntegerValue(data.get("no_invoices_approved_bre_disputed_paid_30days"))*1.0 / noInvoicesApprovedBreDisputed).setScale(4, RoundingMode.HALF_UP));
            }
            if(noInvoicesApprovedBreDisputed.intValue() != 0){
                for (ReasonOfRejection ror : reasonsOfRejection) {
                    rorLineItems.add(new BigDecimal(getIntegerValue(data.get("no_invoices_disputed_due_to_" 
                            + ror.getName().toLowerCase()))*1.0 / noInvoicesApprovedBreDisputed).setScale(4, RoundingMode.HALF_UP));
                }
            } else {
                for (ReasonOfRejection ror : reasonsOfRejection) {
                    rorLineItems.add(BigDecimal.ZERO);
                }
                this.setRorLineItems(rorLineItems);
            }
        } else {
            for (ReasonOfRejection ror : reasonsOfRejection) {
                rorLineItems.add(BigDecimal.ZERO);
            }
            this.setRorLineItems(rorLineItems);
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

    public List<BigDecimal> getRorLineItems() {
        return rorLineItems;
    }

    public void setRorLineItems(List<BigDecimal> rorLineItems) {
        this.rorLineItems = rorLineItems;
    }



}
