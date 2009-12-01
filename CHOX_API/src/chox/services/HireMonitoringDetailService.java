/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.HireMonitoringDetail;

public interface HireMonitoringDetailService {
    HireMonitoringDetail getHireMonitoringDetailByVehicleHireId(int id);
    public HireMonitoringDetail getObject(int id);
    public void updateObject(HireMonitoringDetail engineerReport);
    public void saveObjectForXMLUploader(final ClaimResult claimResult);
}
