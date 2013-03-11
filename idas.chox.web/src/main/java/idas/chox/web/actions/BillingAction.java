package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import idas.chox.core.model.Billing;
import idas.chox.core.model.BillingDetail;
import idas.chox.service.admin.BillingService;
import idas.chox.web.viewdata.BillingViewData;
import idas.chox.web.viewdata.BillingDetailViewData;

/**
 *
 * @author abrar
 */
public class BillingAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(BillingAction.class);
    private int billingId;
    private int orgId;
    private String scheduleName;
    private Date dateFrom;
    private Date dateTo;
    private String billingType;
    private String manual;
    private String reconciled;
    private double amountReceived;
    private String jsonData;
    private boolean billSearch;
    private String choReference;
    private String claimNumber;
    private BillingService billingService;
    private boolean excludeSupplmntInv;
    private String omitSupplementaryInvoice;


    @Secured ({"ROLE_CHOX_ADMIN"})
    public String loadBillingPanel() {
        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String listBillingGridData() {
        List<BillingViewData> viewList = new ArrayList<BillingViewData>();
        List billingList;
        if ( billSearch){
            billingList = billingService.searchBills(getBillingType(), choReference, claimNumber);
        }else{
            billingList = billingService.getBillingList(getBillingType());
        }
        for (Iterator iterator = billingList.iterator(); iterator.hasNext();) {
            Billing object = (Billing) iterator.next();
            BillingViewData bvd = new BillingViewData(object);
            LOG.debug(bvd.toString());
            viewList.add(bvd);
        }

        setJsonData("{results:" + JSONArray.fromObject(viewList).toString() + "}");
        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String listBillingDetailGridData() {
        List<BillingDetailViewData> viewDetailList = new ArrayList<BillingDetailViewData>();
        List billingDetailList = billingService.getBillingDetailList(billingType, billingId);
        LOG.debug("Billing details record size: {} ", billingDetailList.size());
        for (Iterator itorDetails = billingDetailList.iterator(); itorDetails.hasNext();) {
            BillingDetail object = (BillingDetail) itorDetails.next();
            viewDetailList.add(new BillingDetailViewData(object));
        }
        setJsonData("{results:" + JSONArray.fromObject(viewDetailList).toString() + "}");
        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String updateBillingDetail() throws ParseException  {

        List<Map> lm = BillingDetailViewData.mapListFromJsonString(jsonData);
        billingService.updateBillingDetail(billingId, billingType,lm);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", Boolean.TRUE);
        jsonObject.put("message", "saved");
        setJsonData(jsonObject.toString());
        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String listBillingOrgData() {
        List viewList = billingService.getOrgList(getBillingType());
        setJsonData("{results:" + JSONArray.fromObject(viewList).toString() + "}");
        return SUCCESS;
    }

   
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String addBill() throws Exception {
        try {
            LOG.debug("Add billing schedule");
            Map hm = billingService.addBill(getBillingType(), getScheduleName(), getOrgId(), getDateFrom(), getDateTo(), isExcludeSupplmntInv());
            JSONObject jsonObject = JSONObject.fromObject(hm);
            setJsonData(jsonObject.toString());
            LOG.debug("Returning json string: '{}'", jsonObject.toString());
        } catch (Exception e) {

            LOG.error("Exception in addBill(): {}, {}", e.getMessage(),e);
            throw e;
        }
        return SUCCESS;
    }

    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String deleteBill() {
        try {
            LOG.debug("Delete billing schedule");
            Map hm = billingService.deleteBill(billingType, billingId);
            JSONObject jsonObject = JSONObject.fromObject(hm);
            setJsonData(jsonObject.toString());
            LOG.debug("Back from delete schedule");
        } catch (RuntimeException re) {

            LOG.error("Exception in deleteBill(): {}", re.getMessage());
            throw re;
        }
        return SUCCESS;
    }

    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String paymentReceived() {
        try {
            LOG.debug(billingType + "^^^^^^^^" + billingId + "^^^^^^^^" + manual + "^^^^^^^^" + reconciled + "^^^^^^^^" + amountReceived);
            Map hm = billingService.paymentReceived(billingType, billingId, manual, reconciled, amountReceived);

            JSONObject jsonObject = JSONObject.fromObject(hm);
            setJsonData(jsonObject.toString());
        } catch (RuntimeException re) {

            LOG.error("Exception in paymentReceived: {}", re.getMessage());
            throw re;
        }
        return SUCCESS;
    }

    /**
     * @return the billingType
     */
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String getBillingType() {
        return billingType;
    }

    /**
     * @param billingType the billingType to set
     */
    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String getJsonData() {
        return jsonData;
    }

    public BillingService getBillingService() {
        return billingService;
    }

    public void setBillingService(BillingService billingService) {
        this.billingService = billingService;
    }

    /**
     * @return the orgId
     */
    public int getOrgId() {
        return orgId;
    }

    /**
     * @param orgId the orgId to set
     */
    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    /**
     * @return the scheduleName
     */
    public String getScheduleName() {
        return scheduleName;
    }

    /**
     * @param scheduleName the scheduleName to set
     */
    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the billingId
     */
    public int getBillingId() {
        return billingId;
    }

    /**
     * @param billingId the billingId to set
     */
    public void setBillingId(int billingId) {
        this.billingId = billingId;
    }

    /**
     * @return the manual
     */
    public String getManual() {
        return manual;
    }

    /**
     * @param manual the manual to set
     */
    public void setManual(String manual) {
        this.manual = manual;
    }

    /**
     * @return the reconciled
     */
    public String getReconciled() {
        return reconciled;
    }

    /**
     * @param reconciled the reconciled to set
     */
    public void setReconciled(String reconciled) {
        this.reconciled = reconciled;
    }

    /**
     * @return the amountReceived
     */
    public double getAmountReceived() {
        return amountReceived;
    }

    /**
     * @param amountReceived the amountReceived to set
     */
    public void setAmountReceived(double amountReceived) {
        this.amountReceived = amountReceived;
    }

    /**
     * @return the billSearch
     */
    public boolean isBillSearch() {
        return billSearch;
    }

    /**
     * @return the choReference
     */
    public String getChoReference() {
        return choReference;
    }

    /**
     * @return the claimNumber
     */
    public String getClaimNumber() {
        return claimNumber;
    }

    /**
     * @param billSearch the billSearch to set
     */
    public void setBillSearch(boolean billSearch) {
        this.billSearch = billSearch;
    }

    /**
     * @param choReference the choReference to set
     */
    public void setChoReference(String choReference) {
        this.choReference = choReference;
    }

    /**
     * @param claimNumber the claimNumber to set
     */
    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public boolean isExcludeSupplmntInv() {
        return excludeSupplmntInv;
    }

    public void setExcludeSupplmntInv(boolean excludeSupplmntInv) {
        this.excludeSupplmntInv = excludeSupplmntInv;
    }

    public String getOmitSupplementaryInvoice() {
        return omitSupplementaryInvoice;
    }

    public void setOmitSupplementaryInvoice(String omitSupplementaryInvoice) {
        if (omitSupplementaryInvoice != null && omitSupplementaryInvoice.equalsIgnoreCase("on")){
            this.excludeSupplmntInv = true;
        }
    }
}
