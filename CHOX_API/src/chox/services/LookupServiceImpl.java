/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.data.HibernateUtil;
import chox.model.ClaimStatus;
import chox.model.LineOfBusiness;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 *
 * @author Emmanuel
 */
public class LookupServiceImpl  extends DataService implements LookupService {

    public List getStatuses() {
        return ClaimStatus.getStatus();
    }

    public List getLineOfBusinesses() {

        Criteria criteria = currentSession.createCriteria(LineOfBusiness.class);

        return criteria.list();
    }
}
