package idas.chox.core.services;

import java.util.Date;
import java.util.List;

import idas.chox.core.model.Claim;
import idas.chox.core.model.InsurerHireMonitoringEcd;

public interface InsurerHireMonitoringEcdService {

    List<InsurerHireMonitoringEcd> getInsurerHireMonitoringEcdsByClaimIdFilter(int claimId, boolean isAsc, String orderByField);

    List<InsurerHireMonitoringEcd> getInsurerHireMonitoringEcdsByClaimId(int claimId);

    InsurerHireMonitoringEcd getInsurerHireMonitoringEcd(int hireMonitoringEcdId);

    void saveInsurerHireMonitoringEcd(InsurerHireMonitoringEcd engineerReport);

    Date getLatestInsurerHireMonitoringECDDate(Claim claim);
    
    void addNewInsurerHireMonitoringEcd(Claim claim, InsurerHireMonitoringEcd ecd);
}
