package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.HireMonitoringDetail;

public interface HireMonitoringDetailService {

    HireMonitoringDetail getHireMonitoringDetailByVehicleHireId(int vehicleHireId);

    public HireMonitoringDetail getHireMonitoringDetail(int hireMonitoringDetailId);

    public void saveHireMonitoringDetail(HireMonitoringDetail engineerReport);

    public void saveObjectForXMLUploader(final ClaimResult claimResult);
}
