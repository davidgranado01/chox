/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Billing;
import idas.chox.service.admin.BillingService;
import idas.chox.web.viewdata.BillingViewData;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;

import idas.chox.core.model.BillingDetail;
import idas.chox.web.viewdata.BillingDetailViewData;
import java.util.Date;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author abrar
 */
public class BillingAction extends BaseAction {

    private static final Logger log = Logger.getLogger(BillingAction.class);

    public String loadBillingPanel() {
        return SUCCESS;
    }

    public String listBillingGridData() {
        List<BillingViewData> viewList = new ArrayList<BillingViewData>();
        List billingList = billingService.getBillingList(getBillingType());
        for (Iterator iterator = billingList.iterator(); iterator.hasNext();) {
            Billing object = (Billing) iterator.next();
            BillingViewData bvd = new BillingViewData(object);
            log.debug(bvd);
            viewList.add(bvd);
        }
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("test", "Hello world");
        ActionContext.getContext().getValueStack().set("test", "hello world 1");


        //String count = "totalCount:"+ viewList.size()+ ",";
        setJsonData("{results:" + JSONArray.fromObject(viewList).toString() + "}");
        return SUCCESS;
    }

    public String listBillingDetailGridData() {
        List<BillingDetailViewData> viewDetailList = new ArrayList<BillingDetailViewData>();
        List billingDetailList = billingService.getBillingDetailList(billingType, billingId);
        log.debug("billing details record " + billingDetailList.size());
        for (Iterator itorDetails = billingDetailList.iterator(); itorDetails.hasNext();) {
            BillingDetail object = (BillingDetail) itorDetails.next();
            viewDetailList.add(new BillingDetailViewData(object));
        }
        setJsonData("{results:" + JSONArray.fromObject(viewDetailList).toString() + "}");
        return SUCCESS;
    }

    public String updateBillingDetail() throws ParseException  {

        List<Map> lm = BillingDetailViewData.mapListFromJsonString(jsonData);
        billingService.updateBillingDetail(billingId, billingType,lm);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", Boolean.TRUE);
        jsonObject.put("message", "saved");
        setJsonData(jsonObject.toString());
        return SUCCESS;
    }

    public String listBillingOrgData() {
        List viewList = billingService.getOrgList(getBillingType());
        setJsonData("{results:" + JSONArray.fromObject(viewList).toString() + "}");
        return SUCCESS;
    }

    public String addBill() throws Exception {
        try {
            log.debug("update billing schedule");
            Map hm = billingService.addBill(getBillingType(), getScheduleName(), getOrgId(), getDateFrom(), getDateTo());
            JSONObject jsonObject = JSONObject.fromObject(hm);
            setJsonData(jsonObject.toString());
        } catch (Exception e) {

            log.error(e.getMessage(), e);
            throw e;
        }
        return SUCCESS;
    }

    public String deleteBill() {
        try {
            log.debug("delete billing schedule");
            Map hm = billingService.deleteBill(billingType, billingId);
            JSONObject jsonObject = JSONObject.fromObject(hm);
            setJsonData(jsonObject.toString());
            log.debug("back from delete schedule");
        } catch (RuntimeException re) {

            log.error(re.getMessage(), re);
            throw re;
        }
        return SUCCESS;
    }

    public String paymentReceived() {
        try {
            log.debug("######################################################################");
            log.debug(billingType + "^^^^^^^^" + billingId + "^^^^^^^^" + manual + "^^^^^^^^" + reconciled + "^^^^^^^^" + amountReceived);
            Map hm = billingService.paymentReceived(billingType, billingId, manual, reconciled, amountReceived);

            JSONObject jsonObject = JSONObject.fromObject(hm);
            setJsonData(jsonObject.toString());
        } catch (RuntimeException re) {

            log.error(re.getMessage(), re);
            throw re;
        }
        return SUCCESS;
    }

    /**
     * @return the billingType
     */
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

    public String getJsonData() {
        return jsonData;
    }

    public BillingService getBillingService() {
        return billingService;
    }

    public void setBillingService(BillingService billingService) {
        this.billingService = billingService;
    }
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
    private BillingService billingService;

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
}
