/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.VehicleHire;
import chox.model.XMLParseResult;

public interface VehicleHireService {

    XMLParseResult saveVehicleHireForXMLUploader(XMLParseResult xmlParseResult);

    public VehicleHire getObject(int id);

    public void updateObject(VehicleHire vehicleHire);
}
