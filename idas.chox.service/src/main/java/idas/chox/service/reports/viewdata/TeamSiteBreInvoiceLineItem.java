/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports.viewdata;

import java.math.BigInteger;
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
    private Integer perInvoicesApprovedBreDisputed;
    private Integer perInvoicesApprovedBreNotDisputedPaid15days;
    private Integer perInvoicesApprovedBreNotDisputedPaid30days;
    private Integer perInvoicesApprovedBreDisputedPaid15days;
    private Integer perInvoicesApprovedBreDisputedPaid30days;
    private Integer perInvoicesDisputedDueToHireCharge;
    private Integer perInvoicesDisputedDueToHireDuration;
    private Integer perInvoicesDisputedDueToLiabilityDispute;
    private Integer perInvoicesDisputedDueToLikeForLike;
    private Integer perInvoicesDisputedDueToQuantam;
    private Integer perInvoicesDisputedDueToRepairCost;
    private Integer perInvoicesDisputedDueToInvoiceAlreadyPaid;
    private Integer perInvoicesDisputedDueToUndisclosed;
    private Integer perInvoicesDisputedDueToOther;





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
        this.setPerInvoicesApprovedBreDisputed(((BigInteger)data.get("no_invoices_approved_bre_disputed")).intValue());
        this.setPerInvoicesApprovedBreNotDisputedPaid15days(((BigInteger)data.get("no_invoices_approved_bre_not_disputed_paid_15days")).intValue());
        this.setPerInvoicesApprovedBreNotDisputedPaid30days(((BigInteger)data.get("no_invoices_approved_bre_not_disputed_paid_30days")).intValue());
        this.setPerInvoicesApprovedBreDisputedPaid15days(((BigInteger)data.get("no_invoices_approved_bre_disputed_paid_15days")).intValue());
        this.setPerInvoicesApprovedBreDisputedPaid30days(((BigInteger)data.get("no_invoices_approved_bre_disputed_paid_30days")).intValue());
        this.setPerInvoicesDisputedDueToHireCharge(((BigInteger)data.get("no_invoices_disputed_due_to_hire_charge")).intValue());
        this.setPerInvoicesDisputedDueToHireDuration(((BigInteger)data.get("no_invoices_disputed_due_to_hire_duration")).intValue());
        this.setPerInvoicesDisputedDueToLiabilityDispute(((BigInteger)data.get("no_invoices_disputed_due_to_liability_dispute")).intValue());
        this.setPerInvoicesDisputedDueToLikeForLike(((BigInteger)data.get("no_invoices_disputed_due_to_like_for_like")).intValue());
        this.setPerInvoicesDisputedDueToQuantam(((BigInteger)data.get("no_invoices_disputed_due_to_quantam")).intValue());
        this.setPerInvoicesDisputedDueToRepairCost(((BigInteger)data.get("no_invoices_disputed_due_to_repair_cost")).intValue());
        this.setPerInvoicesDisputedDueToInvoiceAlreadyPaid(((BigInteger)data.get("no_invoices_disputed_due_to_invoice_already_paid")).intValue());
        this.setPerInvoicesDisputedDueToUndisclosed(((BigInteger)data.get("no_invoices_disputed_due_to_undisclosed")).intValue());
        this.setPerInvoicesDisputedDueToOther(((BigInteger)data.get("no_invoices_disputed_due_to_other")).intValue());




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
    public Integer getPerInvoicesApprovedBreDisputed() {
        return perInvoicesApprovedBreDisputed;
    }

    /**
     * @param perInvoicesApprovedBreDisputed the perInvoicesApprovedBreDisputed to set
     */
    public void setPerInvoicesApprovedBreDisputed(Integer perInvoicesApprovedBreDisputed) {
        this.perInvoicesApprovedBreDisputed = perInvoicesApprovedBreDisputed;
    }

    /**
     * @return the perInvoicesApprovedBreNotDisputedPaid15days
     */
    public Integer getPerInvoicesApprovedBreNotDisputedPaid15days() {
        return perInvoicesApprovedBreNotDisputedPaid15days;
    }

    /**
     * @param perInvoicesApprovedBreNotDisputedPaid15days the perInvoicesApprovedBreNotDisputedPaid15days to set
     */
    public void setPerInvoicesApprovedBreNotDisputedPaid15days(Integer perInvoicesApprovedBreNotDisputedPaid15days) {
        this.perInvoicesApprovedBreNotDisputedPaid15days = perInvoicesApprovedBreNotDisputedPaid15days;
    }

    /**
     * @return the perInvoicesApprovedBreNotDisputedPaid30days
     */
    public Integer getPerInvoicesApprovedBreNotDisputedPaid30days() {
        return perInvoicesApprovedBreNotDisputedPaid30days;
    }

    /**
     * @param perInvoicesApprovedBreNotDisputedPaid30days the perInvoicesApprovedBreNotDisputedPaid30days to set
     */
    public void setPerInvoicesApprovedBreNotDisputedPaid30days(Integer perInvoicesApprovedBreNotDisputedPaid30days) {
        this.perInvoicesApprovedBreNotDisputedPaid30days = perInvoicesApprovedBreNotDisputedPaid30days;
    }

    /**
     * @return the perInvoicesApprovedBreDisputedPaid15days
     */
    public Integer getPerInvoicesApprovedBreDisputedPaid15days() {
        return perInvoicesApprovedBreDisputedPaid15days;
    }

    /**
     * @param perInvoicesApprovedBreDisputedPaid15days the perInvoicesApprovedBreDisputedPaid15days to set
     */
    public void setPerInvoicesApprovedBreDisputedPaid15days(Integer perInvoicesApprovedBreDisputedPaid15days) {
        this.perInvoicesApprovedBreDisputedPaid15days = perInvoicesApprovedBreDisputedPaid15days;
    }

    /**
     * @return the perInvoicesApprovedBreDisputedPaid30days
     */
    public Integer getPerInvoicesApprovedBreDisputedPaid30days() {
        return perInvoicesApprovedBreDisputedPaid30days;
    }

    /**
     * @param perInvoicesApprovedBreDisputedPaid30days the perInvoicesApprovedBreDisputedPaid30days to set
     */
    public void setPerInvoicesApprovedBreDisputedPaid30days(Integer perInvoicesApprovedBreDisputedPaid30days) {
        this.perInvoicesApprovedBreDisputedPaid30days = perInvoicesApprovedBreDisputedPaid30days;
    }

    /**
     * @return the perInvoicesDisputedDueToHireCharge
     */
    public Integer getPerInvoicesDisputedDueToHireCharge() {
        return perInvoicesDisputedDueToHireCharge;
    }

    /**
     * @param perInvoicesDisputedDueToHireCharge the perInvoicesDisputedDueToHireCharge to set
     */
    public void setPerInvoicesDisputedDueToHireCharge(Integer perInvoicesDisputedDueToHireCharge) {
        this.perInvoicesDisputedDueToHireCharge = perInvoicesDisputedDueToHireCharge;
    }

    /**
     * @return the perInvoicesDisputedDueToHireDuration
     */
    public Integer getPerInvoicesDisputedDueToHireDuration() {
        return perInvoicesDisputedDueToHireDuration;
    }

    /**
     * @param perInvoicesDisputedDueToHireDuration the perInvoicesDisputedDueToHireDuration to set
     */
    public void setPerInvoicesDisputedDueToHireDuration(Integer perInvoicesDisputedDueToHireDuration) {
        this.perInvoicesDisputedDueToHireDuration = perInvoicesDisputedDueToHireDuration;
    }

    /**
     * @return the perInvoicesDisputedDueToLiabilityDispute
     */
    public Integer getPerInvoicesDisputedDueToLiabilityDispute() {
        return perInvoicesDisputedDueToLiabilityDispute;
    }

    /**
     * @param perInvoicesDisputedDueToLiabilityDispute the perInvoicesDisputedDueToLiabilityDispute to set
     */
    public void setPerInvoicesDisputedDueToLiabilityDispute(Integer perInvoicesDisputedDueToLiabilityDispute) {
        this.perInvoicesDisputedDueToLiabilityDispute = perInvoicesDisputedDueToLiabilityDispute;
    }

    /**
     * @return the perInvoicesDisputedDueToLikeForLike
     */
    public Integer getPerInvoicesDisputedDueToLikeForLike() {
        return perInvoicesDisputedDueToLikeForLike;
    }

    /**
     * @param perInvoicesDisputedDueToLikeForLike the perInvoicesDisputedDueToLikeForLike to set
     */
    public void setPerInvoicesDisputedDueToLikeForLike(Integer perInvoicesDisputedDueToLikeForLike) {
        this.perInvoicesDisputedDueToLikeForLike = perInvoicesDisputedDueToLikeForLike;
    }

    /**
     * @return the perInvoicesDisputedDueToQuantam
     */
    public Integer getPerInvoicesDisputedDueToQuantam() {
        return perInvoicesDisputedDueToQuantam;
    }

    /**
     * @param perInvoicesDisputedDueToQuantam the perInvoicesDisputedDueToQuantam to set
     */
    public void setPerInvoicesDisputedDueToQuantam(Integer perInvoicesDisputedDueToQuantam) {
        this.perInvoicesDisputedDueToQuantam = perInvoicesDisputedDueToQuantam;
    }

    /**
     * @return the perInvoicesDisputedDueToRepairCost
     */
    public Integer getPerInvoicesDisputedDueToRepairCost() {
        return perInvoicesDisputedDueToRepairCost;
    }

    /**
     * @param perInvoicesDisputedDueToRepairCost the perInvoicesDisputedDueToRepairCost to set
     */
    public void setPerInvoicesDisputedDueToRepairCost(Integer perInvoicesDisputedDueToRepairCost) {
        this.perInvoicesDisputedDueToRepairCost = perInvoicesDisputedDueToRepairCost;
    }

    /**
     * @return the perInvoicesDisputedDueToInvoiceAlreadyPaid
     */
    public Integer getPerInvoicesDisputedDueToInvoiceAlreadyPaid() {
        return perInvoicesDisputedDueToInvoiceAlreadyPaid;
    }

    /**
     * @param perInvoicesDisputedDueToInvoiceAlreadyPaid the perInvoicesDisputedDueToInvoiceAlreadyPaid to set
     */
    public void setPerInvoicesDisputedDueToInvoiceAlreadyPaid(Integer perInvoicesDisputedDueToInvoiceAlreadyPaid) {
        this.perInvoicesDisputedDueToInvoiceAlreadyPaid = perInvoicesDisputedDueToInvoiceAlreadyPaid;
    }

    /**
     * @return the perInvoicesDisputedDueToUndisclosed
     */
    public Integer getPerInvoicesDisputedDueToUndisclosed() {
        return perInvoicesDisputedDueToUndisclosed;
    }

    /**
     * @param perInvoicesDisputedDueToUndisclosed the perInvoicesDisputedDueToUndisclosed to set
     */
    public void setPerInvoicesDisputedDueToUndisclosed(Integer perInvoicesDisputedDueToUndisclosed) {
        this.perInvoicesDisputedDueToUndisclosed = perInvoicesDisputedDueToUndisclosed;
    }

    /**
     * @return the perInvoicesDisputedDueToOther
     */
    public Integer getPerInvoicesDisputedDueToOther() {
        return perInvoicesDisputedDueToOther;
    }

    /**
     * @param perInvoicesDisputedDueToOther the perInvoicesDisputedDueToOther to set
     */
    public void setPerInvoicesDisputedDueToOther(Integer perInvoicesDisputedDueToOther) {
        this.perInvoicesDisputedDueToOther = perInvoicesDisputedDueToOther;
    }



}
