/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import idas.chox.core.model.BillingChoRate;
import idas.chox.core.services.BillingChoRateService;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 *
 * @author abrar
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test-pg.xml", "classpath:applicationContext-services-test.xml"})
public class BillingChoRateServiceTest {
    private static final Log log = LogFactory.getLog(BillingChoRateServiceTest.class);
    @Autowired
    private LocalSessionFactoryBean sessionFactory;
    @Autowired
    private BillingChoRateService billingChoRateService;
    @Autowired
    @Qualifier("dataService")
    private SecureDataService dataService;

    @Ignore
    @Test
    public void testCanGetChoRates() {
        /*
        Assert.assertNotNull(sessionFactory);
        Assert.assertNotNull(billingChoRateService);
        Assert.assertNotNull(dataService);
        
        try{
        List billingChoRates = billingChoRateService.getBillingChoRates();
        BillingChoRate bcr = billingChoRateService.getObject(2);
        System.out.println("#################################"+billingChoRates.size());
        System.out.println("Fee 44444444"+ bcr.getFee()+ bcr.getMaxVolume());
        //System.out.println("######################"+getChargeRate(1006, 300));
        Assert.assertTrue(billingChoRates.size()> 0);
        }catch(Exception e){
            e.printStackTrace();
            log.error(e);
        }
 
         */
    }

   @Ignore
   @Test
   public void getChargeRate() throws Exception {

        BillingChoRate billingChoRate;
        BigDecimal fee = BigDecimal.ZERO;
        log.debug("hello world");
        try {
            int volume = 910;

            DetachedCriteria criteria = DetachedCriteria.forClass(BillingChoRate.class);
            criteria.createCriteria("chorganisation").add(Restrictions.eq("id", 1006));
            Criterion minVolume = Restrictions.le("minVolume", volume);
            Criterion maxVolume = Restrictions.ge("maxVolume", volume);
            Criterion isNull = Restrictions.isNull("maxVolume");

            LogicalExpression and1 = Restrictions.and(minVolume, maxVolume);
            LogicalExpression and2 = Restrictions.and(minVolume, isNull);

            LogicalExpression or = Restrictions.or(and1, and2);
            criteria.add(or);


            List  myList =  dataService.findByCriteria(criteria);
            System.out.println("############################################### List "+ myList.size());
            billingChoRate = (BillingChoRate)myList.get(0);
            System.out.println("##################" + billingChoRate.getId() + " " + billingChoRate.getFee() );




        } catch (Exception e) {
           log.error(e.getMessage(),e);
           throw e;
        }

        
    }


}
