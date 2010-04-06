/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.data.services;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.Notification;
import idas.chox.core.model.NotificationType;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.DataService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author abrar
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test-pg.xml", "classpath:applicationContext-services-test.xml"})
public class ClaimServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(ClaimServiceImplTest.class);
    @Autowired
    @Qualifier("dataService")
    private SecureDataService dataService;

    @Autowired
    @Qualifier("auditTrailService")
    private AuditTrailService auditTrailService;

    @Autowired
    @Qualifier("claimService")
    private ClaimService claimService;

    @Ignore
    @Test
    public void deleteClaim(){
        Claim claim = claimService.getClaim(5099);
        
        
        List<AuditTrail> list = auditTrailService.getAuditTrailByClaim(claim.getId());
        for (AuditTrail auditTrail : list) {
            ((DataService)auditTrailService).delete(auditTrail);
        }

        claimService.delete(claim);
        
    }
    @Ignore
    @Test
    public void doProjectionTest(){
    	Criteria crit = dataService.getCurrentSession().createCriteria(Claim.class,"cl");
    	DetachedCriteria noti = DetachedCriteria.forClass(Notification.class).setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("claim"))));
    	crit.add(Subqueries.propertyIn("cl.id", noti));
    	List my = crit.list();
    	for (Iterator iterator = my.iterator(); iterator.hasNext();) {
			Claim type = (Claim) iterator.next();
			log.debug(type.getChoReference() + " " + type.getStatus());
		}
    }
    

    @Ignore
    @Test
    public void doAnomaliesTest(){
        log.debug("hello test");
        Criteria criteria = dataService.getCurrentSession().createCriteria(Claim.class);
        
        //.createAlias("this.invoice", "iv", CriteriaSpecification.LEFT_JOIN)
        //.createAlias("this.customer", "cs", CriteriaSpecification.LEFT_JOIN)
        //.createAlias("this.workgroup", "wg", CriteriaSpecification.LEFT_JOIN)
        //.createAlias("this.thirdParty", "tp", CriteriaSpecification.LEFT_JOIN)
        	//.createAlias("this.vehicleHire", "vh", CriteriaSpecification.LEFT_JOIN)
        //.createAlias("this.chorganisation", "cho", CriteriaSpecification.LEFT_JOIN)
        //.createAlias("this.createdBy", "cb", CriteriaSpecification.LEFT_JOIN)
        //.createAlias("this.claimOwner", "co", CriteriaSpecification.LEFT_JOIN)
        //.createAlias("this.hireMonitoringDetail", "hmd", CriteriaSpecification.LEFT_JOIN)
        //.createAlias("this.insurer", "ins", CriteriaSpecification.LEFT_JOIN);
    	log.debug("@@@@@@ is anomalies");
        Set AnomaliesStatus = new HashSet();
        
        AnomaliesStatus.add(ClaimStatus.CLAIM_REF_TO_ENG);
        AnomaliesStatus.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        AnomaliesStatus.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        AnomaliesStatus.add(ClaimStatus.CLAIM_PENDING);
        AnomaliesStatus.add(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        AnomaliesStatus.add(ClaimStatus.CLAIM_REJECTED);
        AnomaliesStatus.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        AnomaliesStatus.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                
        String sql = "select distinct claim_id from notification where type <> 'LiabilityStatusUpdatedNotification' and status in ("
        	+ "'" + ClaimStatus.CLAIM_REF_TO_ENG + "'" 
        	+ "'" + ClaimStatus.CLAIM_REFERRED_TO_FNOL + "'" 
        	+ "'" + ClaimStatus.CLAIM_REJECTION_CONTESTED + "'" 
        	+ "'" + ClaimStatus.CLAIM_PENDING + "'" 
        	+ "'" + ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO + "'" 
        	+ "'" + ClaimStatus.CLAIM_REJECTED + "'" 
        	+ "'" + ClaimStatus.CLAIM_UPDATE_BY_ENG + "'"
        	+ "'" + ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED + "'";
        
        DetachedCriteria noti = DetachedCriteria.forClass(Notification.class).setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("claim"))));	
        //ProjectionList projections = Projections.projectionList().add(Projections.property("claim_id"), "claim_id");
        //criteria.add(Restrictions.sqlRestriction("distinct claim_id"));
        /*
        Junction c1 = Restrictions.conjunction().add(Restrictions.in("notifications.type", NotificationType.getInsurerNotificationTypes()))
        		.add(Restrictions.sizeGt("notifications", 0));
        criteria.add(c1);
        */
        //criteria.add(Restrictions.sqlRestriction("id in (select distinct claim_id from notification)"));
        //criteria.createCriteria("notifications").add(Restrictions.in("type", NotificationType.getInsurerNotificationTypes()));
        //criteria.add(Restrictions.in("notifications.type", NotificationType.getInsurerNotificationTypes()));
        //criteria.add(Restrictions.sizeGt("notifications", 0));
        
        //criteria.add(Restrictions.in("notifications.type", NotificationType.getInsurerNotificationTypes()));
        //Subqueries.in(id, dc)
        criteria.add(Subqueries.propertyIn("id" , noti));
        criteria.add(Restrictions.in("status", AnomaliesStatus));
        
        
        criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<HashMap> resultMap = criteria.list();

        List claims = new ArrayList<Claim>();

        for (HashMap m : resultMap) {
            Claim cm = (Claim) m.get("this");
            log.debug(cm.getChoReference() + " " + cm.getStatus());
            claims.add(cm);
        }
    }
    
    @Ignore
    @Test
    public void doTest() {
        log.debug("hello test");
        Criteria criteria = dataService.getCurrentSession().createCriteria(Claim.class)
        .createAlias("this.invoice", "iv", CriteriaSpecification.LEFT_JOIN)
        .createAlias("this.customer", "cs", CriteriaSpecification.LEFT_JOIN)
        .createAlias("this.workgroup", "wg", CriteriaSpecification.LEFT_JOIN)
        .createAlias("this.thirdParty", "tp", CriteriaSpecification.LEFT_JOIN)
        .createAlias("this.vehicleHire", "vh", CriteriaSpecification.LEFT_JOIN)
        .createAlias("this.chorganisation", "cho", CriteriaSpecification.LEFT_JOIN)
        .createAlias("this.createdBy", "cb", CriteriaSpecification.LEFT_JOIN)
        .createAlias("this.claimOwner", "co", CriteriaSpecification.LEFT_JOIN)
        .createAlias("this.hireMonitoringDetail", "hmd", CriteriaSpecification.LEFT_JOIN)
        .createAlias("this.insurer", "ins", CriteriaSpecification.LEFT_JOIN);



        criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_LOGGED));
        criteria.add(Restrictions.ne("status", ClaimStatus.CLAIM_CLOSED));
        criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_REJECTED_ACCEPTED));
        criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_PAYMENT_RECEIVED));
        criteria.add(Restrictions.ne("status", ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        criteria.add(Restrictions.ge("iv.penaltyAlertQty", 0));
        

        Junction nonSplit = Restrictions.conjunction()
                .add(Restrictions.sqlRestriction("extract(day from current_date- iv1_.created_date) + 1 >(iv1_.penalty_alert_qty+1)*30"))
                    .add(Restrictions.disjunction()
                        .add(Restrictions.isNull("liabilityStatus"))
                        .add(Restrictions.conjunction()
                            .add(Restrictions.ne("liabilityStatus", LiabilityStatus.LIABILITY_SPLIT))
                            .add(Restrictions.ne("liabilityStatus", LiabilityStatus.PROCEED_WITHOUT_PREJUDICE))));
        
        Junction split = Restrictions.conjunction()
                .add(Restrictions.sqlRestriction("extract(day from current_date - liability_agreed_date) + 1 >(iv1_.penalty_alert_qty+1)*30"))
                .add(Restrictions.disjunction()
                    .add(Restrictions.eq("liabilityStatus", LiabilityStatus.LIABILITY_SPLIT))
                    .add(Restrictions.eq("liabilityStatus", LiabilityStatus.PROCEED_WITHOUT_PREJUDICE)));
        criteria.add(Restrictions.disjunction().add(nonSplit).add(split));
        




        criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<HashMap> resultMap = criteria.list();

        List claims = new ArrayList<Claim>();

        for (HashMap m : resultMap) {
            Claim cm = (Claim) m.get("this");
            log.debug(cm.getChoReference() + " " + cm.getStatus());
            claims.add(cm);
        }
    }
}
