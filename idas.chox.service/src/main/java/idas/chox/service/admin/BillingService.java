package idas.chox.service.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.BillingCho;
import idas.chox.core.model.BillingChoDetail;
import idas.chox.core.model.BillingDetail;
import idas.chox.core.model.BillingInsurer;
import idas.chox.core.model.BillingInsurerDetail;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Insurer;
import idas.chox.core.services.BillingChoDetailService;
import idas.chox.core.services.BillingChoService;
import idas.chox.core.services.BillingInsurerDetailService;
import idas.chox.core.services.BillingInsurerService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.LookupService;
import idas.chox.core.util.CalcHelper;

public class BillingService {

    private static final Logger LOG = LoggerFactory.getLogger(BillingService.class);
    private static final String INSURER = "insurer";
    private BillingInsurerService billingInsurerService;
    private BillingInsurerDetailService billingInsurerDetailService;
    private BillingChoService billingChoService;
    private BillingChoDetailService billingChoDetailService;
    private LookupService lookupService;
    private InsurerService insurerService;
    private ChorganisationService chorganisationService;

    public List searchBills(String type, String choReference, String claimNumber) {
        if (type.equals(INSURER)) {
            return getBillingInsurerService().searchBills(choReference, claimNumber);
        } else {
            return getBillingChoService().searchBills(choReference, claimNumber);
        }
    }

    public List getBillingList(String type) {
        if (type.equals(INSURER)) {
            return getBillingInsurerService().getBillingInsurers();
        } else {
            return getBillingChoService().getBillingChos();
        }
    }

    public List getBillingDetailList(String type, int billingId) {
        if (type != null && type.equals(INSURER)) {
            return getBillingInsurerDetailService().getBillingInsurerDetails(billingId);
        } else if (type != null) {
            return getBillingChoDetailService().getBillingChoDetails(billingId);
        } else {
            LOG.warn("No type specified to get billing details list - billingId={}", billingId);
            return new ArrayList(0);
        }
    }

    public List getBillingInsurerList() {
        return getBillingInsurerService().getBillingInsurers();
    }

    public List getBillingChoList() {
        return getBillingChoService().getBillingChos();

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public void updateBillingDetail(int billingId, String type, List<Map> lm) {
        if (type.equals(INSURER)) {
            updateBillingInsurerDetail(billingId, lm);
        } else {
            updateBillingChoDetail(billingId, lm);
        }
    }

    public void updateBillingInsurerDetail(int billingId, List<Map> list) {

        for (Map changedFields : list) {
            int detailId = (Integer) changedFields.get("billingDetailId");
            BillingInsurerDetail detail = getBillingInsurerDetailService().getObject(detailId);
            detail.setComment(changedFields.get("comment").toString());
            detail.setReceivedDate((Date) changedFields.get("receivedDate"));
            detail.setTriggerDate((Date) changedFields.get("triggerDate"));
            detail.setTriggerPoint((String) changedFields.get("triggerPoint"));
            detail.setAmountReceived((BigDecimal) changedFields.get("amountReceived"));
            detail.setReconciled((Boolean) changedFields.get("reconciled"));
            getBillingInsurerDetailService().updateObject(detail);
        }
        BillingInsurer is = getBillingInsurerService().getObject(billingId);
        List sumList = getBillingInsurerDetailService().sumPaymentAmount(billingId);
        LOG.debug("sum: {}", (BigDecimal) sumList.get(0));
        is.setAmountReceived((BigDecimal) sumList.get(0));
        if (((BigDecimal) sumList.get(0)).equals(is.getInvoiceAmount())) {
            is.setReconciled(true);
        } else {
            is.setReconciled(false);
        }
        getBillingInsurerService().updateObject(is);
    }

    public void updateBillingChoDetail(int billingId, List<Map> list) {

        for (Map changedFields : list) {
            int detailId = (Integer) changedFields.get("billingDetailId");
            BillingChoDetail detail = getBillingChoDetailService().getObject(detailId);
            detail.setComment(changedFields.get("comment").toString());
            detail.setReceivedDate((Date) changedFields.get("receivedDate"));
            detail.setTriggerDate((Date) changedFields.get("triggerDate"));
            detail.setTriggerPoint((String) changedFields.get("triggerPoint"));
            detail.setAmountReceived((BigDecimal) changedFields.get("amountReceived"));
            detail.setReconciled((Boolean) changedFields.get("reconciled"));
            getBillingChoDetailService().updateObject(detail);
        }
        BillingCho is = getBillingChoService().getObject(billingId);
        List sumList = getBillingChoDetailService().sumPaymentAmount(billingId);
        LOG.debug("sum: {}", (BigDecimal) sumList.get(0));
        is.setAmountReceived((BigDecimal) sumList.get(0));
        if (((BigDecimal) sumList.get(0)).equals(is.getInvoiceAmount())) {
            is.setReconciled(true);
        } else {
            is.setReconciled(false);
        }
        getBillingChoService().updateObject(is);
    }

    public List getOrgList(String type) {
        List list;
        List returnList = new ArrayList();
        if (type.equals(INSURER)) {
            list = lookupService.getAllInsurers();
            for (Iterator it = list.iterator(); it.hasNext();) {
                Insurer object = (Insurer) it.next();
                HashMap record = new HashMap();
                record.put("orgId", object.getId());
                record.put("name", object.getName());
                returnList.add(record);
            }

        } else {
            list = lookupService.getSuppliers(true);
            for (Iterator it = list.iterator(); it.hasNext();) {
                Chorganisation object = (Chorganisation) it.next();
                HashMap record = new HashMap();
                record.put("orgId", object.getId());
                record.put("name", object.getName());
                returnList.add(record);
            }
        }
        return returnList;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public Map addBill(String type, String scheduleName, int orgId, Date dateFrom, Date dateTo) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTo);
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.SECOND, -1);
        dateTo = cal.getTime();
        if (type.equals(INSURER)) {
            return addInsurerBill(scheduleName, orgId, dateFrom, dateTo);
        } else {
            return addChoBill(scheduleName, orgId, dateFrom, dateTo);
        }
    }

    Map validateInsurerBill(String scheduleName, int insurerId, Date dateFrom, Date dateTo) {
        Map hm = new HashMap();

        Map errors = billingInsurerService.checkObject(scheduleName, dateFrom, dateTo, insurerId);
        if (errors.size() > 0) {
            hm.put("success", Boolean.FALSE);
            hm.put("errors", errors);
        } else {
            hm.put("success", Boolean.TRUE);
        }
        return hm;
    }

    public Map addInsurerBill(String scheduleName, int orgId, Date dateFrom, Date dateTo) throws Exception {
        Map hm = validateInsurerBill(scheduleName, orgId, dateFrom, dateTo);
        if (hm.get("success") != Boolean.TRUE) {
            return hm;
        }
        LOG.debug(scheduleName + orgId + dateFrom + dateTo);
        Insurer insurer = insurerService.getInsurer(orgId);
        List<BillingInsurerDetail> billingInsurerDetails = billingInsurerService.findClaimsforSchedule(dateFrom, dateTo, insurer);
        LOG.debug("no of claims: {}", billingInsurerDetails.size());
        if (billingInsurerDetails.isEmpty()) {
            hm.remove("success");
            hm.put("success", Boolean.FALSE);
            Map errors = new HashMap();
            errors.put("scheduleName", "Generated schedule would contain no entries");
            hm.put("errors", errors);
            return hm;
        }
        BillingInsurer bi = new BillingInsurer();

        BigDecimal totalBillCost = BigDecimal.ZERO;
        bi.setInsurer(insurer);
        bi.setScheduleName(scheduleName);
        bi.setDateFrom(dateFrom);
        bi.setDateTo(dateTo);
        
        try {
            Set detailSet = bi.getBillingDetails();
            for (BillingInsurerDetail bid : billingInsurerDetails) {
                bid.setBilling(bi);
                detailSet.add(bid);
                totalBillCost = totalBillCost.add(bid.getGrossBillAmount());
            }
            bi.setInvoiceAmount(totalBillCost);
            billingInsurerService.updateObject(bi);
        } catch (Exception e) {
            LOG.error("Exception thrown in addInsurerBill: {}", e.getMessage());
            throw e;
        }

        return hm;
    }

    Map validateChoBill(String scheduleName, int choId, Date dateFrom, Date dateTo) {
        Map hm = new HashMap();

        Map errors = billingChoService.checkObject(scheduleName, dateFrom, dateTo, choId);
        if (errors.size() > 0) {
            hm.put("success", Boolean.FALSE);
            hm.put("errors", errors);
        } else {
            hm.put("success", Boolean.TRUE);
        }
        return hm;
    }

    public Map addChoBill(String scheduleName, int orgId, Date dateFrom, Date dateTo) throws Exception {
        Map hm = validateChoBill(scheduleName, orgId, dateFrom, dateTo);
        if (hm.get("success") != Boolean.TRUE) {
            return hm;
        }
        LOG.debug(scheduleName + orgId + dateFrom + dateTo);
        Chorganisation cho = chorganisationService.getChorganisation(orgId);
        List<BillingChoDetail> billingChoDetails = billingChoService.findClaimsforSchedule(dateFrom, dateTo, cho);
        LOG.debug("no of claims: {}", billingChoDetails.size());
        if (billingChoDetails.isEmpty()) {
            hm.remove("success");
            hm.put("success", Boolean.FALSE);
            Map errors = new HashMap();
            errors.put("scheduleName", "Generated schedule would contain no entries");
            hm.put("errors", errors);
            return hm;
        }
        BillingCho bc = new BillingCho();

        BigDecimal totalBillCost = BigDecimal.ZERO;
        bc.setCho(cho);
        bc.setScheduleName(scheduleName);
        bc.setDateFrom(dateFrom);
        bc.setDateTo(dateTo);
        
        try {
            Set detailSet = bc.getBillingDetails();
            for (BillingChoDetail bcd : billingChoDetails) {
                bcd.setBilling(bc);
                detailSet.add(bcd);
                totalBillCost = totalBillCost.add(bcd.getGrossBillAmount());
            }
            bc.setInvoiceAmount(totalBillCost);
            billingChoService.updateObject(bc);
        } catch (Exception e) {
            LOG.error("Exception thrown in addInsurerBill: {}", e.getMessage());
            throw e;
        }

        return hm;

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public Map deleteBill(String type, int billingId) {
        if (type.equals(INSURER)) {
            return deleteInsurerBill(billingId);
        } else {
            return deleteChoBill(billingId);
        }
    }

    public Map deleteInsurerBill(int billingId) {
        Map hm = new HashMap();
        try {
            BillingInsurer bi = billingInsurerService.getObject(billingId);
            billingInsurerService.deleteObject(bi);
        } catch (RuntimeException re) {
            LOG.error("Error thrown in deleteInsurerBill: {}", re.getMessage());
            throw re;
        }
        hm.put("success", Boolean.TRUE);
        return hm;
    }

    public Map deleteChoBill(int billingId) {
        Map hm = new HashMap();
        try {
            BillingCho bi = billingChoService.getObject(billingId);
            billingChoService.deteteObject(bi);
        } catch (RuntimeException re) {
            LOG.error("Error thrown: {}", re.getMessage());
            throw re;
        }
        hm.put("success", Boolean.TRUE);
        return hm;
    }

    /////////////////////////////////////////////
    //// Reconciliation
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public Map paymentReceived(String type, int billingId, String manual, String reconciled, double amountReceived) {

        if (type.equals(INSURER)) {

            return paymentReceivedInsurer(billingId, manual, reconciled, amountReceived);
        } else {
            return paymentReceivedCho(billingId, manual, reconciled, amountReceived);
        }

    }

    public Map paymentReceivedInsurer(int billingId, String manual, String reconciled, double amountReceived) {
        if (manual != null && manual.equalsIgnoreCase("on")) {
            LOG.debug("updating INSURER payment manully");
            return updateBillManualInsurer(billingId, amountReceived, reconciled);
        } else if (reconciled != null && reconciled.equalsIgnoreCase("on")) {
            LOG.debug("auto INSURER  reconcile payment");
            return reconcileBillInsurer(billingId);
        }
        return null;

    }

    public Map updateBillManualInsurer(int billingId, double amountReceived, String reconciled) {
        Map hm = new HashMap();
        try {
            BillingInsurer bc = billingInsurerService.getObject(billingId);
            bc.setAmountReceived(new BigDecimal(amountReceived));
            bc.setManual(true);
            bc.setReconciled(reconciled != null ? (reconciled.equalsIgnoreCase("on")) : false);
            billingInsurerService.updateObject(bc);
        } catch (RuntimeException re) {
            LOG.error("Error thrown in updateBillManualInsurer: {}", re.getMessage());
            throw re;
        }
        hm.put("success", Boolean.TRUE);
        return hm;
    }

    public Map reconcileBillInsurer(int billingId) {
        Map hm = new HashMap();
        try {
            Date dt = Calendar.getInstance().getTime();
            BillingInsurer schedule = billingInsurerService.getObject(billingId);
            Set<BillingDetail> dtls = schedule.getBillingDetails();
            BigDecimal rcv = BigDecimal.ZERO;
            for (BillingDetail billingDetail : dtls) {
                if (billingDetail.getAmountReceived().doubleValue() == 0) {
                    billingDetail.setReceivedDate(dt);
                    billingDetail.setAmountReceived(billingDetail.getGrossBillAmount());
                    billingDetail.setReconciled(true);
                    //billingDetail.setComment("Reconciled");
                    LOG.debug("{} payment marked {}", billingDetail.getId(), billingDetail.getAmountReceived());
                } else {
                    LOG.debug("{} payment not marked {}", billingDetail.getId(), billingDetail.getAmountReceived());
                }
                rcv = rcv.add(billingDetail.getAmountReceived());
            }
            schedule.setManual(false);
            schedule.setAmountReceived(rcv);
            schedule.setReconciled(true);

            billingInsurerService.updateObject(schedule);
        } catch (RuntimeException re) {

            LOG.error(re.getMessage(), re);
            throw re;
        }
        hm.put("success", Boolean.TRUE);
        return hm;
    }

    public Map paymentReceivedCho(int billingId, String manual, String reconciled, double amountReceived) {
        if (manual != null && manual.equalsIgnoreCase("on")) {
            LOG.debug("updating CHO payment manully");
            return updateBillManualCho(billingId, amountReceived, reconciled);
        } else if (reconciled != null && reconciled.equalsIgnoreCase("on")) {
            LOG.debug("auto CHO  reconcile payment");
            return reconcileBillCho(billingId);
        }
        return null;
    }

    public Map updateBillManualCho(int billingId, double amountReceived, String reconciled) {
        Map hm = new HashMap();
        try {
            BillingCho bc = billingChoService.getObject(billingId);
            bc.setAmountReceived(new BigDecimal(amountReceived));
            bc.setManual(true);
            bc.setReconciled(reconciled != null ? (reconciled.equalsIgnoreCase("on")) : false);
            billingChoService.updateObject(bc);
        } catch (RuntimeException re) {
            LOG.error("Errorthrown in updateBillManualCho: {}", re.getMessage());
            throw re;
        }
        hm.put("success", Boolean.TRUE);
        return hm;
    }

    public Map reconcileBillCho(int billingId) {
        Map hm = new HashMap();
        try {
            Date dt = Calendar.getInstance().getTime();
            BillingCho schedule = billingChoService.getObject(billingId);
            BigDecimal rcv = BigDecimal.ZERO;
            Set<BillingDetail> dtls = schedule.getBillingDetails();
            for (BillingDetail billingDetail : dtls) {
                if (billingDetail.getAmountReceived().doubleValue() == 0) {
                    billingDetail.setReceivedDate(dt);
                    billingDetail.setAmountReceived(billingDetail.getGrossBillAmount());
                    billingDetail.setReconciled(true);
                    //billingDetail.setComment("Reconciled");
                    LOG.debug("{} payment marked {}", billingDetail.getId(), billingDetail.getAmountReceived());
                } else {
                    LOG.debug("{} payment not marked {}", billingDetail.getId(), billingDetail.getAmountReceived());
                }
                rcv = rcv.add(billingDetail.getAmountReceived());
            }
            schedule.setManual(false);
            schedule.setAmountReceived(rcv);
            schedule.setReconciled(true);
            billingChoService.updateObject(schedule);
        } catch (RuntimeException re) {
            LOG.error("Exception thrown: {}", re.getMessage());
            throw re;
        }
        hm.put("success", Boolean.TRUE);
        return hm;
    }

    /**
     * @return the billingInsurerService
     */
    public BillingInsurerService getBillingInsurerService() {
        return billingInsurerService;
    }

    /**
     * @param billingInsurerService the billingInsurerService to set
     */
    public void setBillingInsurerService(BillingInsurerService billingInsurerService) {
        this.billingInsurerService = billingInsurerService;
    }

    /**
     * @return the billingInsurerDetailService
     */
    public BillingInsurerDetailService getBillingInsurerDetailService() {
        return billingInsurerDetailService;
    }

    /**
     * @param billingInsurerDetailService the billingInsurerDetailService to set
     */
    public void setBillingInsurerDetailService(BillingInsurerDetailService billingInsurerDetailService) {
        this.billingInsurerDetailService = billingInsurerDetailService;
    }

    /**
     * @return the billingChoService
     */
    public BillingChoService getBillingChoService() {
        return billingChoService;
    }

    /**
     * @param billingChoService the billingChoService to set
     */
    public void setBillingChoService(BillingChoService billingChoService) {
        this.billingChoService = billingChoService;
    }

    /**
     * @return the billingChoDetailService
     */
    public BillingChoDetailService getBillingChoDetailService() {
        return billingChoDetailService;
    }

    /**
     * @param billingChoDetailService the billingChoDetailService to set
     */
    public void setBillingChoDetailService(BillingChoDetailService billingChoDetailService) {
        this.billingChoDetailService = billingChoDetailService;
    }

    /**
     * @return the lookupService
     */
    public LookupService getLookupService() {
        return lookupService;
    }

    /**
     * @param lookupService the lookupService to set
     */
    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    /**
     * @return the insurerService
     */
    public InsurerService getInsurerService() {
        return insurerService;
    }

    /**
     * @param insurerService the insurerService to set
     */
    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    /**
     * @return the chorganisationService
     */
    public ChorganisationService getChorganisationService() {
        return chorganisationService;
    }

    /**
     * @param chorganisationService the chorganisationService to set
     */
    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }
}
