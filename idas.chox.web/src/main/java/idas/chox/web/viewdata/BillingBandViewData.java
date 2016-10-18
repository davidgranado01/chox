package idas.chox.web.viewdata;

import idas.chox.core.model.ChoBillingBand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.InsurerBillingBand;

public class BillingBandViewData {

    private static final Logger LOG = LoggerFactory.getLogger(BillingBandViewData.class);

    final private int id;
    final private String orgName;
    final private String bandName;
    final private String trigger;
    final private String costPerClaim;
    final private String excludeSupplementary;

    public BillingBandViewData(InsurerBillingBand record) {
        id = record.getId();
        orgName = record.getInsurer().getName();
        bandName = record.getBandName();
        trigger = convertStatus(record.getTriggerStatus());
        costPerClaim = record.getCostPerClaim().setScale(2).toString();
        excludeSupplementary = record.isExcludeSupplementary() ? "Yes" : "No";
    }

    public BillingBandViewData(ChoBillingBand record) {
        id = record.getId();
        orgName = record.getChorganisation().getName();
        bandName = record.getBandName();
        trigger = convertStatus(record.getTriggerStatus());
        costPerClaim = record.getCostPerClaim().setScale(2).toString();
        excludeSupplementary = record.isExcludeSupplementary() ? "Yes" : "No";
    }

    public int getId() {
        return id;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getBandName() {
        return bandName;
    }

    public String getTrigger() {
        return trigger;
    }

    public String getCostPerClaim() {
        return costPerClaim;
    }

    public String getExcludeSupplementary() {
        return excludeSupplementary;
    }

    private String convertStatus(String status) {
        switch(status) {
            case "AwaitingCarHireInfo":
                return "Accepted Claims";
            case "PaymentReceived":
                return "Payment Received";
            case "InvoicePaymentLogged":
                return "Invoice Payment Logged";
            case "ManualInvoicePaid":
                return "Manual Invoice Paid";
            default:
                return null;
        }
    }
}
