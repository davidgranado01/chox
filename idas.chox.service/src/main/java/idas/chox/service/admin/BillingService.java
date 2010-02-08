package idas.chox.service.admin;

import idas.chox.core.model.BillingCho;
import idas.chox.core.model.BillingChoDetail;
import idas.chox.core.model.BillingDetail;
import idas.chox.core.model.BillingInsurer;
import idas.chox.core.model.BillingInsurerDetail;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Insurer;
import idas.chox.core.services.BillingChoDetailService;
import idas.chox.core.services.BillingChoRateService;
import java.util.Date;
import java.util.List;

import idas.chox.core.services.BillingChoService;
import idas.chox.core.services.BillingInsurerDetailService;
import idas.chox.core.services.BillingInsurerService;

import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.LookupService;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class BillingService {

    private static final Logger log = Logger.getLogger(BillingService.class);
    
    private static final Object INSURER = "insurer";
    private BillingChoRateService billingChoRateService;
    private BillingInsurerService billingInsurerService;
    private BillingInsurerDetailService billingInsurerDetailService;
    private BillingChoService billingChoService;
    private BillingChoDetailService billingChoDetailService;
    private LookupService lookupService;
    private InsurerService insurerService;
    private ChorganisationService chorganisationService;

    public List searchBills(String type,String choReference,String claimNumber){
         if (type.equals(INSURER)) {
            return getBillingInsurerService().searchBills(choReference,claimNumber);
        }else{
            return getBillingChoService().searchBills(choReference,claimNumber);
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
        if (type.equals(INSURER)) {
            return getBillingInsurerDetailService().getBillingInsurerDetails(billingId);
        } else {
            return getBillingChoDetailService().getBillingChoDetails(billingId);
        }
    }

    public List getBillingInsurerList() {
        return getBillingInsurerService().getBillingInsurers();
    }

    public List getBillingChoList() {
        return getBillingChoService().getBillingChos();

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateBillingDetail(int billingId, String type, List<Map> lm) {
        if (type.equals(INSURER)) {
            updateBillingInsurerDetail(billingId, lm);
        }else{
            updateBillingChoDetail(billingId,lm);
        }
    }

    public void updateBillingInsurerDetail(int billingId, List<Map> list) {

        for (Map changedFields : list) {
            int detailId = (Integer) changedFields.get("billingDetailId");
            BillingInsurerDetail detail = getBillingInsurerDetailService().getObject(detailId);
            detail.setComment(changedFields.get("comment").toString());
            detail.setReceivedDate((Date) changedFields.get("receivedDate"));
            detail.setAmountReceived((BigDecimal) changedFields.get("amountReceived"));
            detail.setReconciled((Boolean)changedFields.get("reconciled"));
            getBillingInsurerDetailService().updateObject(detail);
       }
        BillingInsurer is = getBillingInsurerService().getObject(billingId);
        List sumList = getBillingInsurerDetailService().sumPaymentAmount(billingId);
        log.debug("sum " + (BigDecimal) sumList.get(0));
        is.setAmountReceived((BigDecimal) sumList.get(0));
        getBillingInsurerService().updateObject(is);
    }
    
    public void updateBillingChoDetail(int billingId, List<Map> list) {

        for (Map changedFields : list) {
            int detailId = (Integer) changedFields.get("billingDetailId");
            BillingChoDetail detail = getBillingChoDetailService().getObject(detailId);
            detail.setComment(changedFields.get("comment").toString());
            detail.setReceivedDate((Date) changedFields.get("receivedDate"));
            detail.setAmountReceived((BigDecimal) changedFields.get("amountReceived"));
            detail.setReconciled((Boolean)changedFields.get("reconciled"));
            getBillingChoDetailService().updateObject(detail);
       }
        BillingCho is = getBillingChoService().getObject(billingId);
        List sumList = getBillingChoDetailService().sumPaymentAmount(billingId);
        log.debug("sum " + (BigDecimal) sumList.get(0));
        is.setAmountReceived((BigDecimal) sumList.get(0));
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
            list = lookupService.getAllSuppliers();
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

    public Map addBill(String type, String scheduleName, int orgId, Date dateFrom, Date dateTo) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTo);
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.MILLISECOND,-1);
        dateTo = cal.getTime();
        if (type.equals(INSURER)) {
            return addInsurerBill(scheduleName, orgId, dateFrom, dateTo);
        } else {
            return addChoBill(scheduleName, orgId, dateFrom, dateTo);
        }
    }

    Map validateInsurerBill(String scheduleName, int insurerId, Date dateFrom, Date dateTo) {
        Map hm = new HashMap();

        Map errors = billingInsurerService.checkObject(scheduleName, dateFrom, dateTo);
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
        log.debug(scheduleName + orgId + dateFrom + dateTo);
        Insurer insurer = insurerService.getInsurer(orgId);
        List<Claim> claimsInDate = billingInsurerService.findClaimsforSchedule(dateFrom, dateTo, insurer);
        log.debug("no of claims" + claimsInDate.size());
        BigDecimal agreedBenefit = insurer.getScsAgreedBenefitShareValue();
        log.debug("agreed benefit value " + agreedBenefit);
        BigDecimal inv = new BigDecimal(0.0);
        BillingInsurer bi = new BillingInsurer();
        bi.setInsurer(insurer);
        bi.setScheduleName(scheduleName);
        bi.setDateFrom(dateFrom);
        bi.setDateTo(dateTo);
        try {

            Set detailSet = bi.getBillingDetails();
            for (Claim claim : claimsInDate) {
                BillingInsurerDetail bid = new BillingInsurerDetail();
                bid.setBilling(bi);
                bid.setClaim(claim);
                log.debug(claim.getClaimNumber());
                bid.setBillAmount(agreedBenefit);
                bid.setAmountReceived(new BigDecimal(0.0));
                // insurerScheduleDetailService.updateObject(isd);
                detailSet.add(bid);
                inv = inv.add(agreedBenefit);
            }
            bi.setInvoiceAmount(inv);
            billingInsurerService.updateObject(bi);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }

        return hm;
    }

    Map validateChoBill(String scheduleName, int insurerId, Date dateFrom, Date dateTo) {
        Map hm = new HashMap();

        Map errors = billingChoService.checkObject(scheduleName, dateFrom, dateTo);
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
        log.debug(scheduleName + orgId + dateFrom + dateTo);
        Chorganisation cho = chorganisationService.getChorganisation(orgId);
        log.debug("cho name " + cho.getName());
        //List<Claim> claimsInDate = billingChoService.findClaimsforSchedule(dateFrom, dateTo, cho);
        List<Claim> claimsInDate = billingChoService.findInvoiceforSchedule(dateFrom, dateTo, cho);
        log.debug("no of claims" + claimsInDate.size());
        BigDecimal rate = new BigDecimal(1.2);
        //billingChoRateService.getRateForCho2(orgId, claimsInDate.size());
        BigDecimal inv = new BigDecimal(0);
        BillingCho bc = new BillingCho();
        bc.setCho(cho);
        bc.setScheduleName(scheduleName);
        bc.setDateFrom(dateFrom);
        bc.setDateTo(dateTo);
        try {
            Set detailSet = bc.getBillingDetails();
            for (Claim claim : claimsInDate) {

                if (claim.getInvoice() != null) {
                    BillingChoDetail bcd = new BillingChoDetail();
                    bcd.setBilling(bc);
                    bcd.setClaim(claim);
                    BigDecimal toPay = claim.getInvoice().getTotalToPay();
                    log.debug("toPay " + toPay);
                    log.debug("rate " + rate);
                    bcd.setBillAmount(toPay.multiply(rate).divide(new BigDecimal(100.00)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    //.setScale(2, BigDecimal.ROUND_HALF_UP)));
                    bcd.setAmountReceived(new BigDecimal(0.0));
                    //isd.setPaymentAmount(agreedBenefit);
                    // insurerScheduleDetailService.updateObject(isd);
                    detailSet.add(bcd);
                    inv = inv.add(bcd.getBillAmount());
                } else {
                    log.debug("no invoice for claim " + claim);
                }
            }
            bc.setInvoiceAmount(inv);
            billingChoService.updateObject(bc);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }

        return hm;
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
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
            billingInsurerService.deteteObject(bi);
        } catch (RuntimeException re) {
            // TODO Auto-generated catch block
            log.error(re.getMessage(), re);
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
            // TODO Auto-generated catch block
            log.error(re.getMessage(), re);
            throw re;
        }
        hm.put("success", Boolean.TRUE);
        return hm;
    }

    /////////////////////////////////////////////
    //// Reconciliation
    public Map paymentReceived(String type, int billingId, String manual, String reconciled, double amountReceived) {

        if (type.equals(INSURER)) {
            log.debug("######################################################################" + "insurer");

            return paymentReceivedInsurer(billingId, manual, reconciled, amountReceived);
        } else {
            log.debug("######################################################################" + "cho");
            return paymentReceivedCho(billingId, manual, reconciled, amountReceived);
        }


    }

    public Map paymentReceivedInsurer(int billingId, String manual, String reconciled, double amountReceived) {
        if (manual != null && manual.equalsIgnoreCase("on")) {
            log.debug("################################updating INUSRER payment manully");
            return updateBillManualInsurer(billingId, amountReceived, reconciled);
        } else if (reconciled != null && reconciled.equalsIgnoreCase("on")) {
            log.debug("##############################auto INSURER  reconcile payment");
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
            bc.setReconciled(reconciled != null ? (reconciled.equalsIgnoreCase("on") ? true : false) : false);
            billingInsurerService.updateObject(bc);
        } catch (RuntimeException re) {
            // TODO Auto-generated catch block
            log.error(re.getMessage(), re);
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
            BigDecimal amount = schedule.getInsurer().getScsAgreedBenefitShareValue();
            Set<BillingDetail> dtls = schedule.getBillingDetails();
            BigDecimal rcv = new BigDecimal(0);
            for (Iterator iterator = dtls.iterator(); iterator.hasNext();) {
                BillingDetail billingDetail = (BillingDetail) iterator.next();
                if (billingDetail.getAmountReceived().doubleValue() == 0) {
                    billingDetail.setReceivedDate(dt);
                    billingDetail.setAmountReceived(amount);
                    billingDetail.setReconciled(true);
                    //billingDetail.setComment("Reconciled");
                    log.debug(billingDetail.getId() + " payment marked " + billingDetail.getAmountReceived());
                } else {
                    log.debug(billingDetail.getId() + " payment not marked " + billingDetail.getAmountReceived());
                }
                rcv = rcv.add(billingDetail.getAmountReceived());
            }
            schedule.setManual(false);
            schedule.setAmountReceived(rcv);
            schedule.setReconciled(true);

            billingInsurerService.updateObject(schedule);
        } catch (RuntimeException re) {
       
            log.error(re.getMessage(), re);
            throw re;
        }
        hm.put("success", Boolean.TRUE);
        return hm;
    }

    public Map paymentReceivedCho(int billingId, String manual, String reconciled, double amountReceived) {
        if (manual != null && manual.equalsIgnoreCase("on")) {
            log.debug("################################updating CHO payment manully");
            return updateBillManualCho(billingId, amountReceived, reconciled);
        } else if (reconciled != null && reconciled.equalsIgnoreCase("on")) {
            log.debug("##############################auto CHO  reconcile payment");
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
            bc.setReconciled(reconciled != null ? (reconciled.equalsIgnoreCase("on") ? true : false) : false);
            billingChoService.updateObject(bc);
        } catch (RuntimeException re) {
            // TODO Auto-generated catch block
            log.error(re.getMessage(), re);
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
            BigDecimal rcv = new BigDecimal(0);
            Set<BillingDetail> dtls = schedule.getBillingDetails();
            for (Iterator iterator = dtls.iterator(); iterator.hasNext();) {
                BillingDetail billingDetail = (BillingDetail) iterator.next();
                if (billingDetail.getAmountReceived().doubleValue() == 0) {
                    billingDetail.setReceivedDate(dt);
                    billingDetail.setAmountReceived(billingDetail.getBillAmount());
                    billingDetail.setReconciled(true);
                    //billingDetail.setComment("Reconciled");
                    log.debug(billingDetail.getId() + " payment marked " + billingDetail.getAmountReceived());
                } else {
                    log.debug(billingDetail.getId() + " payment not marked " + billingDetail.getAmountReceived());
                }
                rcv = rcv.add(billingDetail.getAmountReceived());
            }
            schedule.setManual(false);
            schedule.setAmountReceived(rcv);
            schedule.setReconciled(true);
            billingChoService.updateObject(schedule);
        } catch (RuntimeException re) {
            // TODO Auto-generated catch block
            log.error(re.getMessage(), re);
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
     * @return the billingChoRateService
     */
    public BillingChoRateService getBillingChoRateService() {
        return billingChoRateService;
    }

    /**
     * @param billingChoRateService the billingChoRateService to set
     */
    public void setBillingChoRateService(BillingChoRateService billingChoRateService) {
        this.billingChoRateService = billingChoRateService;
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
