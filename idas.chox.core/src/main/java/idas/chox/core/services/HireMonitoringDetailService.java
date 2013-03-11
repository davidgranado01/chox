package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.HireMonitoringDetail;

public interface HireMonitoringDetailService {

    HireMonitoringDetail getHireMonitoringDetailByVehicleHireId(int vehicleHireId);

    HireMonitoringDetail getHireMonitoringDetail(int hireMonitoringDetailId);

    void saveHireMonitoringDetail(HireMonitoringDetail engineerReport);

    void saveObjectForXMLUploader(final ClaimResult claimResult);
}
