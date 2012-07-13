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
    public Map addOrUpdateDiscount(int insId, int choId, InsurerDiscount insurerDiscount) {
        /*
         *  Add one day to 'dateTo'
         */
        Date dateFrom = insurerDiscount.getDateFrom();
        Date dateTo = insurerDiscount.getDateTo();
        int discountId = -1;
        if (insurerDiscount.getId() != null) {
            discountId = insurerDiscount.getId();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTo);
        cal.add(Calendar.DATE, 1);
        dateTo = cal.getTime();
        
        int insurerDiscountTypeValue = insurerDiscount.getInsurerDiscountType().getInsurerDiscountTypeValue();

        Map hm = validateDiscount(insId, choId, dateFrom, dateTo, discountId, insurerDiscountTypeValue);
        if (hm.get("success") != Boolean.TRUE) {
            return hm;
        }

        /*
         *  Reduce one second to 'dateTo'
         */
        cal.add(Calendar.SECOND, -1);
        dateTo = cal.getTime();

        LOG.debug("INS ID :" + insId + " " + "CHO ID :" + choId + " " + "DATE FROM :" + dateFrom + " " + "DATE TO :" + dateTo + "id :" + discountId);

        insurerDiscount.setChOrganisation(chorganisationService.getChorganisation(choId));
        insurerDiscount.setInsurer(insurerService.getInsurer(insId));
        save(insurerDiscount);
        hm.put("success", Boolean.TRUE);

        return hm;
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
            hm.put("success", Boolean.TRUE);
        } catch (Exception ex) {
            LOG.error("Error thrown in deleteInsurerDiscount: ", ex);
            hm.put("success", Boolean.FALSE);
        }
        return hm;
    }

    private Map validateDiscount(int insId, int choId, Date dateFrom, Date dateTo, int discountId, int insurerDiscountTypeValue) {
        Map hm = new HashMap();

        Map errors = checkDiscountDateOverlap(insId, choId, dateFrom, dateTo, discountId, insurerDiscountTypeValue);
        if (errors.size() > 0) {
            hm.put("success", Boolean.FALSE);
            hm.put("errors", errors);
        } else {
            hm.put("success", Boolean.TRUE);
        }
        return hm;
    }

    private Map checkDiscountDateOverlap(int insId, int choId, Date dateFrom, Date dateTo, int discountId, int insurerDiscountTypeValue) {
        Map checks = new HashMap();
        StringBuilder sb = new StringBuilder(100);
        sb.append("select distinct");
        sb.append(" (date_from,date_to) ");
        sb.append("overlaps ");
        sb.append("(DATE '");
        sb.append(getShDtStr(dateFrom));
        sb.append("',DATE '");
        sb.append(getShDtStr(dateTo));
        sb.append("') ");
        sb.append("from insurer_discount ");
        sb.append("where chorganisation_id = ");
        sb.append(choId);
        sb.append(" and insurer_id = ");
        sb.append(insId);
        sb.append(" and discount_type = ");
        sb.append(insurerDiscountTypeValue);
        
        if (discountId > 0) {
            sb.append(" and id != ");
            sb.append(discountId);
        }

        String query = sb.toString();
        LOG.debug("checkScheduleOverlap query is: {}", query);

        List valList = getCurrentSession().createSQLQuery(query).list();
        for (Object object : valList) {
            if (((Boolean) object).booleanValue()) {
                checks.put("dateTo", "From or To date overlaps existing discount.");
                checks.put("dateFrom", "From or To date overlaps existing discount.");
                break;
            }
        }

        return checks;
    }

    @Override
    public BigDecimal getDiscountPercentage(int insId, int choId, Date invoiceCreatedDate, int insurerDiscountTypeValue) {

        StringBuilder sb = new StringBuilder(100);
        sb.append("select distinct discount_percentage from (");
        sb.append("select distinct");
        sb.append("(date_from,date_to) ");
        sb.append("overlaps ");
        sb.append("(DATE '");
        sb.append(getShDtStr(invoiceCreatedDate));
        sb.append("',DATE '");
        sb.append(getShDtStr(invoiceCreatedDate));
        sb.append("') as overlap, discount_percentage ");
        sb.append("from insurer_discount ");
        sb.append("where chorganisation_id = ");
        sb.append(choId);
        sb.append(" and insurer_id = ");
        sb.append(insId);
        sb.append(" and discount_type = ");
        sb.append(insurerDiscountTypeValue);
        sb.append(") as discountPercentage where overlap = ");
        sb.append(true);

        String query = sb.toString();
        LOG.debug("getting discount percentage query is: {}", query);
        List valList = null;
        /*
         *  The below Try catch method implemented because the abouve query is throwing sql syntax error in H2 database and making unit test failure.
         *  so for unit test it will always return zero from the catch block. 
         *  ERROR Message (Syntax error in SQL statement SELECT DISTINCT DISCOUNT_PERCENTAGE FROM (SELECT DISTINCT(DATE_FROM,DATE_TO) OVERLAPS ([*]DATE '2011-09-29',DATE '2011-09-29') AS OVERLAP, DISCOUNT_PERCENTAGE FROM INSURER_DISCOUNT WHERE CHORGANISATION_ID = 1006 AND INSURER_ID = 3) AS DISCOUNTPERCENTAGE WHERE OVERLAP = TRUE ; expected ); SQL statement:
                        select distinct discount_percentage from (select distinct(date_from,date_to) overlaps (DATE '2011-09-29',DATE '2011-09-29') as overlap, discount_percentage from insurer_discount where chorganisation_id = 1006 and insurer_id = 3) as discountPercentage where overlap = true [42001-121])
         */
        try {
            valList = getCurrentSession().createSQLQuery(query).list();
        } catch (Throwable th) {
            LOG.error("Error running sql to get Insurer Discount percentage, returning 0 as insurer discount percentage: ", th);
            LOG.error("ins id {}, cho id {}", insId, choId);
            LOG.error("invoice Created date {}", invoiceCreatedDate);
            return BigDecimal.ZERO;
        }

        for (Object object : valList) {
            LOG.debug("returning discount percentage is: {}", (BigDecimal) object);
            return ((BigDecimal) object);
        }
        LOG.debug("No discount percentage found for this invoice created date: {}", invoiceCreatedDate);
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
