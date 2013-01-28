package idas.chox.core.services;

import java.util.Date;
import java.util.List;

import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringEcd;

public interface HireMonitoringEcdService {

    public List<HireMonitoringEcd> getHireMonitoringEcdsByClaimIdFilter(int claimId, boolean isAsc, String orderByField);

    public List<HireMonitoringEcd> getHireMonitoringEcdsByClaimId(int claimId);

    public HireMonitoringEcd getHireMonitoringEcd(int hireMonitoringEcdId);

    public void saveHireMonitoringEcd(HireMonitoringEcd engineerReport);

    public Date getLatestHireMonitoringECDDate(Claim claim);
    
    public void addNewHireMonitoringEcd(Claim claim, HireMonitoringEcd ecd);
}
