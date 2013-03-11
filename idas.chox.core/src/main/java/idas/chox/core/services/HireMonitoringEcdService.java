package idas.chox.core.services;

import java.util.Date;
import java.util.List;

import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringEcd;

public interface HireMonitoringEcdService {

    List<HireMonitoringEcd> getHireMonitoringEcdsByClaimIdFilter(int claimId, boolean isAsc, String orderByField);

    List<HireMonitoringEcd> getHireMonitoringEcdsByClaimId(int claimId);

    HireMonitoringEcd getHireMonitoringEcd(int hireMonitoringEcdId);

    void saveHireMonitoringEcd(HireMonitoringEcd engineerReport);

    Date getLatestHireMonitoringECDDate(Claim claim);
    
    void addNewHireMonitoringEcd(Claim claim, HireMonitoringEcd ecd);
}
