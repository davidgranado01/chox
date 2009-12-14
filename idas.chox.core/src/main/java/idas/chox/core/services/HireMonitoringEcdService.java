/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringEcd;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public interface HireMonitoringEcdService {

    public List<HireMonitoringEcd> getHireMonitoringEcdsByClaimIdFilter(int claimId, boolean isAsc, String orderByField);

    public List<HireMonitoringEcd> getHireMonitoringEcdsByClaimId(int claimId);

    public HireMonitoringEcd getObject(int id);

    public void updateObject(HireMonitoringEcd engineerReport);

    public Date getLatestHireMonitoringECDDate(Claim claim);
}
