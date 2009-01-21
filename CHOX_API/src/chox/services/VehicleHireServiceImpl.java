package chox.services;

import chox.Util.XmlHelper;
import chox.Util.DateHelper;
import chox.model.*;

public class VehicleHireServiceImpl extends DataService implements VehicleHireService {

    public XMLParseResult saveVehicleHireForXMLUploader(XMLParseResult xmlParseResult) {

        if ((xmlParseResult.getClaim().getVehicleHire()) != null) {

            if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                try {
                    xmlParseResult.getCurrentSession().saveOrUpdate(xmlParseResult.getClaim().getVehicleHire());
                } catch (Exception e) {
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                }
            }
        }


        return xmlParseResult;
    }

    public VehicleHire getObject(int id) {
        return (VehicleHire) getCurrentSession().get(VehicleHire.class, id);
    }

    public void updateObject(VehicleHire vehicleHire) {

        getCurrentSession().beginTransaction();
        getCurrentSession().update(vehicleHire);
        getCurrentSession().getTransaction().commit();
    }
}
