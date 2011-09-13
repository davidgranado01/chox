/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import idas.chox.core.model.InsurerDiscount;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.InsurerDiscountService;
import idas.chox.core.services.InsurerService;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author seeni
 */
public class InsurerDiscountServiceImpl extends SecureDataService implements InsurerDiscountService {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerDiscountServiceImpl.class);
    private ChorganisationService chorganisationService;
    private InsurerService insurerService;

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public Map addDiscount(int insId, int choId, Date dateFrom, Date dateTo, BigDecimal discountAmount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTo);
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.SECOND, -1);
        dateTo = cal.getTime();
        return addInsurerDiscount(insId, choId, dateFrom, dateTo, discountAmount);
    }

    @Override
    public List<InsurerDiscount> getInsurerDiscount(int choId, int InsId) {

        List<InsurerDiscount> list = new ArrayList<InsurerDiscount>();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(InsurerDiscount.class);
            if (choId >= 1) {
                criteria.add(Restrictions.eq("chOrganisation.id", choId));
            }
            criteria.add(Restrictions.eq("insurer.id", InsId));
            criteria.addOrder(Order.desc("dateFrom"));
            list = findByCriteria(criteria);
        } catch (Throwable e) {
            LOG.error("Error getting Insurer Discount: {}", e.getMessage());
//            e.printStackTrace();
        }
        LOG.debug("total record in insurer Discount for insurer: {}, {} ", list.size());
        return list;
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public Map deleteInsurerDiscount(InsurerDiscount insurerDiscount) {

        Map hm = new HashMap();
        try {
            delete(insurerDiscount);
        } catch (Exception ex) {
            LOG.error("Error thrown in deleteInsurerDiscount: {}", ex.getMessage());
        }
        hm.put("success", Boolean.TRUE);
        return hm;
    }

    private Map addInsurerDiscount(int insId, int choId, Date dateFrom, Date dateTo, BigDecimal discountAmount) {
        Map hm = validateDiscount(insId, choId, dateFrom, dateTo);
        if (hm.get("success") != Boolean.TRUE) {
            return hm;
        }
        LOG.debug("INS ID :" + insId + " " + "CHO ID :" + choId + " " + "DATE FROM :" + dateFrom + " " + "DATE TO :" + dateTo);

        InsurerDiscount insurerDiscount = new InsurerDiscount();
        insurerDiscount.setChOrganisation(chorganisationService.getChorganisation(choId));
        insurerDiscount.setInsurer(insurerService.getInsurer(insId));
        insurerDiscount.setDateFrom(dateFrom);
        insurerDiscount.setDateTo(dateTo);
        insurerDiscount.setDiscountAmount(discountAmount);
        save(insurerDiscount);
        hm.put("success", Boolean.TRUE);
        return hm;
    }

    private Map validateDiscount(int insId, int choId, Date dateFrom, Date dateTo) {
        Map hm = new HashMap();

        Map errors = checkDiscountDateOverlap(insId, choId, dateFrom, dateTo);
        if (errors.size() > 0) {
            hm.put("success", Boolean.FALSE);
            hm.put("errors", errors);
        } else {
            hm.put("success", Boolean.TRUE);
        }
        return hm;
    }

    private Map checkDiscountDateOverlap(int insId, int choId, Date dateFrom, Date dateTo) {
        Map checks = new HashMap();
        StringBuilder sb = new StringBuilder(100);
        sb.append("select distinct");
        sb.append("(date_from,date_to) ");
        sb.append("overlaps ");
        sb.append("(DATE '");
        sb.append(getShDtStr(dateFrom));
        sb.append("',DATE '");
        sb.append(getShDtStr(dateTo));
        sb.append("') ");
        sb.append("from insurer_discount ");
        sb.append("where chorganisation_id = ");
        sb.append(choId);
        sb.append("and insurer_id = ");
        sb.append(insId);

        String query = sb.toString();
        LOG.debug("checkScheduleOverlap query is: {}", query);

        List valList = getCurrentSession().createSQLQuery(query).list();
        for (Object object : valList) {
            if (((Boolean) object).booleanValue()) {
                checks.put("dateTo", "From or To date overlaps existing schedule.");
                checks.put("dateFrom", "From or To date overlaps existing schedule.");
                break;
            }
        }

        return checks;
    }
    
    @Override
    public BigDecimal getDiscountAmount(int insId, int choId, Date invoiceCreatedDate) {
        
        StringBuilder sb = new StringBuilder(100);
        sb.append("select distinct discount_amount from (");
        sb.append("select distinct");
        sb.append("(date_from,date_to) ");
        sb.append("overlaps ");
        sb.append("(DATE '");
        sb.append(getShDtStr(invoiceCreatedDate));
        sb.append("',DATE '");
        sb.append(getShDtStr(invoiceCreatedDate));
        sb.append("') as overlap, discount_amount ");
        sb.append("from insurer_discount ");
        sb.append("where chorganisation_id = ");
        sb.append(choId);
        sb.append(" and insurer_id = ");
        sb.append(insId);
        sb.append(") as discountAmount where overlap = ");
        sb.append(true);

        String query = sb.toString();
        LOG.debug("getting discount amount query is: {}", query);

        List valList = getCurrentSession().createSQLQuery(query).list();
        for (Object object : valList) {
          LOG.debug("returning discount amount is: {}", (BigDecimal) object);
          return ((BigDecimal) object);
        }
        LOG.debug("No discount amount found for this invoice created date: {}", invoiceCreatedDate);
        return BigDecimal.ZERO;
         
    }

    private String getShDtStr(Date date) {
        DateFormat overlap_literal_format = new SimpleDateFormat("yyyy-MM-dd");
        return overlap_literal_format.format(date);
    }

    @Override
    public InsurerDiscount getInsurerDiscount(int insurerDiscountId) {
        return (InsurerDiscount) get(InsurerDiscount.class, insurerDiscountId);
    }
}
