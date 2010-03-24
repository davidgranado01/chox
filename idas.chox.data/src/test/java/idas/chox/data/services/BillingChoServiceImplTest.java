/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.data.services;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.services.ChorganisationService;
import java.util.Date;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test-pg.xml", "classpath:applicationContext-services-test.xml"})
public class BillingChoServiceImplTest {
    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    @Autowired
    @Qualifier("dataService")
    private SecureDataService dataService;

    @Autowired
    private ChorganisationService chorganisationService;

    @Test
    public void doTest(){
        Date from = new Date(2009,9,1);
        Date to = new Date(2009,10,30);
        Chorganisation cho = chorganisationService.getChorganisation(1006);
        List lst = find2(from, to, cho);
        System.out.println("List size "+ lst.size());
    }

    public List findInoviceforSchedule(Date from,Date to,Chorganisation cho){
        DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        //Restrictions.between("createdDate",from,to);
        criteria.add(Restrictions.between("createdDate",from,to));

        return dataService.findByCriteria(criteria);
        //return dataService.getCurrentSession().createCriteria(Claim.class)
                
        //createCriteria("invoice").
        //DetachedCriteria criteria = DetachedCriteria.forClass(Claim.class);
        //criteria.createCriteria("invoice").add(Restrictions.ge("createdDate", from)).add(Restrictions.le("createdDate", to));
        //criteria.createCriteria("invoice").add(Restrictions.ge("createdDate", from));
        //criteria.add(Restrictions.eq("chorganisation", cho));
        //return dataService.findByCriteria(criteria);
    }

    public List find2(Date from,Date to,Chorganisation cho){
        return dataService.getCurrentSession().createCriteria(Claim.class).
                createCriteria("invoice").add(Restrictions.between("createdDate",from,to)).list();
    }

}
