/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import idas.chox.core.model.BillingInsurer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author abrar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test-pg.xml", "classpath:applicationContext-services-test.xml"})
public class BillingInsurerServiceImplTest {

    private static final Log log = LogFactory.getLog(BillingInsurerServiceImplTest.class);
    @Autowired
    @Qualifier("dataService")
    private SecureDataService dataService;

    @Test
    public void doTest() {
        HashMap hm = new HashMap();
        //Date df = new Date(2009,9,10);
        //Date dt = new Date(2009,9,14);
        //checkScheduleOverlap(hm, "2009-09-10", "2009-09-14");
        //checkScheduleOverlap(hm, df, dt);
        List list = searchBills("", "200910008599");
        System.out.println(list.size());
    }

    public List searchBills(String choReference, String claimNumber) {
        List list = new ArrayList<BillingInsurer>();
        
        try {
            log.debug("Cho Ref" + choReference);
            log.debug("Claim Number " + claimNumber);

            DetachedCriteria criteria = DetachedCriteria.forClass(BillingInsurer.class);
            DetachedCriteria dc = criteria.createCriteria("billingDetails").createCriteria("claim");
            if (claimNumber != null && !claimNumber.equals("")) {
                Criterion c2 = Restrictions.eq("claimNumber", claimNumber);
                dc.add(c2);
            }
            
            
            if (choReference != null && !choReference.equals("")) {
                Criterion c1 = Restrictions.eq("choReference", choReference);
                dc.add(c1);
            }
           
            
            list = dataService.findByCriteria(criteria);
        } catch (RuntimeException e) {
            throw e;
        }
        
        return list;
    }

    public void checkScheduleOverlap(Map checkmap, Date dateFrom, Date dateTo) {

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("(date_from,date_to) ");
        sb.append("overlaps ");
        sb.append("( :p_1 , :p_2 ) ");
        sb.append("from billing_cho ");
        String query = sb.toString();
        Map paramMap = new HashMap();
        paramMap.put("p_1", (Date) dateFrom);
        paramMap.put("p_2", (Date) dateTo);
        Object obj = dataService.getCurrentSession().createSQLQuery(query).setDate("p_1", dateFrom).setDate("p_2", dateTo).uniqueResult();
        System.out.println(obj);
        /*
        List result = dataService.externalQuery(query,paramMap);
        Map resultMap = (Map)result.get(0);
        Boolean b = (Boolean)resultMap.get("overlaps");
        if ( !b ){
        checkmap.put("dateTo", "Date overlaps existing schedule.");
        checkmap.put("dateFROM", "Date overlaps existing schedule.");
        }

         */
    }

    public void checkScheduleOverlap2(Map checkmap) {

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("(date_from,date_to) ");
        sb.append("overlaps ");
        sb.append("( DATE '2009-09-10', DATE '2009-09-15') ");
        sb.append("from billing_cho ");
        String query = sb.toString();
        Map paramMap = new HashMap();

        Object obj = dataService.getCurrentSession().createSQLQuery(query).uniqueResult();
        System.out.println(obj);
        /*
        List result = dataService.externalQuery(query,paramMap);
        Map resultMap = (Map)result.get(0);
        Boolean b = (Boolean)resultMap.get("overlaps");
        if ( !b ){
        checkmap.put("dateTo", "Date overlaps existing schedule.");
        checkmap.put("dateFROM", "Date overlaps existing schedule.");
        }

         */
    }

    public void checkScheduleOverlap(Map checkmap, String dateFrom, String dateTo) {

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("(date_from,date_to) ");
        sb.append("overlaps ");
        sb.append("(DATE ':p_1',DATE ':p_2') ");
        sb.append("from billing_cho ");
        String query = sb.toString();
        Map paramMap = new HashMap();
        paramMap.put("p_1", dateFrom);
        paramMap.put("p_2", dateTo);
        List result = dataService.externalQuery(query, paramMap);
        Map resultMap = (Map) result.get(0);
        Boolean b = (Boolean) resultMap.get("overlaps");
        if (!b) {
            checkmap.put("dateTo", "Date overlaps existing schedule.");
            checkmap.put("dateFROM", "Date overlaps existing schedule.");
        }
    }
}
